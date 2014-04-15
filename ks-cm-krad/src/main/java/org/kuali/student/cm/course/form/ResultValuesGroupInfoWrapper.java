/**
 * Copyright 2005-2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.cm.course.form;

import java.util.ArrayList;
import java.util.List;

import org.kuali.student.r2.lum.lrc.dto.ResultValuesGroupInfo;


/**
 * This is the helper class for CourseView
 * 
 * @author OpenCollab/rSmart KRAD CM Conversion Alliance!
 */
public class ResultValuesGroupInfoWrapper extends ResultValuesGroupInfo{
    
    private static final long serialVersionUID = 8595074563846388089L;
    private List<ResultValueKeysWrapper> resultValueKeysDisplay;

    private ResultValuesGroupInfo resultValuesGroupInfo;
    
    public ResultValuesGroupInfoWrapper() {
        this.resultValueKeysDisplay = new ArrayList<ResultValueKeysWrapper>();
    }

    public List<ResultValueKeysWrapper> getResultValueKeysDisplay() {
        return resultValueKeysDisplay;
    }

    public void setResultValueKeysDisplay(List<ResultValueKeysWrapper> resultValueKeysDisplay) {
        this.resultValueKeysDisplay = resultValueKeysDisplay;
    }

    public ResultValuesGroupInfo getResultValuesGroupInfo() {
        return resultValuesGroupInfo;
    }

    public void setResultValuesGroupInfo(ResultValuesGroupInfo resultValuesGroupInfo) {
        this.resultValuesGroupInfo = resultValuesGroupInfo;
    }
}