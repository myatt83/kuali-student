
package org.kuali.student.lum.lrc.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2
 * Tue Apr 21 14:42:30 PDT 2009
 * Generated source version: 2.2
 */

@XmlRootElement(name = "updateResultComponent", namespace = "http://service.lrc.lum.student.kuali.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateResultComponent", namespace = "http://service.lrc.lum.student.kuali.org/", propOrder = {"arg0","arg1"})

public class UpdateResultComponent {

    @XmlElement(name = "arg0")
    private java.lang.String arg0;
    @XmlElement(name = "arg1")
    private org.kuali.student.lum.lrc.dto.ResultComponentInfo arg1;

    public java.lang.String getArg0() {
        return this.arg0;
    }

    public void setArg0(java.lang.String newArg0)  {
        this.arg0 = newArg0;
    }

    public org.kuali.student.lum.lrc.dto.ResultComponentInfo getArg1() {
        return this.arg1;
    }

    public void setArg1(org.kuali.student.lum.lrc.dto.ResultComponentInfo newArg1)  {
        this.arg1 = newArg1;
    }

}

