package com.obal.core.hbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.obal.core.EntryInfo;
import com.obal.core.EntryWrapper;
import com.obal.core.meta.EntityAttr;

public abstract class HEntryWrapper<GB extends EntryInfo> extends EntryWrapper<GB> {

	public static Logger LOGGER = LoggerFactory.getLogger(HEntryWrapper.class);
	/**
	 * Get primitive value from cell, primitive means int,long,double,string,date
	 * 
	 * @param attr the attribute of entry
	 * @param cell the Cell of certain Row in hbase
	 * 
	 * @return Object the value object
	 **/
	public Object getPrimitiveValue(EntityAttr attr, Cell cell){
		
		Object rtv = null;
		byte[] bytes = cell.getValueArray();
		switch(attr.type){
			case INT:
				rtv = Bytes.toInt(bytes);
				break;
			case BOOL:
				rtv = Bytes.toBoolean(bytes);
				break;
			case DOUBLE:
				rtv = Bytes.toDouble(bytes);
				break;
			case LONG:
				rtv = Bytes.toLong(bytes);
				break;
			case STRING:
				rtv = Bytes.toString(bytes);
				break;
			case DATE:
				Long time = Bytes.toLong(bytes);
				rtv = new Date(time);
				break;
			default:
				break;
		}
		if(LOGGER.isDebugEnabled()){
			
			LOGGER.debug("PRIMITIVE -> attribute:{} | value:{}", new String[]{attr.getAttrName(),String.valueOf(rtv)});
		}
		return rtv;
	}

	/**
	 * Get map value from cells, every cell is the entry of map
	 * 
	 * @param attr the attribute of entry
	 * @param cells the Cells of certain Row in hbase
	 * 
	 * @return Object the map object
	 **/
	public Map<String,Object> getMapValue(EntityAttr attr, NavigableMap<byte[], byte[]> cells){

		byte[] qualifier = attr.getQualifier().getBytes();
		Map<String, Object> map = new HashMap<String, Object>();

		for(Map.Entry<byte[], byte[]> e:cells.entrySet()){

			byte[] cqualifier = e.getKey();
			if(Bytes.startsWith(cqualifier, qualifier)){
				byte[] key = Bytes.tail(cqualifier, cqualifier.length - qualifier.length);
				byte[] bytes = e.getValue();
				switch(attr.type){
					case INT:
						map.put(new String(key), Bytes.toInt(bytes));
						break;
					case BOOL:
						map.put(new String(key), Bytes.toBoolean(bytes));
						break;
					case DOUBLE:
						map.put(new String(key), Bytes.toDouble(bytes));
						break;
					case LONG:
						map.put(new String(key), Bytes.toLong(bytes));
						break;
					case STRING:
						map.put(new String(key), Bytes.toString(bytes));
						break;
					case DATE:
						Long time = Bytes.toLong(bytes);
						map.put(new String(key), new Date(time));
						break;
					default:
						
						break;
				}
				if(LOGGER.isDebugEnabled()){
					
					LOGGER.debug("MAP -> attribute:{} - key:{} - value:{}", 
							new String[]{attr.getAttrName(),new String(key),String.valueOf(map.get(new String(key)))});
				}
			}
		}				
				
		return map;
	}

	/**
	 * Get list value from cells, every cell is the entry of map
	 * 
	 * @param attr the attribute of entry
	 * @param cells the Cells of certain Row in hbase
	 * 
	 * @return Object the list object
	 **/
	public List<Object> getListValue(EntityAttr attr, NavigableMap<byte[], byte[]> cells){

		byte[] qualifier = attr.getQualifier().getBytes();
		List<Object> list = new ArrayList<Object>();

		for(byte[] e:cells.descendingKeySet()){
			
			if(Bytes.startsWith(e, qualifier)){
				byte[] bytes = cells.get(e);
				switch(attr.type){
					case INT:
						list.add(Bytes.toInt(bytes));
						break;
					case BOOL:
						list.add(Bytes.toBoolean(bytes));
						break;
					case DOUBLE:
						list.add(Bytes.toDouble(bytes));
						break;
					case LONG:
						list.add(Bytes.toLong(bytes));
						break;
					case STRING:
						list.add(Bytes.toString(bytes));
						break;
					case DATE:
						Long time = Bytes.toLong(bytes);
						list.add(new Date(time));
						break;
					default:
						
						break;
				}
				if(LOGGER.isDebugEnabled()){
					
					LOGGER.debug("LIST -> attribute:{} - key:{} - value:{}", 
							new String[]{attr.getAttrName(),new String(e),String.valueOf(list.get(list.size()-1))});
				}
			}
		}				
				
		return list;
	}
	
