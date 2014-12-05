package com.obal.disruptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.obal.exception.RingEventException;

public class EventDispatcher {

	static Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);
	Executor executor = null;
	Disruptor<RingEvent> disruptor = null;
	RingEventHandler handler = new RingEventHandler();
	List<EventHooker> hookers = new ArrayList<EventHooker>();
	
	public static EventDispatcher instance;
	
	private void initialize(){
		// Executor that will be used to construct new threads for consumers
        this.executor = Executors.newCachedThreadPool();
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;
        EventFactory<RingEvent> auditbuilder = RingEvent.EVENT_FACTORY;
        // Construct the Disruptor
        disruptor = new Disruptor<RingEvent>(auditbuilder, bufferSize, executor);

        // Connect the handler
        disruptor.handleEventsWith(handler);
  	}
	
	private void onRingEvent(RingEvent ringevent, long sequence, boolean endOfBatch){
		
		for(EventHooker eventHooker : hookers){
			
			EventPayload payload = ringevent.getPayload();
			
			if(eventHooker.match(ringevent.getPayload())){
				
				try {
					
					eventHooker.processPayload(ringevent.getPayload());
					
				} catch (RingEventException e) {

					LOGGER.error("Error when processing event[{}] payload",e,payload.getType());
				}
				
				continue;
			}
		}
	}
	
	public void regEventHooker(EventHooker eventHooker){
		
		hookers.add(eventHooker);
	}
	
	public static class RingEventHandler  implements EventHandler<RingEvent>{

		@Override
		public void onEvent(RingEvent ringevent, long sequence,	boolean endOfBatch)
				throws Exception {
			instance.onRingEvent(ringevent,sequence,endOfBatch);
		}

	}
}
