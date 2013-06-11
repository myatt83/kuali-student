package com.sigmasys.kuali.ksa.krad.util;

import com.sigmasys.kuali.ksa.krad.model.TransactionTypeModel;
import com.sigmasys.kuali.ksa.model.TransactionType;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class AuditTooltipUtil {

    public static String getAuditTooltip(TransactionTypeModel ttModel) {
        return getAuditTooltip(ttModel.getTransactionType());
    }

    public static String getAuditTooltip(TransactionType tt) {
        return getAuditTooltip(tt.getCreatorId(), tt.getCreationDate(), tt.getEditorId(), tt.getLastUpdate(), tt.getStartDate(), tt.getEndDate(), null);
    }

    private static String getAuditTooltip(String userId, Date createDate, String updateUserId, Date updateDate, Date effectiveDate, Date expirationDate, String viewLevel) {
        StringBuilder tooltip = new StringBuilder();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        tooltip.append("<h4>Audit Information</h4><br/>");

        if (userId != null) {
            tooltip.append("<b>Created by (user):</b> ").append(userId).append("<br/>");
            PersonService personService = KimApiServiceLocator.getPersonService();
            Person person = personService.getPersonByPrincipalName(userId);
            if (person != null) {
                tooltip.append("<b>Created by (name):</b> ").append(person.getName()).append("<br/>");
                tooltip.append("<b>Contact Phone:</b> ").append(person.getPhoneNumber()).append("<br/>");
                tooltip.append("<b>Contact Email:</b> ").append(person.getEmailAddress()).append("<br/>");
            }
        }

        if (createDate != null) {
            tooltip.append("<b>Creation Date:</b> ").append(df.format(createDate)).append("<br/>");
        }

        if(updateUserId != null){
            tooltip.append("<b>Last Update By:</b> ").append(updateUserId).append("<br/>");
        }

        if (updateDate != null) {
            tooltip.append("<b>Last Updated:</b> ").append(df.format(updateDate)).append("<br/>");
        }

        if (effectiveDate != null) {
            tooltip.append("<b>Effective Date:</b> ").append(df.format(effectiveDate)).append("<br/>");
        }

        if (expirationDate != null) {
            tooltip.append("<b>Expiration Date:</b> ").append(df.format(expirationDate)).append("<br/>");
        }

        if(viewLevel != null){
            tooltip.append("<b>View Level:</b> ").append(viewLevel).append("<br/>");
        }

        return tooltip.toString();
    }

}
