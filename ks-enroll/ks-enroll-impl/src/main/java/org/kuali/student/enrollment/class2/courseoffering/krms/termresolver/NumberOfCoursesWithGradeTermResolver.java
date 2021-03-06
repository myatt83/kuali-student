/**
 * Copyright 2011 The Kuali Foundation Licensed under the
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

package org.kuali.student.enrollment.class2.courseoffering.krms.termresolver;

import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.student.common.util.krms.RulesExecutionConstants;
import org.kuali.student.enrollment.class2.courseoffering.krms.termresolver.util.GradeTermResolverSupport;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.krms.util.KSKRMSExecutionUtil;
import org.kuali.student.r2.core.constants.KSKRMSServiceConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Rule Statement examples:
 * 1) Must successfully complete a minimum of <n> courses from <courses> with a minimum grade of <gradeType> <grade>
 *
 * @author Kuali Student Team
 */
public class NumberOfCoursesWithGradeTermResolver extends GradeTermResolverSupport<Integer> {

    @Override
    public String getOutput() {
        return KSKRMSServiceConstants.TERM_RESOLVER_NUMBEROFCOURSESWITHGRADE;
    }

    @Override
    public Set<String> getParameterNames() {
        Set<String> temp = new HashSet<String>(3);
        temp.add(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_COURSE_CLUSET_KEY);
        temp.add(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_GRADE_KEY);
        temp.add(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_GRADE_TYPE_KEY);
        return Collections.unmodifiableSet(temp);
    }

    @Override
    public int getCost() {
        return 5;
    }

    @Override
    public Integer resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) throws TermResolutionException {
        ContextInfo context = (ContextInfo) resolvedPrereqs.get(RulesExecutionConstants.CONTEXT_INFO_TERM.getName());
        String personId = (String) resolvedPrereqs.get(RulesExecutionConstants.PERSON_ID_TERM.getName());

        int result = 0;
        try {
            ////Retrieve the list of cluIds from the cluset.
            String cluSetId = parameters.get(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_COURSE_CLUSET_KEY);
            String gradeType = parameters.get(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_GRADE_TYPE_KEY);
            String grade = parameters.get(KSKRMSServiceConstants.TERM_PARAMETER_TYPE_GRADE_KEY);

            List<String> versionIndIds = this.getCluIdsForCluSet(cluSetId, parameters, context);
            for(String versionIndId : versionIndIds){
                if(this.checkCourseWithGrade(personId, versionIndId, grade, gradeType, parameters, context)){
                    result++;
                }
            }
        } catch (Exception e) {
            KSKRMSExecutionUtil.convertExceptionsToTermResolutionException(parameters, e, this);
        }

        return result;
    }

}
