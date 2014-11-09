package com.obal.audit.aop;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import com.obal.test.aop.CacheTestAccessor;
import com.obal.test.aop.DemoBean;
import com.obal.util.AspectUtils;

@Aspect
public abstract class AuditAspect {

	@Pointcut
	public abstract void beforeOperation();

	@Before("beforeOperation()")
	public void beforeOperation(JoinPoint jp) {
		
		Object[] paramValues = jp.getArgs();
		String[] paramNames = ((CodeSignature) jp.getStaticPart()
				.getSignature()).getParameterNames();

		StringBuilder logLine = new StringBuilder(jp.getStaticPart()
				.getSignature().getName()).append("(");

		if (paramNames.length != 0)
			AspectUtils.logParamValues(logLine, paramNames, paramValues);
		logLine.append(") - started");
		AspectUtils.getLogger(jp).info(logLine.toString());
	}

	@Pointcut
	public abstract void afterOperation();
	
	@AfterReturning(pointcut="afterOperation()",returning="rtv")
	public void afterOperation(Object rtv,JoinPoint jp) {

		Object target = jp.getTarget();
		CacheTestAccessor cta = (CacheTestAccessor)target;
		DemoBean dp= cta.getdp();
		System.out.println("DemoP is:"+dp.demoStr);
		
		if (rtv != null && (!(rtv instanceof List) || ((List) rtv).size() != 0)) {
			StringBuilder rv = new StringBuilder("Return Value : ");
			rv.append(AspectUtils.toString(rtv));
			AspectUtils.getLogger(jp).info(rv.toString());
		}
		
		Object[] paramValues = jp.getArgs();
		String[] paramNames = ((CodeSignature) jp.getStaticPart()
				.getSignature()).getParameterNames();
		StringBuilder logLine = new StringBuilder(jp.getStaticPart()
				.getSignature().getName()).append("(");
		if (paramNames.length != 0)
			AspectUtils.logParamValues(logLine, paramNames, paramValues);
		logLine.append(") - finished");
		AspectUtils.getLogger(jp).info(logLine.toString());
	}
}
 
 
