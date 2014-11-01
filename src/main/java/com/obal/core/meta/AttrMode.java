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
package com.obal.core.meta;

/**
 * The attribute support four kinds of value store mode:
 * MAP,LIST,SET,PRIMITIVE.
 *  
 * @author despird 
 **/
public enum AttrMode {
	
	PRIMITIVE("PRIMITIVE"), // Primitive :int ,long etc single element
	MAP("MAP"), // Map key-value pair
	LIST("LIST"), // List the array of element
	SET("SET"); // Set the array of element
	
	private String mode = null;
	
	/**
	 * Hide Rtype default constructor 
	 **/
	private AttrMode(String mode){  
		this.mode = mode;
    } 
	
	@Override
	public String toString(){
		return this.mode;
	}
}
