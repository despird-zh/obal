package com.obal.audit;

import com.lmax.disruptor.EventHandler;

public class AuditHandler implements EventHandler<AuditEvent>{

	@Override
	public void onEvent(AuditEvent event, long sequence,	boolean endOfBatch)
			throws Exception {
		
		System.out.println("Event: " + event);
	}

}
