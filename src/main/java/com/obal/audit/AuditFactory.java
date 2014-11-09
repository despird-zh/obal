package com.obal.audit;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class AuditFactory {

	private static AuditFactory instance = null;
	Executor executor = null;
	Disruptor<AuditEvent> disruptor = null;
	AuditHandler handler = null;
	AuditLogger auditlogger = null;
	
	private AuditFactory() {
		initialize();
	}

	public static AuditFactory getInstance() {

		if (instance == null)
			instance = new AuditFactory();

		return instance;
	}

	@SuppressWarnings("unchecked")
	private void initialize(){
		// Executor that will be used to construct new threads for consumers
        this.executor = Executors.newCachedThreadPool();
        this.handler = new AuditHandler();
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;
        EventFactory<AuditEvent> auditbuilder = AuditEvent.EVENT_FACTORY;
        // Construct the Disruptor
        disruptor = new Disruptor<AuditEvent>(auditbuilder, bufferSize, executor);

        // Connect the handler
        disruptor.handleEventsWith(handler);
  	}
	
	private AuditLogger newAuditLogger(){
		
		 RingBuffer<AuditEvent> rbuf = disruptor.getRingBuffer();	        
	     return new AuditLogger(rbuf);
	}
	
	public static AuditLogger getAuditLogger(){
		
		getInstance();
		return instance.newAuditLogger();
	}
		
	public static void stop(){
		
		getInstance();
		instance.disruptor.shutdown();
	}
	
	public static void start(){
		
		getInstance();
		instance.disruptor.start();
	}
}
