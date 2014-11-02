package com.obal.core.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.obal.core.AccessorBuilder;
import com.obal.core.IBaseAccessor;
import com.obal.core.security.Principal;
import com.obal.core.security.PrincipalAware;
import com.obal.exception.EntityException;
/**
 * Hbase-wise implementation of AccessorBuilder.
 * All accessors access the hbase will be created by this class
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * 
 * @see AccessorBuilder
 * @see HEntityAccessor
 * @see HGeneralAccessor
 **/
public class RAccessorBuilder extends AccessorBuilder{

	static Logger LOGGER = LoggerFactory.getLogger(RAccessorBuilder.class);
	
	private JedisPoolConfig  config = null;
	
	private JedisPool pool = null;
	
	/**
	 * constructor 
	 * @param builderName 
	 * @param accessorMap 
	 **/
	public RAccessorBuilder(String builderName, String accessormap) throws EntityException{
		
		super(builderName,accessormap);
		
		config = new JedisPoolConfig();  
	    config.setMaxIdle(20000);  
	    config.setTestOnBorrow(true);  
	    config.setTestOnReturn(true);
	    try{    
            /** 
             *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息 
             *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒) 
             */  
            pool = new JedisPool(config, "127.0.0.1", 6379 , 12000);  
        } catch(Exception e) {  
            e.printStackTrace();  
        } 
	}

	@Override
	public void assembly(Principal principal,IBaseAccessor accessor) {
		Jedis jedis = null;		
		if(accessor instanceof RJedisAware){
			
			try {
				
				jedis = pool.getResource();
				((RJedisAware) accessor).setJedis(jedis);
				
			} catch (Exception e) {
				LOGGER.error("Error when assembly Accessor:set HConnection",e);
			}			
		}
				
		if(accessor instanceof PrincipalAware){
			((PrincipalAware) accessor).setPrincipal(principal);		
		}
	}

	@Override
	public void assembly(IBaseAccessor mockupAccessor,
			IBaseAccessor... accessors) throws EntityException {
		
		Jedis jedis = null;		
		Principal principal = null;
		for(IBaseAccessor accessor:accessors){
			
			if((mockupAccessor instanceof RJedisAware) 
					&& (accessor instanceof RJedisAware)){
				
				jedis = ((RJedisAware) mockupAccessor).getJedis();
				((RJedisAware) accessor).setJedis(jedis);		
			}
			
			if((mockupAccessor instanceof RJedisAware) 
					&& (accessor instanceof PrincipalAware)){
				principal = ((PrincipalAware) accessor).getPrincipal();
				((PrincipalAware) accessor).setPrincipal(principal);		
			}
			// Set embed flag
			accessor.setEmbed(true);
		}
	}
}
