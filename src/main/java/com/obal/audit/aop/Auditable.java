package com.obal.audit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Auditable indicate the core audit info:subject , verb , object.
 * 
 * <pre>
 * Auditable(subject="admin",verb="putXX",object="parmNameX")
 * </pre>
 * <p>subject : optional setting, usually subject is a principal object used to identify the executor</p>
 * <p>verb : optional setting, usually be the method name</p>
 * <p>object : optional setting, the target object to be accessed, eg. entryKey</p>
 * 
 * @author despird
 * @version 0.1 2014-2-1 
 * 
 **/
@Target(ElementType.METHOD)
public @interface Auditable {
	// operation - verb
	String verb() default "";
	
	String subject() default "";
	
	String object() default "";
}
