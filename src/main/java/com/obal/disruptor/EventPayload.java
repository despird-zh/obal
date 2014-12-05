package com.obal.disruptor;

/**
 * The base class of event payload, it is used to extend for different purpose.
 * 
 * @author despird
 * @version 0.1 2014-3-2
 *
 **/
public class EventPayload {
	
	private EventType type;
	
	private Object data;
	
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

	/**
	 * Get the data of payload 
	 **/
	public Object getData(){
		
		return this.data;
	}
	
	/**
	 * Set data of payload 
	 **/
	public void setData(Object data){
		
		this.data = data;
	}
	
	public static EventPayload newAuditPayload(){
		
		return new EventPayload(EventType.AUDIT);
	}
	
	public static EventPayload newCachePayload(){
		
		return new EventPayload(EventType.CACHE);
	}
}
