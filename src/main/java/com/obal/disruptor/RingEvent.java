package com.obal.disruptor;

import com.lmax.disruptor.EventFactory;

public class RingEvent<T> {
	
	private EventType type;
	
	private T payload;

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}	

	public final static EventFactory<RingEvent> EVENT_FACTORY = new EventFactory<RingEvent> (){
		
		@Override
		public RingEvent newInstance() {

			return new RingEvent();
		}
	};
}
