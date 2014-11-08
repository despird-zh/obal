package com.obal.cache.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cacheable indicate whether the method parameter or return value need cache operation
 * <p>
 * Cache operation usually occurs on two kinds of situation: entry or attribute.
 * as per store decide where to r/w cache.
 * </p>
 * <p>Entry value cache: need to specify necessary( entrykey+entity / entrykey )</p>
 * <p>Attribute value cache: need to specify necessary( entrykey+attr )</p>
 * 
 * @author despird
 * @version 0.1 2014-2-1
 * 
 * @See EntryKey
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheableDel {
	
	
	/**
	 * the parameter name of EntryKey object or String value of key
	 **/
	String entrykey() default "";
	
	/**
	 * the parameter name indicate the entity name
	 **/
	String entity() default "";
	
	
}
