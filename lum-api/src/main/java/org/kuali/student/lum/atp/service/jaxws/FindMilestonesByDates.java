
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

@XmlRootElement(name = "findMilestonesByDates", namespace = "http://org.kuali.student/lum/atp")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findMilestonesByDates", namespace = "http://org.kuali.student/lum/atp", propOrder = {"startDate","endDate"})

public class FindMilestonesByDates {

    @XmlElement(name = "startDate")
    private java.util.Date startDate;
    @XmlElement(name = "endDate")
    private java.util.Date endDate;

    public java.util.Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(java.util.Date newStartDate)  {
        this.startDate = newStartDate;
    }

    public java.util.Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(java.util.Date newEndDate)  {
        this.endDate = newEndDate;
    }

}

