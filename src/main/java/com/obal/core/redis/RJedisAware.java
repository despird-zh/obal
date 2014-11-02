package com.obal.core.redis;

import redis.clients.jedis.Jedis;
/**
 * Interface to indicates object will be set connection
 * 
 *  @author despird
 *  @version 0.1 2014-3-1
 **/
public interface RJedisAware {

	/**
	 * set HConnection 
	 **/
	public void setJedis(Jedis connection);
	
	/**
	 * get HConnection 
	 **/
	public Jedis getJedis();
}
