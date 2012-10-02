package com.sigmasys.kuali.ksa.service;


import static org.springframework.util.Assert.isNull;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sigmasys.kuali.ksa.model.Allocation;
import com.sigmasys.kuali.ksa.model.Charge;
import com.sigmasys.kuali.ksa.model.CompositeAllocation;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.DebitType;
import com.sigmasys.kuali.ksa.model.Deferment;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.model.TransactionType;
import com.sigmasys.kuali.ksa.model.TransactionTypeId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@SuppressWarnings("unchecked")
public class TransactionServiceTest extends AbstractServiceTest {

    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    private Transaction transaction1;
    private Transaction transaction2;

    @Before
    public void setUpWithinTransaction() {
        // set up test data within the transaction
        String userId = "admin";
        accountService.getOrCreateAccount(userId);
    }

    @Test
    public void createTransaction() throws Exception {

        String id = "cash";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e5));

        notNull(transaction);
        notNull(transaction.getId());
        notNull(transaction.getTransactionType());
        notNull(transaction.getAccount());
        notNull(transaction.getAccountId());
        notNull(transaction.getCurrency());

        isTrue("USD".equals(transaction.getCurrency().getCode()));
        isTrue("admin".equals(transaction.getAccount().getId()));
        isTrue(new Date().compareTo(transaction.getEffectiveDate()) >= 0);
        isTrue(new BigDecimal(10e5).equals(transaction.getNativeAmount()));

    }

    private void createAllocation(boolean locked) {

        String id = "1020";

        transaction1 = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(100));
        transaction2 = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(-100));

        notNull(transaction1);
        notNull(transaction2);
        notNull(transaction1.getId());
        notNull(transaction2.getId());
        notNull(transaction1.getAmount());
        notNull(transaction2.getAmount());

        CompositeAllocation compositeAllocation =
                locked ?
                        transactionService.createLockedAllocation(transaction1.getId(), transaction2.getId(), new BigDecimal(90)) :
                        transactionService.createAllocation(transaction1.getId(), transaction2.getId(), new BigDecimal(90));

        notNull(compositeAllocation);

        Allocation allocation = compositeAllocation.getAllocation();

        notNull(allocation);
        notNull(allocation.getId());
        notNull(allocation.getFirstTransaction());
        notNull(allocation.getSecondTransaction());
        notNull(allocation.getFirstTransaction().getId());
        notNull(allocation.getSecondTransaction().getId());
        isTrue(allocation.getFirstTransaction().getId().equals(transaction1.getId()));
        isTrue(allocation.getSecondTransaction().getId().equals(transaction2.getId()));

        transaction1 = transactionService.getTransaction(transaction1.getId());
        transaction2 = transactionService.getTransaction(transaction2.getId());

        notNull(transaction1);
        notNull(transaction2);
        notNull(transaction1.getId());
        notNull(transaction2.getId());

        isTrue(new BigDecimal(90).equals(allocation.getAmount()));

        BigDecimal allocatedAmount1 = locked ?
                transaction1.getLockedAllocatedAmount() :
                transaction1.getAllocatedAmount();

        BigDecimal allocatedAmount2 = locked ?
                transaction2.getLockedAllocatedAmount() :
                transaction2.getAllocatedAmount();

        logger.info("allocatedAmount1 = " + allocatedAmount1);
        logger.info("allocatedAmount2 = " + allocatedAmount2);

        isTrue(new BigDecimal(90).equals(allocatedAmount1));
        isTrue(new BigDecimal(90).equals(allocatedAmount2));
    }

    @Test
    public void createAllocation() throws Exception {

        createAllocation(false);

    }

    @Test
    public void createLockedAllocation() throws Exception {

        createAllocation(true);

    }

    @Test
    public void removeAllocation() throws Exception {

        createAllocation();

        transactionService.removeAllocation(transaction1.getId(), transaction2.getId());

    }

    @Test
    public void removeLockedAllocation() throws Exception {

        createLockedAllocation();

        transactionService.removeLockedAllocation(transaction1.getId(), transaction2.getId());

    }

    @Test
    public void removeAllocations() throws Exception {

        createAllocation();

        transactionService.removeAllocations(transaction2.getId());

    }

    @Test
    public void getTransactions() throws Exception {

        List<Transaction> transactions = transactionService.getTransactions();

        notNull(transactions);
        notEmpty(transactions);

        // Add more assertions when we have some test data
    }

    @Test
    public void getCharges() throws Exception {

        List<Charge> charges = transactionService.getCharges();

        notNull(charges);
        notEmpty(charges);

        // Add more assertions when we have some test data
    }

    @Test
    public void getTransaction() throws Exception {

        Transaction transaction = transactionService.getTransaction(7777777L);

        // Check that the entity does not exist
        isNull(transaction);

        // Add more assertions when we have some test data
    }

    @Test
    public void getTransactionByUserId() throws Exception {

        List<Transaction> transactions = transactionService.getTransactions("dukakis");

        notNull(transactions);

        isTrue(transactions.isEmpty());

        // Add more assertions when we have some test data
    }

    @Test
    public void getChargesWithFormattedAmounts() throws Exception {

        List<Charge> charges = transactionService.getCharges();

        notNull(charges);
        notEmpty(charges);

        for (Charge charge : charges) {
            notNull(charge.getFormattedAmount());
            logger.info("Formatted amount = " + charge.getFormattedAmount());
        }

    }

    @Test
    public void getTransactionType() throws Exception {

        String id = "1020";

        TransactionType transactionType = transactionService.getTransactionType(id, new Date());

        notNull(transactionType);
        notNull(transactionType.getId());
        isTrue("1020".equals(transactionType.getId().getId()));

    }

    @Test
    public void getTransactionTypeClass() throws Exception {

        String id = "1020";

        Class<TransactionType> debitTypeClass = transactionService.getTransactionTypeClass(id);

        notNull(debitTypeClass);
        notNull(debitTypeClass.equals(DebitType.class));

    }

    @Test
    public void reverseTransaction() throws Exception {

        String id = "cash";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e5));

        notNull(transaction);
        notNull(transaction.getId());
        notNull(transaction.getTransactionType());
        notNull(transaction.getTransactionType().getId());

        TransactionTypeId transactionTypeId = transaction.getTransactionType().getId();

        notNull(transaction.getAccount());
        notNull(transaction.getAccountId());
        notNull(transaction.getCurrency());
        notNull(transaction.getAmount());

        transaction = transactionService.reverseTransaction(transaction.getId(), "Memo text", new BigDecimal(150.05),
                "Reversed");

        notNull(transaction);
        notNull(transaction.getId());
        notNull(transaction.getTransactionType());
        notNull(transaction.getAccount());
        notNull(transaction.getAccountId());
        notNull(transaction.getCurrency());
        notNull(transaction.getAmount());

        isTrue(transaction.getStatementText().contains("Reversed"));
        isTrue(transactionTypeId.equals(transaction.getTransactionType().getId()));

    }

    @Test
    public void createDeferment() throws Exception {

        // Must be a credit type 'C'
        String id = "ach";
        String userId = "admin";

        Date effectiveDate = new Date();
        Date expirationDate = new Date(effectiveDate.getTime() * 2);

        Transaction deferment =
                transactionService.createTransaction(id, null, userId, effectiveDate, expirationDate,
                        new BigDecimal(10e5), false);


        notNull(deferment);

        isTrue(deferment instanceof Deferment);

        notNull(deferment.getId());
        notNull(deferment.getTransactionType());
        notNull(deferment.getTransactionType().getId());

        notNull(deferment.getAccount());
        notNull(deferment.getAccountId());
        notNull(deferment.getCurrency());
        notNull(deferment.getAmount());

    }

    @Test
    public void expireDeferment() throws Exception {

        // Must be a credit type 'C'
        String id = "ach";
        String userId = "admin";

        Date effectiveDate = new Date();
        Date expirationDate = new Date(effectiveDate.getTime() * 2);

        Transaction deferment =
                transactionService.createTransaction(id, null, userId, effectiveDate, expirationDate,
                        new BigDecimal(10e5), false);


        notNull(deferment);

        isTrue(deferment instanceof Deferment);

        notNull(deferment.getId());
        notNull(deferment.getTransactionType());
        notNull(deferment.getTransactionType().getId());

        notNull(deferment.getAccount());
        notNull(deferment.getAccountId());
        notNull(deferment.getCurrency());
        notNull(deferment.getAmount());

        transactionService.expireDeferment(deferment.getId());

    }

    @Test
    public void makeEffective() throws Exception {

        String id = "cash";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e5));

        notNull(transaction);
        notNull(transaction.getId());
        notNull(transaction.getTransactionType());
        notNull(transaction.getTransactionType().getId());

        notNull(transaction.getAccount());
        notNull(transaction.getAccountId());
        notNull(transaction.getCurrency());
        notNull(transaction.getAmount());

        transactionService.makeEffective(transaction.getId(), false);

    }

    @Test
    public void makeEffectiveForced() throws Exception {

        String id = "cash";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e5));

        notNull(transaction);
        notNull(transaction.getId());
        notNull(transaction.getTransactionType());
        notNull(transaction.getTransactionType().getId());

        notNull(transaction.getAccount());
        notNull(transaction.getAccountId());
        notNull(transaction.getCurrency());
        notNull(transaction.getAmount());

        transactionService.makeEffective(transaction.getId(), true);

    }

    @Test
    public void writeOffTransaction() throws Exception {

        // Must be a Charge
        String id = "1020";

        Transaction transaction = transactionService.createTransaction(id, "admin", new Date(), new BigDecimal(10e3));

        notNull(transaction);
        notNull(transaction.getId());
        notNull(transaction.getTransactionType());
        notNull(transaction.getTransactionType().getId());

        notNull(transaction.getAccount());
        notNull(transaction.getAccountId());
        notNull(transaction.getCurrency());
        notNull(transaction.getAmount());

        transaction = transactionService.writeOffTransaction(transaction.getId(), null, "Memo text", "Write-off");

        notNull(transaction);
        notNull(transaction.getId());
        notNull(transaction.getTransactionType());
        notNull(transaction.getTransactionType().getId());

        notNull(transaction.getAccount());
        notNull(transaction.getAccountId());
        notNull(transaction.getCurrency());
        notNull(transaction.getAmount());

        isTrue(transaction.getStatementText().contains("Write-off"));
        isTrue(transaction.getAmount().compareTo(new BigDecimal(10e3).negate()) == 0);

    }

    @Test
    public void testTransactionExistsByTransactionType() throws Exception {
    	// Create a new Transaction:
        String transactionTypeId = "1020";
        String accountId = "admin";
        BigDecimal amount = new BigDecimal(10e3);
        Date effectiveDate = new Date();
        
        transactionService.createTransaction(transactionTypeId, accountId, effectiveDate, amount);
        
        // Call the service:
        boolean exists = transactionService.transactionExists(accountId, transactionTypeId);
        
        isTrue(exists);
    	
    	// Try to find a Transaction by a fake Account:
        String fakeAccount = "fake";
        
        exists = transactionService.transactionExists(fakeAccount, transactionTypeId);
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake Transaction ID:
        String fakeTransactionTypeId = "somethingelse";
        
        exists = transactionService.transactionExists(accountId, fakeTransactionTypeId);
        isTrue(!exists);
        
    	// Try to find a Transaction by all fake parameters:
        exists = transactionService.transactionExists(fakeAccount, fakeTransactionTypeId);
        isTrue(!exists);
        
        // Pass invalid parameters:
        try {
        	transactionService.transactionExists(null, fakeTransactionTypeId);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(fakeAccount, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
    }
    
    @Test
    public void testTransactionExistsByTransactionTypeAndAmount() throws Exception {
    	// Create a new Transaction:
        String transactionTypeId = "1020";
        String accountId = "admin";
        BigDecimal amount = new BigDecimal(10e3);
        Date effectiveDate = new Date();
        
        transactionService.createTransaction(transactionTypeId, accountId, effectiveDate, amount);
        
        // Call the service:
        boolean exists = transactionService.transactionExists(accountId, transactionTypeId, amount);
        
        isTrue(exists);
    	
    	// Try to find a Transaction by a fake Account:
        String fakeAccount = "fake";
        
        exists = transactionService.transactionExists(fakeAccount, transactionTypeId, amount);
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake Transaction ID:
        String fakeTransactionTypeId = "somethingelse";
        
        exists = transactionService.transactionExists(accountId, fakeTransactionTypeId, amount);
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake amount:
        BigDecimal fakeAmount = new BigDecimal(1.0);
        
        exists = transactionService.transactionExists(accountId, transactionTypeId, fakeAmount);
        isTrue(!exists);
        
    	// Try to find a Transaction by all fake parameters:
        exists = transactionService.transactionExists(fakeAccount, fakeTransactionTypeId, fakeAmount);
        isTrue(!exists);
        
        // Pass invalid parameters:
        try {
        	transactionService.transactionExists(null, fakeTransactionTypeId, fakeAmount);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(fakeAccount, null, fakeAmount);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(fakeAccount, fakeTransactionTypeId, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
    }
    
    @Test
    public void testTransactionExistsByTransactionTypeAndEffectiveDate() throws Exception {
    	// Create a new Transaction:
        String transactionTypeId = "1020";
        String accountId = "admin";
        BigDecimal amount = new BigDecimal(10e3);
        Date effectiveDate = new Date();
        
        transactionService.createTransaction(transactionTypeId, accountId, effectiveDate, amount);
        
        // Prepare input Date parameters:
		Calendar newDateFrom = Calendar.getInstance();
		int newDateFromYear = newDateFrom.get(Calendar.YEAR) - 1;
		int newDateFromMonth = Calendar.JULY;
		int newDateFromDay = 18;
		Calendar newDateTo = Calendar.getInstance();
		int newDateToYear = newDateTo.get(Calendar.YEAR) + 1;
		int newDateToMonth = Calendar.JANUARY;
		int newDateToDay = 25;
        
		newDateFrom.set(Calendar.YEAR, newDateFromYear);
		newDateFrom.set(Calendar.MONTH, newDateFromMonth);
		newDateFrom.set(Calendar.DAY_OF_MONTH, newDateFromDay);

		newDateTo.set(Calendar.YEAR, newDateToYear);
		newDateTo.set(Calendar.MONTH, newDateToMonth);
		newDateTo.set(Calendar.DAY_OF_MONTH, newDateToDay);
		
        // Call the service:
        boolean exists = transactionService.transactionExists(accountId, transactionTypeId, newDateFrom.getTime(), newDateTo.getTime());
        
        isTrue(exists);
    	
    	// Try to find a Transaction by a fake Account:
        String fakeAccount = "fake";
        
        exists = transactionService.transactionExists(fakeAccount, transactionTypeId, newDateFrom.getTime(), newDateTo.getTime());
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake Transaction ID:
        String fakeTransactionTypeId = "somethingelse";
        
        exists = transactionService.transactionExists(accountId, fakeTransactionTypeId, newDateFrom.getTime(), newDateTo.getTime());
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake Effective Date:
        Date fakeEffectiveDate = new Date(0);
        
        exists = transactionService.transactionExists(accountId, transactionTypeId, fakeEffectiveDate, fakeEffectiveDate);
        isTrue(!exists);
        
    	// Try to find a Transaction by all fake parameters:
        exists = transactionService.transactionExists(fakeAccount, fakeTransactionTypeId, fakeEffectiveDate, fakeEffectiveDate);
        isTrue(!exists);
        
        // Pass invalid parameters:
        try {
        	transactionService.transactionExists(null, fakeTransactionTypeId, fakeEffectiveDate, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(fakeAccount, null, fakeEffectiveDate, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, fakeEffectiveDate, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, fakeEffectiveDate, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, null, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, null, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
    }
    
    @Test
    public void testTransactionExistsByTransactionTypeAmountAndEffectiveDate() throws Exception {
    	// Create a new Transaction:
        String transactionTypeId = "1020";
        String accountId = "admin";
        BigDecimal amount = new BigDecimal(10e3);
        Date effectiveDate = new Date();
        
        transactionService.createTransaction(transactionTypeId, accountId, effectiveDate, amount);
        
        // Prepare input Date parameters:
		Calendar newDateFrom = Calendar.getInstance();
		int newDateFromYear = newDateFrom.get(Calendar.YEAR) - 1;
		int newDateFromMonth = Calendar.JULY;
		int newDateFromDay = 18;
		Calendar newDateTo = Calendar.getInstance();
		int newDateToYear = newDateTo.get(Calendar.YEAR) + 1;
		int newDateToMonth = Calendar.JANUARY;
		int newDateToDay = 25;
        
		newDateFrom.set(Calendar.YEAR, newDateFromYear);
		newDateFrom.set(Calendar.MONTH, newDateFromMonth);
		newDateFrom.set(Calendar.DAY_OF_MONTH, newDateFromDay);

		newDateTo.set(Calendar.YEAR, newDateToYear);
		newDateTo.set(Calendar.MONTH, newDateToMonth);
		newDateTo.set(Calendar.DAY_OF_MONTH, newDateToDay);
		
        // Call the service:
        boolean exists = transactionService.transactionExists(accountId, transactionTypeId, newDateFrom.getTime(), newDateTo.getTime());
        
        isTrue(exists);
    	
    	// Try to find a Transaction by a fake Account:
        String fakeAccount = "fake";
        
        exists = transactionService.transactionExists(fakeAccount, transactionTypeId, amount, newDateFrom.getTime(), newDateTo.getTime());
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake Transaction ID:
        String fakeTransactionTypeId = "somethingelse";
        
        exists = transactionService.transactionExists(accountId, fakeTransactionTypeId, amount, newDateFrom.getTime(), newDateTo.getTime());
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake amount:
        BigDecimal fakeAmount = new BigDecimal(1.0);
        
        exists = transactionService.transactionExists(accountId, transactionTypeId, fakeAmount, newDateFrom.getTime(), newDateTo.getTime());
        isTrue(!exists);
        
    	// Try to find a Transaction by a fake Effective Date:
        Date fakeEffectiveDate = new Date(0);
        
        exists = transactionService.transactionExists(accountId, transactionTypeId, amount, fakeEffectiveDate, fakeEffectiveDate);
        isTrue(!exists);
        
    	// Try to find a Transaction by all fake parameters:
        exists = transactionService.transactionExists(fakeAccount, fakeTransactionTypeId, fakeAmount, fakeEffectiveDate, fakeEffectiveDate);
        isTrue(!exists);
        
        // Pass invalid parameters:
        try {
        	transactionService.transactionExists(null, fakeTransactionTypeId, fakeAmount, fakeEffectiveDate, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(fakeAccount, null, fakeAmount, fakeEffectiveDate, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, amount, fakeEffectiveDate, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, null, fakeEffectiveDate, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, null, fakeEffectiveDate, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, null, null, fakeEffectiveDate);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
        
        try {
        	transactionService.transactionExists(null, null, null, null, null);
        	isTrue(false); // should not even get here
        } catch (Exception e) {}
    }

}
