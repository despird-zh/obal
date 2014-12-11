package com.obal.core.hbase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.log4j.PropertyConfigurator;

/**
 * counts the number of userIDs
 * 
 * @author sujee ==at== sujee.net
 * 
 */
public class HMapRedScan {
	  
	private Configuration config = null;
		
	private String source;
			
	private Scan scan;
		
	public HMapRedScan(String source,Scan scan){
			
		this.source = source;			
		this.scan = scan;			
	}
		
	public int scan() throws Exception {  
	        
	    Job job = Job.getInstance(config);
	    Configuration conf = job.getConfiguration();
	    conf.set("mrscan.limit", "20000");
	    conf.set("mrscan.principal", "demouser");
	    job.setJobName("ObalTestScan");
	    job.setJarByClass(HMapRedScan.class);  

	    TableMapReduceUtil.initTableMapperJob(                  
	         source,   // input table                 
	         scan,  // Scan instance to control CF and attribute selection                  
	         ScanMapper.class,  // mapper class                  
	         ImmutableBytesWritable.class,  // mapper output key                 
	         BytesWritable.class,   // mapper output value  
	         job);  
	    TableMapReduceUtil.initTableReducerJob(                 
	         "obal.garbage",  // output table                   
	         ScanReducer.class,  // reducer class  
	         job); 
	        
	    return job.waitForCompletion(true) ? 0 : 1;        
	        
	} 
	    
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
            // extract userKey from the compositeKey (userId + counter)
            ImmutableBytesWritable userKey = new ImmutableBytesWritable(row.get(), 0, Bytes.SIZEOF_INT);
            try {
            	DataOutputBuffer out = new DataOutputBuffer();
            	out.reset();
                serializer.open(out);
                serializer.serialize(values);
                byte[] barray = out.getData();
                
            	BytesWritable one = new BytesWritable(barray);
                context.write(userKey, one);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
            numRecords++;
            if ((numRecords % 10000) == 0) {
                context.setStatus("mapper processed " + numRecords + " records so far");
            }
        }
        
        protected void cleanup(Context context) throws IOException, InterruptedException {
        	serializer = null;
        	out = null;
        }
    }

    public static class ScanReducer extends TableReducer<ImmutableBytesWritable, BytesWritable, ImmutableBytesWritable> {

    	HttpClient httpClient = null;
    	ThreadPoolExecutor threadPool = null;
    	int count = 0;
    	protected void setup(Context context) throws IOException, InterruptedException {
    		
        	httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        	httpClient.getHostConfiguration().setHost("192.168.1.8", 8080, "http");
        	httpClient.getParams().setSoTimeout(2000);
        	
        	threadPool = new ThreadPoolExecutor(
        			2, // core size
        			5, // max size
        			3, // ttl time
        			TimeUnit.SECONDS, 
        			new LinkedBlockingQueue<Runnable>(),
        			new ThreadPoolExecutor.AbortPolicy());
    	}
    	
        public void reduce(ImmutableBytesWritable key, Iterable<BytesWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (final BytesWritable val : values) {
            	PostMethod post = new PostMethod("/demo/mapredresult/");
            	ResultSendTask task = new ResultSendTask(httpClient, post, val);   	
				threadPool.submit(task);
                sum += 1;
            }
            
            System.out.println(String.format("stats :   key : %d,  count : %d", Bytes.toInt(key.get()), sum));
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
				RequestEntity entity=new InputStreamRequestEntity(dinbuf);
	            post.setRequestEntity(entity);
	             // execute the method
	            httpclient.executeMethod(post);
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
			System.out.println("===:" + path);
			System.setProperty("hadoop.home.dir", "D:\\n.repo\\hadoop-2.2.0");

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws Exception {

    }

}
