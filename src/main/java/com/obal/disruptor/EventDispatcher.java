package com.obal.disruptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.obal.audit.AuditEvent;
import com.obal.audit.AuditHandler;

public class EventDispatcher {

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
		
		for(EventHooker<?> eventHooker : hookers){
			
			if(eventHooker.match(ringevent.getType(), ringevent.getPayload())){
				
				eventHooker.onEvent(ringevent.getPayload());
			}
		}
	}
	
	public void regEventHooker(EventHooker<?> eventHooker){
		
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
