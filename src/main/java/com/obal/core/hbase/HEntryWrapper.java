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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.EntryInfo;
import com.obal.core.meta.EntityAttr;

public abstract class HEntryWrapper<GB extends EntryInfo> {

	public static Logger LOGGER = LoggerFactory.getLogger(HEntryWrapper.class);
	/**
	 * Get primitive value from cell, primitive means int,long,double,string,date
	 * 
	 * @param attr the attribute of entry
	 * @param cell the Cell of certain Row in hbase
	 * 
	 * @return Object the value object
	 **/
	public Object getPrimitiveValue(EntityAttr attr, byte[] value){
		
		Object rtv = null;

		switch(attr.type){
			case INT:
				rtv = Bytes.toInt(value);
				break;
			case BOOL:
				rtv = Bytes.toBoolean(value);
				break;
			case DOUBLE:
				rtv = Bytes.toDouble(value);
				break;
			case LONG:
				rtv = Bytes.toLong(value);
				break;
			case STRING:
				rtv = Bytes.toString(value);
				break;
			case DATE:
				Long time = Bytes.toLong(value);
				rtv = new Date(time);
				break;
			default:
				break;
		}
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("PRIMITIVE -> attribute:{} | value:{}", new String[]{attr.getAttrName(),String.valueOf(rtv)});
		}
		return rtv;
	}

	/**
	 * Get map value from cells, every cell is the entry of map
	 * 
	 * @param attr the attribute of entry
	 * @param cells the Cells of certain Row in hbase
	 * 
	 * @return Object the map object
	 **/
	public Map<String,Object> getMapValue(EntityAttr attr, NavigableMap<byte[], byte[]> cells){

		byte[] qualifier = attr.getQualifier().getBytes();
		Map<String, Object> map = new HashMap<String, Object>();

		for(Map.Entry<byte[], byte[]> e:cells.entrySet()){

			byte[] cqualifier = e.getKey();
			if(Bytes.startsWith(cqualifier, qualifier)){
				byte[] key = Bytes.tail(cqualifier, cqualifier.length - qualifier.length);
				byte[] bytes = e.getValue();
				switch(attr.type){
					case INT:
						map.put(new String(key), Bytes.toInt(bytes));
						break;
					case BOOL:
						map.put(new String(key), Bytes.toBoolean(bytes));
						break;
					case DOUBLE:
						map.put(new String(key), Bytes.toDouble(bytes));
						break;
					case LONG:
						map.put(new String(key), Bytes.toLong(bytes));
						break;
					case STRING:
						map.put(new String(key), Bytes.toString(bytes));
						break;
					case DATE:
						Long time = Bytes.toLong(bytes);
						map.put(new String(key), new Date(time));
						break;
					default:
						
						break;
				}
				if(LOGGER.isDebugEnabled()){
					
					LOGGER.debug("MAP -> attribute:{} - key:{} - value:{}", 
							new String[]{attr.getAttrName(),new String(key),String.valueOf(map.get(new String(key)))});
				}
			}
		}				
				
		return map;
	}

	/**
	 * Get list value from cells, every cell is the entry of map
	 * 
	 * @param attr the attribute of entry
	 * @param cells the Cells of certain Row in hbase
	 * 
	 * @return Object the list object
	 **/
	public List<Object> getListValue(EntityAttr attr, NavigableMap<byte[], byte[]> cells){

		byte[] qualifier = attr.getQualifier().getBytes();
		List<Object> list = new ArrayList<Object>();

		for(byte[] e:cells.descendingKeySet()){
			
			if(Bytes.startsWith(e, qualifier)){
				byte[] bytes = cells.get(e);
				switch(attr.type){
					case INT:
						list.add(Bytes.toInt(bytes));
						break;
					case BOOL:
						list.add(Bytes.toBoolean(bytes));
						break;
					case DOUBLE:
						list.add(Bytes.toDouble(bytes));
						break;
					case LONG:
						list.add(Bytes.toLong(bytes));
						break;
					case STRING:
						list.add(Bytes.toString(bytes));
						break;
					case DATE:
						Long time = Bytes.toLong(bytes);
						list.add(new Date(time));
						break;
					default:
						
						break;
				}
				if(LOGGER.isDebugEnabled()){
					
					LOGGER.debug("LIST -> attribute:{} - key:{} - value:{}", 
							new String[]{attr.getAttrName(),new String(e),String.valueOf(list.get(list.size()-1))});
				}
			}
		}				
				
		return list;
	}


	/**
	 * Get Set value from cells, every cell is the element of set
	 * 
	 * @param attr the attribute of entry
	 * @param cells the Cells of certain Row in hbase
	 * 
	 * @return Object the list object
	 **/
	public Set<Object> getSetValue(EntityAttr attr, NavigableMap<byte[], byte[]> cells){

		byte[] qualifier = attr.getQualifier().getBytes();
		Set<Object> set = new HashSet<Object>();

		for(byte[] e:cells.descendingKeySet()){
			
			if(Bytes.startsWith(e, qualifier)){
				byte[] bytes = cells.get(e);
				switch(attr.type){
					case INT:
						set.add(Bytes.toInt(bytes));
						break;
					case BOOL:
						set.add(Bytes.toBoolean(bytes));
						break;
					case DOUBLE:
						set.add(Bytes.toDouble(bytes));
						break;
					case LONG:
						set.add(Bytes.toLong(bytes));
						break;
					case STRING:
						set.add(Bytes.toString(bytes));
						break;
					case DATE:
						Long time = Bytes.toLong(bytes);
						set.add(new Date(time));
						break;
					default:
						
						break;
				}
				
				if(LOGGER.isDebugEnabled()){
					
					LOGGER.debug("LIST -> attribute:{} - key:{} - value:{}", 
							new String[]{attr.getAttrName(),new String(e), set.toArray().toString()});
				}
			}
		}				
				
		return set;
	}
	
	/**
	 * Put the Primitive value to target Put operation
	 * 
	 * @param put the Hbase Put operation object
	 * @param attr the target attribute object
	 * @param value the value to be put 
	 * 
	 **/
	public void putPrimitiveValue(Put put, EntityAttr attr, Object value){
		byte[] bval = null;
    	if(value == null) return;    	
    	switch(attr.type){
			case INT:
				bval = Bytes.toBytes((Integer)value);
				break;
			case BOOL:
				bval = Bytes.toBytes((Boolean)value);
				break;
			case DOUBLE:
				bval = Bytes.toBytes((Double)value);
				break;
			case LONG:
				bval = Bytes.toBytes((Long)value);
				break;							
			case STRING:
				bval = Bytes.toBytes((String)value);
				break;
			case DATE:
				bval = Bytes.toBytes(((Date)value).getTime());
				break;						
			default:
				
				break;					
		}
    	put.add(attr.getColumn().getBytes(), attr.getQualifier().getBytes(), bval);
	}
	
	/**
	 * Put the map value to target Put operation object
	 * 
	 * @param put the Hbase Put operation object
	 * @param attr the target attribute object
	 * @param value the value to be put 
	 **/
	public void putMapValue(Put put, EntityAttr attr, Map<String,Object> mapVal){
		byte[] bval = null;
    	if(mapVal == null) return;    	
    	for(Map.Entry<String,Object> pe:mapVal.entrySet()){
	    	switch(attr.type){
				case INT:
					bval = Bytes.toBytes((Integer)pe.getValue());
					break;
				case BOOL:
					bval = Bytes.toBytes((Boolean)pe.getValue());
					break;
				case DOUBLE:
					bval = Bytes.toBytes((Double)pe.getValue());
					break;
				case LONG:
					bval = Bytes.toBytes((Long)pe.getValue());
					break;							
				case STRING:
					bval = Bytes.toBytes((String)pe.getValue());
					break;
				case DATE:
					bval = Bytes.toBytes(((Date)pe.getValue()).getTime());
					break;						
				default:
					
					break;					
			}
	    	String newQualifier = attr.getQualifier() + pe.getKey();
	    	put.add(attr.getColumn().getBytes(), newQualifier.getBytes(), bval);
    	}
    	
	}

	/**
	 * Put the list value to target Put operation object
	 * 
	 * @param put the Hbase Put operation object
	 * @param attr the target attribute object
	 * @param value the value to be put 
	 **/
	public void putListValue(Put put, EntityAttr attr, List<Object> listVal){
		byte[] bval = null;
    	if(listVal == null) return;    	
    	for(int i=0;i<listVal.size();i++){
    		
	    	switch(attr.type){
				case INT:
					bval = Bytes.toBytes((Integer)listVal.get(i));
					break;
				case BOOL:
					bval = Bytes.toBytes((Boolean)listVal.get(i));
					break;
				case DOUBLE:
					bval = Bytes.toBytes((Double)listVal.get(i));
					break;
				case LONG:
					bval = Bytes.toBytes((Long)listVal.get(i));
					break;							
				case STRING:
					bval = Bytes.toBytes((String)listVal.get(i));
					break;
				case DATE:
					bval = Bytes.toBytes(((Date)listVal.get(i)).getTime());
					break;						
				default:
					
					break;					
			}
	    	String newQualifier = attr.getQualifier() + i;
	    	put.add(attr.getColumn().getBytes(), newQualifier.getBytes(), bval);
    	}
    	
	}
	

	/**
	 * Put the set value to target Put operation object
	 * 
	 * @param put the Hbase Put operation object
	 * @param attr the target attribute object
	 * @param value the value to be put 
	 **/
	public void putSetValue(Put put, EntityAttr attr, Set<Object> setVal){
		byte[] bval = null;
    	if(setVal == null) return;  
    	Iterator<Object> iterator = setVal.iterator();
    	int i = 0;
    	while(iterator.hasNext()){
    		
    		Object val = iterator.next();
	    	switch(attr.type){
				case INT:
					bval = Bytes.toBytes((Integer)val);
					break;
				case BOOL:
					bval = Bytes.toBytes((Boolean)val);
					break;
				case DOUBLE:
					bval = Bytes.toBytes((Double)val);
					break;
				case LONG:
					bval = Bytes.toBytes((Long)val);
					break;							
				case STRING:
					bval = Bytes.toBytes((String)val);
					break;
				case DATE:
					bval = Bytes.toBytes(((Date)val).getTime());
					break;						
				default:
					
					break;					
			}
	    	
	    	String newQualifier = attr.getQualifier() + i;
	    	put.add(attr.getColumn().getBytes(), newQualifier.getBytes(), bval);
	    	i++;
    	}
    	
	}

	/**
	 * Wrap the rawentry into bean object
	 * 
	 * @param attrs the attributes of rawEntry
	 * @param rawEntry the entry information
	 * @return GB the bean object. 
	 **/
	public abstract GB wrap(List<EntityAttr> attrs, Result rawEntry);
	
	/**
	 * Parse bean object into raw Object
	 * 
	 * @param attrs the attributes of target entity
	 * @param entryInfo the entry information bean
	 * @return Object the raw object. 
	 **/	
	public abstract Put parse(List<EntityAttr> attrs, GB entryInfo);

	
	/**
	 * Wrap the rawentry into bean object
	 * @param entityName the entity name of rawEntry
	 * @param rawEntry the entry information
	 * 
	 * @return GB the bean object. 
	 **/
	public abstract GB wrap(String entityName, Result rawEntry);
}
