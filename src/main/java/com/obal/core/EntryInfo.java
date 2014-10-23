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

import java.io.Serializable;
import java.util.Date;

/**
 * Here will extend other attributes to entryInfo
 **/
public abstract class EntryInfo extends EntryKey implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public EntryInfo(String entryName, String key){
		
		super(entryName, key);
	}

	public EntryInfo(EntryKey key){
		
		super(key.getEntryName(), key.getKey());
	}
	
	public EntryKey getEntryKey(){
		
		return new EntryKey(getEntryName(), getKey());
	}
	
	private String creator ;
	private String modifier ;
	private Date newCreate;
	private Date lastMofify;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getNewCreate() {
		return newCreate;
	}

	public void setNewCreate(Date newCreate) {
		this.newCreate = newCreate;
	}

	public Date getLastMofify() {
		return lastMofify;
	}

	public void setLastMofify(Date lastMofify) {
		this.lastMofify = lastMofify;
	}	
	
}
