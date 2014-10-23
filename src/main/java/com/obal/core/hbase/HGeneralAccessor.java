package com.obal.core.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.HConnection;

import com.obal.core.accessor.GeneralAccessor;

/**
 * Hbase General Accessor base class, it will hold HConnection object. 
 **/
public abstract class HGeneralAccessor extends GeneralAccessor implements HConnAware{

	private HConnection conn;	

	@Override
	public void setConnection(HConnection connection) {
		this.conn = connection;
	}

	@Override
	public HConnection getConnection() {
		
		return conn;
	}
	
	@Override
	public void release() {
		try {
			if (conn != null && !isEmbed())
				this.conn.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
