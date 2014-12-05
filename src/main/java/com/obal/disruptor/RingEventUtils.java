package com.obal.disruptor;

public class RingEventUtils {

	public static void publishPayload(EventPayload payload){
		
		EventDispatcher.getInstance().publishPayload(payload);
	}
}
