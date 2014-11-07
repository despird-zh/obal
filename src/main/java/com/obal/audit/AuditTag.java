package com.obal.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface AuditTag {
	// operation - verb
	String verb();
}
