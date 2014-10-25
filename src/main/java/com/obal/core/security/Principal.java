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
package com.obal.core.security;

import java.util.List;

import com.obal.core.EntryKey;
import com.obal.core.meta.EntityConstants;

/**
 * Store the pincipal info of user
 * 
 * @author despird
 * @version 1.0 2014-01-01
 * @see com.obal.core.security.Profile
 **/
public class Principal extends EntryKey{

	/**
	 * Constructor for new Principal
	 * 
	 * @param account the logon account 
	 * @param name the user name
	 * @param password the password
	 * 
	 **/
	public Principal(String account, String name, String password) {
		
		super(EntityConstants.ENTITY_PRINCIPAL, null);
		this.account = account;
		this.name = name;
		this.password = password;
	}

	/** the domain name*/
	private String domain = "";
	/** the account information */
	private String account = "";
	/** the name  */
	private String name = "";
	/** the password */
	private String password = "";
	/** the source of principal information */
	private String source = "";

	/** the user profile info holder */
	private Profile profile = null;
	
	/**
	 * Get the domain information 
	 **/
	public String getDomain() {
		return domain;
	}
	
	/**
	 * Set the domain information 
	 **/
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	/**
	 * Get Account information 
	 **/
	public String getAccount() {
		return account;
	}
	
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Profile getProfile() {
		
		return profile;
	}
	public void setProfile(Profile profile) {
		
		this.profile = profile;
	}	
	
	public boolean inGroup(String group){
		
		return false;
	}

	public boolean inRole(String role){
		
		return false;
	}
	
	public List<UserGroup> getUserGroups(){
		
		return null;
	}
	
	public List<UserRole> getUserRoles(){
		
		return null;
	}
}
