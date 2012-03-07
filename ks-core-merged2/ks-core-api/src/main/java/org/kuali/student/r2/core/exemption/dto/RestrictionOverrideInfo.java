/*
 * Copyright 2010 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.kuali.student.r2.core.exemption.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.r2.core.exemption.infc.RestrictionOverride;
//import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestrictionOverrideInfo", propOrder = { "restrictionKey"})//, "_futureElements" }) TODO KSCM Non-GWT translatable code

public class RestrictionOverrideInfo implements RestrictionOverride, Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String restrictionKey;

//  TODO KSCM Non-GWT translatable code
//	@XmlAnyElement
//	private List<Element> _futureElements;

	public RestrictionOverrideInfo() {
		super();
		restrictionKey = null;
		
//	    TODO KSCM Non-GWT translatable code
//		_futureElements = null;
	}

	/**
	 * Constructs a new RestrictionOverrideInfo from another
	 * RestrictionOverride.
	 * 
	 * @param exemption the RestrictionOverride to copy
	 */
	public RestrictionOverrideInfo(RestrictionOverride restrictionOverride) {
		super();
		if (null != restrictionOverride) {
			this.restrictionKey = restrictionOverride.getRestrictionKey();
		}

//	    TODO KSCM Non-GWT translatable code
//		_futureElements = null;
	}

	@Override
	public String getRestrictionKey() {
	    return restrictionKey;
	}

	public void setRestrictionKey(String restrictionKey) {
	    this.restrictionKey = restrictionKey;
	}
}
