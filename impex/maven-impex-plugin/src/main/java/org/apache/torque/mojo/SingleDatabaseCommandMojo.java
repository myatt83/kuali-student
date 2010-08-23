package org.apache.torque.mojo;

import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.kuali.db.DatabaseCommand;
import org.kuali.db.SQLGenerator;
import org.kuali.db.Transaction;

/**
 * Runs a command that performs a single operation on a database (create,drop etc)
 */
public abstract class SingleDatabaseCommandMojo extends AbstractDatabaseCommandMojo {

	public abstract DatabaseCommand getCommand();

	protected void updateConfiguration() throws MojoExecutionException {
		super.updateConfiguration();
		getLog().info("-------------------------------------------");
		getLog().info(getCommand() + " database " + getDatabase());
		getLog().info("-------------------------------------------");
	}

	@Override
	protected Properties getContextProperties() {
		Properties properties = super.getContextProperties();
		properties.setProperty(DATABASE_PROPERTY, getDatabase());
		properties.setProperty(DATABASE_PW_PROPERTY, getDatabasePassword());
		properties.setProperty(DATABASE_USERNAME_PROPERTY, getDatabaseUsername());
		return properties;
	}

	@Override
	protected void configureTransactions() throws MojoExecutionException {
		Properties properties = getContextProperties();
		SQLGenerator generator = new SQLGenerator(properties, url, getCommand());
		try {
			generator.setEncoding(getEncoding());
			String sql = generator.getSQL();
			Transaction t = new Transaction();
			t.addText(sql);
			transactions.add(t);
		} catch (IOException e) {
			throw new MojoExecutionException("Error generating SQL", e);
		}
	}
}
