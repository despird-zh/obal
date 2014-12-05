package com.obal.disruptor;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.obal.exception.RingEventException;

public abstract class EventHooker {

	private boolean blocked = false; 
	
	private EventType type;
	
	/**
	 * Constructor:specify the eventype supported 
	 **/
	public EventHooker(EventType type){
		
		this.type = type;
	}
	
	/**
	 * Check if the hooker match the event type 
	 * 
	 * @param payload the payload of ringevent
	 * @param checkBlock true:check hooker is blocked or not; false:don't check block state
	 **/
	public boolean match(EventPayload payload, boolean checkBlock){
		
		if(checkBlock)
			
			return this.type == payload.getType() && !this.blocked ;
		else{
			
			return this.type == payload.getType() ;
		}
	}
	
	/**
	 * Set the block switch flag
	 * 
	 * @param blocked true:Hooker won't process payload; false:Hooker process payload. 
	 **/
	public void setBlocked(boolean blocked){
		
		this.blocked = blocked;
	}
	
	/**
	 * Get the supported EventType 
	 **/
	public EventType getType(){
		
		return type;
	}
	
	/**
	 * Process the payload of event.
	 * 
	 * @param  payload the payload of ring event
	 * @exception RingEventException
	 * 
	 **/
	public abstract void processPayload(EventPayload payload) throws RingEventException;
	
	@Override
	public boolean equals(Object other) {
		// step 1
		if (other == this) {
			return true;
		}
		// step 2
		if (!(other instanceof EventHooker)) {
			return false;
		}
		// step 3
		EventHooker that = (EventHooker) other;
		// step 4
		return new EqualsBuilder()
			.append(this.type, that.type).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.type)
				.toHashCode();
	}
}
