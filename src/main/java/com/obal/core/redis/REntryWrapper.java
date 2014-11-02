package com.obal.core.redis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.obal.core.EntryInfo;
import com.obal.core.EntryWrapper;
import com.obal.core.meta.EntityAttr;

public abstract class REntryWrapper<GB extends EntryInfo> extends
		EntryWrapper<GB> {

	public static Logger LOGGER = LoggerFactory.getLogger(REntryWrapper.class);

	/**
	 * Get primitive value from cell, primitive means
	 * int,long,double,string,date
	 * 
	 * @param attr
	 *            the attribute of entry
	 * @param cell
	 *            the Cell of certain Row in hbase
	 * 
	 * @return Object the value object
	 **/
	public Object getPrimitiveValue(EntityAttr attr, String value) {

		Object rtv = null;

		switch (attr.type) {
		case INT:
			rtv = Integer.valueOf(value);
			break;
		case BOOL:
			rtv = Boolean.valueOf(value);
			break;
		case DOUBLE:
			rtv = Double.valueOf(value);
			break;
		case LONG:
			rtv = Long.valueOf(value);
			break;
		case STRING:
			rtv = value;
			break;
		case DATE:
			Long time = Long.valueOf(value);
			rtv = new Date(time);
			break;
		default:
			break;
		}
		if (LOGGER.isDebugEnabled()) {

			LOGGER.debug("PRIMITIVE -> attribute:{} | value:{}", new String[] {
					attr.getAttrName(), String.valueOf(rtv) });
		}
		return rtv;
	}

	/**
	 * Get map value from cells, every cell is the entry of map
	 * 
	 * @param attr
	 *            the attribute of entry
	 * @param cells
	 *            the Cells of certain Row in hbase
	 * 
	 * @return Object the map object
	 **/
	public Map<String, Object> getMapValue(EntityAttr attr,
			Map<String, String> cells) {

		Map<String, Object> map = new HashMap<String, Object>();

		for (Map.Entry<String, String> e : cells.entrySet()) {

			String key = e.getKey();
			String value = e.getValue();
			switch (attr.type) {
			case INT:
				map.put(key, Integer.valueOf(value));
				break;
			case BOOL:
				map.put(key, Boolean.valueOf(value));
				break;
			case DOUBLE:
				map.put(key, Double.valueOf(value));
				break;
			case LONG:
				map.put(key, Long.valueOf(value));
				break;
			case STRING:
				map.put(key, value);
				break;
			case DATE:
				Long time = Long.valueOf(value);
				map.put(key, new Date(time));
				break;
			default:

				break;
			}
			if (LOGGER.isDebugEnabled()) {

				LOGGER.debug("MAP -> attribute:{} - key:{} - value:{}",
						new String[] { attr.getAttrName(), new String(key),
								String.valueOf(map.get(new String(key))) });
			}
		}

		return map;
	}

	/**
	 * Get list value from cells, every cell is the entry of map
	 * 
	 * @param attr
	 *            the attribute of entry
	 * @param cells
	 *            the Cells of certain Row in hbase
	 * 
	 * @return Object the list object
	 **/
	public List<Object> getListValue(EntityAttr attr, List<String> cells) {

		List<Object> list = new ArrayList<Object>();

		for (String e : cells) {
			switch (attr.type) {
			case INT:
				list.add(Integer.valueOf(e));
				break;
			case BOOL:
				list.add(Boolean.valueOf(e));
				break;
			case DOUBLE:
				list.add(Double.valueOf(e));
				break;
			case LONG:
				list.add(Long.valueOf(e));
				break;
			case STRING:
				list.add(e);
				break;
			case DATE:
				Long time = Long.valueOf(e);
				list.add(new Date(time));
				break;
			default:

				break;
			}
			if (LOGGER.isDebugEnabled()) {

				LOGGER.debug("LIST -> attribute:{} - key:{} - value:{}",
						new String[] { attr.getAttrName(), new String(e),
								String.valueOf(list.get(list.size() - 1)) });
			}

		}

		return list;
	}

	/**
	 * Get Set value from cells, every cell is the element of set
	 * 
	 * @param attr
	 *            the attribute of entry
	 * @param cells
	 *            the Cells of certain Row in hbase
	 * 
	 * @return Object the list object
	 **/
	public Set<Object> getSetValue(EntityAttr attr, List<String> cells) {

		Set<Object> set = new HashSet<Object>();

		for (String e : cells) {

			switch (attr.type) {
			case INT:
				set.add(Integer.valueOf(e));
				break;
			case BOOL:
				set.add(Boolean.valueOf(e));
				break;
			case DOUBLE:
				set.add(Double.valueOf(e));
				break;
			case LONG:
				set.add(Long.valueOf(e));
				break;
			case STRING:
				set.add(e);
				break;
			case DATE:
				Long time = Long.valueOf(e);
				set.add(new Date(time));
				break;
			default:

				break;
			}

			if (LOGGER.isDebugEnabled()) {

				LOGGER.debug("LIST -> attribute:{} - key:{} - value:{}",
						new String[] { attr.getAttrName(), new String(e),
								set.toArray().toString() });
			}

		}

		return set;
	}

	/**
	 * Put the Primitive value to target Put operation
	 * 
	 * @param put
	 *            the Hbase Put operation object
	 * @param attr
	 *            the target attribute object
	 * @param value
	 *            the value to be put
	 * 
	 **/
	public void putPrimitiveValue(Put put, EntityAttr attr, Object value) {
		byte[] bval = null;
		if (value == null)
			return;
		switch (attr.type) {
		case INT:
			bval = Bytes.toBytes((Integer) value);
			break;
		case BOOL:
			bval = Bytes.toBytes((Boolean) value);
			break;
		case DOUBLE:
			bval = Bytes.toBytes((Double) value);
			break;
		case LONG:
			bval = Bytes.toBytes((Long) value);
			break;
		case STRING:
			bval = Bytes.toBytes((String) value);
			break;
		case DATE:
			bval = Bytes.toBytes(((Date) value).getTime());
			break;
		default:

			break;
		}
		put.add(attr.getColumn().getBytes(), attr.getQualifier().getBytes(),
				bval);
	}

	/**
	 * Put the map value to target Put operation object
	 * 
	 * @param put
	 *            the Hbase Put operation object
	 * @param attr
	 *            the target attribute object
	 * @param value
	 *            the value to be put
	 **/
	public void putMapValue(Put put, EntityAttr attr, Map<String, Object> mapVal) {
		byte[] bval = null;
		if (mapVal == null)
			return;
		for (Map.Entry<String, Object> pe : mapVal.entrySet()) {
			switch (attr.type) {
			case INT:
				bval = Bytes.toBytes((Integer) pe.getValue());
				break;
			case BOOL:
				bval = Bytes.toBytes((Boolean) pe.getValue());
				break;
			case DOUBLE:
				bval = Bytes.toBytes((Double) pe.getValue());
				break;
			case LONG:
				bval = Bytes.toBytes((Long) pe.getValue());
				break;
			case STRING:
				bval = Bytes.toBytes((String) pe.getValue());
				break;
			case DATE:
				bval = Bytes.toBytes(((Date) pe.getValue()).getTime());
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
	 * @param put
	 *            the Hbase Put operation object
	 * @param attr
	 *            the target attribute object
	 * @param value
	 *            the value to be put
	 **/
	public void putListValue(Put put, EntityAttr attr, List<Object> listVal) {
		byte[] bval = null;
		if (listVal == null)
			return;
		for (int i = 0; i < listVal.size(); i++) {

			switch (attr.type) {
			case INT:
				bval = Bytes.toBytes((Integer) listVal.get(i));
				break;
			case BOOL:
				bval = Bytes.toBytes((Boolean) listVal.get(i));
				break;
			case DOUBLE:
				bval = Bytes.toBytes((Double) listVal.get(i));
				break;
			case LONG:
				bval = Bytes.toBytes((Long) listVal.get(i));
				break;
			case STRING:
				bval = Bytes.toBytes((String) listVal.get(i));
				break;
			case DATE:
				bval = Bytes.toBytes(((Date) listVal.get(i)).getTime());
				break;
			default:

				break;
			}
			String newQualifier = attr.getQualifier() + i;
			put.add(attr.getColumn().getBytes(), newQualifier.getBytes(), bval);
		}

	}

	/**
	 * Put the set value to target Put operation object
	 * 
	 * @param put
	 *            the Hbase Put operation object
	 * @param attr
	 *            the target attribute object
	 * @param value
	 *            the value to be put
	 **/
	public void putSetValue(Put put, EntityAttr attr, Set<Object> setVal) {
		byte[] bval = null;
		if (setVal == null)
			return;
		Iterator<Object> iterator = setVal.iterator();
		int i = 0;
		while (iterator.hasNext()) {

			Object val = iterator.next();
			switch (attr.type) {
			case INT:
				bval = Bytes.toBytes((Integer) val);
				break;
			case BOOL:
				bval = Bytes.toBytes((Boolean) val);
				break;
			case DOUBLE:
				bval = Bytes.toBytes((Double) val);
				break;
			case LONG:
				bval = Bytes.toBytes((Long) val);
				break;
			case STRING:
				bval = Bytes.toBytes((String) val);
				break;
			case DATE:
				bval = Bytes.toBytes(((Date) val).getTime());
				break;
			default:

				break;
			}

			String newQualifier = attr.getQualifier() + i;
			put.add(attr.getColumn().getBytes(), newQualifier.getBytes(), bval);
			i++;
		}

	}

	@Override
	public boolean supportWrap(Class<?> clazz) {

		return clazz == Jedis.class;
	}

	@Override
	public boolean supportParse(Class<?> clazz) {

		return clazz == Jedis.class;
	}
}
