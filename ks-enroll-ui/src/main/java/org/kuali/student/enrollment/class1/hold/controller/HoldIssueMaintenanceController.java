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
 */
package org.kuali.student.enrollment.class1.hold.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.common.uif.form.KSUifMaintenanceDocumentForm;
import org.kuali.student.common.uif.util.KSControllerHelper;
import org.kuali.student.enrollment.class1.hold.dto.AuthorizingOrgWrapper;
import org.kuali.student.enrollment.class1.hold.dto.HoldIssueMaintenanceWrapper;
import org.kuali.student.enrollment.class1.hold.service.HoldIssueViewHelperService;
import org.kuali.student.enrollment.class1.hold.service.impl.HoldInfoMaintainableImpl;
import org.kuali.student.enrollment.class1.hold.util.HoldIssueConstants;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.core.constants.HoldServiceConstants;
import org.kuali.student.r2.core.hold.dto.HoldIssueInfo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a controller for HoldIssue objects
 *
 * @author Kuali Student Team
 */

@Controller
@RequestMapping(value = "/holdIssueMaintenance")
public class HoldIssueMaintenanceController extends MaintenanceDocumentController {

    @Override
    protected MaintenanceDocumentForm createInitialForm(HttpServletRequest request) {
        return new KSUifMaintenanceDocumentForm();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form,
                              HttpServletRequest request, HttpServletResponse response) {
        //HoldIssueMaintenanceForm holdIssueInfoForm = (HoldIssueMaintenanceForm)form;
        //holdIssueInfoForm.setIsSaveSuccess(false);

        return super.start(form, request, response);
    }

    @RequestMapping(params = "methodToCall=create")
    public ModelAndView create(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        HoldIssueInfo holdIssueInfo = new HoldIssueInfo();
        HoldIssueMaintenanceWrapper holdIssueWrapper = (HoldIssueMaintenanceWrapper)form.getDocument().getNewMaintainableObject().getDataObject();
        HoldInfoMaintainableImpl maintainable = (HoldInfoMaintainableImpl) form.getDocument().getNewMaintainableObject();
        holdIssueInfo.setName(holdIssueWrapper.getName());
        holdIssueInfo.setTypeKey(holdIssueWrapper.getTypeKey());
        holdIssueInfo.setStateKey(HoldServiceConstants.ISSUE_ACTIVE_STATE_KEY);
        holdIssueInfo.setOrganizationId(holdIssueWrapper.getOrganizationId());
        RichTextInfo richTextInfo = new RichTextInfo();
        richTextInfo.setPlain(holdIssueWrapper.getDescr());
        holdIssueInfo.setDescr(richTextInfo);
        maintainable.validateHold(holdIssueWrapper);
        if (GlobalVariables.getMessageMap().hasErrors()) {
            return getUIFModelAndView(form);
        }
        if(StringUtils.isBlank(holdIssueWrapper.getId())){
             HoldIssueInfo createHoldIssueInfo = maintainable.createHoldIssue(holdIssueInfo);
            holdIssueWrapper.setId(createHoldIssueInfo.getId());
            holdIssueWrapper.setStateKey(createHoldIssueInfo.getStateKey());
        }
        else{
            holdIssueInfo.setId(holdIssueWrapper.getId());
            holdIssueInfo.setStateKey(holdIssueWrapper.getStateKey());
            HoldIssueInfo updatedHoldIssueInfo = maintainable.updateHoldIssue(holdIssueInfo);
        }
        form.getView().setApplyDirtyCheck(false);
        //holdIssueWrapper.setHoldIssueInfo(createHoldIssueInfo);

        holdIssueWrapper.setIsSaveSuccess(true);
        GlobalVariables.getMessageMap().putInfo(HoldIssueConstants.HOLD_ISSUE_PROCESS, HoldIssueConstants.HOLD_ISSUE_SAVE_SUCCESS_MSG);
        holdIssueWrapper.setIsSaveSuccess(true);
        clearSearchValues(holdIssueWrapper);
        return refresh(form, result, request, response);
    }

    private void clearSearchValues( HoldIssueMaintenanceWrapper holdIssueWrapper) {
        holdIssueWrapper.setName("");
        holdIssueWrapper.setOrganizationId("");
        holdIssueWrapper.setOrgName("");
        holdIssueWrapper.setStateKey("");
        holdIssueWrapper.setTypeKey("");
        holdIssueWrapper.setDescr("");
        holdIssueWrapper.setOrgName("");
        holdIssueWrapper.setCode("");
        holdIssueWrapper.setOrgAddress("");
        holdIssueWrapper.setBaseType("");
        holdIssueWrapper.setFirstDate("");
        holdIssueWrapper.setFirstTerm("");
        holdIssueWrapper.setOrganizationNames(new ArrayList<AuthorizingOrgWrapper>());
    }

    /**
     * @param form
     * @return
     */
    protected HoldIssueViewHelperService getViewHelper(UifFormBase form) {
        return (HoldIssueViewHelperService) KSControllerHelper.getViewHelperService(form);
    }

}
