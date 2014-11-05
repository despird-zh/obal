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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;  
import org.apache.hadoop.hbase.client.Result;  
import org.apache.hadoop.hbase.client.Scan;  
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;  
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;  
import org.apache.hadoop.hbase.mapreduce.TableMapper;  
import org.apache.hadoop.mapreduce.Job;  

import com.obal.core.util.AdminUtils;
import com.obal.exception.AccessorException;
import com.obal.meta.EntityAttr;

import java.io.IOException;  
import java.util.List;
  
/** 
 * HBase is used as data source as well as data sink. This MapReduce job will try to copy data from 
 * the source table to the target table. Note that no reduce task needed. 
 * @author despird
 * @version 0.1 2014-2-1
 * @since 0.1
 * 
 */  
public class HMapRedScan{  
  
	private String source;
	
	private String target;
	
	private Scan scan;
	
	private List<EntityAttr> attrs;
	
	public HMapRedScan(String source,Scan scan){
		
		this.source = source;
		
		this.scan = scan;
		
	}
	
	public void setTarget(String target, List<EntityAttr> attrs){
		this.attrs = attrs;
		this.target = target;
		try {
			AdminUtils.createSchema(target, attrs);
		} catch (AccessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public int scan() throws Exception {  
        
        Job job = Job.getInstance();  
        Configuration conf = job.getConfiguration();
        conf.set("mrscan.limit", "20000");
        conf.set("mrscan.principal", "demouser");
        job.setJobName("HMRScan");
        job.setJarByClass(HMapRedScan.class);  
 
        TableMapReduceUtil.initTableMapperJob(                  
                source,   // input table                 
                scan,  // Scan instance to control CF and attribute selection                  
                ScanMapper.class,  // mapper class                  
                null,  // mapper output key                 
                null,   // mapper output value  
                job);  
        TableMapReduceUtil.initTableReducerJob(                 
                target,  // output table                   
                null,  // reducer class  
                job);  
        
        job.setNumReduceTasks(0);// No reducer actually needed here  
  
        return job.waitForCompletion(true) ? 0 : 1;
        
        
    }  
  
    public static class ScanMapper extends TableMapper<ImmutableBytesWritable, Put> {  
    	
    	String principal = null;
    	
    	protected void setup(Context context) throws IOException,
    	    InterruptedException {
    		principal = context.getConfiguration().get("mrscan.principal");
    	}
    	 
        public void map(ImmutableBytesWritable row, Result value, Context context)  
                throws IOException, InterruptedException {  
  
            // this example is just copying the data from the source table...  
            context.write(row, resultToPut(row, value));  
        }  
  
        private static Put resultToPut(ImmutableBytesWritable key, Result result) throws IOException {  
            Put put = new Put(key.get());  
            for (Cell kv : result.listCells()) {  
                put.add(kv);  
            }
            return put;  
        }  
    }  
}  