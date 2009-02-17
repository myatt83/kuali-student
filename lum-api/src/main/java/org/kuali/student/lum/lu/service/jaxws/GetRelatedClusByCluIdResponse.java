
package org.kuali.student.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.lum.lu.dto.CluInfo;

/**
 * This class was generated by Apache CXF 2.1.4
 * Tue Feb 17 15:12:57 EST 2009
 * Generated source version: 2.1.4
 */

@XmlRootElement(name = "getRelatedClusByCluIdResponse", namespace = "http://student.kuali.org/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRelatedClusByCluIdResponse", namespace = "http://student.kuali.org/lum/lu")

public class GetRelatedClusByCluIdResponse {

    @XmlElement(name = "return")
    private java.util.List<CluInfo> _return;

    public java.util.List<CluInfo> getReturn() {
        return this._return;
    }

    public void setReturn(java.util.List<CluInfo> new_return)  {
        this._return = new_return;
    }

}

