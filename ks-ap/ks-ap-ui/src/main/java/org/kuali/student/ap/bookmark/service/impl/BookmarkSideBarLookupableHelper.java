package org.kuali.student.ap.bookmark.service.impl;

import org.kuali.rice.krad.lookup.LookupForm;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.student.ap.academicplan.constants.AcademicPlanServiceConstants;
import org.kuali.student.ap.academicplan.dto.LearningPlanInfo;
import org.kuali.student.ap.academicplan.dto.PlanItemInfo;
import org.kuali.student.ap.academicplan.service.AcademicPlanService;
import org.kuali.student.ap.bookmark.dto.BookmarkSideBarWrapper;
import org.kuali.student.ap.coursesearch.CreditsFormatter;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.PlanConstants;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.lum.course.infc.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BookmarkSideBarLookupableHelper extends
        LookupableImpl {
    private static final long serialVersionUID = -8872944782230428634L;

    @Override
    public List<BookmarkSideBarWrapper> performSearch(
            LookupForm lookupForm, Map<String, String> searchCriteria,
            boolean bounded) {
        String studentId = KsapFrameworkServiceLocator.getUserSessionHelper()
                .getStudentId();
        try {
            List<BookmarkSideBarWrapper> plannedCoursesList = getPlanItemsWishList(studentId);
            Collections.sort(plannedCoursesList);
            return plannedCoursesList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected List<BookmarkSideBarWrapper> getPlanItemsWishList(String studentId)
            throws InvalidParameterException, MissingParameterException, DoesNotExistException,
                   OperationFailedException, PermissionDeniedException {
        AcademicPlanServiceConstants.ItemCategory planItemCategory = AcademicPlanServiceConstants.ItemCategory.WISHLIST;
        List<BookmarkSideBarWrapper> bookmarkList = new ArrayList<BookmarkSideBarWrapper>();

        AcademicPlanService academicPlanService = KsapFrameworkServiceLocator.getAcademicPlanService();
        String planTypeKey = PlanConstants.LEARNING_PLAN_TYPE_PLAN;

        List<LearningPlanInfo> learningPlanList = academicPlanService.getLearningPlansForStudentByType(studentId,
                planTypeKey, KsapFrameworkServiceLocator.getContext().getContextInfo());
        Map<String, List<PlanItemInfo>> itemsByPlan = new java.util.HashMap<String, List<PlanItemInfo>>(
                learningPlanList.size());
        Set<String> courseIds = new java.util.LinkedHashSet<String>();
        for (LearningPlanInfo learningPlan : learningPlanList) {
            String learningPlanID = learningPlan.getId();
            List<PlanItemInfo> planItems = academicPlanService.getPlanItemsInPlanByCategory(learningPlanID,AcademicPlanServiceConstants.ItemCategory.WISHLIST,
                    KsapFrameworkServiceLocator.getContext().getContextInfo());
            itemsByPlan.put(learningPlanID, planItems);
            if (planItems != null)
                for (PlanItemInfo planItem : planItems)
                    if (PlanConstants.COURSE_TYPE.equals(planItem.getRefObjectType()))
                        courseIds.add(KsapFrameworkServiceLocator.getCourseHelper()
                                .getCurrentVersionOfCourseByVersionIndependentId(planItem.getRefObjectId())
                                .getId());
        }
        KsapFrameworkServiceLocator.getCourseHelper().frontLoad(new java.util.ArrayList<String>(courseIds));

        for (Map.Entry<String, List<PlanItemInfo>> entry : itemsByPlan.entrySet()){
            for (PlanItemInfo planItem : entry.getValue()) {
                BookmarkSideBarWrapper bookmark = new BookmarkSideBarWrapper();
                bookmark.setLearningPlanId(entry.getKey());
                bookmark.setPlanItemId(planItem.getId());
                Course course = KsapFrameworkServiceLocator.getCourseHelper()
                        .getCurrentVersionOfCourseByVersionIndependentId(planItem.getRefObjectId());
                bookmark.setCourseId(course.getId());
                bookmark.setCourseCd(course.getCode());
                bookmark.setCourseTitle(course.getCourseTitle());
                bookmark.setDateAdded(planItem.getMeta().getCreateTime());
                if(planItem.getCredits()!=null) bookmark.setCredits(planItem.getCredits().toString());
                else bookmark.setCredits(CreditsFormatter.formatCredits(course));

                bookmark.setUniqueId(UUID.randomUUID().toString());

                bookmarkList.add(bookmark);

            }
        }
        Collections.sort(bookmarkList);
        return bookmarkList;
    }
}
