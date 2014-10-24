package com.obal.core.meta.hbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.EntryWrapper;
import com.obal.core.accessor.RawEntry;
import com.obal.core.hbase.HEntityAccessor;
import com.obal.core.hbase.HRawWrapper;
import com.obal.core.meta.BaseEntity;

public class MetaAccessor extends HEntityAccessor<RawEntry>{

	public static Logger LOGGER = LoggerFactory.getLogger(MetaAccessor.class);
	
	public MetaAccessor(BaseEntity schema) {
		super(schema);
	}

	@Override
	public EntryWrapper<RawEntry> getEntryWrapper() {
		
		HRawWrapper wrapper = new HRawWrapper();		

		return wrapper;
	}
	
}
