package com.sigmasys.kuali.ksa.service.fm.impl;

import java.util.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.Query;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.fm.*;
import com.sigmasys.kuali.ksa.model.security.Permission;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.service.fm.FeeManagementService;
import com.sigmasys.kuali.ksa.service.impl.GenericPersistenceService;
import com.sigmasys.kuali.ksa.service.security.PermissionUtils;
import com.sigmasys.kuali.ksa.util.BeanUtils;

/**
 * FeeManagementService implementation.
 * <p/>
 * <p/>
 * This class provides a concrete implementation of the <code>FeeManagementService</code> interface
 * according to the specification in the "Process Diagrams" document
 *
 * @author Sergey Godunov
 */
@Service("feeManagementService")
@Transactional(readOnly = true)
@WebService(serviceName = FeeManagementService.SERVICE_NAME, portName = FeeManagementService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class FeeManagementServiceImpl extends GenericPersistenceService implements FeeManagementService {

    /**
     * This class's logger.
     */
    private static final Log logger = LogFactory.getLog(FeeManagementServiceImpl.class);

    // A query to get Manifests without regard to statuses. Status filtering can be added later:
    private static final String GET_MANIFESTS_JOIN = "select m from FeeManagementManifest m " +
            " left outer join fetch m.keyPairs kp " +
            " left outer join fetch m.tags t " +
            " left outer join fetch m.rate r " +
            " left outer join fetch m.rollup ru " +
            " left outer join fetch m.linkedManifest lm ";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuditableEntityService auditableEntityService;


    /**
     * Reconciles a FeeManagement session.
     * Session reconciliation takes former sessions and ensures that charges that need to be removed are
     * accurately recorded in the new session.
     *
     * @param feeManagementSessionId FeeManagementSession ID
     */
    @Override
    @Transactional(readOnly = false)
    public void reconcileSession(Long feeManagementSessionId) {

        // Get a FeeManagement session with the given ID:
        FeeManagementSession currentSession = getEntity(feeManagementSessionId, FeeManagementSession.class);

        if (currentSession == null) {
            logger.error("Cannot find an FM session with the ID " + feeManagementSessionId);
            throw new IllegalArgumentException("Cannot find an FM session with the ID " + feeManagementSessionId);
        }

        // Check the session passed is CURRENT or SIMULATED
        FeeManagementSessionStatus sessionStatus = currentSession.getStatus();

        if (sessionStatus == null || ((sessionStatus != FeeManagementSessionStatus.CURRENT) && (sessionStatus != FeeManagementSessionStatus.SIMULATED))) {
            String errorMsg = String.format("Invalid FeeManagement Session status [%s]. Session status must be CURRENT or SIMULATED.", sessionStatus);
            logger.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        // Get the associated Account:
        Account account = currentSession.getAccount();

        if (account == null) {
            String errorMsg = String.format("Fee Management Session with id [%s] does not have an associated Account.", feeManagementSessionId);
            logger.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        } else {
            // Check if the Account associated with the FM Session is blocked:
            boolean accountBlocked = isAccountBlocked(account);

            if (accountBlocked) {
                String errorMsg = String.format("The account %s associated with the FM session with id [%s] is blocked.", account.getId(), feeManagementSessionId);
                logger.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
        }

        // Get the last CHARGED FM Session:
        FeeManagementSession lastChargedFmSession = getLastChargedSession(currentSession);
        
        // Get current FM Session's manifests:
        List<FeeManagementManifest> currentManifests = getManifests(currentSession.getId(),
                FeeManagementManifestType.CHARGE, FeeManagementManifestType.CANCELLATION, FeeManagementManifestType.DISCOUNT);

        // If there is a prior CHARGED FM Session, perform line adjustment:
        if (lastChargedFmSession != null) {
            // Perform line comparison and status adjustment.
            processPriorSessionAdjustment(currentSession, lastChargedFmSession, currentManifests);
        }
        
        // Optionally, remove inverse manifests from the list of current manifests:
        boolean preclearCurrentManifests = BooleanUtils.toBoolean(configService.getParameter(Constants.KSA_FM_PRECLEAR_MANIFEST));
        
        if (preclearCurrentManifests) {
        	// Remove inverse manifests:
        	removeInverseManifests(currentManifests);
        }

        // Validate reversal manifests of the current FM session:
        validateCurrentReversals(currentManifests);

        // Updated the status of the current FM Session to RECONCILED or SIMULATED_RECONCILED:
        FeeManagementSessionStatus newFmStatus = (sessionStatus == FeeManagementSessionStatus.CURRENT)
                ? FeeManagementSessionStatus.RECONCILED : FeeManagementSessionStatus.SIMULATED_RECONCILED;

        // Update the entity:
        currentSession.setChargeStatus(newFmStatus);
        persistEntity(currentSession);
    }

    /**
     * Creates charges on the current Fee Management session.
     * Creates reversals and cancellations and also rate transactions.
     *
     * @param feeManagementSessionId FeeManagementSession ID
     */
    @Override
    @Transactional(readOnly = false)
    public void chargeSession(Long feeManagementSessionId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Returns all FM Manifests associated with an FM Session with the given ID.
     *
     * @param feeManagementSessionId An FM Session ID.
     * @return All associated FM Manifests.
     */
    @Override
    public List<FeeManagementManifest> getManifests(Long feeManagementSessionId) {
        return getManifests(feeManagementSessionId, (FeeManagementManifestType[]) null);
    }

    /**
     * Returns all FM Manifests of given types associated with an FM Session with the given ID.
     *
     * @param feeManagementSessionId An FM Session ID.
     * @param manifestTypes          Types of FM Manifests to retrieve.
     * @return All associated FM Manifests of given types.
     */
    @Override
    @WebMethod(exclude = true)
    public List<FeeManagementManifest> getManifests(Long feeManagementSessionId, FeeManagementManifestType... manifestTypes) {

        // Check permissions to perform this operation:
        PermissionUtils.checkPermission(Permission.READ_RATE);

        boolean typesExist = (manifestTypes != null && manifestTypes.length > 0);

        List<String> typeCodes = null;

        if (typesExist) {
            typeCodes = new ArrayList<String>(manifestTypes.length);
            for (FeeManagementManifestType manifestType : manifestTypes) {
                typeCodes.add(manifestType.getId());
            }
        }

        // Create a query to select all FM Manifests linked to the given FM Session:
        Query query = em.createQuery(GET_MANIFESTS_JOIN + " where m.session.id = :fmSessionId" +
                (typesExist ? " and m.typeCode in (:typeCodes)" : ""));

        query.setParameter("fmSessionId", feeManagementSessionId);

        if (typesExist) {
            query.setParameter("typeCodes", typeCodes);
        }

        return query.getResultList();
    }


    /***************************************************************************
     *
     * Private helper methods.
     *
     ***************************************************************************/

    /**
     * A recursive method that checks all previous FM Sessions in succession in
     * order to find the last CHARGED session.
     *
     * @param fmSession A FeeManagement Session to find its last CHARGED prior FM Session.
     * @return The last prior CHARGED FM session or <code>null</code> if there is no last prior
     *         CHARGED FM Session.
     */
    private FeeManagementSession getLastChargedSession(FeeManagementSession fmSession) {
        // Check if the current session is null or its status is CHARGED, end the recursion here:
        if ((fmSession == null) || (fmSession.getStatus() == FeeManagementSessionStatus.CHARGED)) {
            return fmSession;
        } else {
            // Get the prior session and continue the recursion:
            return getLastChargedSession(fmSession.getPrevSession());
        }
    }

    /**
     * Checks if the Account associated with the given FM Session is blocked.
     *
     * @param account Account associated with a Fee Management Session.
     * @return boolean Whether the Account associated with the FM Session is blocked.
     */
    private boolean isAccountBlocked(Account account) {
        // TODO: Perform a check for a blocked account. This method will be defined and implemented later (per Paul):
        boolean accountBlocked = false;

        return accountBlocked;
    }

    /**
     * Performs adjustment of lines between the last charged FM Session and the current session.
     *
     * @param currentFmSession 	Current FM Session.
     * @param priorFmSession   	Last FM Session in the CHARGED status. Guaranteed not to be null.
     * @param currentManifests	A list of current FM session's manifests.
     */
    private void processPriorSessionAdjustment(FeeManagementSession currentFmSession, FeeManagementSession priorFmSession, List<FeeManagementManifest> currentManifests) {
        // Get the prior and current FM sessions' manifests:
        List<FeeManagementManifest> priorManifests = getManifests(priorFmSession.getId(),
                FeeManagementManifestType.CHARGE, FeeManagementManifestType.CANCELLATION, FeeManagementManifestType.DISCOUNT);

        // Get pairs of matching FM manifests in the prior and the current FM sessions:
        List<FeeManagementManifest> unmatchedPriorManifests = new ArrayList<FeeManagementManifest>();
        List<Pair<FeeManagementManifest, FeeManagementManifest>> matchingManifests = getMatchingManifests(priorManifests, currentManifests, unmatchedPriorManifests);

        // Process matching manifest pairs:
        for (Pair<FeeManagementManifest, FeeManagementManifest> matchingPair : matchingManifests) {
            processMatchingManifests(matchingPair, currentFmSession);
        }

        // Process unmatched prior manifests:
        processUnmatchedPriorManifest(unmatchedPriorManifests, currentFmSession);
    }

    /**
     * Returns a <code>List</code> of matching <code>FeeManagementManifest</code>s that match in both prior and current manifest lists.
     * Matching manifests is done by either:
     * 1. registrationId + offeringId + rate, or
     * 2. internalChargeId + rate.
     *
     * @param priorManifests          Prior FM session's manifests.
     * @param currentManifests        Current FM session's manifests.
     * @param unmatchedPriorManifests A List to be populated with unmatched prior FM session's manifests. This is an IN parameter and must not be null.
     * @return A List of matching manifest pairs. The "a" property of the <code>Pair</code> is the prior manifest, and "b" is the current one.
     */
    private List<Pair<FeeManagementManifest, FeeManagementManifest>> getMatchingManifests(List<FeeManagementManifest> priorManifests,
                                                                                          List<FeeManagementManifest> currentManifests, List<FeeManagementManifest> unmatchedPriorManifests) {
        // Create the resulting list:
        List<Pair<FeeManagementManifest, FeeManagementManifest>> matchingManifests = new ArrayList<Pair<FeeManagementManifest, FeeManagementManifest>>();

        // Create a copy of the prior manifest List since we'll be removing matched prior manifests:
        List<FeeManagementManifest> priorManifestsCopy = new ArrayList<FeeManagementManifest>(priorManifests);

        // Iterate through the prior manifests trying find a match in the current manifest list:
        for (Iterator<FeeManagementManifest> itPrior = priorManifestsCopy.iterator(); itPrior.hasNext(); ) {
            FeeManagementManifest priorManifest = itPrior.next();

            // Iterate through the list of current manifests trying to find a match:
            for (FeeManagementManifest currentManifest : currentManifests) {
                if (manifestsMatch(priorManifest, currentManifest, true)) {
                    // Add a new matching pair, remove the matching prior manifest from the underlying list and break out of the loop:
                    matchingManifests.add(new Pair<FeeManagementManifest, FeeManagementManifest>(priorManifest, currentManifest));
                    itPrior.remove();

                    break;
                }
            }
        }

        // Add non-matching prior FM session's manifests to the list of unmatched manifests:
        unmatchedPriorManifests.addAll(priorManifestsCopy);

        return matchingManifests;
    }

    /**
     * Compares two FM manifests, prior and current, and check if they match by either:
     * 1. registrationId + offeringId + rate, or
     * 2. internalChargeId + rate.
     *
     * @param prior             A prior FM session's manifest.
     * @param current           A current FM session's manifest.
     * @param checkRegistration Whether to check the Registration ID>
     * @return    <code>true</code> if the manifests are a match, <code>false</code> otherwise.
     */
    private boolean manifestsMatch(FeeManagementManifest prior, FeeManagementManifest current, boolean checkRegistration) {
        if ((prior != null) && (current != null)) {
            // Check if rates match first:
            boolean ratesMatch = (prior.getRate() != null) && (current.getRate() != null) && prior.getRate().equals(current.getRate());

            // If rates match, check further:
            if (ratesMatch) {
                // Check if Internal Charge IDs match:
                if (StringUtils.equals(prior.getInternalChargeId(), current.getInternalChargeId())) {
                    return true;
                } else if (checkRegistration) {
                    // Check if registration IDs and Offering IDs match:
                    return StringUtils.equals(prior.getRegistrationId(), current.getRegistrationId()) && StringUtils.equals(prior.getOfferingId(), current.getOfferingId());
                } else {
                    // Just check if the Offering IDs match:
                    return StringUtils.equals(prior.getOfferingId(), current.getOfferingId());
                }
            }
        }

        return false;
    }

    /**
     * Processes matching manifests from the prior and current FM sessions by doing one of the following:<br>
     * 1. Either copies the transactionId from the prior manifest to the current one if already charged, or<br>
     * 2. Performs corrections.
     *
     * @param manifests        A <code>Pair</code> of matching manifests, the first one in the pair is the prior session's manifest.
     * @param currentFmSession The current FM session. Used for non-charged matching manifests adjustment.
     */
    private void processMatchingManifests(Pair<FeeManagementManifest, FeeManagementManifest> manifests, FeeManagementSession currentFmSession) {
        // First, check if the payment information on the prior and current manifests match, i.e. current has already been charged:
        FeeManagementManifest prior = manifests.getA();
        FeeManagementManifest current = manifests.getB();
        boolean currentAlreadyCharged = manifestPaymentInformationMatch(prior, current);

        if (currentAlreadyCharged) {
            // Copy the Transaction from the prior to the current:
            current.setTransaction(prior.getTransaction());
            persistEntity(current);
        } else {
            // Create a correction on the current session from the prior manifest:
            createPriorManifestCorrection(prior, currentFmSession);
        }
    }

    /**
     * Checks if the prior and current FM manifests' payment information matches. That includes:<p>
     * 1. effectiveDate<br>
     * 2. recognitionDate<br>
     * 3. transactionTypeId<br>
     * 4. amount
     *
     * @param prior   A prior FM session manifest.
     * @param current A current FM session manifest.
     * @return <code>true</code> if the payment information on both manifests matches.
     */
    private boolean manifestPaymentInformationMatch(FeeManagementManifest prior, FeeManagementManifest current) {
        return (prior.getEffectiveDate() != null) && (current.getEffectiveDate() != null) && prior.getEffectiveDate().equals(current.getEffectiveDate())
                && (prior.getRecognitionDate() != null) && (current.getRecognitionDate() != null) && prior.getRecognitionDate().equals(current.getRecognitionDate())
                && StringUtils.equals(prior.getTransactionTypeId(), current.getTransactionTypeId())
                && safeAmountsEqual(prior, current);
    }

    /**
     * Processes all prior FM session's unmatched manifests (those that do not have matching manifests on the current FM session).
     *
     * @param unmatchedPriorManifests Unmatched prior FM manifests.
     * @param currentFmSession        Current FM session.
     */
    private void processUnmatchedPriorManifest(List<FeeManagementManifest> unmatchedPriorManifests, FeeManagementSession currentFmSession) {
        // Find all full-reversal manifest pairs:
        List<Pair<FeeManagementManifest, FeeManagementManifest>> fullReversalManifests = findReversalManifests(unmatchedPriorManifests, true);

        for (Pair<FeeManagementManifest, FeeManagementManifest> pair : fullReversalManifests) {
            // Remove both manifests from the list of unmatched manifests:
            unmatchedPriorManifests.remove(pair.getA());
            unmatchedPriorManifests.remove(pair.getB());
        }

        // Iterate through the remaining non-full-reversal prior manifests and create a correction on the current FM session out of each one of them:
        for (FeeManagementManifest priorManifest : unmatchedPriorManifests) {
            // Create a correction on the current session from the prior manifest:
            createPriorManifestCorrection(priorManifest, currentFmSession);
        }
    }

    /**
     * Finds all full-reversal FM Manifest pairs in the given list and returns them in a List of Pairs.
     *
     * @param manifests    A list of <code>FeeManagementManifest</code> objects to find full-reversal manifests.
     * @param fullReversal Whether to check for full reversals.
     * @return A List of full-reversal Manifest Pairs
     */
    private List<Pair<FeeManagementManifest, FeeManagementManifest>> findReversalManifests(List<FeeManagementManifest> manifests, boolean fullReversal) {
    	
        List<Pair<FeeManagementManifest, FeeManagementManifest>> reversalManifests = new ArrayList<Pair<FeeManagementManifest, FeeManagementManifest>>();

        // Iterate through a copy of the list of manifests. Begin with the first manifest and try
        // to find its full-reversal counterpart. If one is found, add a Pair, remove them both from
        // the list and start from the same "startIndex":
        List<FeeManagementManifest> copyList = new ArrayList<FeeManagementManifest>(manifests);
        int startIndex = 0;

        while (startIndex < copyList.size() - 1) {
            // Get the first Manifest to match against:
            FeeManagementManifest firstManifest = copyList.get(startIndex);
            boolean matchFound = false;

            for (int i = startIndex + 1, sz = copyList.size(); (i < sz) && !matchFound; i++) {
                // Check if the next manifest is a full-reversal:
                matchFound = isReversal(firstManifest, copyList.get(i), fullReversal);

                if (matchFound) {
                    // Add a Pair, remove both from the list, and start again from the same index:
                    reversalManifests.add(new Pair<FeeManagementManifest, FeeManagementManifest>(firstManifest, copyList.get(i)));
                    copyList.remove(startIndex);
                    copyList.remove(i);
                }
            }

            // If a match not found, increment the start index and keep searching:
            if (!matchFound) {
                startIndex++;
            }
        }

        return reversalManifests;
    }

    /**
     * Checks if the two <code>FeeManagementManifest</code>s are a subject to a reversal, which means that:<p>
     * <p/>
     * 1. The types of the two are CHARGE and CANCEL<br>
     * 2. Manifests match (see {@link #manifestsMatch(FeeManagementManifest, FeeManagementManifest, boolean)}<br>
     * 3. Same transactionTypeId and amount.<br>
     * 4. Their manifests point at each other (optional).<br>
     *
     * @param fmm1         The first FM manifest to check for full reversal.
     * @param fmm2         The second FM manifest to check for full reversal.
     * @param fullReversal Whether to check for full reversals.
     * @return <code>true</code> if the two FM manifests represent a reversal.
     */
    private boolean isReversal(FeeManagementManifest fmm1, FeeManagementManifest fmm2, boolean fullReversal) {

        // First check if the FM manifests point at each other:
        boolean pointAtEachOther = fullReversal
                ? (fmm1.getLinkedManifest() != null) && (fmm2.getLinkedManifest() != null)
                && fmm1.getLinkedManifest().getId().equals(fmm2.getId()) && fmm2.getLinkedManifest().getId().equals(fmm1.getId())
                : true;

        // Check if the types are CHARGE and CANCEL
        if (pointAtEachOther) {
            boolean statusesForReversal = ((fmm1.getType() == FeeManagementManifestType.CHARGE) && (fmm2.getType() == FeeManagementManifestType.CANCELLATION))
                    || ((fmm2.getType() == FeeManagementManifestType.CHARGE) && (fmm1.getType() == FeeManagementManifestType.CANCELLATION));

            // Now check if the manifests match and have the same transactionTypeId and amount:
            if (statusesForReversal && manifestsMatch(fmm1, fmm2, fullReversal)) {
                return fullReversal 
                		? StringUtils.equals(fmm1.getTransactionTypeId(), fmm2.getTransactionTypeId()) && safeAmountsEqual(fmm1, fmm2) 
                				: true;
            }
        }

        return false;
    }

    /**
     * Creates a correction from a prior FM session's manifest into the current FM session.
     *
     * @param priorManifest    A prior FM manifest to create a correction out of.
     * @param currentFmSession Current FM session.
     */
    private void createPriorManifestCorrection(FeeManagementManifest priorManifest, FeeManagementSession currentFmSession) {

        // Clone the prior manifest and add to the current session. Also, create a Correction Manifest:
        FeeManagementManifest priorCopy = copyManifest(priorManifest);
        FeeManagementManifest correctionManifest = copyManifest(priorManifest);

        priorCopy.setSession(currentFmSession);
        priorCopy.setType(FeeManagementManifestType.ORIGINAL);
        priorCopy.setLinkedManifest(correctionManifest);
        correctionManifest.setSession(currentFmSession);
        correctionManifest.setType(FeeManagementManifestType.CORRECTION);
        correctionManifest.setTransaction(null);
        correctionManifest.setLinkedManifest(priorCopy);

        // Persist the new manifests:
        persistEntity(priorCopy);
        persistEntity(correctionManifest);
    }
    
    /**
     * Removes inverse manifests from the list of manifests. 
     * 
     * @param manifests A list of manifests to remove inverse ones from.
     */
    private void removeInverseManifests(List<FeeManagementManifest> manifests) {
    	
        // Iterate through the list of manifests. Begin with the first manifest and try
        // to find its full-reversal counterpart. If one is found, add a Pair, remove them both from
        // the list and start from the same "startIndex":
        int startIndex = 0;

        while (startIndex < manifests.size() - 1) {
            // Get the first Manifest to match against:
            FeeManagementManifest firstManifest = manifests.get(startIndex);
            boolean matchFound = false;

            for (int i = startIndex + 1, sz = manifests.size(); (i < sz) && !matchFound; i++) {
                // Check if the next manifest is an inverse one:
            	FeeManagementManifest anotherManifest = manifests.get(i);
            	
                matchFound = manifestsInverse(firstManifest, anotherManifest);

                if (matchFound) {
                    // Delete both entities, remove both from the list, and start again from the same index:
                    em.remove(firstManifest);
                    em.remove(anotherManifest);
                    manifests.remove(startIndex);
                    manifests.remove(i);
                }
            }

            // If a match not found, increment the start index and keep searching:
            if (!matchFound) {
                startIndex++;
            }
        }
    }
    
    /**
     * Checks if two FM manifests are inverse. According to the documentation, FM Manifests are inverse if:
     * If TYPES are inverse {CHARGE vs. CANCEL} with amounts equal
	 * OR
	 * Same type {Both CHARGE or CANCEL} with inverse amounts && transactionTypeId is the same
	 * 
     * @param m1 An FM Manifest to compare.
     * @param m2 Another FM Manifest to compare.
     * @return true if the two manifests are inverse, false otherwise.
     */
    private boolean manifestsInverse(FeeManagementManifest m1, FeeManagementManifest m2) {
    	if ((m1 != null) && (m2 != null)) {
    		// Check for inverse statuses and equal amounts or same statuses and inverse amounts:
    		if (((m1.getType() == FeeManagementManifestType.CHARGE) && (m2.getType() == FeeManagementManifestType.CANCELLATION))
    				|| ((m1.getType() == FeeManagementManifestType.CANCELLATION) && (m2.getType() == FeeManagementManifestType.CHARGE))) {
    			return safeAmountsEqual(m1, m2);
    		} else  if (((m1.getType() == FeeManagementManifestType.CHARGE) && (m2.getType() == FeeManagementManifestType.CHARGE))
    				|| ((m1.getType() == FeeManagementManifestType.CANCELLATION) && (m2.getType() == FeeManagementManifestType.CANCELLATION))) {
    			return safeAmountsInverse(m1, m2) && StringUtils.equals(m1.getTransactionTypeId(), m2.getTransactionTypeId());
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Safely compares "amount" properties of two <code>FeeManagementManifest</code>s.
     * Calls the "compareTo" method of BigDecimal.
     * Guaranteed not to cause a NullPointerException.
     * 
     * @param m1 A <code>FeeManagementManifest</code>.
     * @param m2 A <code>FeeManagementManifest</code>.
     * @return true if "amount" attributes are equal.
     */
    private boolean safeAmountsEqual(FeeManagementManifest m1, FeeManagementManifest m2) {
    	return (m1.getAmount() != null) && (m2.getAmount() != null) && (m1.getAmount().compareTo(m2.getAmount()) == 0);
    }
    
    /**
     * Safely compares "amount" properties of two <code>FeeManagementManifest</code>s.
     * Calls the "compareTo" method of BigDecimal.
     * Guaranteed not to cause a NullPointerException.
     * 
     * @param m1 A <code>FeeManagementManifest</code>.
     * @param m2 A <code>FeeManagementManifest</code>.
     * @return true if "amount" attributes are inverse.
     */
    private boolean safeAmountsInverse(FeeManagementManifest m1, FeeManagementManifest m2) {
    	return (m1.getAmount() != null) && (m2.getAmount() != null) && (m1.getAmount().compareTo(m2.getAmount().negate()) == 0);
    }
    
    /**
     * Validates that full-reversal manifests point at each other. Repoints them at each other if necessary.
     *
     * @param currentManifests	The current FM Session's manifests to validate reversals.
     */
    private void validateCurrentReversals(List<FeeManagementManifest> currentManifests) {

        // Get reversal Manifest pairs and point them to each other:
        List<Pair<FeeManagementManifest, FeeManagementManifest>> reversalManifests = findReversalManifests(currentManifests, false);

        for (Pair<FeeManagementManifest, FeeManagementManifest> pair : reversalManifests) {
            // Point the manifests at each other:
            pair.getA().setLinkedManifest(pair.getB());
            pair.getB().setLinkedManifest(pair.getA());

            // Persist the changes:
            persistEntity(pair.getA());
            persistEntity(pair.getB());
        }
    }
    
    /**
     * Create a shallow copy of a <code>FeeManagementManifest</code> with only Tags and KeyPairs deep-copied.
     * The new <code>FeeManagementManifest</code> is not attached to any FM session and not linked to any FM Manifests.
     * 
     * @param origManifest 	An original FM manifest to copy.
     * @return	A copy of the original FM manifest.
     */
    private FeeManagementManifest copyManifest(FeeManagementManifest origManifest) {
    	
    	// Create a new FM Manifest as a shallow copy of the original one:
    	FeeManagementManifest copyManifest = BeanUtils.getShallowCopy(origManifest);
    	
    	// Create copies of KeyPairs:
    	if (origManifest.getKeyPairs() != null) {
    		// Create a new Set of KeyPairs if one is not set yet:
    		if (copyManifest.getKeyPairs() == null) {
    			copyManifest.setKeyPairs(new HashSet<KeyPair>()); 
    		} else {
    			// Clear the previous tags:
    			copyManifest.getKeyPairs().clear();
    		}
    		
			// Create and add a new KeyPair identical to the original:
    		for (KeyPair keyPair : origManifest.getKeyPairs()) {
    			copyManifest.getKeyPairs().add(new KeyPair(keyPair.getKey(), keyPair.getValue()));
    		}
    	}
    	
    	// Create copies of Tags:
    	if (origManifest.getTags() != null) {
    		// Create a new Set of Tags if one is not set yet:
    		if (copyManifest.getTags() == null) {
    			copyManifest.setTags(new HashSet<Tag>());
    		} else {
    			copyManifest.getTags().clear();
    		}
    		
    		// Create a new Tag shallow copy:
    		for (Tag tag : origManifest.getTags()) {
    			Tag copyTag = BeanUtils.getShallowCopy(tag);
    			
    			copyTag.setId(null);
    			copyManifest.getTags().add(copyTag);
    		}
    	}
    	
    	
    	// Set non primitive non-java package types:
    	copyManifest.setSession(null);
    	copyManifest.setLinkedManifest(null);
    	copyManifest.setTransaction(origManifest.getTransaction());
    	copyManifest.setRate(origManifest.getRate());
    	copyManifest.setRollup(origManifest.getRollup());
    	copyManifest.setStatus(origManifest.getStatus());
    	copyManifest.setId(null);
    	
    	return copyManifest;
    }
}
