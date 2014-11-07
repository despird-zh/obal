package com.obal.audit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class AuditAspect {
	
	@Pointcut
	public abstract void operation();
	
    @After("operation()")
    public void auditOperation(JoinPoint jp) {
    	System.out.println("--after del");
    }
}
