package com.obal.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * RingEvent class is used to hold payload
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * 
 **/
public class RingEvent {
	
	private EventPayload payload;

	/**
	 * The payload of event 
	 **/
	public EventPayload getPayload() {
		return payload;
	}
	
	/**
	 * the event type 
	 **/
	public EventType getEventType(){
		
		if(this.payload == null)
			
			return EventType.UNKNOWN;
		else{
			
			return this.payload.getType();
		}
	}
	
	/**
	 * Set payload 
	 * 
	 *  @param payload the payload to be passed to event hooker
	 **/
	public void setPayload(EventPayload payload) {
		this.payload = payload;
	}	

	/**
	 * the factory to be used by disruptor to preallocate elements of ringbuffer. 
	 **/
	public final static EventFactory<RingEvent> EVENT_FACTORY = new EventFactory<RingEvent> (){
		
		@Override
		public RingEvent newInstance() {

			return new RingEvent();
		}
	};
}
