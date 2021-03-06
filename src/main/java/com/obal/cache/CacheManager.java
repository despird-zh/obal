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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.obal.core.EntryKey;

/**
 * CacheManager provide entrance to get/put entry in-out backend cache.
 * <p>
 * cache data <b>Put</b> operation runs in producer/consumer mode(disruptor), it's in asynchronized mode.
 * cache data <b>Get</b> operation runs in thread safe mode. entry be fetched directly.
 * </p>
 * 
 *  @author despird
 *  @version 0.1 2014-2-1
 *  @since 0.1
 *  
 **/
public class CacheManager{

	Disruptor<CacheEvent> disruptor = null;
	RingBuffer<CacheEvent> ringBuffer = null;
	ExecutorService executor = null;
	CacheBridge<? extends EntryKey> cacheBridge = null;
	
	/** singleton instance */ 
	private static CacheManager instance;
	
	/** default constructor */
	private CacheManager(){

		initial();
	}
	
	/**
	 * Get the instance of CacheManager 
	 * 
	 * @return CacheManager the singleton instance of manager.
	 * 
	 **/
	public static CacheManager getInstance(){
		
		if(null == instance)
			instance = new CacheManager();
		
		return instance;
	}
	
	/**
	 * Put entry into cache. Internally entry will be posted to cache asynchronously.
	 * the cached entry must be EntryKey subclass instance.
	 * 
	 * @param entry the entry object to be cached.
	 *  
	 **/
	public <K extends EntryKey> void cachePut(K entry){
		
		// Publishers claim events in sequence
		long sequence = ringBuffer.next();
		CacheEvent event = ringBuffer.get(sequence);
		event.type(CacheEvent.EVT_PUT);
		event.setPutEntryData(entry); // this could be more complex with multiple fields

		// make the event available to EventProcessors
		ringBuffer.publish(sequence);  
	}
	
	/**
	 * Put entry attribute data in cache. it need entity and key information, here they 
	 * are wrapped in entryKey object.
	 * 
	 * @param entryKey the entry key object to hold key and entity information
	 * @param attrName the attribute name
	 * @param value the attribute value object.
	 **/
	public void cachePutAttr(EntryKey entryKey, String attrName, Object value){
		
		// Publishers claim events in sequence
		long sequence = ringBuffer.next();
		CacheEvent event = ringBuffer.get(sequence);
		event.type(CacheEvent.EVT_PUT_ATTR);
		event.setPutAttrData(entryKey.getKey(), entryKey.getEntityName(), attrName, value);
		// make the event available to EventProcessors
		ringBuffer.publish(sequence);  
	}
	
	/**
	 * Fetch entry from cache, the returned value must be the EntryKey subclass instance. 
	 * 
	 * @param entityName the entity name
	 * @param key the key of entry data 
	 **/
	@SuppressWarnings("unchecked")
	public <K extends EntryKey> K cacheGet(String entityName, String key){
		
		return (K)cacheBridge.doCacheGet(entityName, key);
	}

	/**
	 * Fetch entry attribute from cache, the returned value wrap the List,SET, Map. 
	 * 
	 * @param entityName the entity name
	 * @param key the key of entry data 
	 * @param attrName the attribute name
	 * 
	 **/
	@SuppressWarnings("unchecked")
	public <M> M cacheGetAttr(String entityName, String key, String attrName){
		
		return (M)cacheBridge.doCacheGetAttr(entityName, key, attrName);
	}
	
	/**
	 * Delete entry from cache
	 * 
	 * @param entityName the entity name
	 * @param keys the string array of key
	 * 
	 **/
	public void cacheDel(String entityName, String ...keys){
		// Publishers claim events in sequence
		long sequence = ringBuffer.next();
		CacheEvent event = ringBuffer.get(sequence);
		event.type(CacheEvent.EVT_DEL);
		event.setDelData(entityName, keys);
		// make the event available to EventProcessors
		ringBuffer.publish(sequence);  
		cacheBridge.doCacheDel(entityName, keys);
	}
	
	/**
	 * initial process 
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initial(){
		
		executor = Executors.newFixedThreadPool(3);
		disruptor =
		new Disruptor<CacheEvent>(CacheEvent.EVENT_FACTORY, 
			256, 
			executor,
			ProducerType.SINGLE,
			new SleepingWaitStrategy());
		
		cacheBridge = new CacheRedisHandler();
		disruptor.handleEventsWith(cacheBridge.getEventHandler());		
		ringBuffer = disruptor.start();
	}
	
}
