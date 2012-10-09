package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.RulesForm;
import com.sigmasys.kuali.ksa.model.RuleSet;
import com.sigmasys.kuali.ksa.service.drools.DroolsPersistenceService;
import com.sigmasys.kuali.ksa.service.drools.DroolsService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.builder.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/rulesView")
public class RulesController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(RulesController.class);

    @Autowired
    private DroolsService droolsService;

    @Autowired
    private DroolsPersistenceService droolsPersistenceService;


    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected RulesForm createInitialForm(HttpServletRequest request) {
        RulesForm rulesForm = new RulesForm();
        rulesForm.initRuleSetNameFinder(droolsPersistenceService.getRuleIds());
        return rulesForm;
    }

    /**
     * @param form RuleForm instance
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") RulesForm form) {
        return getUIFModelAndView(form);
    }

    /**
     * @param form RuleForm instance
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=update")
    public ModelAndView update(@ModelAttribute("KualiForm") RulesForm form) {

        String ruleSetId = form.getRuleSetId();
        String ruleSetBody = form.getRuleSetBody();

        RuleSet ruleSet = new RuleSet(ruleSetId, ruleSetBody);

        // Validating the rule set content
        ResourceType resourceType = ruleSetId.equals(droolsService.getDslId()) ? ResourceType.DSL : ResourceType.DSLR;
        droolsService.validateRuleSet(ruleSet, resourceType);

        droolsPersistenceService.persistRules(ruleSet);

        logger.info("Updated Rule Set => \n" + ruleSetBody);

        return getUIFModelAndView(form);
    }

    /**
     * @param form RulesForm
     * @return ModelAndView instance
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=select")
    public ModelAndView select(@ModelAttribute("KualiForm") RulesForm form) {

        String ruleSetId = form.getRuleSetId();
        if (StringUtils.isBlank(ruleSetId)) {
            String errMsg = "Rule Set ID must be specified";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        RuleSet ruleSet = droolsPersistenceService.getRules(ruleSetId);
        if (ruleSet == null) {
            String errMsg = "Rule Set specified by ID = " + ruleSetId + " does not exist";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        form.setRuleSetId(ruleSetId);
        form.setRuleSetBody(HtmlUtils.htmlEscape(ruleSet.getRules()));



        logger.info("Selected Rule Set => \n" + ruleSet.getRules());

        return getUIFModelAndView(form);
    }


}
