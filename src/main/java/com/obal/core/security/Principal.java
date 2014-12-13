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
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.obal.core.EntryKey;
import com.obal.meta.EntityConstants;

/**
 * Store the pincipal info of user
 * 
 * @author despird
 * @version 1.0 2014-01-01
 * @see com.obal.core.security.Profile
 **/
@JsonIgnoreProperties({"keyBytes","profile"})
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

	/**
	 * Constructor for new Principal
	 * 
	 * @param account the logon account 
	 * @param name the user name
	 * @param password the password
	 * @param source the account source
	 **/	
	@JsonCreator
	public Principal(@JsonProperty("account") String account, @JsonProperty("name") String name, @JsonProperty("password") String password, @JsonProperty("source") String source) {
		
		super(EntityConstants.ENTITY_PRINCIPAL, null);
		this.account = account;
		this.name = name;
		this.password = password;
		this.source = source;
	}
	
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
	private Set<String> groups;
	private Set<String> roles;
	
	/**
	 * Get Account information 
	 **/
	@JsonProperty("account")
	public String account() {
		return account;
	}
	
	@JsonProperty("name")
	public String name() {
		return name;
	}

	@JsonProperty("password")
	public String password() {
		return password;
	}

	@JsonProperty("source")
	public String source() {
		return source;
	}
	
	
	public Profile getProfile() {
		
		return profile;
	}
	
	public void setProfile(Profile profile) {
		
		this.profile = profile;
	}	
	
	@JsonProperty("settings")
	public Map<String, Object> getProfileSettings(){
		
		return this.profile.getSettings();
	}
	
	@JsonProperty("settings")
	public void setProfileSettings(Map<String, Object> settings){
		
		this.profile = new Profile();
		this.profile.setSettings(settings);;
	}
	
	public boolean inGroup(String group){
		
		return false;
	}

	public boolean inRole(String role){
		
		return false;
	}
	
	public void setGroups(Set<String> groups){
		
		this.groups = groups;
	}
	public Set<String> getGroups(){
		
		return this.groups;
	}
	
	public void setRoles(Set<String> roles){
		this.roles = roles;
	}
	public Set<String> getRoles(){
		
		return this.roles;
	}
	
	@Override
	public boolean equals(Object other) {
		// step 1
		if (other == this) {
			return true;
		}
		// step 2
		if (!(other instanceof Principal)) {
			return false;
		}
		// step 3
		Principal that = (Principal) other;
		// step 4
		return new EqualsBuilder()
			.append(this.source(), that.source())
			.append(this.account(), that.account()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.source())
				.append(this.account()).toHashCode();
	}
}
