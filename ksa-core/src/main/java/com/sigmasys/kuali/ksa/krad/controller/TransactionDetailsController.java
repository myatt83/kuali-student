/**
 * Copyright 2005-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.model.Charge;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.krad.form.TransactionDetailsForm;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Transaction details controller
 *
 * @author Michael Ivanov
 */
@Controller
@RequestMapping(value = "/transactionDetails")
public class TransactionDetailsController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected TransactionDetailsForm createInitialForm(HttpServletRequest request) {
        return new TransactionDetailsForm();
    }


    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute TransactionDetailsForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        // TODO:

        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute TransactionDetailsForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        Transaction transaction = new Charge();
        transaction.setId(1L);
        transaction.setExternalId("externalId_1");
        transaction.setAmount(new BigDecimal(1050.34));
        transaction.setLedgerDate(new Date());
        transaction.setInternal(false);
        transaction.setEffectiveDate(new Date());
        transaction.setDocumentReference("Doc reference");
        transaction.setResponsibleEntity("Responsible entity");

        form.setTransaction(transaction);

        return getUIFModelAndView(form);
    }


}
