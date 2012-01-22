package com.sigmasys.kuali.ksa.rm;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Transaction is an abstract class, used to generate different types of transactions within the system.
 *
 * @author Paul Heald
 */
public abstract class Transaction {

    /**
     * the unique transaction identifier for the KSA product.
     */
    private String id;

    /**
     * the identifier of the transaction as generated by another external system. If the external system does not generate a transaction identifier, this will be the same as the internal transaction identifier. For credits, this might take the form of the credit card authorization, etc.
     */
    private String externalId;
    /**
     * This is the date that the transaction is entered in the ledger. It is set when the transaction is instantiated.
     */
    private Date ledgerDate;

    /**
     * This is the date that the transaction is considered effective, allowing, for example, a college to assess fees in summer that are not 'due' until the fall. It is also the date that payment application will use to order the transactions. This date is also used as the defining date for a transaction if its GL code has multiple effective accounts.
     */
    private Date effectiveDate;

    /**
     * This is the date that the transaction originated, so, for example, housing might bill on a certain day, but upload its transactions only once a week. The purpose of this date is purely for tracking transactions from external systems.
     */
    private Date originationDate;

    /**
     * This is the value of the transaction in the system currency. This is the number that is used as the core value of the transaction. All calculations are performed against this number.
     */
    private BigDecimal amount;

    /**
     * This is the native amount of the transaction in the currency in which it was created. For most transactions, this will be identical to the localAmount, as most transactions occur in the local currency.
     */
    private BigDecimal nativeAmount;

    /**
     * This is the identifier for the currency that is used in nativeAmount. Most often, it will be set to the same value as the system currency. This is a 3-letter ISO4217 code. For example, USD, GBP, EUR.
     */
    private String currencyId;

    /**
     * This flag, when true, indicates that the transaction is 'internal', and will not, in most cases, be displayed to the customer. In most cases, internal transactions will be allocated and locked against an equivalent transaction. An example of this would be if a charge is incorrectly applied to an account, a reversal transaction would be created, the transactions could then be allocated together, and marked as internal.
     */
    private boolean internalTransaction;

    /**
     * This is the amount of the transaction that has been allocated. For example, if a $1000 payment is put towards a $2000 charge, the $1000 will have a $1000 allocation amount, and the $2000 charge will have a $1000 allocation amount. The PA module is responsible for allocating charges to payments.
     */
    private BigDecimal allocatedAmount;

    /**
     * This value is set to a value (up to the maximum of localAmount) and is the value of the transaction that has been allocated, and may not be reallocated by the payment application routine. Most commonly, the entire transaction will be locked, by setting the lockedAllocationAmount to the localValue.
     * Schools will also likely lock their allocations of transactions before the current period of time (for example, if an allocation was made with a payment in a pervious year, it will not
     * be allowed to be deallocated by the system.
     */
    private BigDecimal lockedAllocationAmount;

    /**
     * The responsible entity is the entity that created the transaction. It is assumed the identity will be derived from KIM.
     */
    private String responsibleEntity;

    /**
     * The text for the transaction that will be displayed as a summary of the transaction on statements. For example "Bookstore Purchase"  If this string is null during instantiation, the default text will
     * be populated from the transaction code.
     */
    private String statementText;

    /**
     * A reference to an external document that gives more granular information about the transaction. This information will be different depending on the type of transaction. The information will be available when a user "drills down" into a transaction. It will contain information relating to the charge or payment, such as the contact details for the department who generated a charge, and other information as made available by the system. For example, a charge from the bookstore may list the items purchased. The document referenced will be an XML document, allowing flexible creation of different document types by the institutions themselves. Some basic document types will be defined by the project.
     */
    private String documentReference;

    /**
     * If this value is non null, it points to the most recent memo line that refers to this transaction. For example, if the system locks the transaction and enters a memo line in the account memo, the CSR would be able to pull up the transaction and see the memo line directly, instead of having to search for it.
     */
    private String memoReference;

    /**
     * the rollupId is an institutionally specified group of transactions that are rolled up on the initial view of an account, if the transactions fall in to a specified academic
     * time period. For example, setting this flag on all tuition charges to TUITION would cause all tuition charges to roll up into a single line. If a school charges mandatory fees
     * as single transactions, this could also be rolled up in this way. This can be set on a per-charge basis, or can be pulled from the default in the TransactionType.
     */
    private String rollupId;


    /**
     * This will allocate the value of amount on the transaction. A check will be made to ensure that the allocated amount
     * is equal to or less than the localAmount, less any lockedAllocationAmount. The expectation is that this method will only
     * be called by the payment application module.
     * <p/>
     * It can throw the following exceptions: (I don't fully know how to establish exceptions, so if you could give me some pointers, that would be great.)
     * AmountExceedsAvailableValue
     */
    public void allocateAmount(BigDecimal amount) {
    }

    /**
     * This will allocate a locked amount on the transaction. A check will be made to ensure that the lockedAmount and the allocateAmount don't exceed the ledgerAmount
     * of the transaction. Setting an amount as locked prevents the payment application system from reallocating the balance elsewhere. /*
     */
    public void allocateLockedAmount(BigDecimal amount) {

    }

    /**
     * This method can set the internal switch on or off for a transaction. I cannot think of a time when an internal transaction would
     * not also be locked (tbd) so we should ensure that if a transaction is being set to internal, its lockedAllocationAmount == ledgerAmount.
     * Internal transactions are not generally displayed to the user, and are only displayed to the CSR when asked for.
     */

    public void setInternal(boolean internal) {

    }

    /**
     * If a document reference is altered, then a new one can be re-referenced. The documents associated wtih each transaction give more details about the transaction, and are
     * for information for both the student and the CSR. For example, if the bookstore wanted to, it could lists the books it had sold in a transaction so the student would be
     * able to drill down and see what books made up a line on their statement.
     */
    public void setDocumentReference(String newDocumentReference) {

    }

    /**
     * If a memos can be generated in a number of ways. If a memo is generated against a transaction, it is placed in the main memo account, and also the memoReference is
     * set to point to that memo. This allows the CSR to see the most recent memo associated with a certain transaction. Certain transaction instantiations will generate a memo
     * as soon as they are created.
     */
    public void generateTransactionMemo(String memoText) {

    }

    /**
     * If the reverse method is called, the system will generate a negative transaction for the type of the original transaction. A memo transaction will be generated, and
     * the transactions will be locked together. Subject to user customization, the transactions may be marked as hidden. (likely that credits will not be hidden, debits will.)
     * A charge to an account may be reversed when a mistake is made, or a refund is issued. A payment may be reversed when a payment bounces, or for some other reason is entered
     * on to the account and is not payable.
     */
    public void reverse() {

    }

    /**
     * Using the transaction code and the effective date of the transaction, return an array of the tags associated with this transaction.
     */
    public void getTags() {

    }

    /**
     * Writes the transaction to the ledger. At this point, certain attributes such as transaction code and ledger date will be
     * provided to the object.
     */

    public void save() {

    }

}
	


