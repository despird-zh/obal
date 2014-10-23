package com.obal.audit;

import com.lmax.disruptor.EventHandler;

public class AuditHandler implements EventHandler<AuditEvent>{

	@Override
	public void onEvent(AuditEvent arg0, long arg1, boolean arg2)
			throws Exception {
		
		System.out.println("Event: " + arg0);
	}

}
