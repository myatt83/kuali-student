
package org.kuali.student.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1.4
 * Tue Feb 24 12:25:30 EST 2009
 * Generated source version: 2.1.4
 */

@XmlRootElement(name = "getLuDocRelationsByIdList", namespace = "http://student.kuali.org/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLuDocRelationsByIdList", namespace = "http://student.kuali.org/lum/lu")

public class GetLuDocRelationsByIdList {

    @XmlElement(name = "luDocRelationIdList")
    private java.util.List<java.lang.String> luDocRelationIdList;

    public java.util.List<java.lang.String> getLuDocRelationIdList() {
        return this.luDocRelationIdList;
    }

    public void setLuDocRelationIdList(java.util.List<java.lang.String> newLuDocRelationIdList)  {
        this.luDocRelationIdList = newLuDocRelationIdList;
    }

}

