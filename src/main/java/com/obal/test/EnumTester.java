package com.obal.test;

import com.obal.core.security.AclPrivilege;
import com.obal.util.ByteValueUtils;

public class EnumTester {
	 public static void main(String[] args) {  
	    
		 AclPrivilege a =  AclPrivilege.NONE;
		 System.out.println("-------a:"+a);
		 System.out.println("-------a:"+ByteValueUtils.byteToBit(a.toByte()));
		 a =  AclPrivilege.BROWSE;
		 System.out.println("-------a:"+a);
		 System.out.println("-------a:"+ByteValueUtils.byteToBit(a.toByte()));
		 a =  AclPrivilege.READ;
		 System.out.println("-------a:"+a);
		 System.out.println("-------a:"+ByteValueUtils.byteToBit(a.toByte()));
		 
		 System.out.println("-------a:"+ByteValueUtils.byteToBit(a.toByte()));
	 }
}
