package org.kuali.student.sonar.database.exception;

import org.kuali.student.sonar.database.plugin.ForeignKeyConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 5/16/13
 * Time: 11:39 PM
 *
 * Usually indicates that the foreign table does not exists.
 * It may also indicate that the local table is not a table but a view
 */
public class TableMappingException extends FKConstraintException {

    public TableMappingException(ForeignKeyConstraint fkConstraint) {
        super("Improperly mapped table (does not exist) " + fkConstraint.foreignTable + "\n" +
                "see project file referenced by mvn properties kuali.student.sonar.fkvalidation.query.path " +
                "kuali.student.sonar.fkvalidation.query.filename\n" +
                "Constraint: " + fkConstraint.toString());
        this.fkConstraint = fkConstraint;
    }
}
