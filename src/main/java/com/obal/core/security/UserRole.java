package com.obal.core.security;

import com.obal.core.EntryKey;
import com.obal.core.meta.EntityConstants;

public class UserRole extends EntryKey{

	private String role = null;
	
	public UserRole(String role) {
		super(EntityConstants.ENTITY_USER_ROLE, null);
		this.role = role;
	}

	public UserRole(String role, String key){
		
		super(EntityConstants.ENTITY_USER_ROLE, key);
		this.role = role;
	}
	
	public String roleName(){
		
		return role;
	}
	
	public boolean hasUser(String account){
		
		return false;
	}
	
}
