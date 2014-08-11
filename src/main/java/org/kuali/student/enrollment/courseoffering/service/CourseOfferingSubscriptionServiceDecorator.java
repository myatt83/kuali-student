/**
 * Copyright 2014 The Kuali Foundation Licensed under the Educational Community License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */
package org.kuali.student.enrollment.courseoffering.service;

import org.kuali.student.enrollment.academicrecord.service.SubscriptionActionEnum;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DependentObjectsExistException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.exceptions.ReadOnlyException;
import org.kuali.student.r2.common.exceptions.VersionMismatchException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Kuali Student Team
 */
public class CourseOfferingSubscriptionServiceDecorator extends CourseOfferingServiceDecorator
        implements CourseOfferingSubscriptionService {

    private static class Selector {
        String courseOfferingId;
        String termId;
        String courseCode;
        String courseOfferingTypeKey;
        SubscriptionActionEnum action;
        CourseOfferingCallbackService callback;

        public Selector(SubscriptionActionEnum action, CourseOfferingCallbackService callback) {
            this.action = action;
            this.callback = callback;
        }
    }
    private final transient Map<String, Selector> callbacks
            = new LinkedHashMap<String, Selector>();

    @Override
    public String subscribeToCourseOfferings(
            SubscriptionActionEnum action,
            CourseOfferingCallbackService courseOfferingCallbackService,
            ContextInfo contextInfo) throws
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException {
        String id = UUID.randomUUID().toString();
        Selector selector = new Selector(action, courseOfferingCallbackService);
        callbacks.put(id, selector);
        return id;
    }

    @Override
    public String subscribeToCourseOfferingsByTerm(
            SubscriptionActionEnum action,
            String termId,
            CourseOfferingCallbackService courseOfferingCallbackService, ContextInfo contextInfo) throws
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException {
        String id = UUID.randomUUID().toString();
        Selector selector = new Selector(action, courseOfferingCallbackService);
        selector.termId = termId;
        callbacks.put(id, selector);
        return id;
    }

    @Override
    public String subscribeToCourseOfferingsByCourse(
            SubscriptionActionEnum action,
            String courseCode,
            CourseOfferingCallbackService courseOfferingCallbackService, ContextInfo contextInfo) throws
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException {
        String id = UUID.randomUUID().toString();
        Selector selector = new Selector(action, courseOfferingCallbackService);
        selector.courseCode = courseCode;
        callbacks.put(id, selector);
        return id;
    }

    @Override
    public String subscribeToCourseOfferingsByType(
            SubscriptionActionEnum action,
            String courseOfferingTypeKey,
            CourseOfferingCallbackService courseOfferingCallbackService, ContextInfo contextInfo) throws
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException {
        String id = UUID.randomUUID().toString();
        Selector selector = new Selector(action, courseOfferingCallbackService);
        selector.courseOfferingTypeKey = courseOfferingTypeKey;
        callbacks.put(id, selector);
        return id;
    }

    @Override
    public StatusInfo removeSubscription(String subscriptionId, ContextInfo contextInfo) throws DoesNotExistException,
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException {
        this.callbacks.remove(subscriptionId);
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);
        return status;
    }

    protected void fireSelectedCallbacks(Selector target, String id, ContextInfo contextInfo) {
        // consider running this in a different thread so it does not block the main call
        for (Selector selector : this.callbacks.values()) {
            if (matches(selector, target)) {
                callCallback(selector.callback, target.action, id, contextInfo);
            }
        }
    }

    protected void callCallback(CourseOfferingCallbackService callback,
            SubscriptionActionEnum action,
            String id,
            ContextInfo contextInfo) {
        switch (action) {
            case CREATE:
                callback.newCourseOfferings(Arrays.asList(id), contextInfo);
                break;
            case UPDATE:
                callback.updateCourseOfferings(Arrays.asList(id), contextInfo);
                break;
            case DELETE:
                callback.deleteCourseOfferings(Arrays.asList(id), contextInfo);
                break;
        }
    }

    protected boolean matches(Selector selector, Selector target) {
        if (!matches(selector.action, target.action)) {
            return true;
        }
        if (selector.termId != null) {
            if (!selector.termId.equals(target.termId)) {
                return false;
            }
        }
        if (selector.courseCode != null) {
            if (!selector.courseCode.equals(target.courseCode)) {
                return false;
            }
        }
        if (selector.courseOfferingTypeKey != null) {
            if (!selector.courseOfferingTypeKey.equals(target.courseOfferingTypeKey)) {
                return false;
            }
        }
        if (selector.courseOfferingId != null) {
            if (!selector.courseOfferingId.equals(target.courseOfferingId)) {
                return false;
            }
        }
        return true;
    }

    protected boolean matches(SubscriptionActionEnum selector, SubscriptionActionEnum target) {
        if (selector.equals(target)) {
            return true;
        }
        if (selector.equals(SubscriptionActionEnum.ANY)) {
            return true;
        }
        return false;
    }

    @Override
    public CourseOfferingInfo createCourseOffering(String courseId, String termId, String courseOfferingTypeKey,
                                                   CourseOfferingInfo courseOfferingInfo,
                                                   List<String> optionKeys, ContextInfo contextInfo)
            throws DoesNotExistException,
            DataValidationErrorException,
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException,
            ReadOnlyException {

        CourseOfferingInfo info = this.getNextDecorator().createCourseOffering(courseId, termId,
                courseOfferingTypeKey,
                courseOfferingInfo,
                optionKeys, contextInfo);
        String id = info.getId();
        Selector target = new Selector(SubscriptionActionEnum.CREATE, null);
        target.courseCode = info.getCourseCode();
        target.courseOfferingId = id;
        target.courseOfferingTypeKey = info.getTypeKey();
        target.termId = info.getTermId();
        this.fireSelectedCallbacks(target, id, contextInfo);
        return info;
    }

    @Override
    public CourseOfferingInfo updateCourseOffering(String courseOfferingId, CourseOfferingInfo courseOfferingInfo,
                                                   ContextInfo contextInfo)
            throws DataValidationErrorException,
            DoesNotExistException,
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException,
            ReadOnlyException,
            VersionMismatchException {

        CourseOfferingInfo info = this.getNextDecorator().updateCourseOffering(courseOfferingId,
                courseOfferingInfo,
                contextInfo);
        String id = info.getId();
        Selector target = new Selector(SubscriptionActionEnum.UPDATE, null);
        target.courseCode = info.getCourseCode();
        target.courseOfferingId = id;
        target.courseOfferingTypeKey = info.getTypeKey();
        target.termId = info.getTermId();
        this.fireSelectedCallbacks(target, id, contextInfo);
        return info;
    }

    @Override
    public StatusInfo deleteCourseOffering(String courseOfferingId, ContextInfo contextInfo)
            throws DoesNotExistException,
            InvalidParameterException,
            MissingParameterException,
            OperationFailedException,
            PermissionDeniedException,
            DependentObjectsExistException {

        CourseOfferingInfo info = this.getNextDecorator().getCourseOffering(courseOfferingId, contextInfo);
        StatusInfo status = this.getNextDecorator().deleteCourseOffering(courseOfferingId, contextInfo);
        Selector target = new Selector(SubscriptionActionEnum.UPDATE, null);
        String id = info.getId();
        target.courseCode = info.getCourseCode();
        target.courseOfferingId = id;
        target.courseOfferingTypeKey = info.getTypeKey();
        target.termId = info.getTermId();
        this.fireSelectedCallbacks(target, id, contextInfo);
        return status;
    }

}
