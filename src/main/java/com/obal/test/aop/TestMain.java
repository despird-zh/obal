package com.obal.test.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.obal.cache.aop.CacheableGet;
import com.obal.test.BlankTester;

public class TestMain extends BlankTester{

	public static void main(String[] ars){
		
		initLog4j();
		testCache();
	}
	
	public static void testCache(){
		
		CacheTestAccessor cta = new CacheTestAccessor();
		
		cta.doPutDemo1("s1111","t1111");
		String n = cta.doPutDemo2("s2222","t222");
		System.out.println(n);
		String m = cta.doGetDemo1("nn","mm");
		System.out.println(m);
		cta.doDelDemo1();
	}
}
