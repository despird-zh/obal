package com.obal.disruptor;

/**
 * The base class of event payload, it is used to extend for different purpose.
 * 
 * @author despird
 * @version 0.1 2014-3-2
 *
 **/
public abstract class EventPayload {
	
	private EventType type;
	
	/**
	 * Constructor  
	 **/
	public EventPayload(EventType type) {
		
		this.type = type;
	}
	
	/**
	 * Get the event type of payload 
	 **/
	public EventType getType() {
		return type;
	}


}
