package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.exception.AccountTypeNotFoundException;
import com.sigmasys.kuali.ksa.exception.UserNotFoundException;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.ActivityService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.jaxb.Ach;
import com.sigmasys.kuali.ksa.util.CalendarUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Account service implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("accountService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AccountServiceImpl extends GenericPersistenceService implements AccountService, BeanFactoryAware {

    private static final Log logger = LogFactory.getLog(AccountServiceImpl.class);

    private static final String GET_FULL_ACCOUNTS_QUERY = "select distinct a from Account a " +
            "left outer join fetch a.personNames pn " +
            "left outer join fetch a.postalAddresses pa " +
            "left outer join fetch a.electronicContacts ec " +
            "left outer join fetch a.statusType st " +
            "left outer join fetch a.latePeriod lp " +
            "where pn.default = true and " +
            "      pa.default = true and " +
            "      ec.default = true";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ActivityService activityService;


    private PlatformTransactionManager transactionManager;
    private DefaultTransactionDefinition transactionDefinition;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        transactionManager = beanFactory.getBean("transactionManager", PlatformTransactionManager.class);
        transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    /**
     * This process creates a temporary subset of the account as if the account were being administered
     * as a balance forward account. This permits aging the account in a way that is not affected by the
     * payment application methodology. This temporary array is passed to the ageDebt() method.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return a list of pairs [Debit, BigDecimal]
     */
    @Override
    public List<Pair<Debit, BigDecimal>> rebalance(String userId, boolean ignoreDeferment) {

        Query query = em.createQuery("select t from Transaction t " +
                " where t.account.id = :userId and " +
                "       t.effectiveDate <= CURRENT_DATE and type(t) in (:chargeCode) " +
                " order by t.effectiveDate desc");

        query.setParameter("userId", userId);
        query.setParameter("chargeCode", Charge.class);

        List<Debit> debits = query.getResultList();

        List<Pair<Debit, BigDecimal>> balancedDebits = new LinkedList<Pair<Debit, BigDecimal>>();

        // Distributing balance due among the most recent debits
        BigDecimal dueBalance = getDueBalance(userId, ignoreDeferment);
        BigDecimal remainingBalance = dueBalance;

        for (Debit debit : debits) {
            BigDecimal amount = debit.getAmount() != null ? debit.getAmount() : BigDecimal.ZERO;
            if (amount.compareTo(remainingBalance) < 0) {
                balancedDebits.add(new Pair<Debit, BigDecimal>(debit, amount));
                remainingBalance = remainingBalance.subtract(amount);
            } else {
                balancedDebits.add(new Pair<Debit, BigDecimal>(debit, remainingBalance));
                break;
            }
        }

        // Checking that the due balance equals the sum of split amounts
        BigDecimal accountBalance = BigDecimal.ZERO;
        for (Pair<Debit, BigDecimal> pair : balancedDebits) {
            accountBalance = accountBalance.add(pair.getB());
        }

        if (accountBalance.compareTo(dueBalance) != 0) {
            String errMsq = "The balance due " + dueBalance + " does not match " +
                    "the summarized distributed amount " + accountBalance;
            logger.error(errMsq);
            throw new IllegalStateException(errMsq);
        }

        return balancedDebits;

    }

    /**
     * Aging debts for all chargeable accounts in KSA
     *
     * @param ignoreDeferment boolean value
     */
    @Override
    @Transactional(readOnly = false)
    public void ageDebt(boolean ignoreDeferment) {
        List<Account> accounts = getFullAccounts();
        for (Account account : accounts) {
            if (account instanceof ChargeableAccount) {
                ageDebt((ChargeableAccount) account, ignoreDeferment);
            }
        }
    }

    protected ChargeableAccount ageDebt(ChargeableAccount chargeableAccount, boolean ignoreDeferment) {

        LatePeriod latePeriod = chargeableAccount.getLatePeriod();

        final Date curDate = new Date();
        final Date lateDate1 = CalendarUtils.addCalendarDays(curDate, -latePeriod.getDaysLate1());
        final Date lateDate2 = CalendarUtils.addCalendarDays(curDate, -latePeriod.getDaysLate2());
        final Date lateDate3 = CalendarUtils.addCalendarDays(curDate, -latePeriod.getDaysLate3());

        BigDecimal lateAmount1 = BigDecimal.ZERO;
        BigDecimal lateAmount2 = BigDecimal.ZERO;
        BigDecimal lateAmount3 = BigDecimal.ZERO;

        List<Pair<Debit, BigDecimal>> balancedDebits = rebalance(chargeableAccount.getId(), ignoreDeferment);
        for (Pair<Debit, BigDecimal> pair : balancedDebits) {
            Debit debit = pair.getA();
            BigDecimal amount = pair.getB();
            if (debit.getEffectiveDate().compareTo(lateDate1) <= 0 &&
                    debit.getEffectiveDate().compareTo(lateDate2) > 0) {
                lateAmount1 = lateAmount1.add(amount);
            } else if (debit.getEffectiveDate().compareTo(lateDate2) <= 0 &&
                    debit.getEffectiveDate().compareTo(lateDate3) > 0) {
                lateAmount2 = lateAmount2.add(amount);
            } else if (debit.getEffectiveDate().compareTo(lateDate3) <= 0) {
                lateAmount3 = lateAmount3.add(amount);
            }
        }

        chargeableAccount.setAmountLate1(lateAmount1);
        chargeableAccount.setAmountLate2(lateAmount2);
        chargeableAccount.setAmountLate3(lateAmount3);
        chargeableAccount.setLateLastUpdate(curDate);

        persistEntity(chargeableAccount);

        return chargeableAccount;
    }


    /**
     * Aging debts for a chargeable account.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return a chargeable account being updated
     */
    @Override
    @Transactional(readOnly = false)
    public ChargeableAccount ageDebt(String userId, boolean ignoreDeferment) {

        Account account = getFullAccount(userId);
        if (account == null) {
            String errMsg = "Account with ID '" + userId + "' does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        if (!(account instanceof ChargeableAccount)) {
            String errMsg = "Account must be of '" + ChargeableAccount.class.getName() + "' type";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        return ageDebt((ChargeableAccount) account, ignoreDeferment);
    }

    /**
     * Returns the total balance due of all active transactions.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return total amount of balance due
     */
    @Override
    public BigDecimal getDueBalance(String userId, boolean ignoreDeferment) {
        return getBalance(userId, ignoreDeferment, false);
    }

    /**
     * Returns the outstanding balance for the given account
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return total amount of outstanding balance
     */
    @Override
    public BigDecimal getOutstandingBalance(String userId, boolean ignoreDeferment) {
        return getBalance(userId, ignoreDeferment, true);
    }

    public BigDecimal getBalance(String userId, boolean ignoreDeferment, boolean notYetEffective) {
        final String sign = notYetEffective ? ">" : "<=";
        Query query = em.createQuery("select t from Transaction t " +
                " where t.account.id = :userId and t.effectiveDate " + sign + " CURRENT_DATE");
        query.setParameter("userId", userId);
        List<Transaction> transactions = query.getResultList();
        BigDecimal amountBilled = BigDecimal.ZERO;
        BigDecimal amountPaid = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction instanceof Debit) {
                Debit debit = (Debit) transaction;
                BigDecimal amount = debit.getAmount() != null ? debit.getAmount() : BigDecimal.ZERO;
                amountBilled = amountBilled.add(amount);
            } else if (transaction instanceof Credit) {
                boolean includeAmount = true;
                if (transaction instanceof Deferment) {
                    if (ignoreDeferment || ((Deferment) transaction).isExpired()) {
                        includeAmount = false;
                    }
                }
                if (includeAmount) {
                    Credit credit = (Credit) transaction;
                    BigDecimal allocatedAmount = credit.getAllocatedAmount();
                    if (allocatedAmount == null) {
                        allocatedAmount = BigDecimal.ZERO;
                    }
                    BigDecimal lockedAllocatedAmount = credit.getLockedAllocatedAmount();
                    if (lockedAllocatedAmount == null) {
                        lockedAllocatedAmount = BigDecimal.ZERO;
                    }
                    amountPaid = amountPaid.add(allocatedAmount);
                    amountPaid = amountPaid.add(lockedAllocatedAmount);
                }
            }
        }
        return amountBilled.subtract(amountPaid);
    }

    /**
     * Returns unallocated balance for the given Account ID
     *
     * @param userId Account ID
     * @return unallocated balance
     */
    @Override
    public BigDecimal getUnallocatedBalance(String userId) {
        List<Payment> payments = transactionService.getPayments(userId);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Payment payment : payments) {
            BigDecimal amount = (payment.getAmount() != null) ? payment.getAmount() : BigDecimal.ZERO;
            BigDecimal allocatedAmount = (payment.getAllocatedAmount() != null) ?
                    payment.getAllocatedAmount() : BigDecimal.ZERO;
            BigDecimal lockedAllocatedAmount = (payment.getLockedAllocatedAmount() != null) ?
                    payment.getLockedAllocatedAmount() : BigDecimal.ZERO;
            BigDecimal difference = amount.subtract(allocatedAmount.add(lockedAllocatedAmount));
            if (difference.compareTo(BigDecimal.ZERO) > 0) {
                totalAmount = totalAmount.add(difference);
            }
        }
        return totalAmount;
    }

    /**
     * Returns the deferred amount
     *
     * @param userId Account ID
     * @return deferred amount
     */
    @Override
    public BigDecimal getDeferredAmount(String userId) {
        List<Deferment> deferments = transactionService.getDeferments(userId);
        BigDecimal totalAmount = BigDecimal.ZERO;
        Date curDate = new Date();
        for (Deferment deferment : deferments) {
            Date expirationDate = deferment.getExpirationDate();
            if (expirationDate == null || curDate.before(expirationDate)) {
                totalAmount = totalAmount.add(deferment.getAmount());
            }
        }
        return totalAmount;
    }

    /**
     * Checks if KSA account exists
     *
     * @param userId Account ID
     * @return true if the account exists, false otherwise
     */
    @Override
    public boolean ksaAccountExists(String userId) {
        return getEntity(userId, Account.class) != null;
    }

    /**
     * Checks if KSA account exists. If the KSA account does not exist, it tries to look for the existing KIM account
     * and create a new KSA account, if the account does not exist returns false, otherwise true.
     *
     * @param userId Account ID
     * @return true if the account exists, false otherwise
     */
    @Override
    public boolean accountExists(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            try {
                return getOrCreateAccount(userId) != null;
            } catch (UserNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * This methods fetches Account and all its associations by account ID.
     *
     * @param userId Account ID
     * @return the account instance or null if the account does not exist
     */
    @Override
    public Account getFullAccount(String userId) {
        Query query = em.createQuery("select a from Account a " +
                "left outer join fetch a.personNames pn " +
                "left outer join fetch a.postalAddresses pa " +
                "left outer join fetch a.electronicContacts ec " +
                "left outer join fetch a.statusType st " +
                "left outer join fetch a.latePeriod lp " +
                "where a.id = :id");
        query.setParameter("id", userId);
        List<Account> accounts = query.getResultList();
        return (accounts != null && !accounts.isEmpty()) ? accounts.get(0) : null;
    }

    /**
     * This methods fetches all KSA accounts and all their associations.
     *
     * @return the list account instances
     */
    @Override
    public List<Account> getFullAccounts() {
        Query query = em.createQuery(GET_FULL_ACCOUNTS_QUERY);
        return query.getResultList();
    }

    /**
     * This method is used to verify that an account exists before a transaction or other operations are
     * performed on the account. There is an initial inquiry into the KSA store. If no account exist, then there is
     * an inquiry into KIM. If KIM also returns no result, then false is returned. If a KIM account does exist, then
     * a KSA account is created, using the KIM information as a template.
     *
     * @param userId Account ID
     * @return the account instance or null if the account does not exist
     */
    @Override
    @Transactional(readOnly = false)
    public Account getOrCreateAccount(String userId) {
        Account account = getEntity(userId, Account.class);
        if (account == null) {
            PersonService personService = KimApiServiceLocator.getPersonService();
            Person person = personService.getPersonByPrincipalName(userId);
            if (person == null) {
                String errMsg = "The user '" + person + "' does not exist";
                logger.error(errMsg);
                throw new UserNotFoundException(errMsg);
            }
            // If the person exists in KIM we have to create a new KSA account based on that
            account = createAccount(person);
        }
        return account;
    }

    private Account createAccount(Person person) {

        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);

        try {

            // TODO - populate the missing fields
            // TODO: figure out how to distinguish Delegate and DirectCharge account types

            String creatorId = null;

            HttpServletRequest request = RequestUtils.getThreadRequest();
            if (request != null) {
                creatorId = userSessionManager.getUserId(request);
            }

            if (creatorId == null) {
                creatorId = "system";
            }

            final Date creationDate = new Date();

            Account account = new DirectChargeAccount();

            account.setId(person.getPrincipalName());

            account.setCreatorId(creatorId);
            account.setCreationDate(creationDate);

            account.setAbleToAuthenticate(true);
            account.setEntityId(person.getEntityId());
            account.setKimAccount(true);
            account.setCreditLimit(new BigDecimal(0.0));

            PersonName personName = new PersonName();
            personName.setCreatorId(creatorId);
            personName.setLastUpdate(creationDate);
            personName.setDefault(true);
            personName.setFirstName(person.getFirstName());
            personName.setMiddleName(person.getMiddleName());
            personName.setLastName(person.getLastName());
            personName.setKimNameType(person.getEntityTypeCode());
            personName.setDefault(true);

            // Making PersonName persistent and generate ID
            persistEntity(personName);

            PostalAddress address = new PostalAddress();
            address.setCreatorId(creatorId);
            address.setLastUpdate(creationDate);
            address.setDefault(true);
            address.setPostalCode(person.getAddressPostalCode());
            address.setCountry(person.getAddressCountryCode());
            address.setState(person.getAddressStateProvinceCode());
            address.setCity(person.getAddressCity());
            address.setStreetAddress1(person.getAddressLine1());
            address.setStreetAddress2(person.getAddressLine2());
            address.setStreetAddress3(person.getAddressLine3());

            // Making PostalAddress persistent and generate ID
            persistEntity(address);

            ElectronicContact electronicContact = new ElectronicContact();
            electronicContact.setCreatorId(creatorId);
            electronicContact.setLastUpdate(creationDate);
            electronicContact.setDefault(true);
            electronicContact.setEmailAddress(person.getEmailAddress());
            electronicContact.setPhoneNumber(person.getPhoneNumber());
            electronicContact.setPhoneCountry(person.getAddressCountryCode());

            // Making ElectronicContact persistent and generate ID
            persistEntity(electronicContact);

            // Setting references to Account
            account.setPersonNames(new HashSet<PersonName>(Arrays.asList(personName)));
            account.setPostalAddresses(new HashSet<PostalAddress>(Arrays.asList(address)));
            account.setElectronicContacts(new HashSet<ElectronicContact>(Arrays.asList(electronicContact)));

            // "Account is in good standing" (Paul) ID = 1
            AccountStatusType statusType = getEntity(1L, AccountStatusType.class);
            if (statusType != null) {
                account.setStatusType(statusType);
            }

            // Late Period with ID = 1
            LatePeriod latePeriod = getEntity(1L, LatePeriod.class);
            if (latePeriod != null) {
                account.setLatePeriod(latePeriod);
            }

            // Making Account persistent
            persistEntity(account);

            // Linking PersonName, PostalAddress and ElectronicContact back to already persisted Account
            //personName.setAccount(account);
            //address.setAccount(account);
            //electronicContact.setAccount(account);

            transactionManager.commit(transaction);

            return account;

        } catch (Throwable t) {
            transactionManager.rollback(transaction);
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t);
        }

    }

    /**
     * Creates and associates a new person name object with the given Account ID.
     *
     * @param userId     Account ID
     * @param personName Person name
     * @return new PersonName instance with ID
     */
    @Override
    @Transactional(readOnly = false)
    public PersonName addPersonName(String userId, PersonName personName) {

        Account account = getFullAccount(userId);
        if (account == null) {
            String errMsg = "Account with ID = " + userId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Set<PersonName> personNames = account.getPersonNames();
        if (personNames == null) {
            personNames = new HashSet<PersonName>();
        }

        if (personName.isDefault() != null && personName.isDefault()) {
            for (PersonName name : personNames) {
                name.setDefault(false);
            }
        }

        personName.setCreatorId(userSessionManager.getUserId(RequestUtils.getThreadRequest()));
        personName.setLastUpdate(new Date());
        //personName.setAccount(account);

        persistEntity(personName);

        personNames.add(personName);
        account.setPersonNames(personNames);

        return personName;
    }

    /**
     * Creates and associates a new postal address object with the given Account ID.
     *
     * @param userId        Account ID
     * @param postalAddress Postal address
     * @return new PostalAddress instance with ID
     */
    @Override
    @Transactional(readOnly = false)
    public PostalAddress addPostalAddress(String userId, PostalAddress postalAddress) {

        Account account = getFullAccount(userId);
        if (account == null) {
            String errMsg = "Account with ID = " + userId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Set<PostalAddress> addresses = account.getPostalAddresses();
        if (addresses == null) {
            addresses = new HashSet<PostalAddress>();
        }

        if (postalAddress.isDefault() != null && postalAddress.isDefault()) {
            for (PostalAddress address : addresses) {
                address.setDefault(false);
            }
        }

        postalAddress.setCreatorId(userSessionManager.getUserId(RequestUtils.getThreadRequest()));
        postalAddress.setLastUpdate(new Date());
        //postalAddress.setAccount(account);

        persistEntity(postalAddress);

        addresses.add(postalAddress);
        account.setPostalAddresses(addresses);

        return postalAddress;
    }

    /**
     * Creates and associates a new electronic contact with the given Account ID.
     *
     * @param userId            Account ID
     * @param electronicContact Electronic contact
     * @return new ElectronicContact instance with ID
     */
    @Override
    @Transactional(readOnly = false)
    public ElectronicContact addElectronicContact(String userId, ElectronicContact electronicContact) {

        Account account = getFullAccount(userId);
        if (account == null) {
            String errMsg = "Account with ID = " + userId + " does not exist";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Set<ElectronicContact> contacts = account.getElectronicContacts();
        if (contacts == null) {
            contacts = new HashSet<ElectronicContact>();
        }

        if (electronicContact.isDefault() != null && electronicContact.isDefault()) {
            for (ElectronicContact contact : contacts) {
                contact.setDefault(false);
            }
        }

        electronicContact.setCreatorId(userSessionManager.getUserId(RequestUtils.getThreadRequest()));
        electronicContact.setLastUpdate(new Date());
        //electronicContact.setAccount(account);

        persistEntity(electronicContact);

        contacts.add(electronicContact);
        account.setElectronicContacts(contacts);

        return electronicContact;

    }

    /**
     * Get ACH looks into the AccountProtectedInformation class (which triggers a system event) to look for
     * the ACH information for the user
     *
     * @param userId Account ID
     * @return Ach instance
     */
    @Override
    public Ach getAch(String userId) throws AccountTypeNotFoundException {

        String errMsg = "ACH Account with ID = " + userId + " does not exist";

        AccountProtectedInfo account = getAccountProtectedInfo(userId);
        if (account == null) {
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        String achBankType = configService.getParameter(Constants.REFUND_ACH_BANK_TYPE);
        if (achBankType == null) {
            errMsg = "Refund ACH Bank Type '" + achBankType + "' does not exist";
            throw new IllegalArgumentException(errMsg);
        }

        String details = account.getBankDetails();
        String[] detailsArr = details.split(":");

        if (detailsArr.length < 3) {
            logger.error(errMsg);
            throw new AccountTypeNotFoundException(errMsg);
        }
        String routingNumber = detailsArr[0];
        String act = detailsArr[1];
        String type = detailsArr[2];

        if (!("C".equals(type) || "S".equals(type))) {
            logger.error(errMsg);
            throw new AccountTypeNotFoundException(errMsg);
        }

        Pattern p = Pattern.compile("^(\\d{7}|\\d{9})$");
        Matcher m = p.matcher(routingNumber);
        if (!m.matches()) {
            errMsg = "Routing number '" + routingNumber + "' is invalid";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        Ach ach = new Ach();
        ach.setAccountType(type);
        ach.setAccountNumber(act);
        ach.setRoutingNumber(routingNumber);

        return ach;
    }

    /**
     * Returns the account protected information by user ID.
     *
     * @param userId Account ID
     * @return AccountProtectedInfo instance
     */
    @Override
    public AccountProtectedInfo getAccountProtectedInfo(String userId) {
        Query query = em.createQuery("select a from AccountProtectedInfo a " +
                " left outer join fetch a.bankType" +
                " left outer join fetch a.taxType" +
                " left outer join fetch a.identityType" +
                " where a.id = :id");
        query.setParameter("id", userId);
        List<AccountProtectedInfo> protectedInfoList = query.getResultList();
        if (CollectionUtils.isNotEmpty(protectedInfoList)) {
            AccountProtectedInfo accountInfo = protectedInfoList.get(0);
            if (accountInfo != null) {
                Activity activity = new Activity();
                activity.setAccountId(userId);
                activity.setEntityId(accountInfo.getId());
                activity.setEntityType(AccountProtectedInfo.class.getSimpleName());
                activity.setIpAddress(RequestUtils.getClientIpAddress());
                activity.setLogDetail("Account Protected Info access [" + userId + "]");
                activityService.persistActivity(activity);
            }
            return accountInfo;
        }
        return null;
    }

    /**
     * Returns the list of matching accounts for the given name pattern.
     *
     * @param namePattern Name pattern
     * @return List of Account instances
     */
    @Override
    public List<Account> findAccountsByNamePattern(String namePattern) {

        boolean patternIsNotEmpty = (namePattern != null) && !namePattern.isEmpty();

        StringBuilder builder = new StringBuilder(GET_FULL_ACCOUNTS_QUERY);

        if (patternIsNotEmpty) {
            builder.append(" and (lower(a.id) like :pattern or lower(pn.firstName) like :pattern or " +
                    "lower(pn.middleName) like :pattern or lower(pn.lastName) like :pattern)");
        }

        builder.append(" order by a.id");

        Query query = em.createQuery(builder.toString());

        if (patternIsNotEmpty) {
            query.setParameter("pattern", "%" + namePattern.toLowerCase() + "%");
        }

        return query.getResultList();
    }

}
