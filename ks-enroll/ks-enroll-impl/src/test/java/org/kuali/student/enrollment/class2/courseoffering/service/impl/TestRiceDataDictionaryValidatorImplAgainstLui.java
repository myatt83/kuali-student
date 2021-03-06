/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.enrollment.class2.courseoffering.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.student.enrollment.lui.dto.LuiInfo;
import org.kuali.student.r2.common.datadictionary.DataDictionaryValidator;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.ValidationResultInfo;
import org.kuali.student.r2.common.infc.ValidationResult;
import org.kuali.student.r2.common.util.constants.LuiServiceConstants;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author nwright
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:co-test-with-class2-mock-context.xml"})
public class TestRiceDataDictionaryValidatorImplAgainstLui {
    public ContextInfo callContext = null;

    @Resource
    protected DataDictionaryValidator validator;

    public TestRiceDataDictionaryValidatorImplAgainstLui() {
    }

    @Before
    public void setup() throws Exception {
        callContext = new ContextInfo();
        callContext.setPrincipalId("principalId.1");
    }

    @After
    public void tearDown() throws Exception {
    }

    private Date parseDate(String str) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        return df.parse(str);
    }

    private LuiInfo getDefaultLuiInfo() throws Exception {
        LuiInfo lui = new LuiInfo();
        lui.setName("test lui");
        lui.setTypeKey(LuiServiceConstants.COURSE_OFFERING_TYPE_KEY);
        lui.setStateKey(LuiServiceConstants.LUI_CO_STATE_DRAFT_KEY);
        lui.setEffectiveDate(this.parseDate("2011-01-01"));
        lui.setCluId("fake-clu-id");
        lui.setAtpId("fake-atp-id");
        return lui;
    }

    /**
     * Test of validate method, of class RiceValidatorImpl.
     */
    @Test
    public void testValidate1() throws Exception {
        DataDictionaryValidator.ValidationType validationType = DataDictionaryValidator.ValidationType.FULL_VALIDATION;
        LuiInfo lui = this.getDefaultLuiInfo();

        List<ValidationResultInfo> result = validator.validate(validationType, lui, callContext);
        for (ValidationResultInfo r : result) {
            System.out.println(r.getElement() + ", " + r.getLevel() + ", " + r.getMessage());
        }
        assertTrue(result.isEmpty());

        lui.setTypeKey("");
        result = validator.validate(validationType, lui, callContext);
        assertEquals(1, result.size());
        assertEquals("typeKey", result.get(0).getElement());
        assertEquals(ValidationResult.ErrorLevel.ERROR, result.get(0).getLevel());

        validationType = DataDictionaryValidator.ValidationType.SKIP_REQUREDNESS_VALIDATIONS;
        result = validator.validate(validationType, lui, callContext);
        assertTrue(result.isEmpty());
    }
}
