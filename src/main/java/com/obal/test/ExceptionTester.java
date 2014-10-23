package com.obal.test;

import com.obal.exception.EntityException;

public class ExceptionTester {

	
	public static void main(String[] args){
		
		try{
			
			throw new EntityException("ERR01",new Exception("sdf"),"[p1]","[p2]");		

		}catch(EntityException e){
			e.printStackTrace();
		}
		try{

		throw new EntityException("ERR02","[p1]","[p2]");		
		
		}catch(EntityException e){
			e.printStackTrace();
		}
	}
}
