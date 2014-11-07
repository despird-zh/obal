package com.obal.audit.aop;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import com.obal.util.AspectUtils;

@Aspect
public abstract class AuditAspect {

	@Pointcut
	public abstract void operation();

	@After("operation()")
	public void auditOperation(JoinPoint jp) {
		System.out.println("--after operation");
	}

	@Before("operation()")
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
	
	@AfterReturning(pointcut="operation()",returning="r")
	public void afterOperation(Object r,JoinPoint jp) {

		if (r != null && (!(r instanceof List) || ((List) r).size() != 0)) {
			StringBuilder rv = new StringBuilder("Return Value : ");
			rv.append(AspectUtils.toString(r));
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
