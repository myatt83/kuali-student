package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Currency;
import org.kuali.rice.krad.web.form.UifFormBase;


/**
 * Currency details form
 *
 * @author Michael Ivanov
 */
public class CurrencyDetailsForm extends UifFormBase {

    private Currency currency;

   /**
    * Get the Currency model object
    * @return
    */
    public Currency getCurrency() {
        return currency;
    }

   /**
    * Set teh Currency model object
    * @param currency
    */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
