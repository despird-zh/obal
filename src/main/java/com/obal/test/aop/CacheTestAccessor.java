package com.obal.test.aop;

import com.obal.audit.aop.Auditable;

public class CacheTestAccessor {
	
	DemoP dp = new DemoP("CTA String --s-s");
	
	@Auditable(verb="putdemo")
	public void doPutDemo1(String a1,String a2){
		
		System.out.println("--------calling doPutDemo1");
	}

	@Auditable(verb="putdemo")
	public String doPutDemo2(String a1,String a2){
		
		System.out.println("--------calling doPutDemo2");
		return "===doget result===";
	}
	
	public String doGetDemo1(String a1,String a2){
		
		
		System.out.println("--------calling doGetDemo1");
		return "===doget result===";
	}
	
	public void doDelDemo1(){
		
		System.out.println("--------calling doDelDemo1");
	}
	
	public DemoP getdp(){
		
		return dp;
	}
}
