package com.obal.audit.hbase;

import com.obal.audit.AuditEvent;
import com.obal.core.hbase.HEntityAccessor;
import com.obal.core.hbase.HEntryWrapper;
import com.obal.core.meta.BaseEntity;

public class AuditAccessor extends HEntityAccessor<AuditEvent>{

	public AuditAccessor(BaseEntity schema) {
		super(schema);		
	}

	@Override
	public BaseEntity getEntitySchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HEntryWrapper<AuditEvent> getEntryWrapper() {
		// TODO Auto-generated method stub
		return null;
	}

}
