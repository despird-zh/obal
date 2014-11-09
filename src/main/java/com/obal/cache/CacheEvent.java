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
package com.obal.cache;

import com.lmax.disruptor.EventFactory;
import com.obal.core.EntryKey;

/**
 * CacheEvent is the placeholder element in disruptor RingBuffer, it holds the entry information
 * the entry will be processed by handler of disruptor.
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * @since 0.1
 **/
public class CacheEvent{
	
	public static final String EVT_PUT = "_PUT_ENTRY";
	public static final String EVT_PUT_ATTR = "_PUT_ATTR";
	public static final String EVT_DEL = "_DEL_ENTRY";
	private String type = EVT_PUT;

	private Object value;
	
	/**
	 * Get the value of different events.
	 *  
	 **/
	@SuppressWarnings("unchecked")
	public <K> K value(){
		
		return (K)this.value;
	}
    
	/**
	 * Set the setting of entry attribute put operation.
	 *  
	 **/
	public void setPutAttrData(String key,String entity,String attr,Object value){
		
		PutAttrData s = new PutAttrData();
		s.key = key;
		s.entity = entity;
		s.attr = attr;
		s.value = value;
		
		this.value = s;
		
	}

	/**
	 * Set the setting of entry attribute put operation.
	 *  
	 **/
	public void setPutEntryData(EntryKey entryInfo){
		
		PutEntryData ped = new PutEntryData();
		ped.entryInfo = entryInfo;
		
		this.value= ped;
	}

	/**
	 * Set the setting of entry delete operation.
	 *  
	 **/
	public void setDelData(String entity, String ...keys){
		
		DelEntryData ded = new DelEntryData();
		ded.entity = entity;
		ded.keys = keys;
	}
	
    public static class PutAttrData{
    	
    	public String key = null;
    	public String entity = null;
    	public String attr = null;
    	public Object value = null;
    }
    
    public static class DelEntryData{
    	
    	public String entity = null;
    	public String[] keys = null;
    }
    
    public static class PutEntryData{
    	
    	public EntryKey entryInfo = null;
    }
    
    public String type(String type){
    	
    	if(null != type)
    		this.type = type;
    	
    	return this.type;
    }
    
    
    /**
     * The CacheEvent factroy disruptor use it to allocate elements in RingBuffer. 
     **/
	public final static EventFactory<CacheEvent> EVENT_FACTORY =
		 new EventFactory<CacheEvent>()
		 {
		     public CacheEvent newInstance() {
		  	 	return new CacheEvent();
		 }
	};
}	
