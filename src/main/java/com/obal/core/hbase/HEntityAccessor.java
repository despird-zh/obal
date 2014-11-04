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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.EntryFilter;
import com.obal.core.EntryInfo;
import com.obal.core.EntryKey;
import com.obal.core.accessor.EntityAccessor;
import com.obal.core.meta.AttrMode;
import com.obal.core.meta.BaseEntity;
import com.obal.core.meta.EntityAttr;
import com.obal.exception.AccessorException;

/**
 * Base class of entry accessor, it holds HConnection object 
 * 
 **/
public abstract class HEntityAccessor<GB extends EntryInfo> extends EntityAccessor<GB> implements HConnAware {
	
	Logger LOGGER = LoggerFactory.getLogger(HEntityAccessor.class);
	
	public HEntityAccessor(BaseEntity schema) {
		super(schema);
	}

	private HConnection conn;
	
	@Override
	public boolean isFilterSupported(EntryFilter<?> scanfilter,boolean throwExcep) throws AccessorException{
		
		if(!Filter.class.isInstance(scanfilter.getFilter())){
			
			if(throwExcep){
				throw new AccessorException("Filter:{} is expected.",Filter.class.getName());
			}
			return false;
		}
		
		return true;
	}


	/**
	 * get entry wrapper
	 * @return wrapper object 
	 **/
	public abstract HEntryWrapper<GB> getEntryWrapper();
		
	@Override
	public List<GB> scanEntry(EntryFilter<?> scanfilter) throws AccessorException{
		
		List<GB> result = new LinkedList<GB>();
		HEntryWrapper<GB> wrapper = this.getEntryWrapper();
		HTableInterface table = null;
		Scan scan=new Scan();
		try {
			
			if(scanfilter != null && scanfilter != null){
				
				isFilterSupported(scanfilter,true);
				
				Filter hfilter = (Filter) scanfilter.getFilter();
				scan.setFilter(hfilter);
			}
			
			table = conn.getTable(super.getEntitySchema().getSchemaBytes());
			
			ResultScanner rs = table.getScanner(scan);
			
			for (Result r : rs) {  
			     GB entry = wrapper.wrap(super.getEntitySchema().getEntityName(),r);
			     result.add(entry);
			}
		} catch (IOException e) {
			
			throw new AccessorException("Scan exception .",e);
		}finally{
			
			if(table != null)
				try {
					table.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}
		
		return result;
	}

	@Override
	public void setConnection(HConnection connection) {
		this.conn = connection;
	}

	@Override
	public HConnection getConnection() {
		
		return conn;
	}
	
	@SuppressWarnings("unchecked")
	public <K> K getEntryAttr(String entryKey ,String attrName ) throws AccessorException{
		
		HTableInterface table = null;
		Object rtv = null;
		BaseEntity entitySchema = (BaseEntity)getEntitySchema();
		EntityAttr attr = entitySchema.getEntityMeta().getAttr(attrName);
        try {
        	byte[] column = attr.getColumn().getBytes();
        	byte[] qualifier = attr.getQualifier().getBytes();
        	table = getConnection().getTable(entitySchema.getSchema());
        	Get get = new Get(entryKey.getBytes());
        	
        	HEntryWrapper<GB> wrapper = (HEntryWrapper<GB>)getEntryWrapper();

        	if(attr.mode == AttrMode.PRIMITIVE){
				get.addColumn(column, qualifier);
	        	Result entry = table.get(get);
	        	byte[] cell = entry.getValue(column, qualifier);
				rtv = wrapper.getPrimitiveValue(attr, cell);		
			}
			
			if(attr.mode == AttrMode.MAP){
				get.addFamily(column);
	        	Result entry = table.get(get);
	        	NavigableMap<byte[], byte[]> cells = entry.getFamilyMap(column);
				rtv = wrapper.getMapValue(attr, cells);			
			}
			
			if(attr.mode == AttrMode.LIST){
				get.addFamily(column);
	        	Result entry = table.get(get);
	        	NavigableMap<byte[], byte[]> cells = entry.getFamilyMap(column);
				rtv = wrapper.getListValue(attr, cells);				
			}
           
        } catch (IOException e) {  
        	
            throw new AccessorException("Error get entry row,key:{} attr:{}",e,entryKey,attr.getAttrName());
        }finally{
        	
        	try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}        	
        }
		return (K)rtv;
	}
	
