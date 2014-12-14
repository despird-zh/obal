package com.obal.accessor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.obal.core.CoreManager;
import com.obal.core.security.Principal;
import com.obal.core.security.hbase.UserAccessor;
import com.obal.core.util.AccessorUtils;
import com.obal.exception.AccessorException;
import com.obal.exception.EntityException;
import com.obal.test.BlankTester;

public class PrincipalTest extends BlankTester{

	public void testCore(){
		
		Principal princ = new Principal("demo1","demouser1","demopwd","demosrc");
		try {
			Map<String,Object> groups = new HashMap<String,Object>();
			groups.put("gk1","group1");
			groups.put("gk2","group2");
			princ.setGroups(groups);
			
			Map<String,Object> roles = new HashMap<String,Object>();
			roles.put("rk1","role1");
			roles.put("rk2","role2");
			princ.setRoles(roles);
			UserAccessor pa = AccessorUtils.getEntityAccessor(princ, "obal.user");
			princ.setKey("101001");
			
			princ.setCreator("crt01");
			princ.setModifier("mdfier01");
			princ.setNewCreate(new Date());
			princ.setLastModify(new Date());
			pa.doPutEntry(princ);
			
		} catch (EntityException | AccessorException e) {
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
