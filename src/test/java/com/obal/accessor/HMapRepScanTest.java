package com.obal.accessor;

import java.util.Properties;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.log4j.PropertyConfigurator;

import com.obal.admin.EntityAdmin;
import com.obal.core.hbase.HMapRedScan;
import com.obal.test.BlankTester;


public class HMapRepScanTest extends BlankTester{


	public void test() {
    	
		Properties prop = new Properties();

		prop.setProperty("log4j.rootCategory", "DEBUG, CONSOLE");
		prop.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss,SSS} [%t] %-5p %C{1} : %m%n");
		
		PropertyConfigurator.configure(prop);
    	String source = "obal.meta.attr";
    	Scan scan = new Scan();
    	scan.addFamily("c0".getBytes());
    	HMapRedScan hmrScan = new HMapRedScan(source, scan);
    	hmrScan.init();
    	try {
			hmrScan.scan();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 protected void setUp() throws Exception {  
		 initLog4j();
		//EntityAdmin eadmin = EntityAdmin.getInstance();
		//eadmin.loadEntityMeta();
	     super.setUp();  
	 }  
	  
	 protected void tearDown() throws Exception {  
	    
		 super.tearDown();  
	 } 
}
