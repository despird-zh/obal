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
package com.obal.core.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class CacheManager{

	Disruptor<CacheEvent> disruptor = null;
	RingBuffer<CacheEvent> ringBuffer = null;
	ExecutorService executor = null;
	CacheBridge<CacheEvent> cacheBridge = null;
	
	private static CacheManager instance;
	
	private CacheManager(){

		initial();
	}
	
	public static CacheManager getInstance(){
		
		if(null == instance)
			instance = new CacheManager();
		
		return instance;
	}
	
	public <K> void cachePut(K entry){
		
		// Publishers claim events in sequence
		long sequence = ringBuffer.next();
		CacheEvent event = ringBuffer.get(sequence);

		event.setEntry(null); // this could be more complex with multiple fields

		// make the event available to EventProcessors
		ringBuffer.publish(sequence);  
	}
	
	@SuppressWarnings("unchecked")
	public <K> K catchGet(String entityName, String key){
		
		return (K)cacheBridge.doCacheGet(entityName, key);
	}
	
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
