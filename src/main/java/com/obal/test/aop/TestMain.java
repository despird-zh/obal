package com.obal.test.aop;

import com.obal.test.BlankTester;

public class TestMain extends BlankTester{

	public static void main(String[] ars){
		
		initLog4j();
		
		CacheTestAccessor cta = new CacheTestAccessor();
		
		cta.doPutDemo1("s1111","t1111");
		String n = cta.doPutDemo2("s2222","t222");
		System.out.println(n);
		String m = cta.doGetDemo1("nn","mm");
		System.out.println(m);
		cta.doDelDemo1();
	}
}
