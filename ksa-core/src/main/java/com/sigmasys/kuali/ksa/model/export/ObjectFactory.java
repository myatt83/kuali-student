//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 04:57:41 PM EST 
//


package com.sigmasys.kuali.ksa.model.export;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.sigmasys.kuali.ksa.model.export package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TotalRecords_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "totalRecords");
    private final static QName _EmailAddress_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "emailAddress");
    private final static QName _DebitOrCreditCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "debitOrCreditCode");
    private final static QName _TransactionDate_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "transactionDate");
    private final static QName _EncumbranceUpdateCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "encumbranceUpdateCode");
    private final static QName _PersonUserId_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "personUserId");
    private final static QName _DepartmentName_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "departmentName");
    private final static QName _DocumentReversalDate_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "documentReversalDate");
    private final static QName _SubAccountNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "subAccountNumber");
    private final static QName _ChartOfAccountsCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "chartOfAccountsCode");
    private final static QName _TransactionLedgerEntryDescription_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "transactionLedgerEntryDescription");
    private final static QName _ReferenceDocumentNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "referenceDocumentNumber");
    private final static QName _BalanceTypeCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "balanceTypeCode");
    private final static QName _OrganizationReferenceId_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "organizationReferenceId");
    private final static QName _CampusCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "campusCode");
    private final static QName _DetailText_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "detailText");
    private final static QName _MailingAddress_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "mailingAddress");
    private final static QName _ReferenceOriginationCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "referenceOriginationCode");
    private final static QName _TransactionEntrySequenceId_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "transactionEntrySequenceId");
    private final static QName _OrganizationCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "organizationCode");
    private final static QName _CollectorDetailSequenceNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "collectorDetailSequenceNumber");
    private final static QName _OrganizationDocumentNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "organizationDocumentNumber");
    private final static QName _Batch_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "batch");
    private final static QName _ObjectTypeCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "objectTypeCode");
    private final static QName _TransactionLedgerEntryAmount_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "transactionLedgerEntryAmount");
    private final static QName _BatchSequenceNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "batchSequenceNumber");
    private final static QName _ProjectCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "projectCode");
    private final static QName _DocumentTypeCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "documentTypeCode");
    private final static QName _ObjectCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "objectCode");
    private final static QName _TransmissionDate_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "transmissionDate");
    private final static QName _TotalAmount_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "totalAmount");
    private final static QName _DocumentNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "documentNumber");
    private final static QName _SubObjectCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "subObjectCode");
    private final static QName _CreateDate_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "createDate");
    private final static QName _AccountNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "accountNumber");
    private final static QName _OriginationCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "originationCode");
    private final static QName _Amount_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "amount");
    private final static QName _UniversityFiscalAccountingPeriod_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "universityFiscalAccountingPeriod");
    private final static QName _PhoneNumber_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "phoneNumber");
    private final static QName _ReferenceDocumentTypeCode_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "referenceDocumentTypeCode");
    private final static QName _UniversityFiscalYear_QNAME = new QName("http://www.kuali.org/kfs/gl/collector", "universityFiscalYear");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sigmasys.kuali.ksa.model.export
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DetailType }
     */
    public DetailType createDetailType() {
        return new DetailType();
    }

    /**
     * Create an instance of {@link GlEntryType }
     */
    public GlEntryType createGlEntryType() {
        return new GlEntryType();
    }

    /**
     * Create an instance of {@link HeaderType }
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link TrailerType }
     */
    public TrailerType createTrailerType() {
        return new TrailerType();
    }

    /**
     * Create an instance of {@link BatchType }
     */
    public BatchType createBatchType() {
        return new BatchType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "totalRecords")
    public JAXBElement<BigInteger> createTotalRecords(BigInteger value) {
        return new JAXBElement<BigInteger>(_TotalRecords_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "emailAddress")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createEmailAddress(String value) {
        return new JAXBElement<String>(_EmailAddress_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "debitOrCreditCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createDebitOrCreditCode(String value) {
        return new JAXBElement<String>(_DebitOrCreditCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "transactionDate")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createTransactionDate(String value) {
        return new JAXBElement<String>(_TransactionDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "encumbranceUpdateCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createEncumbranceUpdateCode(String value) {
        return new JAXBElement<String>(_EncumbranceUpdateCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "personUserId")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createPersonUserId(String value) {
        return new JAXBElement<String>(_PersonUserId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "departmentName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createDepartmentName(String value) {
        return new JAXBElement<String>(_DepartmentName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "documentReversalDate")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createDocumentReversalDate(String value) {
        return new JAXBElement<String>(_DocumentReversalDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "subAccountNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createSubAccountNumber(String value) {
        return new JAXBElement<String>(_SubAccountNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "chartOfAccountsCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createChartOfAccountsCode(String value) {
        return new JAXBElement<String>(_ChartOfAccountsCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "transactionLedgerEntryDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createTransactionLedgerEntryDescription(String value) {
        return new JAXBElement<String>(_TransactionLedgerEntryDescription_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "referenceDocumentNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createReferenceDocumentNumber(String value) {
        return new JAXBElement<String>(_ReferenceDocumentNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "balanceTypeCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createBalanceTypeCode(String value) {
        return new JAXBElement<String>(_BalanceTypeCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "organizationReferenceId")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createOrganizationReferenceId(String value) {
        return new JAXBElement<String>(_OrganizationReferenceId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "campusCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createCampusCode(String value) {
        return new JAXBElement<String>(_CampusCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "detailText")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createDetailText(String value) {
        return new JAXBElement<String>(_DetailText_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "mailingAddress")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createMailingAddress(String value) {
        return new JAXBElement<String>(_MailingAddress_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "referenceOriginationCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createReferenceOriginationCode(String value) {
        return new JAXBElement<String>(_ReferenceOriginationCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "transactionEntrySequenceId")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createTransactionEntrySequenceId(String value) {
        return new JAXBElement<String>(_TransactionEntrySequenceId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "organizationCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createOrganizationCode(String value) {
        return new JAXBElement<String>(_OrganizationCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "collectorDetailSequenceNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createCollectorDetailSequenceNumber(String value) {
        return new JAXBElement<String>(_CollectorDetailSequenceNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "organizationDocumentNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createOrganizationDocumentNumber(String value) {
        return new JAXBElement<String>(_OrganizationDocumentNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BatchType }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "batch")
    public JAXBElement<BatchType> createBatch(BatchType value) {
        return new JAXBElement<BatchType>(_Batch_QNAME, BatchType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "objectTypeCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createObjectTypeCode(String value) {
        return new JAXBElement<String>(_ObjectTypeCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "transactionLedgerEntryAmount")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createTransactionLedgerEntryAmount(String value) {
        return new JAXBElement<String>(_TransactionLedgerEntryAmount_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "batchSequenceNumber")
    public JAXBElement<BigInteger> createBatchSequenceNumber(BigInteger value) {
        return new JAXBElement<BigInteger>(_BatchSequenceNumber_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "projectCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createProjectCode(String value) {
        return new JAXBElement<String>(_ProjectCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "documentTypeCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createDocumentTypeCode(String value) {
        return new JAXBElement<String>(_DocumentTypeCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "objectCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createObjectCode(String value) {
        return new JAXBElement<String>(_ObjectCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "transmissionDate")
    public JAXBElement<XMLGregorianCalendar> createTransmissionDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TransmissionDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "totalAmount")
    public JAXBElement<BigDecimal> createTotalAmount(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_TotalAmount_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "documentNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createDocumentNumber(String value) {
        return new JAXBElement<String>(_DocumentNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "subObjectCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createSubObjectCode(String value) {
        return new JAXBElement<String>(_SubObjectCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "createDate")
    public JAXBElement<XMLGregorianCalendar> createCreateDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CreateDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "accountNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createAccountNumber(String value) {
        return new JAXBElement<String>(_AccountNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "originationCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createOriginationCode(String value) {
        return new JAXBElement<String>(_OriginationCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "amount")
    public JAXBElement<BigDecimal> createAmount(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Amount_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "universityFiscalAccountingPeriod")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createUniversityFiscalAccountingPeriod(String value) {
        return new JAXBElement<String>(_UniversityFiscalAccountingPeriod_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "phoneNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createPhoneNumber(String value) {
        return new JAXBElement<String>(_PhoneNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "referenceDocumentTypeCode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createReferenceDocumentTypeCode(String value) {
        return new JAXBElement<String>(_ReferenceDocumentTypeCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.kuali.org/kfs/gl/collector", name = "universityFiscalYear")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public JAXBElement<String> createUniversityFiscalYear(String value) {
        return new JAXBElement<String>(_UniversityFiscalYear_QNAME, String.class, null, value);
    }

}
