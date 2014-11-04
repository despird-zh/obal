/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package com.obal.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.obal.core.AccessorFactory;
import com.obal.core.EntryFilter;
import com.obal.core.EntryInfo;
import com.obal.core.EntryKey;
import com.obal.core.accessor.EntityAccessor;
import com.obal.core.meta.BaseEntity;
import com.obal.core.meta.EntityAttr;
import com.obal.core.util.CoreConstants;
import com.obal.exception.AccessorException;

public abstract class REntityAccessor <GB extends EntryInfo> extends EntityAccessor<GB> implements RJedisAware{

	Logger LOGGER = LoggerFactory.getLogger(REntityAccessor.class);
	
	private Jedis jedis;
	
	public REntityAccessor(BaseEntity entitySchema) {
		super(entitySchema);
	}

	/**
	 * get entry wrapper
	 * @return wrapper object 
	 **/
	public abstract REntryWrapper<GB> getEntryWrapper();
	
	@Override
	public EntryKey putEntry(GB entryInfo) throws AccessorException {

		EntryKey rtv = null;
		BaseEntity entitySchema = (BaseEntity)getEntitySchema();

		REntryWrapper<GB> wrapper = this.getEntryWrapper();

		wrapper.parse(entitySchema.getEntityMeta().getAllAttrs(), this.jedis,entryInfo);

		rtv = entryInfo.getEntryKey();

		return rtv;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EntryKey putEntryAttr(String entryKey, String attrName, Object value)
			throws AccessorException {
		EntryKey rtv = null;
		BaseEntity entitySchema = (BaseEntity)getEntitySchema();
		EntityAttr attr = entitySchema.getEntityMeta().getAttr(attrName);
		REntryWrapper<GB> wrapper = this.getEntryWrapper();

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("--==>>attr:{} - value:{}",attr.getAttrName(),value);
        }
        switch(attr.mode){
        
            case PRIMITIVE:
            	wrapper.putPrimitiveValue(jedis,entryKey, attr, value);
            	break;
            case MAP:
            	if(!(value instanceof Map<?,?>))
        			throw new AccessorException("the attr:{} value is not Map object",attrName);        		
        		wrapper.putMapValue(jedis,entryKey, attr, (Map<String,Object>)value);	
        		break;
            case LIST:
            	if(!(value instanceof List<?>))
        			throw new AccessorException("the attr:{} value is not List object",attrName);        		
        		wrapper.putListValue(jedis,entryKey, attr, (List<Object>)value);	
        		break;
            case SET:
            	if(!(value instanceof List<?>))
        			throw new AccessorException("the attr:{} value is not List object",attrName);        		
        		wrapper.putSetValue(jedis, entryKey, attr, (Set<Object>)value);	
        		break;
            default:
            	break;      	
        }
		return rtv;
	}

	@Override
	public GB getEntry(String entryKey) throws AccessorException {
		GB rtv = null;
		BaseEntity entrySchema = (BaseEntity)getEntitySchema();
		REntryWrapper<GB> wrapper = (REntryWrapper<GB>)getEntryWrapper();
		rtv = wrapper.wrap(entrySchema.getEntityName(),entryKey, jedis);
		return rtv;
	}

	@Override
	public <K> K getEntryAttr(String entryKey, String attrName)
			throws AccessorException {
		Object rtv = null;
		BaseEntity entitySchema = (BaseEntity)getEntitySchema();
		EntityAttr attr = entitySchema.getEntityMeta().getAttr(attrName);
    	REntryWrapper<GB> wrapper = (REntryWrapper<GB>)getEntryWrapper();

    	switch(attr.mode){
	    	case PRIMITIVE:
	    		byte[] cell = jedis.hget(entryKey.getBytes(), attr.getAttrName().getBytes());
				rtv = wrapper.getPrimitiveValue(attr, cell);
	    		break;
	    	case MAP:			
	        	Map<byte[], byte[]> cells = jedis.hgetAll(entryKey.getBytes());
				rtv = wrapper.getMapValue(attr, cells);		    		
	    		break;
	    	case LIST:
	    		long len = jedis.llen(entryKey);
	    		List<byte[]> celllist = jedis.lrange(entryKey.getBytes(), 0, len);
				rtv = wrapper.getListValue(attr, celllist);		    		
	    		break;
	    	case SET:
	    		Set<byte[]> cellset = jedis.smembers(entryKey.getBytes());
				rtv = wrapper.getSetValue(attr, cellset);	
	    		break;
	    	default:
	    		break;
    	}
		return (K)rtv;
	}

	@Override
	public void deleteEntry(String... entryKeys) throws AccessorException {
		BaseEntity entitySchema = (BaseEntity)getEntitySchema();
		// get non-primitive attributes
		List<EntityAttr> attrs = entitySchema.getEntityMeta().getAttrs(false);
		
		for(String entrykey:entryKeys){
			
			String redisKey = entitySchema.getEntityName()+ CoreConstants.KEYS_SEPARATOR + entrykey;
			jedis.del(redisKey); // delete primitive data
			for(EntityAttr attr:attrs){
				// delete non-primitive data
				jedis.del(redisKey + CoreConstants.KEYS_SEPARATOR + attr.getAttrName());
			}
		}
	}

	@Deprecated
	@Override
	public List<GB> scanEntry(EntryFilter<?> scanfilter)
			throws AccessorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	@Override
	public abstract boolean isFilterSupported(EntryFilter<?> scanfilter,
			boolean throwExcep) throws AccessorException ;

	@Override
	public void setJedis(Jedis jedis) {

		this.jedis = jedis;
	}

	@Override
	public Jedis getJedis() {
		
		return this.jedis;
	}

	@Override
	public void release() {
		try {
			// embed means share connection, close it directly affect other accessors using this conn.
			if (jedis != null && !isEmbed()){
				
				RAccessorBuilder builder = (RAccessorBuilder)AccessorFactory.getInstance().getAccessorBuilder(CoreConstants.BUILDER_REDIS);
				builder.returnJedis(jedis);
			}

			super.release();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
