package com.obal.core.meta;

import com.obal.core.EntryKey;
import com.obal.exception.MetaException;

public class GeneralEntity extends BaseEntity{
		
	public GeneralEntity(EntityMeta meta) {
		super(meta);
	}

	@Override
	public String getSchema() {
		
		return getEntityMeta().getEntityName();
	}

	@Override
	public EntryKey newKey(Object... parameter) throws MetaException {
		
		return newKey();
	}

	@Override
	public EntryKey newKey() throws MetaException {
		
		String key = String.valueOf(System.currentTimeMillis());		
		return new EntryKey(getEntityMeta().getEntityName(),key);
	}
	
}
