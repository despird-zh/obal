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
package com.obal.core.accessor;

import com.obal.core.EntryKey;
import com.obal.core.IEntityAccessor;
import com.obal.meta.BaseEntity;

/**
 * Abstract EntryAccessor with EntrySchema information, it provides operation on
 * Entity
 * <p>The obvious difference from the GeneralAccessor is entityAccessor don't provide
 * principal related method. In fact the principal is held by entitySchema object.
 * because it's possible use principal to calculate the schema name, if want to get principal
 * , it's better use entitySchema object to retrieve it.</p>
 * 
 * @author despird
 * @version 0.1 2014-9-1
 * 
 **/
public abstract class EntityAccessor<GB extends EntryKey> implements IEntityAccessor <GB>{

	private BaseEntity entitySchema = null;// entry meta information holder
	private boolean embed = false;
	
	/**
	 * Constructor with entry schema information 
	 * 
	 * @param entitySchema the schema of entity
	 **/
	public EntityAccessor(BaseEntity entitySchema){
		
		this.entitySchema = entitySchema;
	}
	
	/**
	 * Get the entity schema  
	 * 
	 * @return entity schema
	 **/
	@Override
	public BaseEntity getEntitySchema(){
		
		return this.entitySchema;
	}
	
	/**
	 * Release the entity schema and clear the principal in it.
	 **/
	public void release(){
		
		if(entitySchema != null){
			
			entitySchema.clearPrincipal();
			
		}
		entitySchema = null;
	}
		
	public boolean isEmbed(){
		
		return embed;
	}
	
	public void setEmbed(boolean embed){
		
		this.embed = embed;
	}

}
