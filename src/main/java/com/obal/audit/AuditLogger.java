package com.obal.audit;

import com.lmax.disruptor.RingBuffer;

public class AuditLogger {
	
    private final RingBuffer<AuditEvent> ringBuffer;

    public AuditLogger(RingBuffer<AuditEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void doAudit(AuditEvent auditevent)
    {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try
        {
        	AuditEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            event.copy(auditevent);                                     // for the sequence
        }
        finally
        {
            ringBuffer.publish(sequence);
        }
    }
}
