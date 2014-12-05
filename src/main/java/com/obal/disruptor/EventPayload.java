package com.obal.disruptor;

public abstract class EventPayload {
	
	private EventType type;
	
	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}
}