	/**
	 * Put the Primitive value to target Put operation
	 * 
	 * @param put the Hbase Put operation object
	 * @param attr the target attribute object
	 * @param value the value to be put 
	 * 
	 **/
	public void putPrimitiveValue(Put put, EntityAttr attr, Object value){
		byte[] bval = null;
    	if(value == null) return;    	
    	switch(attr.type){
			case INT:
				bval = Bytes.toBytes((Integer)value);
				break;
			case BOOL:
				bval = Bytes.toBytes((Boolean)value);
				break;
			case DOUBLE:
				bval = Bytes.toBytes((Double)value);
				break;
			case LONG:
				bval = Bytes.toBytes((Long)value);
				break;							
			case STRING:
				bval = Bytes.toBytes((String)value);
				break;
			case DATE:
				bval = Bytes.toBytes(((Date)value).getTime());
				break;						
			default:
				
				break;					
		}
    	put.add(attr.getColumn().getBytes(), attr.getQualifier().getBytes(), bval);
	}
	
	/**
	 * Put the map value to target Put operation object
	 * 
	 * @param put the Hbase Put operation object
	 * @param attr the target attribute object
	 * @param value the value to be put 
	 **/
	public void putMapValue(Put put, EntityAttr attr, Map<String,Object> mapVal){
		byte[] bval = null;
    	if(mapVal == null) return;    	
    	for(Map.Entry<String,Object> pe:mapVal.entrySet()){
	    	switch(attr.type){
				case INT:
					bval = Bytes.toBytes((Integer)pe.getValue());
					break;
				case BOOL:
					bval = Bytes.toBytes((Boolean)pe.getValue());
					break;
				case DOUBLE:
					bval = Bytes.toBytes((Double)pe.getValue());
					break;
				case LONG:
					bval = Bytes.toBytes((Long)pe.getValue());
					break;							
				case STRING:
					bval = Bytes.toBytes((String)pe.getValue());
					break;
				case DATE:
					bval = Bytes.toBytes(((Date)pe.getValue()).getTime());
					break;						
				default:
					
					break;					
			}
	    	String newQualifier = attr.getQualifier() + pe.getKey();
	    	put.add(attr.getColumn().getBytes(), newQualifier.getBytes(), bval);
    	}
    	
	}

	/**
	 * Put the list value to target Put operation object
	 * 
	 * @param put the Hbase Put operation object
	 * @param attr the target attribute object
	 * @param value the value to be put 
	 **/
	public void putListValue(Put put, EntityAttr attr, List<Object> listVal){
		byte[] bval = null;
    	if(listVal == null) return;    	
    	for(int i=0;i<listVal.size();i++){
    		
	    	switch(attr.type){
				case INT:
					bval = Bytes.toBytes((Integer)listVal.get(i));
					break;
				case BOOL:
					bval = Bytes.toBytes((Boolean)listVal.get(i));
					break;
				case DOUBLE:
					bval = Bytes.toBytes((Double)listVal.get(i));
					break;
				case LONG:
					bval = Bytes.toBytes((Long)listVal.get(i));
					break;							
				case STRING:
					bval = Bytes.toBytes((String)listVal.get(i));
					break;
				case DATE:
					bval = Bytes.toBytes(((Date)listVal.get(i)).getTime());
					break;						
				default:
					
					break;					
			}
	    	String newQualifier = attr.getQualifier() + i;
	    	put.add(attr.getColumn().getBytes(), newQualifier.getBytes(), bval);
    	}
    	
	}
	
}
