/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
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
