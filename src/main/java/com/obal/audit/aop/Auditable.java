/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
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
