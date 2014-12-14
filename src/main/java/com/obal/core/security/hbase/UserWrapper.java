package com.obal.core.security.hbase;

import java.util.List;
import java.util.Map;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

import com.obal.core.hbase.HEntryWrapper;
import com.obal.core.security.Principal;
import com.obal.exception.WrapperException;
import com.obal.meta.EntityAttr;
import com.obal.meta.EntityManager;

public class UserWrapper extends HEntryWrapper<Principal>{

	@Override
	public Principal wrap(List<EntityAttr> attrs, Result rawEntry)
			throws WrapperException {
		
		Result entry = (Result)rawEntry;
				
		Principal princ = new Principal(new String(entry.getRow()));
		String val = null;
		for(EntityAttr attr: attrs){
			byte[] column = attr.getColumn().getBytes();
			byte[] qualifier = attr.getQualifier().getBytes();
			byte[] cell = entry.getValue(column, qualifier);
			if("i_account".equals(attr.getAttrName())){
				val = (String)super.getPrimitiveValue(attr, cell);
				princ.setAccount(val);
				continue;
			}else if("i_name".equals(attr.getAttrName())){
				
				val = (String)super.getPrimitiveValue(attr, cell);
				princ.setName(val);
				continue;
			}else if("i_source".equals(attr.getAttrName())){
				
				val = (String)super.getPrimitiveValue(attr, cell);
				princ.setSource(val);
				continue;
			}else if("i_password".equals(attr.getAttrName())){
				
				val = (String)super.getPrimitiveValue(attr, cell);
				princ.setPassword(val);
				continue;
			}else if("i_groups".equals(attr.getAttrName())){
				
				Map<String,Object> groups = (Map<String,Object>)super.getMapValue(attr, cell);
				princ.setGroups(groups);
				continue;
			}else if("i_roles".equals(attr.getAttrName())){
				
				Map<String,Object> roles = (Map<String,Object>)super.getMapValue(attr, cell);
				princ.setGroups(roles);
				continue;
			}			
		}
		
		
		
		return princ;
	}

	@Override
	public Put parse(List<EntityAttr> attrs, Principal entryInfo)
			throws WrapperException {
		
		return null;
	}

	@Override
	public Principal wrap(String entityName, Result rawEntry)
			throws WrapperException {
		
		List<EntityAttr> attrs = EntityManager.getInstance().getEntityMeta(entityName).getAllAttrs();
		return wrap(attrs, rawEntry);
	}

}
