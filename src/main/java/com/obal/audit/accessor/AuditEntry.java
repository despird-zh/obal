package com.obal.audit.accessor;

import com.obal.core.EntryKey;
import com.obal.core.meta.BaseEntity;
import com.obal.core.meta.EntityMeta;
import com.obal.exception.MetaException;

public class AuditEntry extends BaseEntity{

	public AuditEntry(EntityMeta meta) {
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
