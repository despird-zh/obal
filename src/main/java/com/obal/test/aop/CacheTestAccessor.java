package com.obal.test.aop;

public class CacheTestAccessor {
	
	public void doPutDemo1(String a1,String a2){
		
		System.out.println("--------calling doPutDemo1");
	}
	
	public String doGetDemo1(String a1,String a2){
		
		
		System.out.println("--------calling doGetDemo1");
		return "===doget result===";
	}
	
	public void doDelDemo1(){
		
		System.out.println("--------calling doDelDemo1");
	}
}
