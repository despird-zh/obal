package com.obal.test;

import com.obal.admin.EntitySetup;

public class AdminTester extends BlankTester{

	public static void main(String[] args){
		
		initLog4j();
		AdminTester tester = new AdminTester();
	
		tester.testInitializer();

	}
	
	public void testInitializer(){
		
		EntitySetup ei = new EntitySetup();
		ei.setup();
		
	}

}
