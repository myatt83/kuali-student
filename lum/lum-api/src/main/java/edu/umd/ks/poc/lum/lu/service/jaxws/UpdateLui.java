
package edu.umd.ks.poc.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by the CXF 2.0.4-incubator
 * Fri Apr 18 16:30:06 EDT 2008
 * Generated source version: 2.0.4-incubator
 * 
 */

@XmlRootElement(name = "updateLui", namespace = "http://edu.umd.ks/poc/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateLui", namespace = "http://edu.umd.ks/poc/lum/lu")

public class UpdateLui {

    @XmlElement(name = "luiId")
    private java.lang.String luiId;
    @XmlElement(name = "luiUpdateInfo")
    private edu.umd.ks.poc.lum.lu.dto.LuiUpdateInfo luiUpdateInfo;

    public java.lang.String getLuiId() {
        return this.luiId;
    }
    
    public void setLuiId( java.lang.String newLuiId ) {
        this.luiId = newLuiId;
    }
    
    public edu.umd.ks.poc.lum.lu.dto.LuiUpdateInfo getLuiUpdateInfo() {
        return this.luiUpdateInfo;
    }
    
    public void setLuiUpdateInfo( edu.umd.ks.poc.lum.lu.dto.LuiUpdateInfo newLuiUpdateInfo ) {
        this.luiUpdateInfo = newLuiUpdateInfo;
    }
    
}

