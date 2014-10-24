package com.obal.core.hbase;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.AccessorBuilder;
import com.obal.core.IBaseAccessor;
import com.obal.core.security.Principal;
import com.obal.core.security.PrincipalAware;
import com.obal.exception.EntityException;
/**
 * Hbase-wise implementation of AccessorBuilder.
 * All accessors access the hbase will be created by this class
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * 
 * @see AccessorBuilder
 * @see HEntityAccessor
 * @see HGeneralAccessor
 **/
public class HAccessorBuilder extends AccessorBuilder{

	static Logger LOGGER = LoggerFactory.getLogger(HAccessorBuilder.class);
	
	private Configuration config = null;
	
	/**
	 * constructor 
	 * @param builderName 
	 * @param accessorMap 
	 **/
	public HAccessorBuilder(String builderName, String accessormap) throws EntityException{
		super(builderName,accessormap);
		
		config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.property.clientPort", "2181");
		config.set("hbase.zookeeper.quorum", "192.168.1.133");
		config.set("hbase.master", "192.168.1.133:60010");
		File file = new File(".");
		try {

			String path = file.getCanonicalPath();
			System.out.println("===:" + path);
			System.setProperty("hadoop.home.dir", path + "/target/classes");

		} catch (IOException e) {
			LOGGER.error("Error when create AccessorBuilder",e);
		}
	}

	@Override
	public void assembly(Principal principal,IBaseAccessor accessor) {
		HConnection connection = null;		
		HBaseAdmin hBaseAdmin = null;
		if(accessor instanceof HConnAware){
			try {
				connection = HConnectionManager.createConnection(config);
				((HConnAware) accessor).setConnection(connection);
			} catch (IOException e) {
				LOGGER.error("Error when assembly Accessor:set HConnection",e);
			}			
		}
		
		if(accessor instanceof HAdminAware){
			try {
				hBaseAdmin = new HBaseAdmin(config);
				((HAdminAware) accessor).setAdmin(hBaseAdmin);
				
			} catch (IOException e) {
				LOGGER.error("Error when assembly Accessor:set HBaseAdmin",e);
			}			
		}
		
		if(accessor instanceof PrincipalAware){
			((PrincipalAware) accessor).setPrincipal(principal);		
		}
	}

	@Override
	public void assembly(IBaseAccessor mockupAccessor,
			IBaseAccessor... accessors) throws EntityException {
		
		HConnection connection = null;		
		HBaseAdmin hBaseAdmin = null;
		Principal principal = null;
		for(IBaseAccessor accessor:accessors){
			
			if((mockupAccessor instanceof HConnAware) 
					&& (accessor instanceof HConnAware)){
				
				connection = ((HConnAware) mockupAccessor).getConnection();
				((HConnAware) accessor).setConnection(connection);		
			}
			
			if((mockupAccessor instanceof HConnAware) 
					&& (accessor instanceof HAdminAware)){

				hBaseAdmin = ((HAdminAware) accessor).getAdmin();
				((HAdminAware) accessor).setAdmin(hBaseAdmin);
		
			}
			
			if((mockupAccessor instanceof HConnAware) 
					&& (accessor instanceof PrincipalAware)){
				principal = ((PrincipalAware) accessor).getPrincipal();
				((PrincipalAware) accessor).setPrincipal(principal);		
			}
			// Set embed flag
			accessor.setEmbed(true);
		}
	}
}
