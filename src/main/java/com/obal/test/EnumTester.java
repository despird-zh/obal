package com.obal.test;

import com.obal.core.security.AclPrivilege;

public class EnumTester {
	 public static void main(String[] args) {  
	    	
		 AclPrivilege a =  AclPrivilege.READ;
		 System.out.println("-------a:"+a);
		 AclPrivilege b = AclPrivilege.valueOf("BROWSE");
		 System.out.println("-------b:"+b);

	 }  
}
