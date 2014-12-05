package com.obal.core.util;

import com.obal.audit.AuditHooker;
import com.obal.audit.AuditInfo;
import com.obal.disruptor.EventDispatcher;
import com.obal.disruptor.EventPayload;
import com.obal.disruptor.EventType;

public class AuditUtils {

	public static void regAuditHooker(){
		
		AuditHooker auditHooker = new AuditHooker();
		EventDispatcher.getInstance().regEventHooker(auditHooker);
	}
	
	public static void unRegAuditHooker(){
		
		EventDispatcher.getInstance().unRegEventHooker(EventType.AUDIT);
	}
	
	public static void doAudit(AuditInfo auditevent){
		
		EventPayload payload = new EventPayload(EventType.AUDIT);
		payload.setData(auditevent);
		EventDispatcher.getInstance().sendPayload(payload);
	}
		
	public static EventPayload newAuditPayload(){
		
		return new EventPayload(EventType.AUDIT);
	}
}
