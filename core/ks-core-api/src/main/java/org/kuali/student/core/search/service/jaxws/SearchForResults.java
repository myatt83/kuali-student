/*
 * Copyright 2009 The Kuali Foundation Licensed under the
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
package org.kuali.student.core.search.service.jaxws;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1.4
 * Thu Feb 19 10:05:55 EST 2009
 * Generated source version: 2.1.4
 */

@XmlRootElement(name = "searchForResults", namespace = "http://student.kuali.org/wsdl/search")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchForResults", namespace = "http://student.kuali.org/wsdl/search", propOrder = {"searchTypeKey","queryParamValues"})

public class SearchForResults {

    @XmlElement(name = "searchTypeKey")
    private java.lang.String searchTypeKey;
    @XmlElement(name = "queryParamValues")
    private java.util.List<org.kuali.student.core.search.dto.QueryParamValue> queryParamValues;

    public java.lang.String getSearchTypeKey() {
        return this.searchTypeKey;
    }

    public void setSearchTypeKey(java.lang.String newSearchTypeKey)  {
        this.searchTypeKey = newSearchTypeKey;
    }

    public java.util.List<org.kuali.student.core.search.dto.QueryParamValue> getQueryParamValues() {
    	if (this.queryParamValues == null) {
    		this.queryParamValues = new ArrayList<org.kuali.student.core.search.dto.QueryParamValue>(0);
    	}
        return this.queryParamValues;
    }

    public void setQueryParamValues(java.util.List<org.kuali.student.core.search.dto.QueryParamValue> newQueryParamValues)  {
        this.queryParamValues = newQueryParamValues;
    }

}

