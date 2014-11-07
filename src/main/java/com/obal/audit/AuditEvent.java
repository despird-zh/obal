/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 2006-2011, QOS.ch. All rights reserved.
 * 
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *  
 *   or (per the licensee's choosing)
 *  
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package com.obal.audit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.obal.core.EntryInfo;

public class AuditEvent extends EntryInfo{

	private static final long serialVersionUID = 1L;

	public static final String ENTRY_TYPE_AUDIT="_ENTRY_AUDIT";

	private Date timestamp;
	String subject;
	String verb;
	String object;

	Map<String, String> predicateMap = new HashMap<String, String>();

	AccessPoint accessPoint;
	
	public AuditEvent(String entryType, String key) {
		super(entryType, key);
		timestamp = new Date(System.currentTimeMillis());
	}

	public AuditEvent( String key) {
		super(ENTRY_TYPE_AUDIT, key);
		timestamp = new Date(System.currentTimeMillis());
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public void addPredicate(Predicate predicate) {
		predicateMap.put(predicate.getName(), predicate.getValue());
	}

	public void setPredicateMap(Map<String, String> predicateMap) {
		this.predicateMap = predicateMap;
	}

	public Map<String, String> getPredicateMap() {
		return predicateMap;
	}

	public AccessPoint getAccessPoint() {
		return accessPoint;
	}

	public void setAccessPoint(AccessPoint accessPoint) {
		this.accessPoint = accessPoint;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((getKey() == null) ? 0 : getKey().hashCode());
		result = PRIME * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = PRIME * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AuditEvent other = (AuditEvent) obj;

		// timestamp cannot be null
		if (!timestamp.equals(other.timestamp)) {
			return false;
		}
		if (getKey() == null) {
			if (other.getKey() != null)
				return false;
		} else if (!getKey().equals(other.getKey())) {
			return false;
		}

		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject)) {
			return false;
		}

		if (verb == null) {
			if (other.verb != null)
				return false;
		} else if (!verb.equals(other.verb)) {
			return false;
		}
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object)) {
			return false;
		}

		if (predicateMap == null) {
			if (other.predicateMap != null)
				return false;
		} else if (!predicateMap.equals(other.predicateMap)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {

		String retValue = "";

		retValue = "AuditEvent(key=" + this.getKey() + ", timestamp=" + this.timestamp
				+ ", subject=" + this.subject + ", verb=" + this.verb
				+ ", object=" + this.object + ", predicateMap = " + this.predicateMap 
				+ ")";

		return retValue;
	}
	
	/**
	 * Copy information from the parameter event
	 * 
	 * @param fromOne the event object.
	 * 
	 **/
	public void copy(AuditEvent fromOne){
		
		this.setKey(fromOne.getKey());
		this.setTimestamp(fromOne.getTimestamp());
		this.setEntityName(fromOne.getEntityName());
		this.setVerb(fromOne.getVerb());
		this.setSubject(fromOne.getSubject());
		this.setObject(fromOne.getObject());
		this.setAccessPoint(fromOne.getAccessPoint());
		this.setPredicateMap(fromOne.getPredicateMap());
	}
	
}
