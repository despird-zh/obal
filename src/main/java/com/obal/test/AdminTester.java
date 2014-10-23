package com.obal.test;

import java.util.ArrayList;

import com.obal.admin.EntityAdmin;
import com.obal.admin.EntitySetup;
import com.obal.admin.IAdminAccessor;
import com.obal.core.meta.EntityAttr;
import com.obal.core.security.Principal;
import com.obal.exception.AccessorException;

public class AdminTester extends BlankTester{

	public static void main(String[] args){
		
		initLog4j();
		AdminTester tester = new AdminTester();
		//tester.testCreateSchema();
		tester.testInitializer();
	}
	
	public void testInitializer(){
		
		EntitySetup ei = new EntitySetup();
		ei.initial();
		
	}
	
	public void testCreateSchema(){
		
		EntityAdmin ea = EntityAdmin.getInstance();
		Principal princ = new Principal();
		
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
