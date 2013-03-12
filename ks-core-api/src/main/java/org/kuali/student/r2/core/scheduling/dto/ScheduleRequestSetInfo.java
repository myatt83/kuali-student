package org.kuali.student.r2.core.scheduling.dto;

import org.kuali.student.r2.common.dto.IdEntityInfo;
import org.kuali.student.r2.core.scheduling.infc.ScheduleRequestSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Sri komandur@uw.edu
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScheduleRequestSetInfo", propOrder = {"id", "typeKey", "stateKey", "name", "descr",
        "refObjectIds", "refObjectTypeKey", "isMaxEnrollmentShared", "maximumEnrollment",
        "meta", "attributes", "_futureElements" })
public class ScheduleRequestSetInfo extends IdEntityInfo implements ScheduleRequestSet, Serializable {

    @XmlElement
    private List<String> refObjectIds;
    @XmlElement
    private String refObjectTypeKey;
    @XmlElement
    private Boolean isMaxEnrollmentShared;
    @XmlElement
    private Integer maximumEnrollment;
    @XmlAnyElement
    private List<Object> _futureElements;

    @Override
    public List<String> getRefObjectIds() {

        if(refObjectIds == null) {
            return new ArrayList<String>();
        }
        return this.refObjectIds;
    }

    public void setRefObjectIds(List<String> refObjectIds) {
        this.refObjectIds = refObjectIds;
    }

    @Override
    public String getRefObjectTypeKey() {
        return this.refObjectTypeKey;
    }

    public void setRefObjectTypeKey(String refObjectTypeKey) {
        this.refObjectTypeKey = refObjectTypeKey;
    }

    @Override
    public Boolean getIsMaxEnrollmentShared() {
        return this.isMaxEnrollmentShared;
    }

    public void setMaxEnrollmentShared(Boolean maxEnrollmentShared) {
        this.isMaxEnrollmentShared = maxEnrollmentShared;
    }

    @Override
    public Integer getMaximumEnrollment() {
        return this.maximumEnrollment;
    }

    public void setMaximumEnrollment(Integer maximumEnrollment) {
        this.maximumEnrollment = maximumEnrollment;
    }
}
