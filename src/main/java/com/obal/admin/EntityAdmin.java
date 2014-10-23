package com.obal.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.AccessorFactory;
import com.obal.core.security.Principal;
import com.obal.exception.EntityException;

public class EntityAdmin {

	Logger LOGGER = LoggerFactory.getLogger(EntityAdmin.class);
	
	public static String PERSIST_PLATFORM="persistence.platform";
	
	private EntityAdmin(){
		
		String path = this.getClass().getPackage().getName().replace(".", "/");
		path += "/AccessorMap.hbase.properties";
		AccessorFactory.getInstance().appendMapping("hbase", path);		
	}
	
	private static EntityAdmin instance;
	
	public static EntityAdmin getInstance(){
		
		if(instance == null){
			
			instance = new EntityAdmin();
		}
		
		return instance;
	}
	
	public IAdminAccessor getAdminAccessor(Principal principal){
		
		IAdminAccessor aa = null;
		try {
			aa = AccessorFactory.getInstance().buildGeneralAccessor(principal, IAdminAccessor.class);
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return aa;
	}
	
}
