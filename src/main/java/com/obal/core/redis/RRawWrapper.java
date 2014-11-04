/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package com.obal.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

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
public class RRawWrapper extends REntryWrapper<RawEntry> {

	public static Logger LOGGER = LoggerFactory.getLogger(RRawWrapper.class);

	@Override
	public RawEntry wrap(String entityName, String key, Jedis rawEntry) {

		Jedis entry = rawEntry;
		// not exist return null;
		if(!entry.exists(key))
			return null;		
		EntityMeta meta = EntityManager.getInstance().getEntityMeta(entityName);
				
		List<EntityAttr> attrs = meta.getAllAttrs();

		RawEntry gei = new RawEntry(entityName, key);

		for (EntityAttr attr : attrs) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Wrapping entity:{} - attribute:{}", entityName,
						attr.getAttrName());
			}
			Map<byte[], byte[]> cells = null;
			
			switch (attr.mode) {

			case PRIMITIVE:
				byte[] cell = entry.hget(key.getBytes(), attr.getAttrName()
						.getBytes());
				Object value = super.getPrimitiveValue(attr, cell);
				gei.put(attr.getAttrName(), value);
				break;
			case MAP:
				String mapkey = key + ":" + attr.getAttrName();
				cells = entry.hgetAll(mapkey.getBytes());
				Map<String, Object> map = super.getMapValue(attr, cells);
				gei.put(attr.getAttrName(), map);
				break;
			case LIST:
				String listkey = key + ":" + attr.getAttrName();
				Long llen = entry.llen(listkey.getBytes());
				List<byte[]> listcells = entry.lrange(listkey.getBytes(), 0,
						llen);
				List<Object> list = super.getListValue(attr, listcells);
				gei.put(attr.getAttrName(), list);
				break;

			case SET:
				String setkey = key + ":" + attr.getAttrName();
				Set<byte[]> setcells = entry.smembers(setkey.getBytes());

				Set<Object> set = super.getSetValue(attr, setcells);
				gei.put(attr.getAttrName(), set);
				break;

			default:
				break;

			}
		}

		return gei;
	}

	@Override
	public RawEntry wrap(List<EntityAttr> attrs, String key, Jedis rawEntry) {

		Jedis entry = rawEntry;
		String entityName = attrs.size() > 0 ? (attrs.get(0).getEntityName())
				: EntityConstants.ENTITY_BLIND;
		if (entityName == null || entityName.length() == 0) {

			entityName = EntityConstants.ENTITY_BLIND;
		}
		RawEntry gei = new RawEntry(entityName, key);

		for (EntityAttr attr : attrs) {

			Map<byte[], byte[]> cells = null;
			switch (attr.mode) {

				case PRIMITIVE:
					byte[] cell = entry.hget(key.getBytes(), attr.getAttrName()
							.getBytes());
					Object value = super.getPrimitiveValue(attr, cell);
					gei.put(attr.getAttrName(), value);
					break;
				case MAP:
					String mapkey = key + ":" + attr.getAttrName();
					cells = entry.hgetAll(mapkey.getBytes());
					Map<String, Object> map = super.getMapValue(attr, cells);
					gei.put(attr.getAttrName(), map);
					break;
				case LIST:
					String listkey = key + ":" + attr.getAttrName();
					List<byte[]> listcells = entry.lrange(listkey.getBytes(), 0,
							10000);
					List<Object> list = super.getListValue(attr, listcells);
					gei.put(attr.getAttrName(), list);
					break;
	
				case SET:
					String setkey = key + ":" + attr.getAttrName();
					Set<byte[]> setcells = entry.smembers(setkey.getBytes());
	
					Set<Object> set = super.getSetValue(attr, setcells);
					gei.put(attr.getAttrName(), set);
					break;
				default:
					break;

			}

		}

		return gei;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parse(List<EntityAttr> attrs,Jedis jedis, RawEntry entryInfo) {

		for (EntityAttr attr : attrs) {

			Object value = entryInfo.get(attr.getAttrName());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("--==>>attr:{} - value:{}", attr.getAttrName(),
						value);
			}
			if (null == value)
				continue;

			switch (attr.mode) {

			case PRIMITIVE:
				super.putPrimitiveValue(jedis,entryInfo.getKey(), attr, value);
				break;
			case MAP:
				super.putMapValue(jedis, entryInfo.getKey(), attr, (Map<String, Object>) value);
				break;
			case LIST:
				super.putListValue(jedis, entryInfo.getKey(), attr, (List<Object>) value);
				break;
			case SET:
				super.putSetValue(jedis, entryInfo.getKey(), attr, (Set<Object>) value);
				break;
			default:
				break;

			}
		}
	}

}
