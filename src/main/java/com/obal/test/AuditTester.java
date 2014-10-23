package com.obal.test;

import com.obal.audit.AuditEvent;
import com.obal.audit.AuditFactory;
import com.obal.audit.AuditLogger;

public class AuditTester {

	public static void main(String[] args){
		AuditFactory.start();
		AuditLogger logger = AuditFactory.getAuditLogger();
		
		for(int i = 1; i<100; i++){
			
			AuditEvent evt = new AuditEvent("key-"+i);
			logger.doAudit(evt);
		}
		
		AuditFactory.stop();
	}
}
