package com.obal.core.security;

import java.util.List;

import com.obal.core.EntryKey;
import com.obal.core.meta.EntityConstants;

public class UserGroup extends EntryKey{

	private String group = null;
	
	public UserGroup(String group, String key) {
		super(EntityConstants.ENTITY_USER_GROUP, key);
		this.group = group;
	}

	public UserGroup(String group){
		
		super(EntityConstants.ENTITY_USER_GROUP, null);
		this.group = group;
	}
	
	public String groupName(){
		
		return group;
	}
	
	public boolean hasUser(String account){
		
		return false;
	}
	
	public boolean hasGroup(String group){
		
		return false;
	}
	
	public List<UserGroup> getGroups(){
		
		return null;
	}
}
