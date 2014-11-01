package com.obal.core.security;

import com.obal.core.util.CoreConstants;

public class EntryAcl {

	/**
	 * class to store Access control entry 
	 * 
	 **/
	public static class EntryAce{
		
		/** the role name */
		public String name;
		
		/** the privilege of role*/
		public AclPrivilege privilege;
		
		/** the entry type */
		public String type;
		
		/**
		 * constructor 
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
		 * constructor 
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
	}
}
