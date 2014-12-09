/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package com.obal.core.hbase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;  
import org.apache.hadoop.hbase.client.Scan;  
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;  
import org.apache.hadoop.hbase.mapreduce.ResultSerialization;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;  
import org.apache.hadoop.hbase.mapreduce.TableMapper;  
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.mapreduce.Job;  
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;  
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Properties;
  
/** 
 * HBase is used as data source as well as data sink. This MapReduce job will try to copy data from 
 * the source table to the target table. Note that no reduce task needed. 
 * @author despird
 * @version 0.1 2014-2-1
 * @since 0.1
 * 
 */  
public class HMapRedScan{  
  
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
                Result.class,   // mapper output value  
                job);  
        TableMapReduceUtil.initTableReducerJob(                 
                "obal.garbage",  // output table                   
                ScanReducer.class,  // reducer class  
                job); 
        
        job.setNumReduceTasks(0);// No reducer actually needed here  
  
        return job.waitForCompletion(true) ? 0 : 1;        
        
    }  
  
    public static class ScanMapper extends TableMapper<ImmutableBytesWritable, ObjectWritable> {  
    	
    	String principal = null;
    	
    	protected void setup(Context context) throws IOException, InterruptedException {
    		principal = context.getConfiguration().get("mrscan.principal");
    	}
    	 
        public void map(ImmutableBytesWritable row, Result value, Context context)  
                throws IOException, InterruptedException {  
        	System.out.println("Row:"+row);
        	ObjectWritable ow = new ObjectWritable(Result.class, value);
            context.write(row, ow);  
        }
   
        protected void cleanup(Context context) throws IOException, InterruptedException {
        	// Nothing
        }
    }
    
    public static class ScanReducer extends TableReducer<ImmutableBytesWritable, ObjectWritable, ImmutableBytesWritable> {
    	
    	Serializer<Result> serializer;
    	String feedback = "__DONE__";
    	HttpClient httpClient = null;
    	PostMethod post = null;
    	
    	protected void setup(Context context) throws IOException, InterruptedException {
    		ResultSerialization rstSerialUtil = new ResultSerialization();
        	serializer = rstSerialUtil.getSerializer(Result.class);
        	httpClient = new HttpClient();
        	httpClient.getHostConfiguration().setHost("192.168.1.154", 8080, "http");
        	post = new PostMethod("/demo/mapredresult/");
    	}
       
    	public void reduce(ImmutableBytesWritable key, Iterable<ObjectWritable> values, Context context)
                throws IOException, InterruptedException {
       
            for (ObjectWritable val : values) {
//                PipedOutputStream out = new PipedOutputStream();
//                serializer.open(out);
//            	serializer.serialize(val);
//            	sendToClient(out);
            	System.out.println("----------"+key);
            }            
        }
        
        private void sendToClient(PipedOutputStream out){
        	
        	PipedInputStream in = new PipedInputStream();
        	try {
				out.connect(in);
				RequestEntity entity=new InputStreamRequestEntity(in);
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
        	serializer = null;
        	httpClient = null;
        	post = null;
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
    
    public static void main(String[] args) {
		Properties prop = new Properties();

		prop.setProperty("log4j.rootCategory", "DEBUG, CONSOLE");
		prop.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss,SSS} [%t] %-5p %C{1} : %m%n");
		
		PropertyConfigurator.configure(prop);
    	String source = "obal.meta.attr";
    	Scan scan = new Scan();
    	scan.addFamily("c0".getBytes());
    	HMapRedScan hmrScan = new HMapRedScan(source, scan);
    	hmrScan.init();
    	try {
			hmrScan.scan();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
}  