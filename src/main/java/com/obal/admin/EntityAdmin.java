package com.obal.admin; 

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.AccessorFactory;
import com.obal.core.security.Principal;
import com.obal.core.util.AccessorUtils;
import com.obal.exception.AccessorException;
import com.obal.exception.EntityException;
import com.obal.meta.EntityConstants;
import com.obal.meta.EntityManager;
import com.obal.meta.EntityMeta;
import com.obal.meta.accessor.IMetaAttrAccessor;

/**
 * EntityAdmin in charge of the entity loading and create, drop etc. operation.
 * 
 * @author despird
 * @version 0.1 2014-11-15 
 * 
 * @see AdminAccessor
 **/
public class EntityAdmin {

	Logger LOGGER = LoggerFactory.getLogger(EntityAdmin.class);
	
	private EntityAdmin(){
		
		String path = this.getClass().getPackage().getName().replace(".", "/");
		path += "/AccessorMap.hbase.properties";
		AccessorFactory.getInstance().appendMapping("hbase", path);		
	}
	
	private static EntityAdmin instance;
	
	/**
	 * Get the singleton instance 
	 * 
	 **/
	public static EntityAdmin getInstance(){
		
		if(instance == null){
			
			instance = new EntityAdmin();
		}
		
		return instance;
	}
	
	/**
	 * Get the AdminAccessor instance, it is used for entity meta operation
	 * 
	 * @param principal the principal object. 
	 **/
	public IAdminAccessor getAdminAccessor(Principal principal){
		
		IAdminAccessor aa = null;
		try {
			aa = AccessorFactory.getInstance().buildGeneralAccessor(principal, "AdminAccessor");
		} catch (EntityException e) {
			
			LOGGER.error("Error when getting Admin service",e);
		}
		
		return aa;
	}


	/**
	 * Load all the entity meta information from hbase.
	 * meta information will be stored in EntityManager instance.
	 * 
	 **/
	public void loadEntityMeta() {

		IMetaAttrAccessor imeta = null;
		Principal princ = new Principal("acc", "demo", "pwd");
		try {
			EntityManager smgr = EntityManager.getInstance();
			imeta = AccessorUtils.getGeneralAccessor(princ,
					EntityConstants.ENTITY_META_GENERAL);

			List<EntityMeta> entrymetas = imeta.getEntityMetaList();
			for (EntityMeta em : entrymetas) {

				smgr.putEntityMeta(em);
			}

		} catch (AccessorException e) {
			
			LOGGER.debug("Error when loading entity meta information",e);
		} catch (EntityException e) {
			
			LOGGER.debug("Error when loading entity meta information",e);
		} finally {
			AccessorUtils.releaseAccessor(imeta);
		}
	}
	
	/**
	 * Setup the schema create the schema as per the meta information.
	 * 
	 * @param meta the entity meta information
	 **/
	public void setupSchema(EntityMeta meta) {

		Principal princ = new Principal("acc", "demo", "pwd");

		IAdminAccessor aa = getAdminAccessor(princ);
		IMetaAttrAccessor metaAttrAccessor = null;
		try {

			aa.createSchema(meta.getEntityName(),
					meta.getAllAttrs());

			metaAttrAccessor = AccessorUtils.getGeneralAccessor(princ,
					EntityConstants.ENTITY_META_GENERAL);

			metaAttrAccessor.putEntityMeta(meta);

		} catch (AccessorException e) {
			
			LOGGER.debug("Error when loading entity meta information",e);
		} catch (EntityException e) {
			
			LOGGER.debug("Error when loading entity meta information",e);
		} finally {

			AccessorUtils.releaseAccessor(metaAttrAccessor, aa);
		}
	}
}
