package com.obal.core.cache;

import com.lmax.disruptor.EventHandler;

/**
 * Cache bridge handle the in-out exchange of data cache. In cache manager we use disruptor to handle 
 * the cache requests, so it provides EventHandler for disruptor. 
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * @since 0.1
 **/
public interface CacheBridge<K> {
	
	/**
	 * Get the event handler instance to swallow the disruptor ringbuffer event. 
	 **/
	public EventHandler<CacheEvent> getEventHandler();
	
	/**
	 * Put the entry data into cache
	 * 
	 * @param entry the entry data
	 **/
	public void doCachePut(K entry);
	
	/**
	 * Get the entry data from cache 
	 * 
	 * @param entityName the entity name
	 * @param key the key of entry
	 **/
	public K doCacheGet(String entityName, String key); 
}
