package com.obal.core.security;

public enum AclPrivilege {

	NONE("NONE"),
	BROWSE("BROWSE"),
	READ("READ"),
	WRITE("WRITE"),
	EXECUTE("EXECUTE");
	
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
	
	@Deprecated
	public byte toByte(){
		
		byte b = (byte)1;
		for(int i = 0; i< this.ordinal();i++)
			b = (byte)(b << 1);
		
		return b;
	}
}
