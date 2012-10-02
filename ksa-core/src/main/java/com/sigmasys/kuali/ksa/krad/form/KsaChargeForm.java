package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Charge;

import java.math.BigDecimal;

/**
 * Created by: dmulderink on 9/28/12 at 7:56 AM
 */
public class KsaChargeForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   private Charge charge;

   private String chargeTransactionTypeId;

   private BigDecimal estimatedCurrentBalance;

   private BigDecimal estimatedPendingBalance;

   private String statusMessage;

   /*
     Get / Set methods
   */

   public Charge getCharge() {
      return charge;
   }

   public void setCharge(Charge charge) {
      this.charge = charge;
   }


   public String getChargeTransactionTypeId() {
      return chargeTransactionTypeId;
   }

   public void setChargeTransactionTypeId(String chargeTransactionTypeId) {
      this.chargeTransactionTypeId = chargeTransactionTypeId;
   }

   public BigDecimal getEstimatedCurrentBalance() {
      return estimatedCurrentBalance;
   }

   public void setEstimatedCurrentBalance(BigDecimal estimatedCurrentBalance) {
      this.estimatedCurrentBalance = estimatedCurrentBalance;
   }

   public BigDecimal getEstimatedPendingBalance() {
      return estimatedPendingBalance;
   }

   public void setEstimatedPendingBalance(BigDecimal estimatedPendingBalance) {
      this.estimatedPendingBalance = estimatedPendingBalance;
   }

   public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }
}
