package com.obal.disruptor;

import com.lmax.disruptor.EventFactory;

public class RingEvent {
	
	private EventPayload payload;

	public EventPayload getPayload() {
		return payload;
	}
	
	public EventType getEventType(){
		
		if(this.payload == null)
			
			return EventType.UNKNOWN;
		else{
			
			return this.payload.getType();
		}
	}
	
	public void setPayload(EventPayload payload) {
		this.payload = payload;
	}	

	public final static EventFactory<RingEvent> EVENT_FACTORY = new EventFactory<RingEvent> (){
		
		@Override
		public RingEvent newInstance() {

			return new RingEvent();
		}
	};
}
