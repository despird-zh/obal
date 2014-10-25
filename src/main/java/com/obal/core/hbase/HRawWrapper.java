package com.obal.core.hbase;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.accessor.RawEntry;
import com.obal.core.meta.AttrMode;
import com.obal.core.meta.EntityAttr;
import com.obal.core.meta.EntityConstants;
import com.obal.core.meta.EntityManager;
import com.obal.core.meta.EntityMeta;

/**
 * Hbase Raw entry wrapper in charge of hbase Result to object list conversion.
 * 
 * @author despird
 * @version 0.1 2014-3-1
 * 
 **/
public class HRawWrapper extends HEntryWrapper<RawEntry>{

	public static Logger LOGGER = LoggerFactory.getLogger(HRawWrapper.class);

	@Override
	public RawEntry wrap(String entityName,Object rawEntry) {
						
		Result entry = (Result)rawEntry;	
		EntityMeta meta = EntityManager.getInstance().getEntityMeta(entityName);
		
		List<EntityAttr> attrs = meta.getAllAttrs();
		
		RawEntry gei = new RawEntry(entityName,new String(entry.getRow()));
		
		for(EntityAttr attr: attrs){
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Wrapping entity:{} - attribute:{}",entityName, attr.getAttrName());
			}
			byte[] column = attr.getColumn().getBytes();
			byte[] qualifier = attr.getQualifier().getBytes();
			if(attr.mode == AttrMode.PRIMITIVE){
				
				Cell cell = entry.getColumnLatestCell(column, qualifier);
				Object value = super.getPrimitiveValue(attr, cell);
				gei.put(attr.getAttrName(), value);				
			}
			
			if(attr.mode == AttrMode.MAP){
				
				NavigableMap<byte[], byte[]> cells = entry.getFamilyMap(column);
				Map<String, Object> map = super.getMapValue(attr, cells);				
				gei.put(attr.getAttrName(), map);
			}
			
			if(attr.mode == AttrMode.LIST){

				NavigableMap<byte[], byte[]> cells = entry.getFamilyMap(column);
				List<Object> list = super.getListValue(attr, cells);
				
				gei.put(attr.getAttrName(), list);
			}
		}
		
		return gei;
	}
	
	@Override
	public RawEntry wrap(List<EntityAttr> attrs,Object rawEntry) {
						
		Result entry = (Result)rawEntry;
		String entityName = attrs.size()>0? (attrs.get(0).getEntityName()):EntityConstants.ENTITY_BLIND;
		if(entityName == null || entityName.length()==0){
			
			entityName = EntityConstants.ENTITY_BLIND;
		}
		RawEntry gei = new RawEntry(entityName,new String(entry.getRow()));
		
		for(EntityAttr attr: attrs){
			byte[] column = attr.getColumn().getBytes();
			byte[] qualifier = attr.getQualifier().getBytes();
			if(attr.mode == AttrMode.PRIMITIVE){
				
				Cell cell = entry.getColumnLatestCell(column, qualifier);
				Object value = super.getPrimitiveValue(attr, cell);
				gei.put(attr.getAttrName(), value);				
			}
			
			if(attr.mode == AttrMode.MAP){
				
				NavigableMap<byte[], byte[]> cells = entry.getFamilyMap(column);
				Map<String, Object> map = super.getMapValue(attr, cells);				
				gei.put(attr.getAttrName(), map);
			}
			
			if(attr.mode == AttrMode.LIST){

				NavigableMap<byte[], byte[]> cells = entry.getFamilyMap(column);
				List<Object> list = super.getListValue(attr, cells);
				
				gei.put(attr.getAttrName(), list);
			}
		}
		
		return gei;
	}

	@Override
	public boolean supportWrap(Class<?> clazz) {
		
		return clazz == Result.class;
	}

	@Override
	public boolean supportParse(Class<?> clazz) {
		
		return clazz == Put.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object parse(List<EntityAttr> attrs,RawEntry entryInfo) {
		Put put = new Put(entryInfo.getKeyBytes());

        for(EntityAttr attr:attrs){

        	Object value = entryInfo.get(attr.getAttrName());
        	if(LOGGER.isDebugEnabled()){
        		LOGGER.debug("--==>>attr:{} - value:{}",attr.getAttrName(),value);
        	}
        	if(null == value) continue;
        	
			if(attr.mode == AttrMode.PRIMITIVE){
				
				super.putPrimitiveValue(put, attr, value);		
			}
			
			if(attr.mode == AttrMode.MAP){
				
				super.putMapValue(put, attr, (Map<String,Object>)value);	
			}
			
			if(attr.mode == AttrMode.LIST){

				super.putListValue(put, attr, (List<Object>)value);	
			}
        }
        return put;
	}

}
