
package org.kuali.student.lum.atp.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1.3
 * Wed Nov 19 10:43:53 EST 2008
 * Generated source version: 2.1.3
 */

@XmlRootElement(name = "fetchDateRangeType", namespace = "http://org.kuali.student/lum/atp")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fetchDateRangeType", namespace = "http://org.kuali.student/lum/atp")

public class FetchDateRangeType {

    @XmlElement(name = "dateRangeTypeKey")
    private java.lang.String dateRangeTypeKey;

    public java.lang.String getDateRangeTypeKey() {
        return this.dateRangeTypeKey;
    }

    public void setDateRangeTypeKey(java.lang.String newDateRangeTypeKey)  {
        this.dateRangeTypeKey = newDateRangeTypeKey;
    }

}

