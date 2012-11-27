package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.exception.InvalidGeneralLedgerTypeException;
import com.sigmasys.kuali.ksa.exception.TransactionNotFoundException;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.*;
import com.sigmasys.kuali.ksa.util.EnumUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

import static com.sigmasys.kuali.ksa.model.Constants.*;

/**
 * General Ledger service implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("generalLedgerService")
@Transactional(readOnly = true)
@WebService(serviceName = GeneralLedgerService.SERVICE_NAME, portName = GeneralLedgerService.PORT_NAME,
        targetNamespace = WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class GeneralLedgerServiceImpl extends GenericPersistenceService implements GeneralLedgerService {

    private static final Log logger = LogFactory.getLog(GeneralLedgerServiceImpl.class);


    /**
     * Creates a new general ledger transaction based on the given parameters
     *
     * @param transactionId ID of the corresponding transaction
     * @param userId        General ledger account ID
     * @param amount        Transaction amount
     * @param operationType GL operation type
     * @param isQueued      Set status to Q unless isQueued is passed and is false, in which case, set status to W
     * @return new GL Transaction instance
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount,
                                             GlOperationType operationType, boolean isQueued) {

        Transaction transaction = em.find(Transaction.class, transactionId);
        if (transaction == null) {
            String errMsg = "Transaction with ID = " + transactionId + " does not exist";
            logger.error(errMsg);
            throw new TransactionNotFoundException(errMsg);
        }

        GlTransaction glTransaction = new GlTransaction();
        glTransaction.setDate(new Date());
        glTransaction.setAmount(amount != null ? amount : BigDecimal.ZERO);
        glTransaction.setGlAccountId(userId);
        glTransaction.setGlOperation(operationType);
        glTransaction.setDescription(operationType.toString());
        glTransaction.setTransactions(new HashSet<Transaction>(Arrays.asList(transaction)));
        glTransaction.setStatus(isQueued ? GlTransactionStatus.QUEUED : GlTransactionStatus.WAITING);

        Date recognitionDate = transaction.getRecognitionDate();
        if (recognitionDate == null) {
            String errMsg = "GL recognition date cannot be null, Transaction ID = " + transactionId;
            throw new IllegalStateException(errMsg);
        }

        GlRecognitionPeriod recognitionPeriod = getGlRecognitionPeriod(recognitionDate);
        if (recognitionPeriod == null) {
            String errMsg = "GL recognition period cannot be found for the given date = " + recognitionDate;
            throw new IllegalStateException(errMsg);
        }

        glTransaction.setRecognitionPeriod(recognitionPeriod);

        persistEntity(glTransaction);

        return glTransaction;

    }

    /**
     * Creates a new general ledger transaction based on the given parameters
     *
     * @param transactionId ID of the corresponding transaction
     * @param userId        General ledger account ID
     * @param amount        Transaction amount
     * @param operationType GL operation type
     * @return new GL Transaction instance
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public GlTransaction createGlTransaction(Long transactionId, String userId, BigDecimal amount,
                                             GlOperationType operationType) {
        return createGlTransaction(transactionId, userId, amount, operationType, true);

    }

    /**
     * Summarizes the general ledger transactions for the given GL transaction list
     *
     * @param transactions List of general ledger transactions
     * @return the modified list of GL transactions
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public List<GlTransaction> summarizeGlTransactions(List<GlTransaction> transactions) {

        // Creating a new array list based on the given GL transaction list
        List<GlTransaction> glTransactions = new ArrayList<GlTransaction>(transactions);

        // Map of [GL Account ID, GL Recognition Period ID] and set of transactions
        Map<Pair<String, Long>, Set<Transaction>> transactionMap = new HashMap<Pair<String, Long>, Set<Transaction>>();

        // Map of [GL Account ID, GL Recognition Period ID] and total transaction amount
        Map<Pair<String, Long>, BigDecimal> amountMap = new HashMap<Pair<String, Long>, BigDecimal>();

        // Map of [GL Account ID, GL Recognition Period ID] and the first GL transaction for this GL account from the given list
        Map<Pair<String, Long>, List<GlTransaction>> glTransactionMap = new HashMap<Pair<String, Long>, List<GlTransaction>>();

        for (GlTransaction glTransaction : glTransactions) {

            if (glTransaction.getStatus() != null && GlTransactionStatus.WAITING == glTransaction.getStatus()) {

                String glAccountId = glTransaction.getGlAccountId();
                Long recognitionPeriodId = glTransaction.getRecognitionPeriod().getId();

                if (glAccountId != null && recognitionPeriodId != null) {

                    final Pair<String, Long> key = new Pair<String, Long>(glAccountId, recognitionPeriodId);

                    Set<Transaction> transactionsForAccount = transactionMap.get(key);
                    if (transactionsForAccount == null) {
                        transactionsForAccount = new HashSet<Transaction>();
                        transactionMap.put(key, transactionsForAccount);
                    }

                    // Adding all transactions to the list of transactions for the current GL account and Recognition Period
                    transactionsForAccount.addAll(glTransaction.getTransactions());

                    List<GlTransaction> glTransactionsForAccount = glTransactionMap.get(key);
                    if (glTransactionsForAccount == null) {
                        glTransactionsForAccount = new LinkedList<GlTransaction>();
                        glTransactionMap.put(key, glTransactionsForAccount);
                    }

                    // Adding GL transaction to the list of GL transactions for the current GL account and Recognition Period
                    glTransactionsForAccount.add(glTransaction);

                    BigDecimal amount = amountMap.get(key);
                    if (amount == null) {
                        amount = BigDecimal.ZERO;
                    }

                    for (Transaction trans : glTransaction.getTransactions()) {
                        amount = amount.add(trans.getAmount() != null ? trans.getAmount() : BigDecimal.ZERO);
                    }

                    // Sum up the transaction amount for the current GL account and Recognition Period
                    amountMap.put(key, amount.add(amount));
                }
            }
        }

        Set<Long> glTransactionIdsToDelete = new HashSet<Long>();
        for (Pair<String, Long> key : glTransactionMap.keySet()) {

            List<GlTransaction> glTransactionsForAccount = glTransactionMap.get(key);

            if (!CollectionUtils.isEmpty(glTransactionsForAccount)) {

                BigDecimal totalAmount = amountMap.get(key);

                if (!BigDecimal.ZERO.equals(totalAmount)) {

                    // Persisting the first GL transaction with the total amount and all
                    // transactions for the current GL account
                    GlTransaction firstGlTransaction = glTransactionsForAccount.get(0);
                    firstGlTransaction.setTransactions(transactionMap.get(key));
                    firstGlTransaction.setAmount(totalAmount);
                    persistEntity(firstGlTransaction);
                    // We have to remove the rest of GL transactions
                    for (int i = 1; i < glTransactionsForAccount.size(); i++) {
                        glTransactionIdsToDelete.add(glTransactionsForAccount.get(i).getId());
                    }

                } else {
                    for (GlTransaction glTransaction : glTransactionsForAccount) {
                        glTransactionIdsToDelete.add(glTransaction.getId());
                    }
                }
            }
        }

        // Deleting GL transactions that must be eliminated from the list and database and
        // persisting the rest with the status 'Q'
        for (Iterator<GlTransaction> iterator = glTransactions.iterator(); iterator.hasNext(); ) {
            GlTransaction glTransaction = iterator.next();
            if (glTransactionIdsToDelete.contains(glTransaction.getId())) {
                em.remove(glTransaction);
                iterator.remove();
            } else {
                glTransaction.setStatus(GlTransactionStatus.QUEUED);
                persistEntity(glTransaction);
            }
        }

        return glTransactions;
    }

    /**
     * Returns the general ledger type instance for the given code.
     *
     * @param glTypeCode General Ledger type code
     * @return GeneralLedgerType instance
     */
    @Override
    public GeneralLedgerType getGeneralLedgerType(String glTypeCode) {
        Query query = em.createQuery("select t from GeneralLedgerType t where t.code = :code");
        query.setParameter("code", glTypeCode);
        List<GeneralLedgerType> glTypes = query.getResultList();
        if (glTypes != null && !glTypes.isEmpty()) {
            return glTypes.get(0);
        }
        String errMsg = "Cannot find GeneralLedgerType for the code = " + glTypeCode;
        logger.error(errMsg);
        throw new InvalidGeneralLedgerTypeException(errMsg);
    }

    /**
     * Returns the default general ledger type.
     *
     * @return GeneralLedgerType instance
     */
    @Override
    public GeneralLedgerType getDefaultGeneralLedgerType() {
        return getGeneralLedgerType(configService.getInitialParameter(DEFAULT_GL_TYPE_PARAM_NAME));
    }

    /**
     * Returns the default general ledger mode.
     *
     * @return GeneralLedgerMode instance
     */
    @Override
    public GeneralLedgerMode getDefaultGeneralLedgerMode() {
        String glMode = configService.getInitialParameter(DEFAULT_GL_MODE_PARAM_NAME);
        if (glMode != null) {
            return EnumUtils.findById(GeneralLedgerMode.class, glMode);
        }
        String errMsg = "ksa.general.ledger.mode' parameter must be set in the KSA configuration";
        logger.error(errMsg);
        throw new IllegalStateException(errMsg);
    }

    private GlTransmission createGlTransmission(GlTransaction transaction, GlRecognitionPeriod recognitionPeriod,
                                                String batchId) {

        GlTransmission transmission = new GlTransmission();
        transmission.setBatchId(batchId);
        transmission.setGlAccountId(transaction.getGlAccountId());
        transmission.setEarliestDate(transaction.getDate());
        transmission.setLatestDate(transaction.getDate());
        transmission.setRecognitionPeriod(recognitionPeriod);
        transmission.setDate(new Date());
        transmission.setGlOperation(transaction.getGlOperation());
        transmission.setAmount(transaction.getAmount());

        persistEntity(transmission);

        return transmission;

    }

    /**
     * Finds the GL recognition period for the given date.
     *
     * @param date <code>java.util.Date</code> instance
     * @return GlRecognitionPeriod instance or null if not found
     */
    private GlRecognitionPeriod getGlRecognitionPeriod(Date date) {
        Query query = em.createQuery("select rp from GlRecognitionPeriod rp where rp.startDate <= :date and rp.endDate >= :date");
        query.setParameter("date", date);
        List<GlRecognitionPeriod> results = (List<GlRecognitionPeriod>) query.getResultList();
        return (results != null && !results.isEmpty()) ? results.get(0) : null;
    }

    private synchronized void prepareGlTransmissions(Date fromDate, Date toDate, boolean isEffectiveDate,
                                                     String... recognitionPeriods) {

        if (fromDate != null && toDate != null && fromDate.after(toDate)) {
            String errMsg = "Start Date cannot be greater than End Date: fromDate = " + fromDate +
                    ", toDate = " + toDate;
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        String glModeCode = configService.getInitialParameter(DEFAULT_GL_MODE_PARAM_NAME);
        GeneralLedgerMode glMode = EnumUtils.findById(GeneralLedgerMode.class, glModeCode);
        if (glMode == null) {
            String errMsg = "General Ledger mode must be specified";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        Query query;
        if (fromDate != null && toDate != null) {
            // If isEffectiveDate is true then use the transaction effective date, otherwise the recognition date
            String dateField = isEffectiveDate ? "effectiveDate" : "recognitionDate";
            query = em.createQuery("select distinct t from GlTransaction t " +
                    " inner join fetch t.recognitionPeriod rp " +
                    " left outer join t.transactions ts " +
                    " where t.statusCode = :status and " +
                    " ts." + dateField + " <> null and ts." + dateField + " between :fromDate and :toDate " +
                    " order by t.date asc");
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
        } else {
            boolean periodExists = (recognitionPeriods != null && recognitionPeriods.length > 0);
            String inClause = periodExists ? " and rp.code in (:recognitionPeriods) " : "";
            query = em.createQuery("select t from GlTransaction t " +
                    " inner join fetch t.recognitionPeriod rp " +
                    " where t.statusCode = :status " + inClause + " order by t.date asc");
            if (periodExists) {
                query.setParameter("recognitionPeriods", Arrays.asList(recognitionPeriods));
            }
        }

        query.setParameter("status", GlTransactionStatus.QUEUED.getId());
        if (glMode == GeneralLedgerMode.INDIVIDUAL) {
            query.setMaxResults(1);
        }

        List<GlTransaction> glTransactions = query.getResultList();
        if (CollectionUtils.isEmpty(glTransactions)) {
            if (fromDate != null && toDate != null) {
                logger.warn("Cannot find GL transactions for the given date range: fromDate = " + fromDate +
                        ", toDate = " + toDate);
            } else {
                logger.warn("No GL transactions found");
            }
            return;
        }

        if (glMode == GeneralLedgerMode.INDIVIDUAL) {
            GlTransaction earliestGlTransaction = glTransactions.get(0);
            GlRecognitionPeriod recognitionPeriod = earliestGlTransaction.getRecognitionPeriod();
            GlTransmission transmission = createGlTransmission(earliestGlTransaction, recognitionPeriod, "RT");
            earliestGlTransaction.setTransmission(transmission);
            earliestGlTransaction.setStatus(GlTransactionStatus.COMPLETED);
            persistEntity(earliestGlTransaction);
        } else if (glMode == GeneralLedgerMode.BATCH) {
            for (GlTransaction transaction : glTransactions) {
                GlRecognitionPeriod recognitionPeriod = transaction.getRecognitionPeriod();
                GlTransmission transmission = createGlTransmission(transaction, recognitionPeriod, null);
                transaction.setTransmission(transmission);
                transaction.setStatus(GlTransactionStatus.COMPLETED);
                persistEntity(transaction);
            }

        } else if (glMode == GeneralLedgerMode.BATCH_ROLLUP) {

            // Creating a map of <GL account, set of GL transactions>
            HashMap<String, Set<GlTransaction>> accountTransactions = new HashMap<String, Set<GlTransaction>>();
            for (GlTransaction transaction : glTransactions) {
                String accountId = transaction.getGlAccountId();
                Set<GlTransaction> glTransactionSet = accountTransactions.get(accountId);
                if (glTransactionSet == null) {
                    glTransactionSet = new HashSet<GlTransaction>();
                    accountTransactions.put(accountId, glTransactionSet);
                }
                glTransactionSet.add(transaction);
            }

            for (Map.Entry<String, Set<GlTransaction>> entry : accountTransactions.entrySet()) {

                String accountId = entry.getKey();

                Set<GlTransaction> glTransactionSet = entry.getValue();

                if (glTransactionSet.isEmpty()) {
                    continue;
                }

                GlOperationType groupOperation = null;
                BigDecimal amount1 = BigDecimal.ZERO;
                BigDecimal amount2 = BigDecimal.ZERO;
                Date minDate = null;
                Date maxDate = null;

                for (GlTransaction transaction : glTransactionSet) {
                    if (groupOperation == null) {
                        groupOperation = transaction.getGlOperation();
                    }
                    GlOperationType glOperation = transaction.getGlOperation();
                    if (glOperation != null) {
                        if (glOperation == groupOperation) {
                            amount1 = amount1.add(transaction.getAmount());
                        } else {
                            amount2 = amount2.add(transaction.getAmount());
                        }
                    }
                    if (minDate == null || minDate.after(transaction.getDate())) {
                        minDate = transaction.getDate();
                    }
                    if (maxDate == null || maxDate.before(transaction.getDate())) {
                        maxDate = transaction.getDate();
                    }
                }

                GlTransmission transmission = new GlTransmission();
                transmission.setGlAccountId(accountId);
                transmission.setEarliestDate(minDate);
                transmission.setLatestDate(maxDate);
                transmission.setDate(new Date());
                transmission.setGlOperation(groupOperation);
                transmission.setAmount(amount1.subtract(amount2));
                persistEntity(transmission);

                // Updating GL transactions for the current account
                for (GlTransaction transaction : glTransactionSet) {
                    transaction.setStatus(GlTransactionStatus.COMPLETED);
                    transaction.setTransmission(transmission);
                    persistEntity(transaction);
                }
            }
        } else {
            String errMsg = "Unexpected GeneralLedgerMode: " + glMode;
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

    }

    /**
     * Prepares a transmission to the general ledger for the given range of effective dates.
     * This process takes into account the different ways in which an institution may choose to transmit to
     * the general ledger, including real-time, batch, and rollup modes.
     *
     * @param fromDate Start effective date
     * @param toDate   End effective date
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public void prepareGlTransmissionsForEffectiveDates(Date fromDate, Date toDate) {
        prepareGlTransmissions(fromDate, toDate, true);
    }

    /**
     * Prepares a transmission to the general ledger for the given range of recognition dates.
     * This process takes into account the different ways in which an institution may choose to transmit to
     * the general ledger, including real-time, batch, and rollup modes.
     *
     * @param fromDate Start recognition date
     * @param toDate   End recognition date
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public void prepareGlTransmissionsForRecognitionDates(Date fromDate, Date toDate) {
        prepareGlTransmissions(fromDate, toDate, false);
    }

    /**
     * Prepares a transmission to the general ledger for all GL transactions in status Q.
     * This process takes into account the different ways in which an institution may choose to transmit to
     * the general ledger, including real-time, batch, and rollup modes.
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public void prepareGlTransmissions() {
        prepareGlTransmissions(null, null, true);
    }

    /**
     * Prepares a transmission to the general ledger for the given GL recognition
     *
     * @param recognitionPeriods an array of GL recognition period codes
     */
    @Override
    @WebMethod(exclude = true)
    @Transactional(readOnly = false)
    public void prepareGlTransmissions(String... recognitionPeriods) {
        prepareGlTransmissions(null, null, true, recognitionPeriods);
    }

    /**
     * Retrieves all GL transmissions with the empty "result" field for export.
     *
     * @return list of GlTransmission instances
     */
    @Override
    public List<GlTransmission> getGlTransmissionsForExport() {
        Query query = em.createQuery("select glt from GlTransmission glt " +
                " inner join fetch glt.recognitionPeriod rp " +
                " where glt.result is null");
        return query.getResultList();
    }

}