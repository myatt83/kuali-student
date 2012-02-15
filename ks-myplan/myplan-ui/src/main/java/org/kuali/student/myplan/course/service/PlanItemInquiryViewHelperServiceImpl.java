package org.kuali.student.myplan.course.service;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.student.lum.course.service.CourseService;
import org.kuali.student.myplan.academicplan.infc.PlanItem;
import org.kuali.student.myplan.academicplan.service.AcademicPlanService;
import org.kuali.student.myplan.academicplan.service.AcademicPlanServiceConstants;
import org.kuali.student.myplan.course.dataobject.SavedCoursesItem;
import org.kuali.student.r2.common.dto.ContextInfo;

import javax.xml.namespace.QName;
import java.util.Map;


public class PlanItemInquiryViewHelperServiceImpl extends KualiInquirableImpl {
    private transient CourseService courseService;

    private transient AcademicPlanService academicPlanService;

    private transient CourseDetailsInquiryViewHelperServiceImpl courseDetailsInquiryService;

    public SavedCoursesItem retrieveDataObject(Map fieldValues) {
        return retrievePlanItemDetails((String) fieldValues.get("planItemId"));
    }

    public SavedCoursesItem retrievePlanItemDetails(String planItemId) {
        try {
            AcademicPlanService academicPlanService = getAcademicPlanService();

            Person user = GlobalVariables.getUserSession().getPerson();

            ContextInfo context = new ContextInfo();
            String studentID = user.getPrincipalId();

            String planTypeKey = AcademicPlanServiceConstants.LEARNING_PLAN_TYPE_PLAN;

            PlanItem planItem = academicPlanService.getPlanItem(planItemId, context);
            String courseID = planItem.getRefObjectId();

            SavedCoursesItem item = new SavedCoursesItem();
            item.setId(planItem.getId());
            item.setCourseDetails(getCourseDetailsInquiryService().retrieveCourseDetails(courseID));
            item.setDateAdded(planItem.getMeta().getCreateTime());

            return item;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AcademicPlanService getAcademicPlanService() {
        if (academicPlanService == null) {
            academicPlanService = (AcademicPlanService)
                    GlobalResourceLoader.getService(new QName(AcademicPlanServiceConstants.NAMESPACE,
                            AcademicPlanServiceConstants.SERVICE_NAME));
        }
        return academicPlanService;
    }

    public void setAcademicPlanService(AcademicPlanService academicPlanService) {
        this.academicPlanService = academicPlanService;
    }

    public synchronized CourseDetailsInquiryViewHelperServiceImpl getCourseDetailsInquiryService() {
        if (this.courseDetailsInquiryService == null) {
            this.courseDetailsInquiryService = new CourseDetailsInquiryViewHelperServiceImpl();
        }
        return courseDetailsInquiryService;
    }

    public void setCourseDetailsInquiryService(CourseDetailsInquiryViewHelperServiceImpl courseDetailsInquiryService) {
        this.courseDetailsInquiryService = courseDetailsInquiryService;
    }

}