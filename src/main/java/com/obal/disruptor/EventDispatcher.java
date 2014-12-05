package com.obal.disruptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.obal.exception.RingEventException;

/**
 * EventDisptcher is a singleton pattern object. It holds the necessary objects
 * needed by Disruptor.
 * 
 * @author despird
 * @version 0.1 2014-6-2
 **/
public class EventDispatcher {

	static Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);

	/** the executor pool */
	Executor executor = null;

	/** the disruptor instance */
	Disruptor<RingEvent> disruptor = null;

	/** the event handler */
	RingEventHandler handler = new RingEventHandler();

	/** the event hooker list */
	Map<EventType, EventHooker> hookers = new HashMap<EventType, EventHooker>();

	/** single instance */
	private static EventDispatcher instance;

	/**
	 * default event disptacher
	 **/
	private EventDispatcher() {

	}

	/**
	 * Get the single instance of event dispatcher
	 * 
	 * @return the single instance
	 **/
	public static EventDispatcher getInstance() {

		if (null == instance)
			instance = new EventDispatcher();

		return instance;
	}

	public void start() {
		
		disruptor.start();
	}

	public void shutdown(){
		
		disruptor.shutdown();
	}
	
	@SuppressWarnings("unchecked")
	private void setup() {
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

	private void onRingEvent(RingEvent ringevent, long sequence, boolean endOfBatch) {

		EventPayload payload = ringevent.getPayload();
		EventHooker eventHooker = hookers.get(payload.getType());

		if (eventHooker != null && eventHooker.match(ringevent.getPayload(),true)) {

			try {

				eventHooker.processPayload(ringevent.getPayload());

			} catch (RingEventException e) {

				LOGGER.error("Error when processing event[{}] payload", e, payload.getType());
			}

		}else{
			
			LOGGER.warn("eventhooker not exist or unmatch type:{}", payload.getType());
		}

	}

	public void regEventHooker(EventHooker eventHooker) {

		hookers.put(eventHooker.getType(),eventHooker);
	}
	
	public void disableEventHooker(EventType type){
		
		
	}
	
	public static class RingEventHandler implements EventHandler<RingEvent> {

		@Override
		public void onEvent(RingEvent ringevent, long sequence,
				boolean endOfBatch) throws Exception {
			instance.onRingEvent(ringevent, sequence, endOfBatch);
		}

	}
}
