package com.obal.test.aop;

public class TestMain {

	public static void main(String[] ars){
		
		CacheTestAccessor cta = new CacheTestAccessor();
		
		cta.doPutDemo1();
		
		String m = cta.doGetDemo1();
		System.out.println(m);
		cta.doDelDemo1();
	}
}
