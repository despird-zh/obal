package com.obal.accessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.ResultSerialization;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.mapreduce.Job;

/**
 * HMapRedHttpScan provide Map Reduce function, this will be used to validate the ACL
 * of record at server side.
 * <p>
 * ACL validation is processed in mapper; while in reducer the results are sent to 
 * remote listening servlet directly.
 * </p>
 * 
 * @author desprid
 * @version 0.1 2014-3-1
 */
public class HMapRedHttpScan2 {
	
	private Configuration config = null;
	
	/** the source table */
	private String source;
	/** the scan */
	private Scan scan;
	/** the setting of job */
	private Map<String, String> setting;
	
	/**
	 * Constructor
	 * 
	 * @param source the source table name
	 * @param scan the scan object. 
	 **/
	public HMapRedHttpScan2(String source,Scan scan){
			
		this.source = source;			
		this.scan = scan;			
	}
	
	/**
	 * Set the job setting 
	 **/
	public void setJobSetting(Map<String, String> setting){
		this.setting = setting;
	}
	
	/**
	 * Launch the mapreduce scan 
	 * @param jobName the name of job 
	 **/
	public int mapredScan(String jobName) throws Exception {  
	        
	    Job job = Job.getInstance(config);
	    // Initialize the job setting 
	    if(setting != null){
	    	Configuration conf = job.getConfiguration();
	    	for(Map.Entry<String, String> entry:setting.entrySet()){
	    		
	    		conf.set(entry.getKey(), entry.getValue());
	    	}	  
	    }
	    
	    job.setJobName(jobName);
	    job.setJarByClass(HMapRedHttpScan2.class);  

	    TableMapReduceUtil.initTableMapperJob(                  
	         source,                  
	         scan,  
	         ScanMapper.class,                
	         ImmutableBytesWritable.class,                
	         BytesWritable.class,  
	         job);  
	    TableMapReduceUtil.initTableReducerJob(                 
	         "obal.garbage",               
	         ScanReducer.class,  
	         job); 
	        
	    return job.waitForCompletion(true) ? 0 : 1;        
	        
	} 
	
	/**
	 * The Mapper class here the Acl will be parsed
	 * 
	 **/
    public static class ScanMapper extends TableMapper<ImmutableBytesWritable, BytesWritable> {

        private int numRecords = 0;
        Serializer<Result> serializer;
        DataOutputBuffer out = null;
        
    	protected void setup(Context context) throws IOException, InterruptedException {
    		ResultSerialization rstSerialUtil = new ResultSerialization();
        	serializer = rstSerialUtil.getSerializer(Result.class);
        	out = new DataOutputBuffer();
    	}
    	
        @Override
        public void map(ImmutableBytesWritable row, Result values, Context context) throws IOException {
            numRecords++;
            System.out.println("map entrance "+numRecords);
        	// extract userKey from the compositeKey (userId + counter)
            ImmutableBytesWritable userKey = new ImmutableBytesWritable(Bytes.toBytes(numRecords));
            try {
            	DataOutputBuffer out = new DataOutputBuffer();
            	out.reset();
                serializer.open(out);
                serializer.serialize(values);
                byte[] barray = out.getData();
                System.out.println("map data "+Bytes.toString(row.get())+" length-"+numRecords+":"+barray.length);
            	BytesWritable one = new BytesWritable(barray);
                context.write(userKey, one);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
            
            if ((numRecords % 1000) == 0) {
                context.setStatus("mapper processed " + numRecords + " records so far");
            }
        }
        
        protected void cleanup(Context context) throws IOException, InterruptedException {
        	serializer = null;
        	out = null;
        }
    }

    /**
     * Reducer the result will be sent to remote servlet collector. 
     **/
    public static class ScanReducer extends TableReducer<ImmutableBytesWritable, BytesWritable, ImmutableBytesWritable> {

    	HttpClient httpClient = null;
    	ThreadPoolExecutor threadPool = null;
    	int count = 0;
    	protected void setup(Context context) throws IOException, InterruptedException {
    		
        	httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        	httpClient.getHostConfiguration().setHost("192.168.1.154", 8080, "http");
        	httpClient.getParams().setSoTimeout(10000);
        	
        	threadPool = new ThreadPoolExecutor(
        			2, // core thread size
        			5, // max thread size
        			3, // idle time
        			TimeUnit.SECONDS, 
        			new LinkedBlockingQueue<Runnable>(),
        			new ThreadPoolExecutor.AbortPolicy());
    	}
    	
        public void reduce(ImmutableBytesWritable key, Iterable<BytesWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            System.out.println("reduce data "+Bytes.toInt(key.get()));
            for (final BytesWritable val : values) {
            	sum++;
//                if(sum > 2)
//                	return;
//                else if(sum ==0){
//                	continue;
//                }else{
            	System.out.println("iterate data "+Bytes.toInt(key.get())+" length:"+ val.getLength());
            	PostMethod post = new PostMethod("/demo/mapredresult/");
            	ResultSendTask task = new ResultSendTask(httpClient, post, val);   	
				threadPool.submit(task);
            	//task.run();
//                }
            }

            //context.write(key, put);
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
        	httpClient = null;
        	threadPool.shutdown();
        	threadPool = null;
        }
    }

    public static class ResultSendTask implements Runnable{
    	
    	HttpClient httpclient = null;
    	BytesWritable resultBytes = null;
    	PostMethod post =  null;
    	static int count = 0;
    	int i = 0;
    	
    	public ResultSendTask(HttpClient httpclient,PostMethod post,BytesWritable resultBytes){
    		i = count++;
    		this.post = post;
    		this.httpclient = httpclient;
    		this.resultBytes = resultBytes;
    	}
    	
		@Override
		public void run() {
			
			String feedback ="NONE";
			
        	try {        		
        		
        		System.out.println("----start sending result:"+ i);
        		DataInputBuffer dinbuf = new DataInputBuffer();
        		dinbuf.reset(resultBytes.getBytes(),0,resultBytes.getLength());
        		ClientProtos.Result proto = ClientProtos.Result.parseDelimitedFrom(dinbuf);
        		ProtobufUtil.toResult(proto);
        		System.out.println("task-length of data-"+i+":"+resultBytes.getLength());
				RequestEntity entity=new InputStreamRequestEntity(dinbuf);
	            post.setRequestEntity(entity);
	            post.addRequestHeader("dataindex", String.valueOf(i));
	             // execute the method
	            //httpclient.executeMethod(post);
	            InputStream ins = post.getResponseBodyAsStream();	           
	           	feedback = IOUtils.toString(ins) ;
	            
			} catch (IOException e) {
		
				e.printStackTrace();
			} finally {
	             // always release the connection after we're done 
	             post.releaseConnection();
	             System.out.println("----end sending result:" + i + " feedback:" + feedback);
	        }
			cleanup();
		}
    	
    	private void cleanup(){
    		
    		this.post = null;
    		this.httpclient = null;
    		this.resultBytes = null;
    	}
    }
    
    public void init(){
    	
		config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.property.clientPort", "2181");
		config.set("hbase.zookeeper.quorum", "192.168.1.133");
		config.set("hbase.master", "192.168.1.133:60010");
		File file = new File(".");
		try {

			String path = file.getCanonicalPath();
			System.out.println("---init start:" + path);
			System.setProperty("hadoop.home.dir", "D:\\n.repo\\hadoop-2.2.0");

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
