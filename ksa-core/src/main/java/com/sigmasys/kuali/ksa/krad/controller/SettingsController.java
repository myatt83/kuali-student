package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.SettingsForm;
import com.sigmasys.kuali.ksa.krad.model.AuditableEntityModel;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by: dmulderink on 10/10/12 at 8:06 PM
 */
@Controller
@RequestMapping(value = "/settingsView")
public class SettingsController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(SponsorController.class);

    @Autowired
    private AuditableEntityService auditableEntityService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected SettingsForm createInitialForm(HttpServletRequest request) {
        SettingsForm form = new SettingsForm();
        form.setStatusMessage("");
        return form;
    }

    /**
     * @param form
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") SettingsForm form, HttpServletRequest request) {

        // do get stuff...
        String viewId = request.getParameter("viewId");
        String pageId = request.getParameter("pageId");

        String entityId = request.getParameter("entityId");

        logger.info("View: " + viewId + " Page: " + pageId + " Entity ID: " + entityId);

        // Currency type
        if ("CurrencyPage".equals(pageId)) {
            form.setAuditableEntity(new Currency());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(Currency.class));
        } else if ("CurrencyDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), Currency.class));
        } else if ("RollupPage".equals(pageId)) {
            form.setAuditableEntity(new Rollup());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(Rollup.class));
        } else if ("RollupDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), Rollup.class));
        } else if ("BankTypePage".equals(pageId)) {
            form.setAuditableEntity(new BankType());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(BankType.class));
        } else if ("BankTypeDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), BankType.class));
        } else if ("TaxTypePage".equals(pageId)) {
            form.setAuditableEntity(new TaxType());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(TaxType.class));
        } else if ("TaxTypeDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), LatePeriod.class));
        } else if ("LatePeriodPage".equals(pageId)) {
            form.setAuditableEntity(new LatePeriod());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(LatePeriod.class));
        } else if ("LatePeriodDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), LatePeriod.class));
        } else if ("AccountStatusTypePage".equals(pageId)) {
            form.setAuditableEntity(new AccountStatusType());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(AccountStatusType.class));
        } else if ("AccountStatusTypeDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), AccountStatusType.class));
        } else if ("FlagTypePage".equals(pageId)) {
            form.setAuditableEntity(new FlagType());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(FlagType.class));
        } else if ("FlagTypeDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), FlagType.class));
        } else if ("ActivityTypePage".equals(pageId)) {
            form.setAuditableEntity(new ActivityType());
            form.setAuditableEntities(auditableEntityService.getAuditableEntities(ActivityType.class));
        } else if ("ActivityTypeDetailsPage".equals(pageId)) {
            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }
            form.setAuditableEntity(auditableEntityService.getAuditableEntity(Long.valueOf(entityId), ActivityType.class));
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @return
     */
/*    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") SettingsForm form) {
        // do refresh stuff...

        // refresh the list of currencies. the form and view manage the refresh

        // a currency instance for the view and model. User may add a currency in this page.
        // this is not a persisted currency
        form.setCurrency(new Currency());
        // this is the existing currencies in the system
        form.setCurrencies(currencyService.getCurrencies());
        // clear the status message
        form.setStatusMessage("");

        return getUIFModelAndView(form);
    }
 */
    /**
     * @param form
     * @return
     */
    /*@RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertCurrency")
    public ModelAndView insertCurrency(@ModelAttribute("KualiForm") SettingsForm form) {
        AuditableEntityModel entity = form.getAuditableEntity();
        if (!(entity.getParentEntity() instanceof Currency)) {
            String errMsg = "Entity must be of Currency type";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        return insertAuditableEntity(form);
    }

    */
    /**
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertRollup")
    public ModelAndView insertAuditableEntity(@ModelAttribute("KualiForm") SettingsForm form) {

        AuditableEntityModel entity = form.getAuditableEntity();
        AuditableEntity parentEntity = entity.getParentEntity();
        try {

            String code = parentEntity.getCode();
            String name = parentEntity.getName();
            String description = parentEntity.getDescription();

            auditableEntityService.createAuditableEntity(code, name, description, parentEntity.getClass());

            form.setAuditableEntities(auditableEntityService.getAuditableEntities(parentEntity.getClass()));

            // success in creating the currency.
            String statusMsg = "Success: " + parentEntity.getClass().getName() + " saved, ID = " + parentEntity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to create the currency. Leave the currency information in the view
            String statusMsg = "Failure: " + parentEntity.getClass().getName() + "entity did not save, ID = " + parentEntity.getId() +
                    ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

    public <T extends AuditableEntity> ModelAndView updateAuditableEntity(@ModelAttribute("KualiForm")
                                                                           SettingsForm form) {

        AuditableEntity entity = form.getAuditableEntity();

        try {
            // occurs in the detail page.
            auditableEntityService.persistAuditableEntity(entity);
            // success in updating the currency.
            String statusMsg = "Success: " + entity.getClass() + " entity updated. Entity ID =  " + entity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to update the currency. Leave the currency information in the view
            String statusMsg = "Failure: " + entity.getClass() + " entity did not update, Entity ID =  " + entity.getId() + ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }

}
