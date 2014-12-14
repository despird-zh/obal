package com.obal.accessor;

import com.obal.core.CoreManager;
import com.obal.core.security.Principal;
import com.obal.core.security.hbase.UserAccessor;
import com.obal.core.util.AccessorUtils;
import com.obal.exception.EntityException;
import com.obal.test.BlankTester;

public class PrincipalTest extends BlankTester{

	public void testCore(){
		
		Principal princ = new Principal("demo1","demouser1","demopwd","demosrc");
		try {
			UserAccessor pa = AccessorUtils.getEntityAccessor(princ, "obal.user");
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	protected void setUp() throws Exception {  
		initLog4j();
		CoreManager.initial();
		CoreManager.start();
	    super.setUp();  
	}  
	  
	protected void tearDown() throws Exception {  
	    CoreManager.stop();
		super.tearDown();  
	} 
}
