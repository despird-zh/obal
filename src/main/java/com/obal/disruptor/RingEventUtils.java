package com.obal.disruptor;

public class RingEventUtils {

	public static void publishPayload(EventPayload payload){
		
		EventDispatcher.getInstance().publishPayload(payload);
	}
	
	public static void regEventHooker(EventHooker eventHooker){
		
		EventDispatcher.getInstance().regEventHooker(eventHooker);
	}
	
	public static void unRegEventHooker(EventType type){
		
		EventDispatcher.getInstance().unRegEventHooker(type);
	}
	
	public static void blockEventHooker(EventType type, boolean blocked){
		
		EventDispatcher.getInstance().blockEventHooker(type, blocked);
	}
}
