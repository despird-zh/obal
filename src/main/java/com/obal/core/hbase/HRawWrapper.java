package com.obal.core.hbase;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.accessor.RawEntry;
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
	public RawEntry wrap(String entityName,Result rawEntry) {
						
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
			NavigableMap<byte[], byte[]> cells = null;
			switch(attr.mode){
			
				case PRIMITIVE :
					byte[] cell = entry.getValue(column, qualifier);
					Object value = super.getPrimitiveValue(attr, cell);
					gei.put(attr.getAttrName(), value);	
					break;
				case MAP :
					cells = entry.getFamilyMap(column);
					Map<String, Object> map = super.getMapValue(attr, cells);				
					gei.put(attr.getAttrName(), map);
					break;
				case LIST :
					cells = entry.getFamilyMap(column);
					List<Object> list = super.getListValue(attr, cells);					
					gei.put(attr.getAttrName(), list);
					break;
					
				case SET :
					cells = entry.getFamilyMap(column);
					Set<Object> set = super.getSetValue(attr, cells);					
					gei.put(attr.getAttrName(), set);
					break;
					
				default:
					break;
				
			}
		}
		
		return gei;
	}
	
	@Override
	public RawEntry wrap(List<EntityAttr> attrs,Result rawEntry) {
						
		Result entry = (Result)rawEntry;
		String entityName = attrs.size()>0? (attrs.get(0).getEntityName()):EntityConstants.ENTITY_BLIND;
		if(entityName == null || entityName.length()==0){
			
			entityName = EntityConstants.ENTITY_BLIND;
		}
		RawEntry gei = new RawEntry(entityName,new String(entry.getRow()));
		
		for(EntityAttr attr: attrs){
			byte[] column = attr.getColumn().getBytes();
			byte[] qualifier = attr.getQualifier().getBytes();
			NavigableMap<byte[], byte[]> cells = null;
			switch(attr.mode){
			
				case PRIMITIVE :
					byte[] cell = entry.getValue(column, qualifier);
					Object value = super.getPrimitiveValue(attr, cell);
					gei.put(attr.getAttrName(), value);	
					break;
				case MAP :
					cells = entry.getFamilyMap(column);
					Map<String, Object> map = super.getMapValue(attr, cells);				
					gei.put(attr.getAttrName(), map);
					break;
				case LIST :
					cells = entry.getFamilyMap(column);
					List<Object> list = super.getListValue(attr, cells);
					
					gei.put(attr.getAttrName(), list);
					break;
				case SET :
					cells = entry.getFamilyMap(column);
					Set<Object> set = super.getSetValue(attr, cells);
					
					gei.put(attr.getAttrName(), set);
					break;
				default:
					break;
				
			}
			
		}
		
		return gei;
	}

	@Override
	public Put parse(List<EntityAttr> attrs,RawEntry entryInfo) {
		Put put = new Put(entryInfo.getKeyBytes());

        for(EntityAttr attr:attrs){

        	Object value = entryInfo.get(attr.getAttrName());
        	if(LOGGER.isDebugEnabled()){
        		LOGGER.debug("--==>>attr:{} - value:{}",attr.getAttrName(),value);
        	}
        	if(null == value) continue;
        	
        	switch(attr.mode){
        	
        		case PRIMITIVE:
        			super.putPrimitiveValue(put, attr, value);					
        			break;
        		case MAP:
        			super.putMapValue(put, attr, (Map<String,Object>)value);	
        			break;
        		case LIST:
        			super.putListValue(put, attr, (List<Object>)value);	
        			
        			break;
        		case SET:
        			super.putSetValue(put, attr, (Set<Object>)value);				
        			break;
        		default:
        			break;
        	
        	}
        }
        return put;
	}

}
