package org.kuali.student.deploy.spring;

import java.util.List;

import org.kuali.common.util.config.spring.BuildPropertySourceConfig;
import org.kuali.student.deploy.config.SourceDbConstants;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DumpSourceDbPropertySourceConfig extends BuildPropertySourceConfig {

	@Override
	protected List<String> getConfigIds() {
		return SourceDbConstants.DUMP_SOURCE_DB_CONFIG_IDS;
	}

}
