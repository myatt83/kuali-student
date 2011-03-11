package org.kuali.db.jdbc;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationTest {

	@Test
	public void appTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext("jdbc-test-context.xml");
		Credentials c = (Credentials) context.getBean("org.kuali.jdbc.dba.credentials");
		Assert.assertNotNull(c);
	}
}
