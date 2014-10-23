package com.obal.core.util;

import com.obal.audit.AuditEvent;
import com.obal.audit.AuditFactory;

public class AutditUtils {

	public static void startAudit(){
		
		AuditFactory.start();
	}
	
	public static void stopAudit(){
		
		AuditFactory.stop();
	}
	
	public static void doAudit(AuditEvent auditevent){
		
		AuditFactory.getAuditLogger().doAudit(auditevent);
	}
}
