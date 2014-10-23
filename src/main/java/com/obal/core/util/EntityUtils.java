package com.obal.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.meta.BaseEntity;
import com.obal.core.meta.EntityMeta;
import com.obal.core.meta.EntityManager;
import com.obal.core.security.Principal;
import com.obal.exception.MetaException;

public class EntityUtils {

	static Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class);
	
	public static EntityMeta getEntryMeta(String entryName){
		
		return EntityManager.getInstance().getEntityMeta(entryName);
	}
	
	public static BaseEntity getEntrySchema(String entryName, Principal principal){
		
		BaseEntity schema = null;
		try {
			schema = (BaseEntity) EntityManager.getInstance().getEntitySchema(entryName, principal);
		} catch (MetaException e) {
			
			LOGGER.error("Error when get schema instance from manager.",e);
		}
		
		return schema;
	}
}
