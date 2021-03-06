package com.obal.admin;

import com.obal.core.security.Principal;
import com.obal.core.util.AccessorUtils;
import com.obal.core.util.EntityUtils;
import com.obal.exception.AccessorException;
import com.obal.meta.AttrMode;
import com.obal.meta.AttrType;
import com.obal.meta.EntityAttr;
import com.obal.meta.EntityConstants;
import com.obal.meta.EntityMeta;
import com.obal.meta.GeneralEntity;

public class EntitySetup {

	public EntitySetup() {

	}

	public void setup() {

		setupMetaSchema();
		setupUserSchema();
		setupGroupSchema();
		setupRoleSchema();
	}

	private void setupMetaSchema() {

		EntityAdmin ea = EntityAdmin.getInstance();
		Principal princ = new Principal("acc", "demo", "pwd");

		IAdminAccessor aa = ea.getAdminAccessor(princ);

		try {

			EntityMeta infoMeta = EntityUtils
					.getEntityMeta(EntityConstants.ENTITY_META_INFO);
			aa.createSchema(infoMeta.getEntityName(), infoMeta.getAllAttrs());

			EntityMeta arrtMeta = EntityUtils
					.getEntityMeta(EntityConstants.ENTITY_META_ATTR);
			aa.createSchema(arrtMeta.getEntityName(), arrtMeta.getAllAttrs());

		} catch (AccessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			AccessorUtils.releaseAccessor(aa);
		}
	}

	private void setupUserSchema() {

		EntityAdmin ea = EntityAdmin.getInstance();
		
		EntityMeta meta = new EntityMeta(EntityConstants.ENTITY_PRINCIPAL);
		meta.setSchemaClass(GeneralEntity.class.getName());
		meta.setDescription("user schema ");

		EntityAttr attr = new EntityAttr("i_account", "c0", "account");
		meta.addAttr(attr);
		attr = new EntityAttr("i_domain", "c0", "domain");
		meta.addAttr(attr);
		attr = new EntityAttr("i_name", "c0", "name");
		meta.addAttr(attr);
		attr = new EntityAttr("i_source", "c0", "source");
		meta.addAttr(attr);
		attr = new EntityAttr("i_password", "c0", "password");
		meta.addAttr(attr);
		attr = new EntityAttr("i_creator", "c0", "creator");
		meta.addAttr(attr);
		attr = new EntityAttr("i_modifier", "c0", "modifier");
		meta.addAttr(attr);
		attr = new EntityAttr("i_newcreate", AttrType.DATE, "c0", "newcreate");
		meta.addAttr(attr);
		attr = new EntityAttr("i_lastmodify", AttrType.DATE, "c0", "lastmodify");
		meta.addAttr(attr);
		attr = new EntityAttr("i_group", AttrMode.MAP, AttrType.STRING, "c1",
				"group");
		meta.addAttr(attr);
		attr = new EntityAttr("i_role", AttrMode.MAP, AttrType.STRING, "c1",
				"role");
		meta.addAttr(attr);
		ea.setupSchema(meta);

	}

	private void setupGroupSchema() {

		EntityAdmin ea = EntityAdmin.getInstance();
		
		EntityMeta meta = new EntityMeta(EntityConstants.ENTITY_USER_GROUP);
		meta.setSchemaClass(GeneralEntity.class.getName());
		meta.setDescription("user Group schema ");

		EntityAttr attr = new EntityAttr("i_group_name", "c0", "groupname");
		meta.addAttr(attr);
		attr = new EntityAttr("i_users", AttrMode.MAP, AttrType.STRING, "c1",
				"u");
		meta.addAttr(attr);
		attr = new EntityAttr("i_groups", AttrMode.MAP, AttrType.STRING, "c1",
				"g");
		meta.addAttr(attr);
		attr = new EntityAttr("i_creator", "c0", "creator");
		meta.addAttr(attr);
		attr = new EntityAttr("i_modifier", "c0", "modifier");
		meta.addAttr(attr);
		attr = new EntityAttr("i_newcreate", AttrType.DATE, "c0", "newcreate");
		meta.addAttr(attr);
		attr = new EntityAttr("i_lastmodify", AttrType.DATE, "c0", "lastmodify");
		meta.addAttr(attr);
		
		ea.setupSchema(meta);
	}

	private void setupRoleSchema() {

		EntityAdmin ea = EntityAdmin.getInstance();
		
		EntityMeta meta = new EntityMeta(EntityConstants.ENTITY_USER_ROLE);
		meta.setSchemaClass(GeneralEntity.class.getName());
		meta.setDescription("user Group schema ");

		EntityAttr attr = new EntityAttr("i_role_name", "c0", "rolename");
		meta.addAttr(attr);
		attr = new EntityAttr("i_users", AttrMode.MAP, AttrType.STRING, "c1",
				"u");
		meta.addAttr(attr);
		attr = new EntityAttr("i_creator", "c0", "creator");
		meta.addAttr(attr);
		attr = new EntityAttr("i_modifier", "c0", "modifier");
		meta.addAttr(attr);
		attr = new EntityAttr("i_newcreate", AttrType.DATE, "c0", "newcreate");
		meta.addAttr(attr);
		attr = new EntityAttr("i_lastmodify", AttrType.DATE, "c0", "lastmodify");
		meta.addAttr(attr);

		ea.setupSchema(meta);
	}

}
