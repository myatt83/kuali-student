package org.kuali.student.ap.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.support.DefaultKsapContext;
import org.kuali.student.ap.academicplan.dto.LearningPlanInfo;
import org.kuali.student.ap.academicplan.dto.PlanItemInfo;
import org.kuali.student.ap.academicplan.infc.LearningPlan;
import org.kuali.student.ap.academicplan.constants.AcademicPlanServiceConstants;
import org.kuali.student.r2.common.dto.MetaInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.dto.ValidationResultInfo;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.lum.clu.CLUConstants;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ks-ap-test-context.xml" })
@TransactionConfiguration(transactionManager = "JtaTxManager", defaultRollback = true)
@Transactional
public class AcademicPlanServiceImplTest extends TestAcademicPlanServiceImplConformanceExtendedCrud {


    private final AcademicPlanServiceTstHelper testHelper = new AcademicPlanServiceTstHelper();

//    @Before
//	public void setUp() throws Exception {
//		DefaultKsapContext.before("student1");
//

//TODO:  KSAP-1016 - Setup Course MapImpl to sppt KSAP service tests

//        CluDataLoader cluDataLoader = new CluDataLoader();
//        cluDataLoader.setCluService(KsapFrameworkServiceLocator.getCluService());
//        cluDataLoader.setCluService(KsapFrameworkServiceLocator.getCluService());
//
//        //the following actually returns the context of current thread
//        cluDataLoader.setContextInfo(new DefaultKsapContext().getContextInfo());
//
//        cluDataLoader.load();
//
////        TermAndCalDataLoader termAndCalDataLoader = new TermAndCalDataLoader();
////        termAndCalDataLoader.loadData();
////
//        testHelper.createKsapTypes();
//    }

    @After
	public void tearDown() {
		DefaultKsapContext.after();
	}

	@Test(expected = DoesNotExistException.class)
	public void getUnknownLearningPlan() throws InvalidParameterException,
			MissingParameterException, DoesNotExistException,
			OperationFailedException {
		KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlan("unknown_plan",
				KsapFrameworkServiceLocator.getContext().getContextInfo());
	}

	@Test
	public void getLearningPlan() throws Throwable {
		String planId = "lp1";
		LearningPlan learningPlan = KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlan(planId,
				KsapFrameworkServiceLocator.getContext().getContextInfo());
		assertNotNull(learningPlan);
		assertEquals(planId, learningPlan.getId());
		assertEquals("student1", learningPlan.getStudentId());
		assertEquals("Student 1 Learning Plan 1", learningPlan.getDescr()
				.getPlain());
	}

	@Test
	public void getLearningPlans() throws Throwable {
		String studentId = "student1";
		List<LearningPlanInfo> learningPlans;
		learningPlans = KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlansForStudentByType(
				studentId,
				AcademicPlanServiceConstants.LEARNING_PLAN_TYPE_PLAN,
				KsapFrameworkServiceLocator.getContext().getContextInfo());
		assertNotNull(learningPlans);
		assertEquals(2, learningPlans.size());

		LearningPlan lp = learningPlans.get(0);
		assertEquals("lp1", lp.getId());

		lp = learningPlans.get(1);
		assertEquals("lp2", lp.getId());
	}

	@Test
	public void addLearningPlan() throws Throwable {
		LearningPlanInfo learningPlan = new LearningPlanInfo();
		learningPlan
				.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_TYPE_PLAN);
		learningPlan.setStudentId("student1");
		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Plan</span>";
		String planDesc = "My Plan";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		learningPlan.setDescr(desc);

		// Set meta data object.
		learningPlan.setMeta(new MetaInfo());

