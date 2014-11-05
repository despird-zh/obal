package com.obal.test;

import java.util.ArrayList;

import org.apache.hadoop.hbase.util.Bytes;

import com.obal.admin.EntityAdmin;
import com.obal.admin.EntitySetup;
import com.obal.admin.IAdminAccessor;
import com.obal.core.security.Principal;
import com.obal.exception.AccessorException;
import com.obal.meta.EntityAttr;

public class AdminTester extends BlankTester{

	public static void main(String[] args){
		
		initLog4j();
		AdminTester tester = new AdminTester();
		//tester.testCreateSchema();
		Object value = false;
		byte[] bval = Bytes.toBytes((Boolean)value);
		printHexString("--1st:",bval);
		Object t = Bytes.toBoolean(bval);
		System.out.println(t);	
		byte[] bytes = Bytes.toBytes((Boolean)false);
		printHexString("--2nd:",bytes);
		t = Bytes.toBoolean(bytes);
		
		System.out.println(t);		
		tester.testInitializer();

	}
	
	public static void printHexString(String hint, byte[] b)  
	{  
	    System.out.print(hint);  
	    for (int i = 0; i < b.length; i++)  
	    {  
	        String hex = Integer.toHexString(b[i] & 0xFF);  
	        if (hex.length() == 1)  
	        {  
	            hex = '0' + hex;  
	        }  
	        System.out.print(hex.toUpperCase() + " ");  
	    }  
	    System.out.println("");  
	}
	
	public void testInitializer(){
		
		EntitySetup ei = new EntitySetup();
		ei.initial();
		
	}
	
	public void testCreateSchema(){
		
		EntityAdmin ea = EntityAdmin.getInstance();
		Principal princ = new Principal("acc","demo","pwd");
		
		IAdminAccessor aa = ea.getAdminAccessor(princ);
		
		try {
			ArrayList<EntityAttr> attrs = new ArrayList<EntityAttr>();
			EntityAttr attr = new EntityAttr("a","cf1","TestIDF1");
			attrs.add(attr);
			attr = new EntityAttr("a1","cf2","TestIDF1");
			attrs.add(attr);
			attr = new EntityAttr("a2","cf2","TestIDF2");
			attrs.add(attr);
			attr = new EntityAttr("a3","cf2","TestIDF3");
			attrs.add(attr);
			
			aa.createSchema("table1",attrs);
			
		} catch (AccessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			aa.release();
		}
		
	}
}
