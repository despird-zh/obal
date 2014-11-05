package com.obal.core.security.hbase;

import com.obal.core.hbase.HEntryWrapper;
import com.obal.core.accessor.RawEntry;
import com.obal.core.hbase.HEntityAccessor;
import com.obal.core.security.accessor.IUserAccessor;
import com.obal.meta.BaseEntity;

public class UserAccessor extends HEntityAccessor<RawEntry> implements IUserAccessor {

	public UserAccessor(BaseEntity schema) {
		super(schema);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HEntryWrapper<RawEntry> getEntryWrapper() {
		// TODO Auto-generated method stub
		return null;
	}

}
