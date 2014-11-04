package com.obal.core.cache;

import com.lmax.disruptor.EventHandler;
import com.obal.core.AccessorFactory;
import com.obal.core.EntryKey;
import com.obal.core.IEntityAccessor;
import com.obal.core.cache.CacheEvent;
import com.obal.core.security.Principal;
import com.obal.exception.AccessorException;
import com.obal.exception.EntityException;

public class CacheRedisHandler<K extends EntryKey> implements CacheBridge<K>{
		
	public void doCachePut(K cacheData){
		
		Principal principal = null;		
		IEntityAccessor<K> eaccessor = null;
		try {
			eaccessor = 
				AccessorFactory.getInstance().buildEntityAccessor("hbase", 
						principal, 
						cacheData.getEntityName());	
		
		
			eaccessor.putEntry(cacheData);
		} catch (AccessorException e) {
			
			e.printStackTrace();
		} catch (EntityException e) {
			
			e.printStackTrace();
		}finally{
			
			eaccessor.release();
		}
	}
	
	public K doCacheGet(String entityName, String key){
		
		return null;
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
}
