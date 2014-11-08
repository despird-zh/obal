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

import com.lmax.disruptor.EventHandler;
import com.obal.cache.CacheEvent;
import com.obal.core.AccessorFactory;
import com.obal.core.EntryKey;
import com.obal.core.IEntityAccessor;
import com.obal.core.security.Principal;
import com.obal.core.util.CoreConstants;
import com.obal.exception.AccessorException;
import com.obal.exception.EntityException;
/**
 * Implmentation of CacheBridge for Redis cache
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * @since 0.1
 * 
 **/
public class CacheRedisHandler<K extends EntryKey> implements CacheBridge<K>{
	
	/**
	 * cache data 
	 * 
	 * @param cacheDate the data to be cached
	 **/
	public void doCachePut(K cacheData){
		
		Principal principal = null;		
		IEntityAccessor<K> eaccessor = null;
		try {
			eaccessor = 
				AccessorFactory.getInstance().buildEntityAccessor(CoreConstants.BUILDER_REDIS, 
						principal, 
						cacheData.getEntityName());	
				

			eaccessor.doPutEntry(cacheData);
			
		} catch (AccessorException e) {
			
			e.printStackTrace();
		} catch (EntityException e) {
			
			e.printStackTrace();
		}finally{
			
			eaccessor.release();
		}
	}
	
	/**
	 * cache get  
	 * 
	 * @param entityName the entity name
	 * @param key the key of target entry
	 **/
	public K doCacheGet(String entityName, String key){
		
		Principal principal = null;		
		K cacheData = null;
		IEntityAccessor<K> eaccessor = null;
		try {
			eaccessor = 
				AccessorFactory.getInstance().buildEntityAccessor(CoreConstants.BUILDER_REDIS, 
						principal, 
						entityName);	
				
			cacheData = eaccessor.doGetEntry(key);
			
		} catch (AccessorException e) {
			
			e.printStackTrace();
		} catch (EntityException e) {
			
			e.printStackTrace();
		}finally{
			
			eaccessor.release();
		}
		
		return cacheData;
	}

	@Override
	public EventHandler<CacheEvent> getEventHandler() {
		
		return new EventHandler<CacheEvent>(){

			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(CacheEvent event, long sequence,
					boolean endOfBatch) throws Exception {
				
				doCachePut((K)event.getEntry());
			}			
		};
	}

	@Override
	public void doCachePutAttr(EntryKey entryKey, String attrName, Object value) {
		Principal principal = null;		
		IEntityAccessor<K> eaccessor = null;
		try {
			eaccessor = 
				AccessorFactory.getInstance().buildEntityAccessor(CoreConstants.BUILDER_REDIS, 
						principal, 
						entryKey.getEntityName());	
				

			eaccessor.doPutEntryAttr(entryKey.getKey(), attrName, value);
			
		} catch (AccessorException e) {
			
			e.printStackTrace();
		} catch (EntityException e) {
			
			e.printStackTrace();
		}finally{
			
			eaccessor.release();
		}
	}

	@Override
	public <M> M doCacheGetAttr(String entityName, String key, String attrName) {
		Principal principal = null;		
		M cacheAttr = null;
		IEntityAccessor<K> eaccessor = null;
		try {
			eaccessor = 
				AccessorFactory.getInstance().buildEntityAccessor(CoreConstants.BUILDER_REDIS, 
						principal, 
						entityName);	
				
			cacheAttr = eaccessor.doGetEntryAttr(key, attrName);
			
		} catch (AccessorException e) {
			
			e.printStackTrace();
		} catch (EntityException e) {
			
			e.printStackTrace();
		}finally{
			
			eaccessor.release();
		}
		
		return cacheAttr;
	}

	@Override
	public void doCacheDel(String entityName, String... keys) {
		Principal principal = null;		
		IEntityAccessor<K> eaccessor = null;
		try {
			eaccessor = 
				AccessorFactory.getInstance().buildEntityAccessor(CoreConstants.BUILDER_REDIS, 
						principal, 
						entityName);	
				
			eaccessor.doDelEntry(keys);
			
		} catch (AccessorException e) {
			
			e.printStackTrace();
		} catch (EntityException e) {
			
			e.printStackTrace();
		}finally{
			
			eaccessor.release();
		}

	}
}
