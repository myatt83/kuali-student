package com.sigmasys.kuali.ksa.service.impl;


import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.LocalizedString;
import com.sigmasys.kuali.ksa.model.LocalizedStringId;
import com.sigmasys.kuali.ksa.model.Pair;
import com.sigmasys.kuali.ksa.service.LocalizationService;
import com.sigmasys.kuali.ksa.service.xliff.TransUnit;
import com.sigmasys.kuali.ksa.service.xliff.Xliff;
import com.sigmasys.kuali.ksa.service.xliff.XliffParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Localization Service implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("unchecked")
@Service(LocalizationService.SERVICE_NAME)
@Transactional(readOnly = true)
public class LocalizationServiceImpl extends GenericPersistenceService implements LocalizationService {

    private static final Log logger = LogFactory.getLog(LocalizationServiceImpl.class);

    @PersistenceContext(unitName = Constants.KSA_PERSISTENCE_UNIT)
    protected EntityManager em;

    @Autowired
    private XliffParser xliffParser;

    /**
     * Imports the locale-aware resources specified by java.io.Reader for the given import type.
     *
     * @param content    the content of the resources to be imported
     * @param importType Import type
     * @return a map of localized strings for the target locale
     */
    @Override
    @Transactional(readOnly = false)
    public ArrayList<Pair<String, LocalizedString>> importResources(String content, ImportType importType) {

        Xliff xliff = xliffParser.parse(content);

        String sourceLocale = xliff.getSourceLocale();
        String targetLocale = xliff.getTargetLocale();

        ArrayList<Pair<String, LocalizedString>> targetStrings = null;
        Map<String, TransUnit> transUnitMap = xliff.getTransUnits();
        if (transUnitMap != null) {
            Collection<TransUnit> transUnits = transUnitMap.values();
            targetStrings = new ArrayList<Pair<String, LocalizedString>>(transUnits.size());
            for (TransUnit transUnit : transUnits) {
                persistTransUnit(transUnit, sourceLocale, importType, true);
                LocalizedString string = persistTransUnit(transUnit, targetLocale, importType, false);
                targetStrings.add(new Pair<String, LocalizedString>(string.getId().getId(), string));
            }
        }

        return (targetStrings != null) ? targetStrings : new ArrayList<Pair<String, LocalizedString>>();
    }

    private LocalizedString persistTransUnit(TransUnit transUnit, String locale, ImportType importType, boolean isSource) {

        LocalizedStringId id = new LocalizedStringId(transUnit.getId(), locale);
        LocalizedString localizedString = getEntity(id, LocalizedString.class);
        boolean isOverridden = (localizedString != null);

        if (localizedString != null) {
            if (ImportType.NEW_ONLY == importType) {
                logger.info("The following localized string is ignored because it already exists and " +
                        "the import type is set to 'NEW_ONLY': " + localizedString);
                return localizedString;
            } else if (ImportType.FULL_NO_OVERRIDE == importType) {
                Boolean canBeOverridden = localizedString.getOverridden();
                if (canBeOverridden != null && !canBeOverridden) {
                    logger.info("The following localized string is ignored because it already exists, " +
                            "cannot be overridden and the import type is set to 'FULL_NO_OVERRIDE': " +
                            localizedString);
                    return localizedString;
                }
            }
        } else {
            localizedString = new LocalizedString();
            localizedString.setId(id);
        }

        localizedString.setValue(isSource ? transUnit.getSource() : transUnit.getTarget());
        localizedString.setMaxLength(transUnit.getMaxBytes());
        localizedString.setOverridden(isOverridden);
        persistEntity(localizedString);
        return localizedString;
    }

    /**
     * Returns all the localized strings for the given locale
     *
     * @param locale the locale string, i.e. "en_US", "fr_FR"
     * @return a map of localized strings
     */
    @Override
    public ArrayList<Pair<String, LocalizedString>> getLocalizedStrings(String locale) {
        Query query = em.createQuery("select s from LocalizedString s where s.id.locale = :locale");
        query.setParameter("locale", locale);
        List<LocalizedString> entities = (List<LocalizedString>) query.getResultList();
        ArrayList<Pair<String, LocalizedString>> strings = new ArrayList<Pair<String, LocalizedString>>(entities.size());
        for (LocalizedString localizedString : entities) {
            strings.add(new Pair<String, LocalizedString>(localizedString.getId().getId(), localizedString));
        }
        return strings;
    }

}
