package com.obal.test;

import com.obal.audit.AuditInfo;
import com.obal.core.util.AuditUtils;
import com.obal.disruptor.EventDispatcher;

public class AuditTester {

	public static void main(String[] args){
		EventDispatcher ed = EventDispatcher.getInstance();
		ed.start();
		AuditUtils.regAuditHooker();
		
		for(int i = 1; i<100; i++){
			
			AuditInfo evt = new AuditInfo("key-"+i);
			AuditUtils.doAudit(evt);
		}
		
		
	}
}
