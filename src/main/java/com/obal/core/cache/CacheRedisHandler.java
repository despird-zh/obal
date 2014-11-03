package com.obal.core.cache;

import com.lmax.disruptor.EventHandler;
import com.obal.core.AccessorFactory;
import com.obal.core.EntryKey;
import com.obal.core.IEntityAccessor;
import com.obal.core.cache.CacheEvent;
import com.obal.core.security.Principal;


public class CacheRedisHandler<K extends EntryKey> implements EventHandler<CacheEvent> {
		
	public void onEvent(final CacheEvent event,
            final long sequence,
            final boolean endOfBatch) throws Exception {
		
		Principal principal = null;		
		IEntityAccessor<K> eaccessor = 
				AccessorFactory.getInstance().buildEntityAccessor("hbase", 
						principal, 
						event.getEntry().getEntityName());	
		
		eaccessor.putEntry((K)event.getEntry());
    }

}
