package com.obal.core.hbase;

import com.obal.core.EntryWrapper;
import com.obal.core.accessor.IUserAccessor;
import com.obal.core.accessor.RawEntry;
import com.obal.core.meta.BaseEntity;

public class UserAccessor extends HEntityAccessor<RawEntry> implements IUserAccessor {

	public UserAccessor(BaseEntity schema) {
		super(schema);
		// TODO Auto-generated constructor stub
	}

	@Override
	public EntryWrapper<RawEntry> getEntryWrapper() {
		// TODO Auto-generated method stub
		return null;
	}

}
