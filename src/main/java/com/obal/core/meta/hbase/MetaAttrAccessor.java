package com.obal.core.meta.hbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;

import com.obal.core.AccessorFactory;
import com.obal.core.EntryFilter;
import com.obal.core.EntryKey;
import com.obal.core.accessor.RawEntry;
import com.obal.core.hbase.HGeneralAccessor;
import com.obal.core.meta.AttrMode;
import com.obal.core.meta.AttrType;
import com.obal.core.meta.EntityAttr;
import com.obal.core.meta.EntityManager;
import com.obal.core.meta.EntityMeta;
import com.obal.core.meta.accessor.IMetaAttrAccessor;
import com.obal.exception.AccessorException;
import com.obal.exception.BaseException;
import com.obal.exception.EntityException;
import com.obal.exception.MetaException;

public class MetaAttrAccessor extends HGeneralAccessor implements IMetaAttrAccessor{

	@Override
	public EntityAttr getAttr(String attrKey) throws AccessorException {
		
		AttrAccessor attraccessor = null;
		EntityAttr attr = null;
		try{
			attraccessor = AccessorFactory.getInstance().buildEmbedEntityAccessor(this, EntityManager.ENTITY_META_ATTR);
			RawEntry minfo = attraccessor.getEntry(attrKey);
			String attrName = (String)minfo.get("i_attr_name");
			String column = (String)minfo.get("i_column");
			String qualifier = (String)minfo.get("i_qualifier");
			
			AttrType type = AttrType.valueOf((String)minfo.get("i_type"));
			AttrMode mode = AttrMode.valueOf((String)minfo.get("i_mode"));
			
			attr = new EntityAttr(attrName,mode,type,column,qualifier);
			attr.setEntryName((String)minfo.get("i_entry"));
			attr.setDescription((String)minfo.get("i_description"));
			attr.setFormat((String)minfo.get("i_format"));
			attr.setHidden((Boolean)minfo.get("i_hidden"));
			attr.setPrimary((Boolean)minfo.get("i_primary"));
			attr.setRequired((Boolean)minfo.get("i_required"));
			attr.setReadonly((Boolean)minfo.get("i_readonly"));
			
		}catch(EntityException ee){
			
			throw new AccessorException("Error when build embed accessor:{}",ee,EntityManager.ENTITY_META_ATTR);
		}finally{
			
			attraccessor.release();
		}
		return attr;

	}

