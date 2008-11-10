
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

@XmlRootElement(name = "updateLuiRelation", namespace = "http://edu.umd.ks/poc/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateLuiRelation", namespace = "http://edu.umd.ks/poc/lum/lu")

public class UpdateLuiRelation {

    @XmlElement(name = "luiId")
    private java.lang.String luiId;
    @XmlElement(name = "relatedLuiId")
    private java.lang.String relatedLuiId;
    @XmlElement(name = "luRelationType")
    private java.lang.String luRelationType;
    @XmlElement(name = "luiRelationUpdateInfo")
    private edu.umd.ks.poc.lum.lu.dto.LuiRelationUpdateInfo luiRelationUpdateInfo;

    public java.lang.String getLuiId() {
        return this.luiId;
    }
    
    public void setLuiId( java.lang.String newLuiId ) {
        this.luiId = newLuiId;
    }
    
    public java.lang.String getRelatedLuiId() {
        return this.relatedLuiId;
    }
    
    public void setRelatedLuiId( java.lang.String newRelatedLuiId ) {
        this.relatedLuiId = newRelatedLuiId;
    }
    
    public java.lang.String getLuRelationType() {
        return this.luRelationType;
    }
    
    public void setLuRelationType( java.lang.String newLuRelationType ) {
        this.luRelationType = newLuRelationType;
    }
    
    public edu.umd.ks.poc.lum.lu.dto.LuiRelationUpdateInfo getLuiRelationUpdateInfo() {
        return this.luiRelationUpdateInfo;
    }
    
    public void setLuiRelationUpdateInfo( edu.umd.ks.poc.lum.lu.dto.LuiRelationUpdateInfo newLuiRelationUpdateInfo ) {
        this.luiRelationUpdateInfo = newLuiRelationUpdateInfo;
    }
    
}

