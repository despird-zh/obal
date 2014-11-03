package com.obal.core.cache;

import com.lmax.disruptor.EventHandler;
import com.obal.core.AccessorFactory;
import com.obal.core.accessor.EntityAccessor;
import com.obal.core.cache.CacheEvent;
import com.obal.core.security.Principal;
import com.obal.exception.BaseException;


public class CacheRedisHandler implements EventHandler<CacheEvent> {
		
	public void onEvent(final CacheEvent event,
            final long sequence,
            final boolean endOfBatch) throws Exception {
		
		EntityAccessor eaccessor = getEntityAccessor(event.getEntry().getEntityName());		
		eaccessor.putEntry(event.getEntry());
    }
	
	public EntityAccessor getEntityAccessor(String entityName) throws BaseException{
		
		Principal principal = null;		
		return AccessorFactory.getInstance().buildEntityAccessor("hbase", principal, entityName);
		
	}
}
