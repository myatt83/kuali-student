/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.sonar.database.utility;

import java.io.IOException;
import javax.xml.bind.JAXBException;

import org.kuali.common.impex.ProducerUtils;
import org.kuali.common.impex.model.Schema;
import org.kuali.common.impex.model.compare.SchemaCompare;
import org.kuali.common.impex.model.compare.SchemaCompareResult;

public class SchemaEqualityValidator {

    public static final String KS_SCHEMA_PATH = "ks-deployments/ks-dbs/ks-impex/ks-impex-app-db/src/main/resources/";

    public static final String KS_SCHEMA_FILENAME = "ks-impex-app-db-schema.xml";
    public static final String KS_CONSTRAINTS_FILENAME = "ks-impex-app-db-constraints-schema.xml";


    public static final String DDL_SCHEMA_PATH = "ks-deployments/ks-dbs/ks-ddl/src/main/resources/org/kuali/student/db/ks-ddl/";
    public static final String DDL_SCHEMA_FILENAME = "schema.xml";
    public static final String DDL_CONSTRAINTS_FILENAME = "constraints.xml";

    public static final String KS_SCHEMA_LABEL = "KSAPP";
    public static final String DDL_SCHEMA_LABEL = "DDL";

    public SchemaCompareResult compareSchemas(SchemaEqualityValidationContext context) throws JAXBException, IOException {
        Schema ksSchema = ProducerUtils.unmarshalSchema(context.getAppPath() + context.getAppSchemaFilename());
        Schema ddlSchema = ProducerUtils.unmarshalSchema(context.getDdlPath() + context.getDdlSchemaFilename());

        ksSchema.setName(context.getAppSchemaName());
        ddlSchema.setName(context.getDdlSchemaName());

        return new SchemaCompare(ksSchema, ddlSchema).compare();
    }

    public SchemaCompareResult compareConstraints(SchemaEqualityValidationContext context) throws JAXBException, IOException {
        Schema ksConstraints = ProducerUtils.unmarshalSchema(context.getAppPath() + context.getAppConstraintsFilename());
        Schema ddlConstraints = ProducerUtils.unmarshalSchema(context.getDdlPath() + context.getDdlConstraintsFilename());

        ksConstraints.setName(context.getAppSchemaName());
        ddlConstraints.setName(context.getDdlSchemaName());

        return new SchemaCompare(ksConstraints, ddlConstraints).compare();
    }
}
