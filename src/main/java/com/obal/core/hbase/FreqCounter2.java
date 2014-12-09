package com.obal.core.hbase;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
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
import org.apache.hadoop.io.DataOutputByteBuffer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.log4j.PropertyConfigurator;

/**
 * counts the number of userIDs
 * 
 * @author sujee ==at== sujee.net
 * 
 */
public class FreqCounter2 {

    static class Mapper1 extends TableMapper<ImmutableBytesWritable, BytesWritable> {

        private int numRecords = 0;
        private static final IntWritable one = new IntWritable(1);
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
                System.out.println("bytearray:"+ Bytes.toString(barray));
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

    public static class Reducer1 extends TableReducer<ImmutableBytesWritable, BytesWritable, ImmutableBytesWritable> {

    	HttpClient httpClient = null;
    	PostMethod post = null;
    	String feedback = "__DONE__";

    	protected void setup(Context context) throws IOException, InterruptedException {
    		
        	httpClient = new HttpClient();
        	httpClient.getHostConfiguration().setHost("192.168.1.8", 8080, "http");
        	post = new PostMethod("/demo/mapredresult/");
    	}
    	
        public void reduce(ImmutableBytesWritable key, Iterable<BytesWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (BytesWritable val : values) {
            	sendToClient(val);
                sum += 1;
            }

            //Put put = new Put(key.get());
            //put.add(Bytes.toBytes("details"), Bytes.toBytes("total"), Bytes.toBytes(sum));
            System.out.println(String.format("stats :   key : %d,  count : %d", Bytes.toInt(key.get()), sum));
            //System.out.println(String.format("resutl:" ));
            //context.write(key, put);
        }
        
        private void sendToClient(BytesWritable val){
        	
        	try {
        		
        		DataInputBuffer inb = new DataInputBuffer();
        		inb.reset(val.getBytes(),0,val.getLength());
				RequestEntity entity=new InputStreamRequestEntity(inb);
	            post.setRequestEntity(entity);
	             // execute the method
	            httpClient.executeMethod(post);
	            feedback = post.getResponseBodyAsString();	           
	            System.out.println("-- feedback:" + feedback);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
        protected void cleanup(Context context) throws IOException, InterruptedException {
        	httpClient = null;
        	post = null;
        }
    }
    
    public static void main(String[] args) throws Exception {
		Properties prop = new Properties();

		prop.setProperty("log4j.rootCategory", "DEBUG, CONSOLE");
		prop.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss,SSS} [%t] %-5p %C{1} : %m%n");
		
		PropertyConfigurator.configure(prop);
		
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", "192.168.1.133");
        conf.set("hbase.master", "192.168.1.133:60010");
		File file = new File(".");
		try {

			String path = file.getCanonicalPath();
			System.out.println("===:" + path);
			System.setProperty("hadoop.home.dir", "D:\\n.repo\\hadoop-2.2.0");

		} catch (IOException e) {
			e.printStackTrace();
		}
        Job job = new Job(conf, "Hbase_FreqCounter1");
        job.setJarByClass(FreqCounter2.class);
        Scan scan = new Scan();
        String columns = "c0"; // comma seperated
        scan.addFamily(columns.getBytes());
        //scan.setFilter(new FirstKeyOnlyFilter());
        TableMapReduceUtil.initTableMapperJob(
        		"obal.meta.attr", 
        		scan, 
        		Mapper1.class, 
        		ImmutableBytesWritable.class,
        		BytesWritable.class, 
                job);
        TableMapReduceUtil.initTableReducerJob("summary_user", Reducer1.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
