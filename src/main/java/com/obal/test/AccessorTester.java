package com.obal.test;

import com.obal.admin.EntityAdmin;
import com.obal.admin.IAdminAccessor;
import com.obal.core.meta.AttrMode;
import com.obal.core.meta.AttrType;
import com.obal.core.meta.EntityAttr;
import com.obal.core.meta.EntityConstants;
import com.obal.core.meta.EntityManager;
import com.obal.core.meta.EntityMeta;
import com.obal.core.meta.GeneralEntity;
import com.obal.core.meta.accessor.IMetaAttrAccessor;
import com.obal.core.security.Principal;
import com.obal.core.util.AccessorUtils;
import com.obal.exception.AccessorException;
import com.obal.exception.EntityException;

public class AccessorTester extends BlankTester{

	public static void main(String[] args){
		initLog4j();
		
		AccessorTester self = new AccessorTester();
		self.createTestSchema();
		
	}
	
	private void createTestSchema(){
		
		EntityAdmin ea = EntityAdmin.getInstance();
		Principal princ = new Principal("useracc","demouser","pwd");
		
		IAdminAccessor aa = ea.getAdminAccessor(princ);
		IMetaAttrAccessor imeta = null;
		try {
			
			EntityMeta meta = new EntityMeta("obal.test");
			meta.setSchemaClass(GeneralEntity.class.getName());
			meta.setDescription("user schema descriptionxxx");
			
			EntityAttr attr = null;
			
			attr = new EntityAttr("i_int",AttrType.INT,"c0","int-val");
			meta.addAttr(attr);
			attr = new EntityAttr("i_double",AttrType.DOUBLE,"c1","double-val");
			meta.addAttr(attr);
			attr = new EntityAttr("i_long",AttrType.LONG,"c1","long-val");
			meta.addAttr(attr);
			attr = new EntityAttr("i_date",AttrType.DATE,"c2","date-val");
			meta.addAttr(attr);
			attr = new EntityAttr("i_string",AttrType.STRING,"c2","str-val");
			meta.addAttr(attr);
			attr = new EntityAttr("i_list_str",AttrMode.LIST,AttrType.STRING,"c3","list-str");
			meta.addAttr(attr);
			
			attr = new EntityAttr("i_list_int",AttrMode.LIST,AttrType.INT,"c3","list-int");
			meta.addAttr(attr);

			attr = new EntityAttr("i_list_dt",AttrMode.LIST,AttrType.DATE,"c3","list-dt");
			meta.addAttr(attr);
			
			attr = new EntityAttr("i_map_str",AttrMode.LIST,AttrType.STRING,"c4","map-str");
			meta.addAttr(attr);
			
			attr = new EntityAttr("i_map_int",AttrMode.LIST,AttrType.INT,"c4","map-int");
			meta.addAttr(attr);

			attr = new EntityAttr("i_map_dt",AttrMode.LIST,AttrType.DATE,"c4","map-dt");
			meta.addAttr(attr);
			
			aa.createSchema("obal.test",meta.getAllAttrs());
			
			imeta = AccessorUtils.getEntryAccessor(princ, EntityConstants.ENTITY_META_INFO);

			imeta.putEntityMeta(meta);
						
		} catch (AccessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{

			AccessorUtils.releaseAccessor(imeta,aa);
		}
	}
}
