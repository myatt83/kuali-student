package com.sigmasys.kuali.ksa.model.pb;

import com.sigmasys.kuali.ksa.model.Identifiable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Payment Billing Date model.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Entity
@Table(name = "KSSA_PB_DATE")
public class PaymentBillingDate implements Identifiable {

    private Long id;

    private PaymentBillingPlan plan;

    private Date effectiveDate;

    private BigDecimal percentage;

    private Long rollupId;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_PB_DATE",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "PB_DATE_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_PB_DATE")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PB_PLAN_ID_FK")
    public PaymentBillingPlan getPlan() {
        return plan;
    }

    public void setPlan(PaymentBillingPlan plan) {
        this.plan = plan;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECTIVE_DATE")
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "PERCENTAGE")
    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Column(name = "ROLLUP_ID")
    public Long getRollupId() {
        return rollupId;
    }

    public void setRollupId(Long rollupId) {
        this.rollupId = rollupId;
    }
}
