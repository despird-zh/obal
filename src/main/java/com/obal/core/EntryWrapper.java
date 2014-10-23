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
package com.obal.core;

import com.obal.core.meta.EntityMeta;

/**
 * EntryWrapper wrap the entry information
 * 
 **/
public abstract class  EntryWrapper<GB extends EntryInfo> {
	
	private EntityMeta entityMeta;
	
	/**
	 * set the entrymeta 
	 **/
	public void setEntityMeta(EntityMeta entityMeta){
		
		this.entityMeta = entityMeta;
	}
	
	/**
	 * get the entrymeta
	 **/
	public EntityMeta getEntityMeta(){
		
		return this.entityMeta;
	}
	
	/**
	 * Check if the wrapper support the entry information
	 * @param rawEntry the entry information
	 * @return boolean true:surport ;flase:unsupport 
	 **/
	public abstract boolean supportWrap(Class<?> clazz);
	
	/**
	 * Wrap the rawentry into bean object
	 * @param rawEntry the entry information
	 * @return GB the bean object. 
	 **/
	public abstract GB wrap(Object rawEntry);
	
	/**
	 * Check if the wrapper support specified raw class
	 * @param clazz the raw class
	 * @return boolean true:surport ;flase:unsupport 
	 **/	
	public abstract boolean supportParse(Class<?> clazz);
	
	/**
	 * Parse bean object into raw Object
	 * @param entryInfo the entry information bean
	 * @return Object the raw object. 
	 **/	
	public abstract Object parse(GB entryInfo);
}
