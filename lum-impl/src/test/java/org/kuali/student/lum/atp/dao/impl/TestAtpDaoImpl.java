package org.kuali.student.lum.atp.dao.impl;

import org.junit.Test;
import org.kuali.student.lum.atp.dao.AtpDao;
import org.kuali.student.common.test.spring.AbstractTransactionalDaoTest;
import org.kuali.student.common.test.spring.Dao;
import org.kuali.student.common.test.spring.PersistenceFileLocation;

@PersistenceFileLocation("classpath:META-INF/atp-persistence.xml")
public class TestAtpDaoImpl extends AbstractTransactionalDaoTest {
	@Dao(value = "org.kuali.student.lum.atp.dao.impl.AtpDaoImpl")
	public AtpDao dao;

	@Test
	public void testCreateType() {
//		Type type = new Type();
//		type.setKey("types.foo.type1");
//		type.setDesc("Description type 1");
//		type.setName("Name for type 1");
//		type.setEffectiveDate(new Date());
//		type.setExpirationDate(new Date());
//		TypeAttribute attr = new TypeAttribute();
//		attr.setId("12345");
//		AttributeType  attrType = new AttributeType();
//		attrType.setId("1234567");
//		attrType.setName("attr1");
//		attr.setType(attrType);
//		attr.setValue("value1");
//		type.getAttributes().add(attr);

//		dao.createType(type);
	}
}
