package com.obal.audit.accessor;

import com.obal.core.EntryKey;
import com.obal.exception.MetaException;
import com.obal.meta.BaseEntity;
import com.obal.meta.EntityMeta;

public class AuditEntity extends BaseEntity{

	public AuditEntity(EntityMeta meta) {
		super(meta);
	}

	@Override
	public String getSchema() {
		
		return "autit.info";
	}

	@Override
	public EntryKey newKey(Object... parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryKey newKey() throws MetaException {
		// TODO Auto-generated method stub
		return null;
	}
}
