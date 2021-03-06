/**
 * Copyright 2012 The Kuali Foundation Licensed under the
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
 *
 * Created by Daniel on 3/28/12
 */
package org.kuali.student.enrollment.class2.appointment.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.student.enrollment.class2.acal.util.AcalCommonUtils;
import org.kuali.student.enrollment.class2.appointment.dto.AppointmentWindowWrapper;
import org.kuali.student.enrollment.class2.appointment.form.RegistrationWindowsManagementForm;
import org.kuali.student.enrollment.class2.appointment.service.AppointmentViewHelperService;
import org.kuali.student.enrollment.class2.appointment.util.AppointmentConstants;
import org.kuali.student.enrollment.class2.appointment.util.AppointmentSlotRuleTypeConversion;
import org.kuali.student.enrollment.class2.population.util.PopulationConstants;
import org.kuali.student.mock.utilities.TestHelper;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.LocaleInfo;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.exceptions.ReadOnlyException;
import org.kuali.student.r2.common.exceptions.VersionMismatchException;
import org.kuali.student.r2.common.util.date.DateFormatters;
import org.kuali.student.r2.core.acal.dto.KeyDateInfo;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.r2.core.acal.service.AcademicCalendarService;
import org.kuali.student.r2.core.appointment.constants.AppointmentServiceConstants;
import org.kuali.student.r2.core.appointment.dto.AppointmentWindowInfo;
import org.kuali.student.r2.core.appointment.service.AppointmentService;
import org.kuali.student.r2.core.class1.type.dto.TypeTypeRelationInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.core.constants.AtpServiceConstants;
import org.kuali.student.r2.core.constants.PopulationServiceConstants;
import org.kuali.student.r2.core.constants.TypeServiceConstants;
import org.kuali.student.r2.core.population.dto.PopulationInfo;
import org.kuali.student.r2.core.population.service.PopulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class provides the KS default implementation of the AppointmentViewHelper Service
 *
 * @author Kuali Student Team
 */