		learningPlan
				.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ACTIVE_STATE_KEY);

		LearningPlanInfo newLearningPlan = KsapFrameworkServiceLocator.getAcademicPlanService()
				.createLearningPlan(learningPlan, KsapFrameworkServiceLocator
						.getContext().getContextInfo());

		assertNotNull(newLearningPlan);
		assertEquals(AcademicPlanServiceConstants.LEARNING_PLAN_TYPE_PLAN,
				newLearningPlan.getTypeKey());
		assertEquals("student1", newLearningPlan.getStudentId());
		assertEquals(formattedDesc, newLearningPlan.getDescr().getFormatted());
		assertEquals(planDesc, newLearningPlan.getDescr().getPlain());

		// Validate metadata was set.
		assertEquals("student1", newLearningPlan.getMeta().getCreateId());
		assertNotNull(newLearningPlan.getMeta().getCreateTime());
		assertEquals("student1", newLearningPlan.getMeta().getUpdateId());
		assertNotNull(newLearningPlan.getMeta().getUpdateTime());
	}

	@Test
	public void updateLearningPlanTimestamp() throws Throwable {

		// Create a new learning plan so that all the meta data is properly
		// initialized.
		LearningPlanInfo learningPlan = new LearningPlanInfo();
		learningPlan
				.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_TYPE_PLAN);
		learningPlan.setStudentId("student1");
		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Plan</span>";
		String planDesc = "My Plan";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		learningPlan.setDescr(desc);

		learningPlan
				.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ACTIVE_STATE_KEY);

		LearningPlanInfo plan;
        plan = KsapFrameworkServiceLocator.getAcademicPlanService().createLearningPlan(learningPlan,
                KsapFrameworkServiceLocator.getContext().getContextInfo());

		assertNotNull(plan);

		Date updated1 = plan.getMeta().getUpdateTime();
		assertNotNull(updated1);

		// FIXME: Implement state.
		plan.setStateKey("fixme");
		Thread.sleep(2000L);

        // NOTE: the following stmt is required to change the date in contextInfo...otherwise it is always the same
        //        for ea transaction  (it is used to set the updateDateTime)
        KsapFrameworkServiceLocator.getContext().getContextInfo().setCurrentDate(new Date());
		plan = KsapFrameworkServiceLocator.getAcademicPlanService().updateLearningPlan(plan.getId(), plan,
				KsapFrameworkServiceLocator.getContext().getContextInfo());
		Date updated2 = plan.getMeta().getUpdateTime();
		assertNotNull(updated2);

		assertFalse(updated1.equals(updated2));
	}

	@Test
	public void deleteLearningPlan() throws Exception {
		String id = "lp1";

		// Make sure the plan exists and has some plan items.
        KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlan(id, KsapFrameworkServiceLocator
                .getContext().getContextInfo());

		int itemCount = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItemsInPlan(id,
				KsapFrameworkServiceLocator.getContext().getContextInfo())
				.size();
		assertEquals(8, itemCount);

		// Delete the plan
        KsapFrameworkServiceLocator.getAcademicPlanService().deleteLearningPlan(id,
                KsapFrameworkServiceLocator.getContext().getContextInfo());

		// Make sure the plan items were cleaned up.
		itemCount = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItemsInPlan(id,
				KsapFrameworkServiceLocator.getContext().getContextInfo())
				.size();
		assertEquals(0, itemCount);

        try {
            KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlan(id, KsapFrameworkServiceLocator
                    .getContext().getContextInfo());
            fail("DoesNotExistException should have been thrown");
        } catch(DoesNotExistException dnee) {
            assertNotNull(dnee.getMessage());
            assertEquals(id, dnee.getMessage());
        }
	}

	@Test
	public void getPlanItemsByPlanIdByRefObjectIdByRefObjectType()
			throws InvalidParameterException, MissingParameterException,
			DoesNotExistException, OperationFailedException {

		String planId = "lp1";
		String refObjectId = "006476b5-18d8-4830-bbb6-2bb9e79600fb";
		String refObjectType = "kuali.lu.type.CreditCourse";

		List<PlanItemInfo> planItems = KsapFrameworkServiceLocator.getAcademicPlanService()
				.getPlanItemsInPlanByRefObjectIdByRefObjectType(planId,
						refObjectId, refObjectType, KsapFrameworkServiceLocator
								.getContext().getContextInfo());
		assertEquals(1, planItems.size());
		assertEquals(planId, planItems.get(0).getLearningPlanId());
		assertEquals(refObjectId, planItems.get(0).getRefObjectId());
		assertEquals(refObjectType, planItems.get(0).getRefObjectType());
	}

	@Test
	public void addAndGetPlanItemWishlist() throws Throwable {
		String planId = "lp1";

		// Create a new plan item.
		PlanItemInfo planItem = new PlanItemInfo();

		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Comment</span>";
		String planDesc = "My Comment";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		planItem.setDescr(desc);

		planItem.setLearningPlanId(planId);
		planItem.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        planItem.setCategory(AcademicPlanServiceConstants.ItemCategory.WISHLIST);
		String courseId = "c796aecc-7234-4482-993c-bf00b8088e84";
		String courseType = CLUConstants.CLU_TYPE_CREDIT_COURSE;

		planItem.setRefObjectId(courseId);
		planItem.setRefObjectType(courseType);

		// Type wishlist has no ATP associated with it so leave plan terms
		// null.

		planItem.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);

		PlanItem newPlanItem = testServiceNoValidator.createPlanItem(planItem,
                KsapFrameworkServiceLocator.getContext().getContextInfo());
		assertNotNull(newPlanItem);
		assertNotNull(newPlanItem.getId());
		assertEquals(planId, newPlanItem.getLearningPlanId());
		assertEquals(formattedDesc, newPlanItem.getDescr().getFormatted());
		assertEquals(planDesc, newPlanItem.getDescr().getPlain());
		assertEquals(courseId, newPlanItem.getRefObjectId());
		assertEquals(courseType, newPlanItem.getRefObjectType());

		// Test getPlanItem
		PlanItem fetchedPlanItem = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItem(newPlanItem
				.getId(), KsapFrameworkServiceLocator.getContext()
				.getContextInfo());

		assertNotNull(fetchedPlanItem);
		assertNotNull(fetchedPlanItem.getId());
		assertEquals(planId, fetchedPlanItem.getLearningPlanId());
		assertEquals(formattedDesc, fetchedPlanItem.getDescr().getFormatted());
		assertEquals(planDesc, fetchedPlanItem.getDescr().getPlain());
		assertEquals(courseId, fetchedPlanItem.getRefObjectId());
		assertEquals(courseType, fetchedPlanItem.getRefObjectType());
	}

	@Test
	public void addAndGetPlanItemPlannedCourse() throws Throwable {
		String planId = "lp1";

		// Create a new plan item.
		PlanItemInfo planItem = new PlanItemInfo();

		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Comment</span>";
		String planDesc = "My Comment";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		planItem.setDescr(desc);

		planItem.setLearningPlanId(planId);
		planItem.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        AcademicPlanServiceConstants.ItemCategory category = AcademicPlanServiceConstants.ItemCategory.PLANNED;
        planItem.setCategory(category);

		// Set some ATP info since this is a planned course.
		List<String> planTermIds = new ArrayList<String>();
		planTermIds.add("20111");
		planTermIds.add("20114");
		planItem.setPlanTermIds(planTermIds);

		String courseId = "COURSE5";
		String courseType = CLUConstants.CLU_TYPE_CREDIT_COURSE;

		planItem.setRefObjectId(courseId);
		planItem.setRefObjectType(courseType);

		planItem.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);

		// Verify the object returned by createPlanItem.
		PlanItem newPlanItem = testServiceNoValidator.createPlanItem(planItem,
				KsapFrameworkServiceLocator.getContext().getContextInfo());
		assertNotNull(newPlanItem);
		assertNotNull(newPlanItem.getId());
		assertEquals(planId, newPlanItem.getLearningPlanId());
		assertEquals(formattedDesc, newPlanItem.getDescr().getFormatted());
		assertEquals(planDesc, newPlanItem.getDescr().getPlain());
		assertEquals(courseId, newPlanItem.getRefObjectId());
		assertEquals(courseType, newPlanItem.getRefObjectType());
        assertEquals(category  , newPlanItem.getCategory());

		assertEquals(2, newPlanItem.getPlanTermIds().size());

		// Verify the object returned by getPlanItem().
		PlanItem fetchedPlanItem = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItem(newPlanItem
				.getId(), KsapFrameworkServiceLocator.getContext()
				.getContextInfo());

		assertNotNull(fetchedPlanItem);
		assertNotNull(fetchedPlanItem.getId());
		assertEquals(planId, fetchedPlanItem.getLearningPlanId());
		assertEquals(formattedDesc, fetchedPlanItem.getDescr().getFormatted());
		assertEquals(planDesc, fetchedPlanItem.getDescr().getPlain());
		assertEquals(courseId, fetchedPlanItem.getRefObjectId());
		assertEquals(courseType, fetchedPlanItem.getRefObjectType());
        assertEquals(category  , fetchedPlanItem.getCategory());

		assertEquals(2, fetchedPlanItem.getPlanTermIds().size());
	}

	@Test
	public void updatePlanItemPlannedCoursePlanTermIds() throws Throwable {

		String planId = "lp1";

		// Create a new plan item.
		PlanItemInfo planItemInfo = new PlanItemInfo();

		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Comment</span>";
		String planDesc = "My Comment";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		planItemInfo.setDescr(desc);
		planItemInfo.setMeta(new MetaInfo());

		planItemInfo.setLearningPlanId(planId);

		// Set some ATP info since this is a planned course.
		List<String> planTermIds = new ArrayList<String>();
		planTermIds.add("20111");
		planTermIds.add("20114");
		planItemInfo.setPlanTermIds(planTermIds);

		String courseId = "c796aecc-7234-4482-993c-bf00b8088e84";
		String courseType = CLUConstants.CLU_TYPE_CREDIT_COURSE;

		planItemInfo.setRefObjectId(courseId);
		planItemInfo.setRefObjectType(courseType);

        planItemInfo.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        AcademicPlanServiceConstants.ItemCategory category = AcademicPlanServiceConstants.ItemCategory.PLANNED;
        planItemInfo.setCategory(category);
		planItemInfo
				.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);

		// Save the plan item
		PlanItemInfo newPlanItem = testServiceNoValidator.createPlanItem(
				planItemInfo, KsapFrameworkServiceLocator.getContext()
						.getContextInfo());
		String planItemId = newPlanItem.getId();

		// Verify the object returned by getPlanItem().
		PlanItemInfo fetchedPlanItem = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItem(
				planItemId, KsapFrameworkServiceLocator.getContext()
						.getContextInfo());

		assertNotNull(fetchedPlanItem);
		assertEquals(planItemId, fetchedPlanItem.getId());
		assertEquals(planId, fetchedPlanItem.getLearningPlanId());
		assertEquals(courseId, fetchedPlanItem.getRefObjectId());
		assertEquals(courseType, fetchedPlanItem.getRefObjectType());
        assertEquals(planItemInfo.getCategory(), fetchedPlanItem.getCategory());
		assertEquals(planItemInfo.getTypeKey(), fetchedPlanItem.getTypeKey());
		assertEquals(planItemInfo.getStateKey(), fetchedPlanItem.getStateKey());
		assertEquals(2, fetchedPlanItem.getPlanTermIds().size());
		assertEquals("student1", fetchedPlanItem.getMeta().getUpdateId());
		assertNotNull(fetchedPlanItem.getMeta().getUpdateTime());

		// Save meta data info.
		Date originalUpdateDate = newPlanItem.getMeta().getUpdateTime();

		// Update the plan item and save.
		fetchedPlanItem.getPlanTermIds().remove("20111");
		assertEquals(1, fetchedPlanItem.getPlanTermIds().size());

        // NOTE: the following stmt is required to change the date in contextInfo...otherwise it is always the same
        //        for ea transaction  (it is used to set the updateDateTime)
        KsapFrameworkServiceLocator.getContext().getContextInfo().setCurrentDate(new Date());

        PlanItemInfo updatedPlanItem = testServiceNoValidator.updatePlanItem(
				planItemId, fetchedPlanItem, KsapFrameworkServiceLocator
						.getContext().getContextInfo());

		assertNotNull(updatedPlanItem);
		assertEquals(planItemId, updatedPlanItem.getId());
		assertEquals(planId, updatedPlanItem.getLearningPlanId());
		assertEquals(formattedDesc, updatedPlanItem.getDescr().getFormatted());
		assertEquals(planDesc, updatedPlanItem.getDescr().getPlain());
		assertEquals(courseId, updatedPlanItem.getRefObjectId());
		assertEquals(courseType, updatedPlanItem.getRefObjectType());
        assertEquals(category, updatedPlanItem.getCategory());
		assertEquals(1, updatedPlanItem.getPlanTermIds().size());
		assertTrue(updatedPlanItem.getPlanTermIds().contains("20114"));
		assertFalse(originalUpdateDate.equals(updatedPlanItem.getMeta()
				.getUpdateTime()));
	}

	@Test
	public void addPlanItemNullCourseType() throws Throwable {
		String planId = "lp1";

		PlanItemInfo planItem = new PlanItemInfo();

		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Comment</span>";
		String planDesc = "My Comment";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		planItem.setDescr(desc);

		planItem.setLearningPlanId(planId);
        planItem.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        planItem.setCategory(AcademicPlanServiceConstants.ItemCategory.WISHLIST);
		String courseId = "c796aecc-7234-4482-993c-bf00b8088e84";
		String courseType = null;

		planItem.setRefObjectId(courseId);
		planItem.setRefObjectType(courseType);
		planItem.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);

        try {
            KsapFrameworkServiceLocator.getAcademicPlanService().createPlanItem(planItem,
                    KsapFrameworkServiceLocator.getContext().getContextInfo());
            fail("DataValidationErrorException should have been thrown");
        } catch (DataValidationErrorException dvee) {
            dvee.printStackTrace();
            assertTrue("validation messages should not be empty", !dvee.getValidationResults().isEmpty());
            ValidationResultInfo resultInfo = dvee.getValidationResults().get(0);
            assertEquals("refObjectType", resultInfo.getElement());
            assertEquals("error.required", resultInfo.getMessage());
        }
    }

	@Test
	public void addPlanItemNullLearningPlan() throws Throwable {
		String planId = null;

		PlanItemInfo planItem = new PlanItemInfo();

		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Comment</span>";
		String planDesc = "My Comment";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		planItem.setDescr(desc);

		planItem.setLearningPlanId(planId);
        planItem.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        planItem.setCategory(AcademicPlanServiceConstants.ItemCategory.WISHLIST);

		String courseId = "c796aecc-7234-4482-993c-bf00b8088e84";
		String courseType = CLUConstants.CLU_TYPE_CREDIT_COURSE;

		planItem.setRefObjectId(courseId);
		planItem.setRefObjectType(courseType);
		planItem.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);

		try {
			KsapFrameworkServiceLocator.getAcademicPlanService().createPlanItem(planItem,
					KsapFrameworkServiceLocator.getContext().getContextInfo());
            fail("InvalidParameterException should have been thrown as learning plan id was null");
        } catch (DataValidationErrorException dvee) {
            dvee.printStackTrace();
            assertTrue("validation messages should not be empty", !dvee.getValidationResults().isEmpty());
            ValidationResultInfo resultInfo = dvee.getValidationResults().get(0);
            assertEquals("learningPlanId", resultInfo.getElement());
            assertEquals("error.required", resultInfo.getMessage());
        }
	}

	@Test
	public void addPlanItemNullCourseId() throws Exception {
		String planId = "lp1";

		PlanItemInfo planItem = new PlanItemInfo();

		RichTextInfo desc = new RichTextInfo();
		String formattedDesc = "<span>My Comment</span>";
		String planDesc = "My Comment";
		desc.setFormatted(formattedDesc);
		desc.setPlain(planDesc);
		planItem.setDescr(desc);

		planItem.setLearningPlanId(planId);
        planItem.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
		planItem.setCategory(AcademicPlanServiceConstants.ItemCategory.WISHLIST);
		String courseId = null;
		String courseType = CLUConstants.CLU_TYPE_CREDIT_COURSE;

		planItem.setRefObjectId(courseId);
		planItem.setRefObjectType(courseType);
		planItem.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);

        try {
            KsapFrameworkServiceLocator.getAcademicPlanService().createPlanItem(planItem,
                    KsapFrameworkServiceLocator.getContext().getContextInfo());
            fail("DataValidationErrorException should have been thrown");
        } catch (DataValidationErrorException dvee) {
            dvee.printStackTrace();
            assertEquals("Error(s) validating plan item.",dvee.getMessage());
            ValidationResultInfo resultInfo = dvee.getValidationResults().get(0);
            assertTrue("validation results should not be empty", !dvee.getValidationResults().isEmpty());
            assertEquals("refObjectId", resultInfo.getElement());
            assertEquals("error.required", resultInfo.getMessage());
        }
    }

	@Test
	public void deletePlanItem() throws Exception {
		String id = "lp1";

		// Make sure the plan exists and has some plan items.
        KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlan(id, KsapFrameworkServiceLocator
                .getContext().getContextInfo());

		List<PlanItemInfo> planItems;
        planItems = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItemsInPlan(id,
                KsapFrameworkServiceLocator.getContext().getContextInfo());
		assertEquals(8, planItems.size());

		// Delete a plan item.
		String planItemId = planItems.get(0).getId();
        KsapFrameworkServiceLocator.getAcademicPlanService().deletePlanItem(planItemId,
                KsapFrameworkServiceLocator.getContext().getContextInfo());

		// Make sure the plan items were cleaned up.
        planItems = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItemsInPlan(id,
                KsapFrameworkServiceLocator.getContext().getContextInfo());
		assertEquals(7, planItems.size());
	}

	@Test
	public void validatePlanItemForCourse() throws InvalidParameterException,
			MissingParameterException, AlreadyExistsException,
			DoesNotExistException, OperationFailedException {

        String planId = "lp1";

        PlanItemInfo planItem = new PlanItemInfo();

        RichTextInfo desc = new RichTextInfo();
        String formattedDesc = "<span>My Comment</span>";
        String planDesc = "My Comment";
        desc.setFormatted(formattedDesc);
        desc.setPlain(planDesc);
        planItem.setDescr(desc);

        planItem.setLearningPlanId(planId);

//        String courseId = "c796aecc-7234-4482-993c-bf00b8088e84";
//        String courseType = CLUConstants.CLU_TYPE_CREDIT_COURSE;

        planItem.setRefObjectId("XX");
        planItem.setRefObjectType(CLUConstants.CLU_TYPE_CREDIT_COURSE);
        planItem.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        planItem.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);
        planItem.setCategory(AcademicPlanServiceConstants.ItemCategory.PLANNED);
		List<ValidationResultInfo> validationResultInfos;
        validationResultInfos = KsapFrameworkServiceLocator.getAcademicPlanService().validatePlanItem(
                "FULL_VALIDATION", planItem,
                KsapFrameworkServiceLocator.getContext().getContextInfo());
        assertTrue("validationResultsInfos should not be empty", !validationResultInfos.isEmpty());
        assertEquals("Could not find course with ID [XX].",validationResultInfos.get(0).getMessage());
	}

	@Test
	public void validatePlanItemForPlannedItem() throws Throwable {
		PlanItemInfo planItemInfo = new PlanItemInfo();
        planItemInfo.setLearningPlanId("lp1");
		planItemInfo.setRefObjectId("XX");
        planItemInfo.setRefObjectType(CLUConstants.CLU_TYPE_CREDIT_COURSE);
        planItemInfo.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);
        planItemInfo.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        planItemInfo.setCategory(AcademicPlanServiceConstants.ItemCategory.PLANNED);
		List<ValidationResultInfo> validationResultInfos = KsapFrameworkServiceLocator.getAcademicPlanService()
				.validatePlanItem("FULL_VALIDATION", planItemInfo,
						KsapFrameworkServiceLocator.getContext()
								.getContextInfo());
		assertEquals("Could not find course with ID [XX].",
				validationResultInfos.get(0).getMessage());
		assertEquals("refObjectId", validationResultInfos.get(0).getElement());
		assertEquals(
				"Plan Item category was ["+AcademicPlanServiceConstants.ItemCategory.PLANNED+"], " +
                        "but no plan terms were defined.",
				validationResultInfos.get(1).getMessage());
		assertEquals("category", validationResultInfos.get(1).getElement());
	}

	@Test
	public void validatePlanItemForBackupPlanItem()
			throws InvalidParameterException, MissingParameterException,
			AlreadyExistsException, DoesNotExistException,
			OperationFailedException {
		PlanItemInfo planItemInfo = new PlanItemInfo();
        planItemInfo.setLearningPlanId("lp1");
        planItemInfo.setRefObjectId("XX");
        planItemInfo.setRefObjectType(CLUConstants.CLU_TYPE_CREDIT_COURSE);
        planItemInfo.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_ACTIVE_STATE_KEY);
        planItemInfo.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_ITEM_TYPE);
        planItemInfo.setCategory(AcademicPlanServiceConstants.ItemCategory.BACKUP);
		List<ValidationResultInfo> validationResultInfos = KsapFrameworkServiceLocator
				.getAcademicPlanService()
				.validatePlanItem("FULL_VALIDATION", planItemInfo,
						KsapFrameworkServiceLocator.getContext()
								.getContextInfo());
		assertEquals("Could not find course with ID [XX].",
				validationResultInfos.get(0).getMessage());
		assertEquals("refObjectId", validationResultInfos.get(0).getElement());
		assertEquals(
				"Plan Item category was ["+ AcademicPlanServiceConstants.ItemCategory.BACKUP +"], " +
                        "but no plan terms were defined.",
				validationResultInfos.get(1).getMessage());
		assertEquals("category", validationResultInfos.get(1).getElement());
	}
}