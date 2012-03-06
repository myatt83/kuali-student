
package org.kuali.student.core.statement.service.jaxws;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.common.dto_rice.ValidationResultInfo;

/**
 * This class was generated by Apache CXF 2.2
 * Wed May 12 12:54:47 PDT 2010
 * Generated source version: 2.2
 */

@XmlRootElement(name = "validateRefStatementRelationResponse", namespace = "http://student.kuali.org/wsdl/statement")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validateRefStatementRelationResponse", namespace = "http://student.kuali.org/wsdl/statement")

public class ValidateRefStatementRelationResponse {

    @XmlElement(name = "return")
    private java.util.List<ValidationResultInfo> _return;

    public java.util.List<ValidationResultInfo> getReturn() {
    	if (_return == null) {
    		_return = new ArrayList<ValidationResultInfo>(0);
    	}
        return this._return;
    }

    public void setReturn(java.util.List<ValidationResultInfo> new_return)  {
        this._return = new_return;
    }

}

