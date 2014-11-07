package com.obal.audit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Auditable {
	// operation - verb
	String verb();
}
