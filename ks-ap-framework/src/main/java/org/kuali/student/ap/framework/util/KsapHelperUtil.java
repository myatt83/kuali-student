package org.kuali.student.ap.framework.util;

import org.kuali.rice.core.api.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;

public class KsapHelperUtil {
    static List<String> termTypes;

    public static Predicate[] getTermPredicates() {
        Predicate predicates[] = new Predicate[getTermTypes().size()];
        for(int i=0;i<getTermTypes().size();i++){
            predicates[i]=equal("typeKey", getTermTypes().get(i));
        }
        return predicates;
    }

    public static List<String> getTermTypes(){
        if(termTypes==null){
            termTypes = new ArrayList<String>();
            for(String term : KsapConstants.defaultTerms){
                termTypes.add(term);
            }
        }
        return termTypes;
    }
}
