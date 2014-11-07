package com.obal.cache.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class CacheAspect {

	@Pointcut
    abstract void cacheAfterPut();
	    
    @After("cacheAfterPut()")
    public void afterPut(JoinPoint jp) {
    	System.out.println("--after put");
    }

	@Pointcut
    abstract void cacheAroundGet();
	
    @Around("cacheAroundGet()")
    public String aroundGet(ProceedingJoinPoint jp) throws Throwable{
    	System.out.println("--around start get");
    	String result = (String)jp.proceed();
    	System.out.println("--around end get");
    	return result;
    }
    
	@Pointcut
    abstract void cacheAfterDel();
	
    @After("cacheAfterDel()")
    public void afterDel(JoinPoint jp) {
    	System.out.println("--after del");
    }
}
