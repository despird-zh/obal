package com.obal.cache.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.obal.core.util.CoreConstants;

@Target(ElementType.METHOD)
public @interface Cacheable {
	
	// cache
	String store() default CoreConstants.BUILDER_REDIS;
}
