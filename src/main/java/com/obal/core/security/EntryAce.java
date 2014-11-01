package com.obal.core.security;

import java.util.HashSet;
import java.util.Set;

import com.obal.core.util.CoreConstants;

public class EntryAce {

	/** the role name */
	public String name;
	
	/** the privilege of role*/
	public AclPrivilege privilege;
	
	/** the entry type */
	public String type;
	
	/** the permission set */
	public Set<String> permissionSet;
	
	/**
	 * Constructor for user ACE item.
	 * 
	 * @param combinedValue
	 *  
	 **/
	public EntryAce(String combinedValue){
		
		this.type = CoreConstants.ACE_TYPE_USER;
		this.name = name;
		this.privilege = privilege;
	}
	
	/**
	 * Constructor for user ACE item.
	 * 
	 * @param roleName the role name
	 * @param privilege the access control privilege
	 *  
	 **/
	public EntryAce(String name, AclPrivilege privilege){
		
		this.type = CoreConstants.ACE_TYPE_USER;
		this.name = name;
		this.privilege = privilege;
	}
	
	/**
	 * Constructor 
	 * 
	 * @param aceType the ace type
	 * @param role the role name
	 * @param privilege the access control privilege
	 *  
	 **/
	public EntryAce(String aceType, String name, AclPrivilege privilege){
		
		this.type = aceType;
		this.name = name;
		this.privilege = privilege;
	}
	
	/**
	 * Constructor 
	 * 
	 * @param aceType the ace type
	 * @param role the role name
	 * @param privilege the access control privilege
	 *  
	 **/
	public EntryAce(String aceType, String name, AclPrivilege privilege,String ... permissions){
		
		this.type = aceType;
		this.name = name;
		this.privilege = privilege;
		if(permissions == null || permissions.length ==0)
			return;
		else
			permissionSet = new HashSet<String>();
		
		for(String permission:permissions){
			
			permissionSet.add(permission);
		}
	}
	
	@Override
	public String toString(){
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(this.name).append(CoreConstants.VALUE_SEPARATOR);
		sbuf.append(this.type).append(CoreConstants.VALUE_SEPARATOR);
		sbuf.append(this.privilege).append(CoreConstants.VALUE_SEPARATOR);
		
		for(String perm:permissionSet){
			
			sbuf.append(perm).append(CoreConstants.COLLECT_ELM_SEPARATOR);
		}
		
		return sbuf.toString();
	}
	
	public void initial(String combinedValue){
		
		
	}
}