	@Override
	public List<EntityAttr> getAttrList(String entryName) throws AccessorException {
		
		Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL,
				new BinaryComparator(entryName.getBytes()));
		AttrAccessor attraccessor = null;
		List<RawEntry> attrs = null;
		List<EntityAttr> rtv = null;
		try{
			attraccessor = AccessorFactory.getInstance().buildEmbedEntityAccessor(this, EntityManager.ENTITY_META_ATTR);
		
			attrs = attraccessor.scanEntry(new EntryFilter<Filter>(filter1));
			
			rtv = new ArrayList<EntityAttr>();
			for(RawEntry minfo:attrs){
	
				String attrName = (String)minfo.get("i_attr_name");
				String column = (String)minfo.get("i_column");
				String qualifier = (String)minfo.get("i_qualifier");
				
				AttrType type = AttrType.valueOf((String)minfo.get("i_type"));
				AttrMode mode = AttrMode.valueOf((String)minfo.get("i_mode"));
				
				EntityAttr attr = new EntityAttr(attrName,mode,type,column,qualifier);
				attr.setEntryName((String)minfo.get("i_entry"));
				attr.setDescription((String)minfo.get("i_description"));
				attr.setFormat((String)minfo.get("i_format"));
				attr.setHidden((Boolean)minfo.get("i_hidden"));
				attr.setPrimary((Boolean)minfo.get("i_primary"));
				attr.setRequired((Boolean)minfo.get("i_required"));
				attr.setReadonly((Boolean)minfo.get("i_readonly"));
				
				rtv.add(attr);
			}
		}catch(EntityException ee){
			
		}finally{
			
			attraccessor.release();
		}
		return rtv;
	}

	@Override
	public EntryKey putEntryAttr(EntityAttr attr) throws AccessorException {
		AttrAccessor attraccessor = null;
		
		try {
			attraccessor = AccessorFactory.getInstance().buildEmbedEntityAccessor(this, EntityManager.ENTITY_META_ATTR);
			EntryKey key = attraccessor.getEntitySchema().newKey();
			RawEntry minfo = new RawEntry(key);

			minfo.put("i_attr_name", attr.getAttrName());
			minfo.put("i_description", attr.getDescription());
			minfo.put("i_format", attr.getFormat());
			minfo.put("i_column", attr.getColumn());
			minfo.put("i_qualifier", attr.getQualifier());
			minfo.put("i_hidden", attr.isHidden());
			minfo.put("i_primary", attr.isPrimary());
			minfo.put("i_required", attr.isRequired());
			minfo.put("i_readonly", attr.isReadonly());
			minfo.put("i_type", attr.type.toString());
			minfo.put("i_mode", attr.mode.toString());
			minfo.put("i_entry", attr.getEntryName());
			
			minfo.put("i_creator",attraccessor.getEntitySchema().getPrincipal().getName());
			minfo.put("i_modifier",attraccessor.getEntitySchema().getPrincipal().getName());
			minfo.put("i_newcreate", new Date());
			minfo.put("i_lastmodify", new Date());
						
			return attraccessor.putEntry(minfo);
			
		} catch (EntityException e) {
			
			throw new AccessorException("Error when put meta attr data.",e);
		} catch (MetaException e) {
			
			throw new AccessorException("Error when create meta attr key.",e);
		}finally{
			
			attraccessor.release();
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EntityMeta getEntryMeta(String entityName) throws AccessorException {

		MetaAccessor metaAccr = null;
		EntityMeta meta = null;
		try{
			metaAccr = AccessorFactory.getInstance().buildEmbedEntityAccessor(this, EntityManager.ENTITY_META_INFO);
		
			RawEntry minfo = metaAccr.getEntry(entityName);
			meta = new EntityMeta(entityName);
			meta.setSchemaClass((String)minfo.get("i_schema_class"));
			meta.setDescription((String)minfo.get("i_description"));
			meta.setEntityName((String)minfo.get("i_entity_name"));
			meta.setSchemas((List<String>)minfo.get("i_schemas"));	
			
		}catch (EntityException ee){
			
			throw new AccessorException("Error when get meta info data.",ee);
		}
		return meta;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntityMeta> getEntryMetaList() throws AccessorException {
		MetaAccessor metaAccr = null;
		List<RawEntry> rlist = null;
		List<EntityMeta> rtv = null;
		try{
			
			metaAccr = AccessorFactory.getInstance().buildEmbedEntityAccessor(this, EntityManager.ENTITY_META_INFO);
		rlist = metaAccr.scanEntry(null);
		rtv = new ArrayList<EntityMeta>();
		for(RawEntry ri:rlist){
			
			EntityMeta meta = new EntityMeta(metaAccr.getEntitySchema().getEntityName());
			meta.setSchemaClass((String)ri.get("i_schema_class"));
			meta.setEntityName((String)ri.get("i_entity_name"));
			meta.setDescription((String)ri.get("i_description"));
			meta.setSchemas((List<String>)ri.get("i_schemas"));	
			// set entry attributes
			rtv.add(meta);
		}
		}catch(BaseException be){
			
			throw new AccessorException("Error when get meta info data.",be);
			
		}finally{
			
			metaAccr.release();
		}
		return rtv;
	}

	@Override
	public EntryKey putEntryMeta(EntityMeta meta) throws AccessorException {
		
		MetaAccessor metaAccr = null;
		try {
			metaAccr = AccessorFactory.getInstance().buildEmbedEntityAccessor(this, EntityManager.ENTITY_META_INFO);
			EntryKey key = metaAccr.getEntitySchema().newKey();
			RawEntry minfo = new RawEntry(key);

			minfo.put("i_entity_name", meta.getEntityName());
			minfo.put("i_schema_class", meta.getSchemaClass());
			minfo.put("i_description", meta.getDescription());
			minfo.put("i_creator",metaAccr.getEntitySchema().getPrincipal().getName());
			minfo.put("i_modifier",metaAccr.getEntitySchema().getPrincipal().getName());
			minfo.put("i_newcreate", new Date());
			minfo.put("i_lastmodify", new Date());
			minfo.put("i_schemas", meta.getSchemas());
						
			EntryKey mkey = metaAccr.putEntry(minfo);
			
			Map<String,String> attrmap = new HashMap<String,String>();
			
			for(EntityAttr tattr:meta.getAllAttrs()){
				
				tattr.setEntryName(meta.getEntityName());
				
				EntryKey akey = putEntryAttr(tattr);
				attrmap.put(tattr.getAttrName(),akey.getKey());
			}
			
			if(!attrmap.isEmpty())
				metaAccr.putEntryAttr(mkey.getKey(), "i_attributes", attrmap);
			
			return mkey;
			
		} catch (BaseException e) {
			
			throw new AccessorException("Error when put metadata.",e);
		}finally{
			
			metaAccr.release();
		}

	}

}
