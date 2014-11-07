package com.obal.test.aop;

import com.obal.test.BlankTester;

public class TestMain extends BlankTester{

	public static void main(String[] ars){
		
		initLog4j();
		
		CacheTestAccessor cta = new CacheTestAccessor();
		
		cta.doPutDemo1();
		
		String m = cta.doGetDemo1();
		System.out.println(m);
		cta.doDelDemo1();
	}
}
