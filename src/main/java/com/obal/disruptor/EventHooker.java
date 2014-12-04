package com.obal.disruptor;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class EventHooker<T> {

	private EventType type;
	
	public EventHooker(EventType type){
		
		this.type = type;
	}
	
	public boolean match(EventType type, Object event){
		
		return this.type.equals(type);
	}
	
	public abstract void onEvent(T event) throws Exception;
	
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
		EventHooker<?> that = (EventHooker<?>) other;
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
