/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package com.obal.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.accessor.EntityAccessor;
import com.obal.core.accessor.GeneralAccessor;
import com.obal.core.hbase.HAccessorBuilder;
import com.obal.core.security.Principal;
import com.obal.core.security.PrincipalAware;
import com.obal.exception.EntityException;

/**
 * AccessorFactory create service instances according to request entry name, class etc.
 * 
 * @author despird 
 * @version 0.1 2014-3-1
 * @see AccessorBuilder
 **/
public class AccessorFactory {

	Logger LOGGER = LoggerFactory.getLogger(AccessorFactory.class);
	/** AccessorBuilder cache */
	private Map<String, AccessorBuilder> builderMap = new HashMap<String, AccessorBuilder>();
	
	/**
	 * Hide from explicit invoke 
	 **/
	private AccessorFactory(){
		
		try{
			defaultBuilder = new HAccessorBuilder("hbase","com/obal/core/AccessorMap.hbase.properties");
			builderMap.put(defaultBuilder.getBuilderName(), defaultBuilder);
			appendMapping("hbase","com/obal/core/meta/AccessorMap.hbase.properties");
		}catch(EntityException ee){
			
			ee.printStackTrace();
		}
	}
	/** singleton */
	private static AccessorFactory instance;
	
	/** default builder */
	private AccessorBuilder defaultBuilder = null;
	
	/**
	 * Singleton intance
	 * @retrun  AccessorFactory the singleton instance.
	 **/
	public static AccessorFactory getInstance(){
		
		if(instance == null)
			instance = new AccessorFactory();
		
		return instance;
	}
	
	/**
	 * Append service mapping to Factory
	 * 
	 * @param builderName the name of builder
	 * @param resourcePath the path of resource file, eg. com/tt/xx/mm.properties
	 *  
	 **/
	public void appendMapping(String builderName, String resourcePath){
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
		
		Properties prop = new Properties();
		try {
			prop.load(is);
			Map<String,String> entries = new HashMap<String,String>();
			
			for (final String name: prop.stringPropertyNames())
				entries.put(name, prop.getProperty(name));
			
			appendMapping("hbase", entries);
			
		} catch (IOException e) {
			
			LOGGER.error("Error during EntryAdmin contrustor.",e);
		}
	}
	
	/**
	 * Append service mapping to Factory
	 * 
	 * @param builderName the name of builder
	 * @param mapping the mapping of services
	 **/
	public void appendMapping(String builderName, Map<String,String> mapping){
		
		builderMap.get(builderName).appendAccessorMap(mapping);
	}
	
	/**
	 * Build entry service
	 * @param principal the principal
	 * @param entryName the name of entry, eg. the map key of service class
	 **/
	public <K> K buildEntityAccessor(Principal principal,String entryName)throws EntityException{
		
		K accessor = defaultBuilder.newEntityAccessor(entryName,principal);
		defaultBuilder.assembly(principal, (EntityAccessor<?>)accessor);
		return accessor;
	}	
	
	/**
	 * Build General service
	 * @param principal the principal
	 * @param accessorName the name of entry, eg. the map key of service class
	 **/
	public <K> K buildGeneralAccessor(Principal principal,String accessorName)throws EntityException{
		
		K accessor = defaultBuilder.newGeneralAccessor(accessorName);
		defaultBuilder.assembly(principal, (GeneralAccessor)accessor);
		return accessor;
	}

	/**
	 * Build General service
	 * 
	 * It's not suggested to use class to build service.
	 * 
	 * @param principal the principal
	 * @param clazz the name of entry interface class, eg. the map key of service class
	 **/
	@Deprecated 
	public <K> K buildGeneralAccessor(Principal principal,Class<K> clazz)throws EntityException{
		
		K accessor = defaultBuilder.newGeneralAccessor(clazz);
		defaultBuilder.assembly(principal, (GeneralAccessor)accessor);
		return accessor;
	}
	
	/**
	 * Build embed EntryAccessor instance
	 * @param mockupAccessor the mock-up accessor instance
	 * @param entryName the name of entry 
	 **/
	public <K> K buildEmbedEntityAccessor(IBaseAccessor mockupAccessor,String entryName)throws EntityException{
		Principal principal = null;
		if(mockupAccessor instanceof PrincipalAware)
			principal = ((PrincipalAware) mockupAccessor).getPrincipal();
		
		K accessor = defaultBuilder.newEntityAccessor(entryName, principal);
		defaultBuilder.assembly(mockupAccessor, (IBaseAccessor)accessor);
		return accessor;
	}

	/**
	 * Build embed GeneralAccessor instance
	 * @param mockupAccessor the mock-up accessor instance
	 * @param entryName the name of entry 
	 **/
	public <K> K buildEmbedGeneralAccessor(IBaseAccessor mockupAccessor,String accessorName)throws EntityException{

		K accessor = defaultBuilder.newGeneralAccessor(accessorName);
		defaultBuilder.assembly(mockupAccessor, (IBaseAccessor)accessor);
		return accessor;
	}
}
