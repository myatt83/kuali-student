/*
 * Copyright 2007 The Kuali Foundation Licensed under the Educational Community License, Version 1.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.opensource.org/licenses/ecl1.php Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.kuali.student.rules.brms.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kuali.student.poc.common.test.spring.AbstractTransactionalDaoTest;
import org.kuali.student.poc.common.test.spring.Dao;
import org.kuali.student.poc.common.test.spring.PersistenceFileLocation;
import org.kuali.student.rules.brms.core.dao.FunctionalBusinessRuleDAO;
import org.kuali.student.rules.brms.core.entity.ComparisonOperator;
import org.kuali.student.rules.brms.core.entity.ComputationAssistant;
import org.kuali.student.rules.brms.core.entity.FunctionalBusinessRule;
import org.kuali.student.rules.brms.core.entity.LeftHandSide;
import org.kuali.student.rules.brms.core.entity.Operator;
import org.kuali.student.rules.brms.core.entity.RightHandSide;
import org.kuali.student.rules.brms.core.entity.RuleElement;
import org.kuali.student.rules.brms.core.entity.RuleElementType;
import org.kuali.student.rules.brms.core.entity.RuleMetaData;
import org.kuali.student.rules.brms.core.entity.RuleProposition;
import org.kuali.student.rules.brms.core.entity.ValueType;
import org.kuali.student.rules.brms.core.entity.YieldValueFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * This is a <code>FunctionalBusinessRuleManagementService</code> test class.
 * 
 * @author Kuali Student Team (zdenek.kuali@google.com)
 */
@PersistenceFileLocation("classpath:META-INF/persistence.xml")
@ContextConfiguration(locations = {"classpath:brms-core-test-context.xml"})
public class FunctionalBusinessRuleManagementServiceTest extends AbstractTransactionalDaoTest {

    public static final String FACT_CONTAINER = "AcademicRecord";

    @Autowired
    FunctionalBusinessRuleManagementService brmsService;

    @Dao("org.kuali.student.rules.brms.core.dao.impl.FunctionalBusinessRuleDAOImpl")
    public FunctionalBusinessRuleDAO businessRuleDAO;

    /*@Test
    public void testRetrieveCompiledRuleIDs() {
        AnchorType type = new AnchorType("course", "clu.type.course");
        Anchor anchor = new Anchor("ems1001", "EMS 1001", type);
        AgendaType agendaType = new AgendaType("Student Enrolls in Course", "student.enrollment.course");
        agendaType.addBusinessRuleType(new BusinessRuleType("course-pre-req", "course.pre.req"));
        agendaType.addBusinessRuleType(new BusinessRuleType("course-co-req", "course.co.req"));
        agendaType.addBusinessRuleType(new BusinessRuleType("course-anti-req", "course.anti.req"));
        Agenda testAgenda = new Agenda("Student Enrolls in Course", agendaType);
        brmsService.retrieveCompiledRuleIDs(testAgenda, anchor);

        assertNotNull(testAgenda.getBusinessRule("333"));
    }*/

    @Test
    public void testRetrieveFunctionalBusinessRules() throws Exception {
        List<String> businessRuleTypes = new ArrayList<String>();
        businessRuleTypes.add("course-pre-req");
        businessRuleTypes.add("course-co-req");
        businessRuleTypes.add("course-anti-req");
        List<FunctionalBusinessRule> functionalBusinessRules = brmsService
                .retrieveFunctionalBusinessRules("Student Enrolls in Course", businessRuleTypes, "course", "EMS 1001");

        assertEquals(functionalBusinessRules.size(), 1);

        for (Iterator<FunctionalBusinessRule> iter = functionalBusinessRules.iterator(); iter.hasNext();) {
            FunctionalBusinessRule businessRule = iter.next();
            assertEquals(businessRule.getAgendaType(), "Student Enrolls in Course");
            assertTrue((businessRule.getBusinessRuleType() == "course-pre-req")
                    || (businessRule.getBusinessRuleType() == "course-co-req")
                    || (businessRule.getBusinessRuleType() == "course-anti-req"));
            assertEquals(businessRule.getAnchorType(), "course");
            assertEquals(businessRule.getAnchor(), "EMS 1001");
        }
    }

