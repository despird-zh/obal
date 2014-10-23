
package com.obal.core.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * Interface to indicates object will be set Admin connection
 * 
 *  @author despird
 *  @version 0.1 2014-3-1
 **/
public interface HAdminAware {

	/**
	 * set Admin
	 * @param admin 
	 **/
	public void setAdmin(HBaseAdmin admin);
	
	/**
	 * get Admin 
	 **/
	public HBaseAdmin getAdmin();
}
