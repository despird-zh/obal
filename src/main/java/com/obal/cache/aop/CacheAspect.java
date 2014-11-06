package com.obal.cache.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class CacheAspect {
	
	@Pointcut
    abstract void cacheput();

    @Before("cacheput()")
    public void beforePut(JoinPoint jp) {
       System.out.println("--before put");
    }
    
    @Around("cacheput()")
    public void aroundPut(ProceedingJoinPoint  jp) {
    	System.out.println("--around put");
       try {
		jp.proceed();
	} catch (Throwable e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
    @After("cacheput()")
    public void afterPut(JoinPoint jp) {
    	System.out.println("--after put");
    }
    
	@Pointcut
    abstract void cacheget();
	
    @Before("cacheget()")
    public void beforeGet(JoinPoint jp) {
       
    }

    @Around("cacheput()")
    public void aroundGet(ProceedingJoinPoint  jp) {
       try {
		jp.proceed();
	} catch (Throwable e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
    @After("cacheget()")
    public void afterGet(JoinPoint jp) {
       
    }
	
	@Pointcut
    abstract void cachedel();
	
    @Before("cachedel()")
    public void beforeDel(JoinPoint jp) {
       
    }
    
    @Around("cachedel()")
    public void aroundDel(ProceedingJoinPoint  jp) {
       
    	try {
			jp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @After("cachedel()")
    public void afterDel(JoinPoint jp) {
       
    }
}
