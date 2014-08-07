package org.kuali.student.cm.course.auth;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentAuthorizerBase;
import org.kuali.student.kim.permission.map.KimPermissionConstants;

public class CMMaintenanceDocumentAuthorizer extends MaintenanceDocumentAuthorizerBase {

    @Override
    public boolean canOpen(Document document, Person user) {
        return isAuthorizedByTemplate(document, KimPermissionConstants.KS_SYS_NAMESPACE,
                KimPermissionConstants.KS_OPEN_DOCUMENT_BY_DOC_TYPE_AND_ROUTE_STATUS, user.getPrincipalId());
    }

    @Override
    public boolean canCancel(Document document, Person user) {
        // TODO KSCM-1722 -- current default permission is invalid due to workflow configuration using invalid org ids
        return false;
    }

}
