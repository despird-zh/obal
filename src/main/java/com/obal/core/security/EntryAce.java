package com.obal.core.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.obal.core.util.CoreConstants;

/**
 * EntryAce is the access control setting for operator, it could be set at three levels.
 * <pre>
 * 1 - Person 
 * 2 - Group
 * 3 - Role
 * </pre> 
 * <p>
 * The setting include privilege and Permission for operation.
 * </p>
 * <pre>
 *   type:_user
 *   name:demo_account
 *   privilege:EXECUTE
 *   permissionSet:MOVE,AUDIT,APPROVE
 * </pre>
 * 
 **/
public class EntryAce {

	/** the role name */
	private String name;
	
	/** the privilege of role*/
	private AclPrivilege privilege;
	
	/** the entry type */
	private String type;
	
	/** the permission set */
	private Set<String> permissionSet;
	
	/**
	 * Constructor for user ACE item.
	 * 
	 * @param combinedValue
	 *  
	 **/
	public EntryAce(String combinedValue){
		
		initial(combinedValue);
		
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
	
	public String name(){
		
		return this.name;
	}
	
	public String type(){
		
		return this.type;
	}
	
	public AclPrivilege privilege(){
		
		return this.privilege;
	}
	
	public Set<String> permissions(){
		
		return this.permissionSet;
	}
	
	@Override
	public String toString(){
		
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(this.name).append(CoreConstants.VALUE_SEPARATOR);
		sbuf.append(this.type).append(CoreConstants.VALUE_SEPARATOR);
		sbuf.append(this.privilege).append(CoreConstants.VALUE_SEPARATOR);
		
		if(null != permissionSet){
			for(String perm:permissionSet){
				
				sbuf.append(perm).append(CoreConstants.COLLECT_ELM_SEPARATOR);
			}
		}
		
		return sbuf.toString();
	}
	
	/**
	 * Initial object with combined value string 
	 **/
	public void initial(String combinedValue){
		
		String[] values = StringUtils.split(combinedValue, CoreConstants.VALUE_SEPARATOR);
		this.name = values[0];
		this.type = values[1];
		this.privilege = AclPrivilege.valueOf(values[2]);
		
		String[] permArray = StringUtils.split(values[4], CoreConstants.COLLECT_ELM_SEPARATOR);
		
		if(permissionSet == null)
			permissionSet = new HashSet<String>();
		else
			permissionSet.clear();
		
		for(String perm:permArray){
			permissionSet.add(perm);
		}
	}
	
	@Override
	public boolean equals(Object other) {
		// step 1
		if (other == this) {
			return true;
		}
		// step 2
		if (!(other instanceof EntryAce)) {
			return false;
		}
		// step 3
		EntryAce that = (EntryAce) other;
		// step 4
		int sumPerms = 0;
		if(null != permissionSet){
			for(String perm:permissionSet){
				
				sumPerms += perm.hashCode();
			}
		}
		int sumPermsThat = 0;
		Set<String> perms = that.permissions();
		if(null != perms){
			
			for(String perm:perms){
				
				sumPermsThat += perm.hashCode();
			}
		}
		return new EqualsBuilder()
			.append(this.type, that.type())
			.append(this.name, that.name())
			.append(this.privilege, that.privilege())
			.append(sumPerms, sumPermsThat).isEquals();
		
	}

	@Override
	public int hashCode() {
		
		int sumPerms = 0;
		if(null != permissionSet){
			for(String perm:permissionSet){
				
				sumPerms += perm.hashCode();
			}
		}
		
		return new HashCodeBuilder(17, 37)
			.append(this.type)
			.append(this.name)
			.append(this.privilege)
			.append(sumPerms)
			.toHashCode();
	}
}
