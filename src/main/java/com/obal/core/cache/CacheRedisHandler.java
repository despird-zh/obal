package com.obal.core.cache;

import com.lmax.disruptor.EventHandler;
import com.obal.core.cache.CacheEvent;


public class CacheRedisHandler implements EventHandler<CacheEvent> {
	
	
	public void onEvent(final CacheEvent event,
            final long sequence,
            final boolean endOfBatch) throws Exception {

    }
}