public class AppointmentViewHelperServiceImpl extends ViewHelperServiceImpl implements AppointmentViewHelperService {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentViewHelperServiceImpl.class);

    private transient AcademicCalendarService academicCalendarService;
    private transient TypeService typeService;
    private transient AppointmentService appointmentService;
    private transient PopulationService populationService;

    @Override
    public void searchForTerm(String typeKey, String year, RegistrationWindowsManagementForm form) throws Exception {

        //Parse the year to a date and the next year's date to compare against the startTerm
        Date minBoundDate = DateFormatters.DEFULT_YEAR_FORMATTER.parse(year);
        Date maxBoundDate = DateFormatters.DEFULT_YEAR_FORMATTER.parse(Integer.toString(Integer.parseInt(year) + 1));

        //Build up a term search criteria
        QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
        qbcBuilder.setPredicates(PredicateFactory.and(
                PredicateFactory.equal("atpType", typeKey),
                PredicateFactory.greaterThanOrEqual("startDate", minBoundDate),
                PredicateFactory.lessThan("startDate", maxBoundDate)));

        QueryByCriteria criteria = qbcBuilder.build();

        //Perform Term Search with Service Call
        AcademicCalendarService academicCalendarService = getAcalService();
        List<TermInfo> terms = academicCalendarService.searchForTerms(criteria, null);

        //Check for exceptions
        if (terms == null || terms.isEmpty()) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, AppointmentConstants.APPOINTMENT_MSG_ERROR_NO_TERMS_FOUND);
            return; //Nothing found
        }

        if (terms.size() > 1) {
            LOG.error("Too many terms!");
        }

        int firstTermInfo = 0;

        TermInfo term = terms.get(firstTermInfo);

        //Populate the result form
        form.setTermInfo(term);

        //Get the milestones and filter out anything that is not registration period
        List<KeyDateInfo> keyDates = academicCalendarService.getKeyDatesForTerm(term.getId(), null);
        if (keyDates != null) {

            //Get the valid period types
            List<TypeTypeRelationInfo> milestoneTypeRelations = getTypeService().getTypeTypeRelationsByOwnerAndType(AtpServiceConstants.MILESTONE_REGISTRATION_PERIOD_GROUP_TYPE_KEY, "kuali.type.type.relation.type.group", new ContextInfo());
            List<String> validMilestoneTypes = new ArrayList<String>();
            for (TypeTypeRelationInfo milestoneTypeRelation : milestoneTypeRelations) {
                validMilestoneTypes.add(milestoneTypeRelation.getRelatedTypeKey());
            }

            //Add in only valid milestones that are registration periods
            List<KeyDateInfo> periodMilestones = new ArrayList<KeyDateInfo>();
            for (KeyDateInfo keyDate : keyDates) {
                if (validMilestoneTypes.contains(keyDate.getTypeKey())) {
                    periodMilestones.add(keyDate);
                }
            }
            form.setPeriodMilestones(periodMilestones);

        }

        //Check if there are no periods (might want to handle this somewhere else and surface to the user)
        if (form.getPeriodMilestones() == null || form.getPeriodMilestones().isEmpty()) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, AppointmentConstants.APPOINTMENT_MSG_ERROR_NO_REG_PERIODS_FOR_TERM);
            //GlobalVariables.getMessageMap().putErrorForSectionId("KS-RegistrationWindowsManagement-SelectTermPage", AppointmentConstants.APPOINTMENT_MSG_ERROR_NO_REG_PERIODS_FOR_TERM);
            //GlobalVariables.getMessageMap().putError("termType", AppointmentConstants.APPOINTMENT_MSG_ERROR_NO_REG_PERIODS_FOR_TERM);
        }

    }

    public void loadTermAndPeriods(String termId, RegistrationWindowsManagementForm form) throws Exception {
        ContextInfo context = TestHelper.getContext1();
        TermInfo term = getAcalService().getTerm(termId, context);

        if (term.getId() != null && !term.getId().isEmpty()) {
            form.setTermInfo(term);
            loadPeriods(termId, form);
        }

    }

    public void loadPeriods(String termId, RegistrationWindowsManagementForm form) throws Exception {
        ContextInfo context = TestHelper.getContext1();
        List<KeyDateInfo> periodMilestones = new ArrayList<KeyDateInfo>();
        List<KeyDateInfo> keyDateInfoList = getAcalService().getKeyDatesForTerm(termId, context);
        List<TypeTypeRelationInfo> relations = getTypeService().getTypeTypeRelationsByOwnerAndType(AtpServiceConstants.MILESTONE_REGISTRATION_PERIOD_GROUP_TYPE_KEY, "kuali.type.type.relation.type.group", context);
        for (KeyDateInfo keyDateInfo : keyDateInfoList) {
            for (TypeTypeRelationInfo relationInfo : relations) {
                String relatedTypeKey = relationInfo.getRelatedTypeKey();
                if (keyDateInfo.getTypeKey().equals(relatedTypeKey)) {
                    periodMilestones.add(keyDateInfo);
                    break;
                }
            }
        }

        form.setPeriodMilestones(periodMilestones);
    }

    public boolean validateApptWidnow(AppointmentWindowWrapper apptWindow) {
        return validateApptWidnow(apptWindow, true);
    }

    public boolean validateApptWidnow(AppointmentWindowWrapper apptWindow, boolean validateForUniqueness) {
        boolean isValid = true;
        //  1) a window end date is not required for a One-Slot or Max Number Slot Allocation Method/Window Type
        //  2) a window end date is required for uniform
        String windowTypeKey = apptWindow.getWindowTypeKey();

        // Check to make sure the Window name is not duplicated with in the period
        if (validateForUniqueness) {
            try {
                QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
                qbcBuilder.setPredicates(PredicateFactory.and(PredicateFactory.equal("periodMilestoneId", apptWindow.getPeriodKey()), PredicateFactory.equal("name", apptWindow.getWindowName())));
                QueryByCriteria criteria = qbcBuilder.build();
                if (getAppointmentService().searchForAppointmentWindows(criteria, new ContextInfo()).size() > 0) {
                    GlobalVariables.getMessageMap().putError("newCollectionLines['appointmentWindows'].appointmentWindowInfo.name",
                            AppointmentConstants.APPOINTMENT_MSG_ERROR_DUPLICATE_WINDOW_FOR_PERIOD);
                    isValid = false;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to search appointment windows by criteria", e);
            }
        }

        if (AppointmentServiceConstants.APPOINTMENT_WINDOW_TYPE_SLOTTED_UNIFORM_KEY.equals(windowTypeKey)) {
            if (apptWindow.getEndDate() == null) {
                GlobalVariables.getMessageMap().putError("newCollectionLines['appointmentWindows'].endDate",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_END_DATE_REQUIRED_FOR_UNIFORM);
                isValid = false;
            }
            if (apptWindow.getEndTime() == null) {
                GlobalVariables.getMessageMap().putError("newCollectionLines['appointmentWindows'].endTime",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_REQUIRED_FOR_UNIFORM);
                isValid = false;
            }
            if (apptWindow.getEndTime().isEmpty()) {
                GlobalVariables.getMessageMap().putError("newCollectionLines['appointmentWindows'].endTimeAmPm",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_REQUIRED_FOR_UNIFORM);
                isValid = false;
            }
        }


        if (apptWindow.getStartDate() == null || StringUtils.isEmpty(apptWindow.getStartTime()) || StringUtils.isEmpty(apptWindow.getStartTimeAmPm())) {
            if (apptWindow.getStartDate() == null) {
                GlobalVariables.getMessageMap().putError("appointmentWindows['appointmentWindows'].startDate",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_START_DATE_REQUIRED_FIELD);
            } else if (apptWindow.getStartDate() == null) {
                GlobalVariables.getMessageMap().putError("appointmentWindows['appointmentWindows'].startTime",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_START_TIME_REQUIRED_FIELD);
            } else {
                GlobalVariables.getMessageMap().putError("appointmentWindows['appointmentWindows'].startTimeAmPm",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_START_TIME_AM_PM_REQUIRED_FIELD);
            }
            isValid = false;
        } else {
            // 4) when end date is not null, start/end date should be in the date range of the selected period
            // Aslo check to make sure the end date is not before the start date
            String periodId = apptWindow.getPeriodKey();
            try {
                KeyDateInfo period = getAcalService().getKeyDate(periodId, getContextInfo());
                Date periodStartDate = DateFormatters.DEFAULT_DATE_FORMATTER.parse(DateFormatters.DEFAULT_DATE_FORMATTER.format(period.getStartDate()));
                Date periodEndDate = DateFormatters.DEFAULT_DATE_FORMATTER.parse(DateFormatters.DEFAULT_DATE_FORMATTER.format(period.getEndDate()));

                if (apptWindow.getEndDate() != null && apptWindow.getEndDate().before(apptWindow.getStartDate())) {
                    GlobalVariables.getMessageMap().putError("appointmentWindows['appointmentWindows'].endDate",
                            AppointmentConstants.APPOINTMENT_MSG_ERROR_END_DATE_IS_BEFORE_START_DATE);
                    isValid = false;
                }
                if (periodStartDate.after(apptWindow.getStartDate()) || periodEndDate.before(apptWindow.getStartDate())) {
                    GlobalVariables.getMessageMap().putError("newCollectionLines['appointmentWindows'].startDate",
                            AppointmentConstants.APPOINTMENT_MSG_ERROR_START_DATE_OUT_OF_RANGE);
                    isValid = false;
                }
                if (apptWindow.getEndDate() != null && !apptWindow.getEndDate().toString().isEmpty()) {
                    if (periodStartDate.after(apptWindow.getEndDate()) || periodEndDate.before(apptWindow.getEndDate())) {
                        GlobalVariables.getMessageMap().putError("newCollectionLines['appointmentWindows'].endDate",
                                AppointmentConstants.APPOINTMENT_MSG_ERROR_END_DATE_OUT_OF_RANGE);
                        isValid = false;
                    }
                }

                // 5) when end date is not null, end time cannot be before the start time
                //if (apptWindow.getEndDate() != null && apptWindow.getEndDate().equals(apptWindow.getStartDate()) && apptWindow.getStartTimeAmPm().equals(apptWindow.getEndTimeAmPm())) {
                if (apptWindow.getEndDate() != null && apptWindow.getEndDate().equals(apptWindow.getStartDate()) && ((apptWindow.getStartTimeAmPm().equals(apptWindow.getEndTimeAmPm()) || (apptWindow.getEndTimeAmPm().equalsIgnoreCase("am") && apptWindow.getStartTimeAmPm().equalsIgnoreCase("pm")) ))) {
                    Date start = DateFormatters.HOUR_MINUTE_TIME_FORMATTER.parse(apptWindow.getStartTime());
                    Date end = DateFormatters.HOUR_MINUTE_TIME_FORMATTER.parse(apptWindow.getEndTime());
                    if (end.before(start)) {
                        GlobalVariables.getMessageMap().putError("appointmentWindows['appointmentWindows'].endTime",
                                AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_BEFORE_START_TIME);
                        isValid = false;
                    }

                    // 6) when end date is not null, end time AM-PM cannot be before the start time
                    if (apptWindow.getEndTimeAmPm().equalsIgnoreCase("am") && apptWindow.getStartTimeAmPm().equalsIgnoreCase("pm")) {
                        GlobalVariables.getMessageMap().putError("newCollectionLines['appointmentWindows'].endTimeAmPm",
                                AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_AM_PM_BEFORE_START_TIME_AM_PM);
                        isValid = false;
                    }
                }
            } catch (Exception e) {
                LOG.error("Fail to find periods for a selected term.", e);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_MESSAGES, AppointmentConstants.APPOINTMENT_MSG_ERROR_NO_REG_PERIODS_FOR_TERM);
                isValid = false;
            }
        }

        try {
            Map<String, String> fieldValues = new HashMap<String, String>();
            fieldValues.put("name", apptWindow.getAssignedPopulationName());
            QueryByCriteria qbc = buildQueryByCriteria(fieldValues);
            List<PopulationInfo> populationInfoList = getPopulationService().searchForPopulations(qbc, getContextInfo());

            if (populationInfoList == null || populationInfoList.isEmpty()) {
                GlobalVariables.getMessageMap().putErrorForSectionId("addRegistrationWindowCollection", PopulationConstants.POPULATION_MSG_ERROR_POPULATION_NOT_FOUND, apptWindow.getAssignedPopulationName());
                isValid = false;
            } else {
                int firstPopulationInfo = 0;
                apptWindow.setAssignedPopulationName(populationInfoList.get(firstPopulationInfo).getName());
                apptWindow.getAppointmentWindowInfo().setAssignedPopulationId(populationInfoList.get(firstPopulationInfo).getId());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isValid;
    }

    public boolean validateApptWidnow(AppointmentWindowWrapper apptWindow, int windowIndex) {
        boolean isValid = true;
        //  1) a window end date is not required for a One-Slot or Max Number Slot Allocation Method/Window Type
        //  2) a window end date is required for uniform
        String windowTypeKey = apptWindow.getWindowTypeKey();

        if (AppointmentServiceConstants.APPOINTMENT_WINDOW_TYPE_SLOTTED_UNIFORM_KEY.equals(windowTypeKey)) {
            if (apptWindow.getEndDate() == null) {
                GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].endDate",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_END_DATE_REQUIRED_FOR_UNIFORM);
                isValid = false;
            }
            if (apptWindow.getEndTime() == null) {
                GlobalVariables.getMessageMap().putError("appointmentWindows[windowIndex].endTime",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_REQUIRED_FOR_UNIFORM);
                isValid = false;
            }
            if (apptWindow.getEndTime().isEmpty()) {
                GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].endTimeAmPm",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_REQUIRED_FOR_UNIFORM);
                isValid = false;
            }
        }

        // 3) start date, time and AmPm are a required field
        if (apptWindow.getStartDate() == null || StringUtils.isEmpty(apptWindow.getStartTime()) || StringUtils.isEmpty(apptWindow.getStartTimeAmPm())) {
            if (apptWindow.getStartDate() == null) {
                GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].startDate",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_START_DATE_REQUIRED_FIELD);
            } else if (apptWindow.getStartDate() == null) {
                GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].startTime",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_START_TIME_REQUIRED_FIELD);
            } else {
                GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].startTimeAmPm",
                        AppointmentConstants.APPOINTMENT_MSG_ERROR_START_TIME_AM_PM_REQUIRED_FIELD);
            }
            isValid = false;
        } else {
            // 4) when end date is not null, start/end date should be in the date range of the selected period
            // Aslo check to make sure the end date is not before the start date
            String periodId = apptWindow.getPeriodKey();
            try {
                KeyDateInfo period = getAcalService().getKeyDate(periodId, getContextInfo());
                if (apptWindow.getEndDate() != null && apptWindow.getEndDate().before(apptWindow.getStartDate())) {
                    GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].endDate",
                            AppointmentConstants.APPOINTMENT_MSG_ERROR_END_DATE_IS_BEFORE_START_DATE);
                    isValid = false;
                }
                if (period.getStartDate().after(apptWindow.getStartDate()) || period.getEndDate().before(apptWindow.getStartDate())) {
                    GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].startDate",
                            AppointmentConstants.APPOINTMENT_MSG_ERROR_START_DATE_OUT_OF_RANGE);
                    isValid = false;
                }
                if (apptWindow.getEndDate() != null && !apptWindow.getEndDate().toString().isEmpty()) {
                    if (period.getStartDate().after(apptWindow.getEndDate()) || period.getEndDate().before(apptWindow.getEndDate())) {
                        GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].endDate",
                                AppointmentConstants.APPOINTMENT_MSG_ERROR_END_DATE_OUT_OF_RANGE);
                        isValid = false;
                    }
                }

                // 5) when end date is not null, end time cannot be before the start time
                //if (apptWindow.getEndDate() != null && apptWindow.getEndDate().equals(apptWindow.getStartDate()) && apptWindow.getStartTimeAmPm().equals(apptWindow.getEndTimeAmPm())) {
                if (apptWindow.getEndDate() != null && apptWindow.getEndDate().equals(apptWindow.getStartDate()) && ((apptWindow.getStartTimeAmPm().equals(apptWindow.getEndTimeAmPm()) || (apptWindow.getEndTimeAmPm().equalsIgnoreCase("am") && apptWindow.getStartTimeAmPm().equalsIgnoreCase("pm")) ))) {
                    Date start = DateFormatters.HOUR_MINUTE_TIME_FORMATTER.parse(apptWindow.getStartTime());
                    Date end = DateFormatters.HOUR_MINUTE_TIME_FORMATTER.parse(apptWindow.getEndTime());
                    if (end.before(start)) {
                        GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].endTime",
                                AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_BEFORE_START_TIME);
                        isValid = false;
                    }

                    // 6) when end date is not null, end time AM-PM cannot be before the start time
                    if (apptWindow.getEndTimeAmPm().equalsIgnoreCase("am") && apptWindow.getStartTimeAmPm().equalsIgnoreCase("pm")) {
                        GlobalVariables.getMessageMap().putError("appointmentWindows[" + windowIndex + "].endTimeAmPm",
                                AppointmentConstants.APPOINTMENT_MSG_ERROR_END_TIME_AM_PM_BEFORE_START_TIME_AM_PM);
                        isValid = false;
                    }
                }

            } catch (Exception e) {
                LOG.error("Fail to find periods for a selected term.", e);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_MESSAGES, AppointmentConstants.APPOINTMENT_MSG_ERROR_NO_REG_PERIODS_FOR_TERM);
                isValid = false;
            }
        }

        try {
            Map<String, String> fieldValues = new HashMap<String, String>();
            fieldValues.put("name", apptWindow.getAssignedPopulationName());
            QueryByCriteria qbc = buildQueryByCriteria(fieldValues);
            List<PopulationInfo> populationInfoList = getPopulationService().searchForPopulations(qbc, getContextInfo());

            if (populationInfoList == null || populationInfoList.isEmpty()) {
                GlobalVariables.getMessageMap().putErrorForSectionId("addRegistrationWindowCollection", PopulationConstants.POPULATION_MSG_ERROR_POPULATION_NOT_FOUND, apptWindow.getAssignedPopulationName());
                isValid = false;
            } else {
                int firstPopulationInfo = 0;
                apptWindow.setAssignedPopulationName(populationInfoList.get(firstPopulationInfo).getName());
                apptWindow.getAppointmentWindowInfo().setAssignedPopulationId(populationInfoList.get(firstPopulationInfo).getId());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isValid;
    }

    private QueryByCriteria buildQueryByCriteria(Map<String, String> fieldValues) {
        String populationName = fieldValues.get("name");

        List<Predicate> predicates = new ArrayList<Predicate>();
        if (StringUtils.isNotBlank(populationName)) {
            predicates.add(PredicateFactory.equalIgnoreCase("name", populationName));
            predicates.add(PredicateFactory.and(PredicateFactory.equal("populationState", PopulationServiceConstants.POPULATION_ACTIVE_STATE_KEY)));
        }

        QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
        qbcBuilder.setPredicates(predicates.toArray(new Predicate[predicates.size()]));

        return qbcBuilder.build();
    }

    public boolean saveApptWindow(AppointmentWindowWrapper appointmentWindowWrapper) throws InvalidParameterException, DataValidationErrorException, MissingParameterException, DoesNotExistException, ReadOnlyException, PermissionDeniedException, OperationFailedException, VersionMismatchException {
        boolean isSave = true;
        //Copy the form data from the wrapper to the bean.
        AppointmentWindowInfo appointmentWindowInfo = appointmentWindowWrapper.getAppointmentWindowInfo();
        appointmentWindowInfo.setTypeKey(appointmentWindowWrapper.getWindowTypeKey());
        appointmentWindowInfo.setPeriodMilestoneId(appointmentWindowWrapper.getPeriodKey());
        appointmentWindowInfo.setStartDate(AcalCommonUtils.getDateWithTime(appointmentWindowWrapper.getStartDate(), appointmentWindowWrapper.getStartTime(), appointmentWindowWrapper.getStartTimeAmPm()));
        appointmentWindowInfo.setEndDate(AcalCommonUtils.getDateWithTime(appointmentWindowWrapper.getEndDate(), appointmentWindowWrapper.getEndTime(), appointmentWindowWrapper.getEndTimeAmPm()));

        //TODO Default to some value if nothing is entered(Service team needs to make up some real types or make not nullable)
        if (appointmentWindowInfo.getAssignedOrderTypeKey() == null || appointmentWindowInfo.getAssignedOrderTypeKey().isEmpty()) {
            appointmentWindowInfo.setAssignedOrderTypeKey("DUMMY_ID");
        }

        //Default to single slot type if nothing is entered
        if (appointmentWindowInfo.getTypeKey() == null || appointmentWindowInfo.getTypeKey().isEmpty()) {
            appointmentWindowInfo.setTypeKey(AppointmentServiceConstants.APPOINTMENT_WINDOW_TYPE_ONE_SLOT_KEY);
        }

        if (appointmentWindowInfo.getId() == null || appointmentWindowInfo.getId().isEmpty()) {
            //Default the state to active
            appointmentWindowInfo.setStateKey(AppointmentServiceConstants.APPOINTMENT_WINDOW_STATE_DRAFT_KEY);

            //Converting appointment rule type code to AppointmentSlotRuleInfo object when apptWindowInfo..getTypeKey != AppointmentServiceConstants.APPOINTMENT_WINDOW_TYPE_ONE_SLOT_KEY
            if (!AppointmentServiceConstants.APPOINTMENT_WINDOW_TYPE_ONE_SLOT_KEY.equals(appointmentWindowInfo.getTypeKey())) {
                appointmentWindowInfo.setSlotRule(AppointmentSlotRuleTypeConversion.convToAppointmentSlotRuleInfo(appointmentWindowWrapper.getSlotRuleEnumType()));
            }
            //appointmentWindowInfo.getSlotRule().setWeekdays(new ArrayList<Integer>());
            //appointmentWindowInfo.getSlotRule().getWeekdays().add(1);
            appointmentWindowInfo = getAppointmentService().createAppointmentWindow(appointmentWindowInfo.getTypeKey(), appointmentWindowInfo, new ContextInfo());
        } else {
            appointmentWindowInfo = getAppointmentService().updateAppointmentWindow(appointmentWindowInfo.getId(), appointmentWindowInfo, new ContextInfo());
        }

        //Reset the windowInfo from the service's returned value
        appointmentWindowWrapper.setAppointmentWindowInfo(appointmentWindowInfo);
        appointmentWindowWrapper.setId(appointmentWindowInfo.getId());
        appointmentWindowWrapper.setWindowName(appointmentWindowInfo.getName());

        return isSave;

    }

    public boolean saveWindows(RegistrationWindowsManagementForm form) throws InvalidParameterException, DataValidationErrorException, MissingParameterException, DoesNotExistException, ReadOnlyException, PermissionDeniedException, OperationFailedException, VersionMismatchException {
        boolean isApptWindowSaved = true;
        boolean allWindowsSaved = true;
        if (form.getAppointmentWindows() != null) {
            int windowIndex = 0;
            for (AppointmentWindowWrapper appointmentWindowWrapper : form.getAppointmentWindows()) {
                boolean isValid = validateApptWidnow(appointmentWindowWrapper, windowIndex);
                if (isValid) {
                    isApptWindowSaved = saveApptWindow(appointmentWindowWrapper);
                    if (!isApptWindowSaved)
                        allWindowsSaved = isApptWindowSaved;
                }
                windowIndex++;
            }
            //Add a success message
            if (isApptWindowSaved)
                GlobalVariables.getMessageMap().addGrowlMessage("", AppointmentConstants.APPOINTMENT_MSG_INFO_SAVED);
        }
        return allWindowsSaved;
    }

    @Override
    public void processBeforeAddLine(ViewModel model, Object addLine, String collectionId, String collectionPath) {
        if (addLine instanceof AppointmentWindowWrapper) {
            RegistrationWindowsManagementForm form = (RegistrationWindowsManagementForm) model;
            List<KeyDateInfo> periodMilestones = form.getPeriodMilestones();
            String periodKey = ((AppointmentWindowWrapper) addLine).getPeriodKey();
            for (KeyDateInfo period : periodMilestones) {
                if (periodKey.equals(period.getId())) {
                    if (period.getName() != null && !period.getName().isEmpty()) {
                        ((AppointmentWindowWrapper) addLine).setPeriodName(period.getName());
                    } else {
                        ((AppointmentWindowWrapper) addLine).setPeriodName(periodKey);
                    }
                    break;
                }
            }
            String windowName = ((AppointmentWindowWrapper) addLine).getAppointmentWindowInfo().getName();
            ((AppointmentWindowWrapper) addLine).setWindowName(windowName);
        }
    }

    @Override
    protected boolean performAddLineValidation(ViewModel model, Object newLine, String collectionId, String collectionPath) {
        boolean isValid = true;
        if (newLine instanceof AppointmentWindowWrapper) {
            AppointmentWindowWrapper apptWindow = (AppointmentWindowWrapper) newLine;
            isValid = validateApptWidnow(apptWindow);
            if (isValid) {
                try {
                    //need to persist the window that has passed the validation to DB
                    saveApptWindow((AppointmentWindowWrapper) newLine);
                    //Add a success message
                    GlobalVariables.getMessageMap().addGrowlMessage("", AppointmentConstants.APPOINTMENT_MSG_INFO_SAVED);
                } catch (Exception e) {
                    LOG.error("Fail to create a window.", e);
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_MESSAGES, AppointmentConstants.APPOINTMENT_MSG_ERROR_WINDOW_SAVE_FAIL);
                    isValid = false;
                }
            }

        } else {
            super.performAddLineValidation(model, newLine, collectionId, collectionPath);
        }
        return isValid;
    }

    public AcademicCalendarService getAcalService() {
        if (academicCalendarService == null) {
            academicCalendarService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE, AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.academicCalendarService;
    }


    public AppointmentService getAppointmentService() {
        if (appointmentService == null) {
            appointmentService = (AppointmentService) GlobalResourceLoader.getService(new QName(AppointmentServiceConstants.NAMESPACE, AppointmentServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return appointmentService;
    }


    public TypeService getTypeService() {
        if (typeService == null) {
            typeService = (TypeService) GlobalResourceLoader.getService(new QName(TypeServiceConstants.NAMESPACE, TypeServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return this.typeService;
    }


    private PopulationService getPopulationService() {
        if (populationService == null) {
            populationService = (PopulationService) GlobalResourceLoader.getService(new QName(PopulationServiceConstants.NAMESPACE, "PopulationService"));
        }
        return populationService;
    }

    public ContextInfo getContextInfo() {
        ContextInfo contextInfo = new ContextInfo();
        contextInfo.setAuthenticatedPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
        contextInfo.setPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
        LocaleInfo localeInfo = new LocaleInfo();
        localeInfo.setLocaleLanguage(Locale.getDefault().getLanguage());
        localeInfo.setLocaleRegion(Locale.getDefault().getCountry());
        contextInfo.setLocale(localeInfo);
        return contextInfo;
    }


}
