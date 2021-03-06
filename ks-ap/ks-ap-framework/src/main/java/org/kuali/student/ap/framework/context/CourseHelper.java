package org.kuali.student.ap.framework.context;

import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.coursesearch.CourseSearchItem;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingDisplayInfo;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.course.infc.Course;

import java.util.List;

/**
 * Helper class that provides some convenience methods around Course interactions.
 */
public interface CourseHelper {

    /**
     * Pre-load the listed courses and terms (and related objects) and add them to a cache so that they are available for future calls
     * @param courseIds
     * @param termId
     */
	void frontLoad(List<String> courseIds, String... termId);

    /**
     * returns the courseInfo for the given courseId by verifying the courseId to
     * be a verifiedCourseId
     *
     * @param courseId
     * @return
     */
	CourseInfo getCourseInfo(String courseId);

    /**
     * Get the list of ActivityOfferingDisplayInfo objects for the given courseId and term
     * @param courseId
     * @param term
     * @return
     */
	List<ActivityOfferingDisplayInfo> getActivityOfferingDisplaysByCourseAndTerm(String courseId, String term);

    /**
     * Get the last/latest offered term in the past for a course
     * @param course
     * @return  the last offered term
     */
	Term getLastOfferedTermForCourse(Course course);


    /**
     * Get a list of term ids where the given course is scheduled
     * @param course
     * @return
     */
	List<String> getScheduledTermsForCourse(Course course);

    /**
     * Get a list of offerings for a list of courses in a given list of terms
     * @param courses - List of courses
     * @return List of all offerings for each course id that occurs during one of the listed terms
     */
    List<CourseOfferingInfo> getCourseOfferingsForCourses(List<CourseSearchItem> courses);

    /**
     * Get a list of offerings for a list of course ids in a given list of terms
     * @param courseIds - List of courses
     * @param terms - List of terms
     * @return List of all offerings for each course id that occurs during one of the listed terms
     */
    List<CourseOfferingInfo> getCourseOfferingsForCoursesAndTerms(List<String> courseIds, List<Term> terms);

    /**
     * Takes a courseId that can be either a version independent Id or a version
     * dependent Id and returns a version dependent Id. In case of being passed
     * in a version depend
     *
     * @param courseId
     * @return
     */
	String getCurrentVersionIdOfCourse(String courseId);

    /**
     * Takes a courseId that can be either a version independent Id or a version
     * dependent Id and returns a version dependent course. In case of being passed
     * in a version depend
     *
     * @param courseId
     * @return
     */
    Course getCurrentVersionOfCourse(String courseId);

    /**
     * returns the course code from given activityId
     * <p/>
     * eg: for activityId '2013:2:CHEM:152:A' course code CHEM 152 is returned
     *
     * @param activityId
     * @return
     */
	String getCourseCdFromActivityId(String activityId);

    /**
     * Get a list of Course objects that have a matching course code
     * @param courseCd Course code used to find all courses
     * @return List of Course objects
     */
	List<Course> getCoursesByCode(String courseCd);

    /**
     * Determines whether a course is in a specific term.
     *
     * @param term Term to search for the given course
     * @param course Course to find within the given term
     * @return True if found, false if not
     */
    boolean isCourseOffered(Term term, Course course);

    /**
     * Determine if the course is already bookmarked in the student's plan
     *
     * @param course - Course that is being displayed
     * @param planItems - The list of plan items for the course
     * @return True if the course is already bookmarked for the plan
     */
    boolean isCourseBookmarked(Course course, List<PlanItem> planItems);

    /**
     * Get the current version of the course by using it's version independent id
     * @param versionIndependentId
     * @return The current version of the CourseInfo
     */
    CourseInfo getCurrentVersionOfCourseByVersionIndependentId(String versionIndependentId);

    /**
     * Get all courseIds (clu IDs) for a particular version independent id
     * @param versionIndependentId
     * @return
     */
    List<String> getAllCourseIdsByVersionIndependentId(String versionIndependentId);

    /**
     * Validates that the course is able to be added to the learning plan
     *
     * @param course - Course to be added
     * @return null if valid, response message otherwise
     */
    public String validateCourseForAdd(Course course);
}
