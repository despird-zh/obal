package com.obal.core.hbase;

import org.apache.hadoop.hbase.client.HConnection;
/**
 * Interface to indicates object will be set connection
 * 
 *  @author despird
 *  @version 0.1 2014-3-1
 **/
public interface HConnAware {

	/**
	 * set HConnection 
	 **/
	public void setConnection(HConnection connection);
	
	/**
	 * get HConnection 
	 **/
	public HConnection getConnection();
}