    /*
     * public void onTearDownAfterTransaction() throws Exception { super.onTearDownInTransaction(); setDirty(); }
     */
    @Before
    public void onSetUpInTransaction() throws Exception {
        int ordinalPosition = 1;
        RuleElement ruleElement = null;
        RuleProposition ruleProp = null;
        LeftHandSide leftSide = null;
        RightHandSide rightSide = null;
        Operator operator = null;
        ComputationAssistant compAssistant = null;

        // setup business rule meta data (for now common to all rules)
        RuleMetaData metaData = new RuleMetaData("Tom Smith", new Date(), "", null, new Date(), new Date(), "1.1",
                "active");

        /********************************************************************************************************************
         * insert "(1 of CPR 101) OR ( 1 of FA 001)"
         *******************************************************************************************************************/

        // create basic rule structure
        FunctionalBusinessRule busRule = new FunctionalBusinessRule("Intermediate CPR",
                "enrollment co-requisites for Intermediate CPR 201", "Rule 1 Success Message",
                "Rule 1 Failure Message", "1", "111", metaData, "Student Enrolls in Course", "course-co-req", "course",
                "CPR 201");

        // left bracket '('
        ruleElement = new RuleElement(RuleElementType.LPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 1 of CPR 101
        compAssistant = new ComputationAssistant(YieldValueFunction.INTERSECTION);
        leftSide = new LeftHandSide("Student.LearningResults", "CPR 101", FACT_CONTAINER, compAssistant,
                ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCourses", "java.lang.Integer", "1");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite courses",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // OR
        ruleElement = new RuleElement(RuleElementType.OR_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 1 of FA 001
        compAssistant = new ComputationAssistant(YieldValueFunction.INTERSECTION);
        leftSide = new LeftHandSide("Student.LearningResults", "FA 001", FACT_CONTAINER, compAssistant,
                ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCourses", "java.lang.Integer", "1");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite courses",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // right bracket ')'
        ruleElement = new RuleElement(RuleElementType.RPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // businessRuleDAO.createBusinessRule(busRule);
        em.persist(busRule);

        /********************************************************************************************************************
         * insert "2 of CPR 101 and CPR 201"
         *******************************************************************************************************************/

        // create basic rule structure
        busRule = new FunctionalBusinessRule("Advanced CPR", "enrollment co-requisites for Advanced CPR 301",
                "Rule 2 Success Message", "Rule 2 Failure Message", "2", "222", metaData, "Student Enrolls in Course",
                "course-co-req", "course", "CPR 301");

        // 2 of CPR 101 and CPR 201
        compAssistant = new ComputationAssistant(YieldValueFunction.INTERSECTION);
        leftSide = new LeftHandSide("Student.LearningResults", "CPR 101, CPR 201", FACT_CONTAINER, compAssistant,
                ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCourses", "java.lang.Integer", "2");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite courses",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // businessRuleDAO.createBusinessRule(busRule);
        em.persist(busRule);

        /********************************************************************************************************************
         * insert "(12 credits from CPR 101, CPR 105, CPR 201, CPR 301) OR (1 of CPR 4005 and 1 of FA 001, WS 001)"
         *******************************************************************************************************************/

        // create basic rule structure
        busRule = new FunctionalBusinessRule("EMS Certificate Program",
                "enrollment co-requisites for Certificate Program EMS 1001", "Rule 3 Success Message",
                "Rule 3 Failure Message", "3", "333", metaData, "Student Enrolls in Course", "course-co-req", "course",
                "EMS 1001");

        // left bracket '('
        ruleElement = new RuleElement(RuleElementType.LPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 12 credits from CPR 101, CPR 105, CPR 201, CPR 301
        compAssistant = new ComputationAssistant(YieldValueFunction.SUM);
        leftSide = new LeftHandSide("Student.LearningResults", "CPR 101, CPR 105, CPR 201, CPR 301", FACT_CONTAINER,
                compAssistant, ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCredits", "java.lang.Integer", "12");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite credits",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // right bracket ')'
        ruleElement = new RuleElement(RuleElementType.RPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // OR
        ruleElement = new RuleElement(RuleElementType.OR_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // left bracket '('
        ruleElement = new RuleElement(RuleElementType.LPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 1 of CPR 4005
        compAssistant = new ComputationAssistant(YieldValueFunction.INTERSECTION);
        leftSide = new LeftHandSide("Student.LearningResults", "CPR 4005", FACT_CONTAINER, compAssistant,
                ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCourses", "java.lang.Integer", "1");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite courses",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // AND
        ruleElement = new RuleElement(RuleElementType.AND_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 1 of FA 001, WS 001
        compAssistant = new ComputationAssistant(YieldValueFunction.INTERSECTION);
        leftSide = new LeftHandSide("Student.LearningResults", "FA 001, WS 001", FACT_CONTAINER, compAssistant,
                ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCourses", "java.lang.Integer", "2");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite courses",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // right bracket ')'
        ruleElement = new RuleElement(RuleElementType.RPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // businessRuleDAO.createBusinessRule(busRule);
        em.persist(busRule);

        /********************************************************************************************************************
         * insert "(12 credits from CPR 101, CPR 105, CPR 201, CPR 301 OR 1 of CPR 4005) and 1 of FA 001, WS 001"
         *******************************************************************************************************************/

        // create basic rule structure
        busRule = new FunctionalBusinessRule("LPN Certificate Program",
                "enrollment co-requisites for Certificate Program LPN 1001", "Rule 4 Success Message",
                "Rule 4 Failure Message", "4", "444", metaData, "Student Enrolls in Course", "course-co-req", "course",
                "LPN 1001");

        // left bracket '('
        ruleElement = new RuleElement(RuleElementType.LPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 12 credits from CPR 101, CPR 105, CPR 201, CPR 301
        compAssistant = new ComputationAssistant(YieldValueFunction.SUBSET);
        leftSide = new LeftHandSide("Student.LearningResults", "CPR 101, CPR 105, CPR 201, CPR 301", FACT_CONTAINER,
                compAssistant, ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCredits", "java.lang.Integer", "12");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite credits",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // OR
        ruleElement = new RuleElement(RuleElementType.OR_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 1 of CPR 4005
        compAssistant = new ComputationAssistant(YieldValueFunction.INTERSECTION);
        leftSide = new LeftHandSide("Student.LearningResults", "CPR 4005", FACT_CONTAINER, compAssistant,
                ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCourses", "java.lang.Integer", "1");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite courses",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // right bracket ')'
        ruleElement = new RuleElement(RuleElementType.RPAREN_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // AND
        ruleElement = new RuleElement(RuleElementType.AND_TYPE, ordinalPosition++, "", "", null, null);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // 1 of FA 001, WS 001
        compAssistant = new ComputationAssistant(YieldValueFunction.INTERSECTION);
        leftSide = new LeftHandSide("Student.LearningResults", "FA 001, WS 001", FACT_CONTAINER, compAssistant,
                ValueType.NUMBER);
        operator = new Operator(ComparisonOperator.EQUAL_TO);
        rightSide = new RightHandSide("requiredCourses", "java.lang.Integer", "2");
        ruleProp = new RuleProposition("co-requisites", "enumeration of required co-requisite courses",
                "prop error message", leftSide, operator, rightSide);
        ruleElement = new RuleElement(RuleElementType.PROPOSITION_TYPE, ordinalPosition++, "", "", null, ruleProp);
        ruleElement.setFunctionalBusinessRule(busRule);
        busRule.addRuleElement(ruleElement);

        // businessRuleDAO.createBusinessRule(busRule);
        em.persist(busRule);
    }
}
