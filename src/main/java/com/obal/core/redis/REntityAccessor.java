package com.obal.core.redis;

import java.io.IOException;
import java.util.List;

import redis.clients.jedis.Jedis;

import com.obal.core.AccessorFactory;
import com.obal.core.EntryFilter;
import com.obal.core.EntryInfo;
import com.obal.core.EntryKey;
import com.obal.core.EntryWrapper;
import com.obal.core.accessor.EntityAccessor;
import com.obal.core.meta.BaseEntity;
import com.obal.core.util.CoreConstants;
import com.obal.exception.AccessorException;

public class REntityAccessor <GB extends EntryInfo> extends EntityAccessor<GB> implements RJedisAware{

	private Jedis jedis;
	
	public REntityAccessor(BaseEntity entitySchema) {
		super(entitySchema);
	}

	@Override
	public EntryWrapper<GB> getEntryWrapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryKey putEntry(GB entryInfo) throws AccessorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryKey putEntryAttr(String entryKey, String attrName, Object value)
			throws AccessorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GB getEntry(String entryKey) throws AccessorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <K> K getEntryAttr(String entryKey, String attrName)
			throws AccessorException {
		
		return null;
	}

	@Override
	public void deleteEntry(String... entryKey) throws AccessorException {

		jedis.del(entryKey);
	}

	@Override
	public List<GB> scanEntry(EntryFilter<?> scanfilter)
			throws AccessorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFilterSupported(EntryFilter<?> scanfilter,
			boolean throwExcep) throws AccessorException {
		
		return false;
	}

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