	@Override
	public GB getEntry(String rowkey) throws AccessorException {
		HTableInterface table = null;
		GB rtv = null;
		BaseEntity entrySchema = (BaseEntity)getEntitySchema();
        try {
        	
           table = getConnection().getTable(entrySchema.getSchema());
           Get get = new Get(rowkey.getBytes());
           
           Result r = table.get(get);
           HEntryWrapper<GB> wrapper = (HEntryWrapper<GB>)getEntryWrapper();

           rtv = wrapper.wrap(super.getEntitySchema().getEntityName(),r);
           
        } catch (IOException e) {  
        	
            throw new AccessorException("Error get entry row,key:{}",e,rowkey);
        }finally{
        	
        	try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}        	
        }
		return rtv;
	}
	
	@SuppressWarnings("unchecked")
	public EntryKey putEntryAttr(String entryKey, String attrName,  Object value) throws AccessorException{
		
		HTableInterface table = null;
		EntryKey rtv = null;
		BaseEntity entitySchema = (BaseEntity)getEntitySchema();
		EntityAttr attr = entitySchema.getEntityMeta().getAttr(attrName);
        try {  
            table = getConnection().getTable(entitySchema.getSchema());
            // support check.
            HEntryWrapper<GB> wrapper = (HEntryWrapper<GB>)this.getEntryWrapper();

            Put put =  new Put(entryKey.getBytes());
            
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("--==>>attr:{} - value:{}",attr.getAttrName(),value);
            }

            if(attr.mode == AttrMode.PRIMITIVE){
        				
            	wrapper.putPrimitiveValue(put, attr, value);		
            }
        			
        	if(attr.mode == AttrMode.MAP){

        		if(!(value instanceof Map<?,?>))
        			throw new AccessorException("the attr:{} value is not Map object",attrName);
        		
        		wrapper.putMapValue(put, attr, (Map<String,Object>)value);	
        	}
        			
        	if(attr.mode == AttrMode.LIST){
        		
        		if(!(value instanceof List<?>))
        			throw new AccessorException("the attr:{} value is not List object",attrName);
        		
        		wrapper.putListValue(put, attr, (List<Object>)value);	
        	}
        	
        	table.put(put);
        	table.flushCommits();
        	rtv = new EntryKey(entitySchema.getEntityName(),entryKey);
        	
        } catch (IOException e) {  
        	 throw new AccessorException("Error put entry row,key:{},attr:{},value{}",e,entryKey,attrName,value.toString());
        }finally{
        	
        	try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}        	
        }
		return rtv;
	}
	
	@Override
	public EntryKey putEntry(GB entryInfo) throws AccessorException {
		HTableInterface table = null;
		EntryKey rtv = null;
		BaseEntity entrySchema = (BaseEntity)getEntitySchema();
        try {  
            table = getConnection().getTable(entrySchema.getSchema());
            HEntryWrapper<GB> wrapper = this.getEntryWrapper();
 
            Put put = (Put)wrapper.parse(entrySchema.getEntityMeta().getAllAttrs(),entryInfo);
        	table.put(put);
        	table.flushCommits();
        	rtv = entryInfo.getEntryKey();
        	
        } catch (IOException e) {  
        	 throw new AccessorException("Error put entry row,key:{}",e,entryInfo.getKey());
        }finally{
        	
        	try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}        	
        }
		return rtv;
	}
	
	@Override
	public void deleteEntry(String... rowkey) throws AccessorException {
		HTableInterface table = null;
		BaseEntity entrySchema = (BaseEntity)getEntitySchema();
		try {
			table = getConnection().getTable(entrySchema.getSchema());
			List<Delete> list = new ArrayList<Delete>();
			for(String key:rowkey){
				
				if(StringUtils.isBlank(key)) continue;
				
				Delete d1 = new Delete(key.getBytes());  
				list.add(d1); 
			}
	        table.delete(list);
	        table.flushCommits();
		} catch (IOException e) {
			throw new AccessorException("Error delete entry row, key:{}",e,rowkey);
		}finally{
        	
        	try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}        	
        }        
	}
	
	@Override
	public void release() {
		try {
			// embed means share connection, close it directly affect other accessors using this conn.
			if (conn != null && !isEmbed()){
				this.conn.close();
				this.conn = null;
			}

			super.release();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
