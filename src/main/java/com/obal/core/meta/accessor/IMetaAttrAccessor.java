package com.obal.core.meta.accessor;

import java.util.List;

import com.obal.core.EntryKey;
import com.obal.core.IBaseAccessor;
import com.obal.core.meta.EntityAttr;
import com.obal.core.meta.EntityMeta;
import com.obal.exception.AccessorException;

public interface IMetaAttrAccessor extends IBaseAccessor{

	public EntityAttr getAttr(String attrId )throws AccessorException;
	
	public List<EntityAttr> getAttrList(String entryName)throws AccessorException;
	
	public EntryKey putEntryAttr(EntityAttr attr)throws AccessorException;
	
	public EntityMeta getEntryMeta(String entryName)throws AccessorException;
	
	public List<EntityMeta> getEntryMetaList()throws AccessorException;
	
	public EntryKey putEntryMeta(EntityMeta meta)throws AccessorException;
	
}
