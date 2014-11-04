package com.obal.core.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class CacheManager{

	Disruptor<CacheEvent> disruptor = null;
	RingBuffer<CacheEvent> ringBuffer = null;
	ExecutorService executor = null;
	EventHandler<CacheEvent> handler = null;
	
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
	
	public <K> K catchGet(String entityName, String key){
		
		return null;
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
		
		handler = new CacheRedisHandler();
		disruptor.handleEventsWith(handler);		
		ringBuffer = disruptor.start();
	}
	
}
