package com.obal.audit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {


	
	/**
	 * verb is the method name 
	 **/
	String verb() default "";
	
	/**
	 * subject is the principal object 
	 **/
	String subject() default "";
	
	/**
	 * object is the target  
	 **/

	String object() default "";
}
