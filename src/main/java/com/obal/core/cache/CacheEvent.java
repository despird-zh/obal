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
	
    private EntryKey entry;

    /**
     * Get the entry information 
     **/
    public EntryKey getEntry() {
    	
    	return this.entry;
    }

    /**
     * Set the entry information 
     **/
    public void setEntry(EntryKey entry) {
    	
    	this.entry = entry;
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
