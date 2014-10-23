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
package com.obal.core.util;

import com.obal.core.AccessorFactory;
import com.obal.core.IBaseAccessor;
import com.obal.core.security.Principal;
import com.obal.exception.EntityException;

/**
 * Utility tool class for Accessor acquire.
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * 
 * @see AccessorFactory
 **/
public class AccessorUtils {

	/**
	 * Get Entry Accessor
	 * @param principal the user principal
	 * @param entryType
	 * 
	 * @return K the instance of accessor 
	 **/
	public static <K> K getEntryAccessor(Principal principal,String entryType)throws EntityException{
		
		return AccessorFactory.getInstance().buildEntityAccessor(principal, entryType);
	}	

	/**
	 * Get Gereral Accessor
	 * @param principal the user principal
	 * @param accessorName
	 * 
	 * @return K the instance of accessor 
	 **/
	public static <K> K getGeneralAccessor(Principal principal,String accessorName)throws EntityException{
		
		return AccessorFactory.getInstance().buildGeneralAccessor(principal, accessorName);
	}

	/**
	 * Get Gereral Accessor
	 * @param principal the user principal
	 * @param class
	 * 
	 * @return K the instance of accessor 
	 **/
	@Deprecated
	public static <K> K getGeneralAccessor(Principal principal,Class<K> clazz)throws EntityException{
		
		return AccessorFactory.getInstance().buildGeneralAccessor(principal, clazz);
	}

	/**
	 * Get embed General Accessor
	 * @param mockupAccessor the mock-up instance 
	 * @param entryName accessorname
	 * 
	 * @return K the instance of accessor 
	 **/
	public static <K> K getEmbedGeneralAccessor(IBaseAccessor mockupAccessor, String accessorName)throws EntityException{
		
		return AccessorFactory.getInstance().buildEmbedGeneralAccessor(mockupAccessor, accessorName);
	}
	
	/**
	 * Get embed Entry Accessor
	 * @param mockupAccessor the mock-up instance 
	 * @param entryName
	 * 
	 * @return K the instance of accessor 
	 **/
	public static <K> K getEmbedEntityAccessor(IBaseAccessor mockupAccessor, String entryName)throws EntityException{
		
		return AccessorFactory.getInstance().buildEmbedEntityAccessor(mockupAccessor, entryName);
	}
	
	/**
	 * Release the resource of accessor array
	 * 
	 * @param bAccessors The accessor array of IBaseAccessor object. 
	 * 
	 **/
	public static void releaseAccessor(IBaseAccessor ... bAccessors){
		
		for(IBaseAccessor bAccessor:bAccessors){
			if(bAccessor != null)
				bAccessor.release();
		}
	}
}
