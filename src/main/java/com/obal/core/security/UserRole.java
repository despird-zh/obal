package com.obal.core.security;

import com.obal.core.EntryKey;
import com.obal.core.meta.EntityConstants;

/**
 * User Role Collect users from different organizations but has same authority
 * e.g Administrator, Manager etc.
 * 
 * <p>Role only include user, no groups</p>
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * 
 **/
public class UserRole extends EntryKey{

	/** role name */
	private String role = null;
	
	/**
	 * Constructor 
	 **/
	public UserRole(String role) {
		super(EntityConstants.ENTITY_USER_ROLE, null);
		this.role = role;
	}

	/**
	 * Constructor 
	 **/
	public UserRole(String role, String key){
		
		super(EntityConstants.ENTITY_USER_ROLE, key);
		this.role = role;
	}
	
	/**
	 * get the role name
	 * @return role name 
	 **/
	public String roleName(){
		
		return role;
	}
	
	/**
	 * check user owns role or not
	 * @return true: own ;false not own 
	 **/
	public boolean hasUser(String account){
		
		return false;
	}
	
}
