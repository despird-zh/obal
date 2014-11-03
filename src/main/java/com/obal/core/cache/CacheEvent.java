package com.obal.core.cache;

import com.lmax.disruptor.EventFactory;
import com.obal.core.EntryKey;

public class CacheEvent{
	
    private EntryKey entry;

    public EntryKey getEntry() {
    	
    	return this.entry;
    }

    public void setEntry(EntryKey entry) {
    	
    	this.entry = entry;
    }

	public final static EventFactory<CacheEvent> EVENT_FACTORY =
		 new EventFactory<CacheEvent>()
		 {
		     public CacheEvent newInstance() {
		  	 	return new CacheEvent();
		 }
	};
}	
