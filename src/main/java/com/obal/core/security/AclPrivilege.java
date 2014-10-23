package com.obal.core.security;

public enum AclPrivilege {

	NONE("NONE"),
	BROWSE("BROWSE"),
	READ("READ"),
	WRITE("WRITE"),
	DELETE("DELETE");
	
	private String privilege = null;
	
	/**
	 * Hide Rtype default constructor 
	 **/
	private AclPrivilege(String privilege){  
		this.privilege = privilege;
    }
	
	@Override
	public String toString(){
		return this.privilege;
	}
}
