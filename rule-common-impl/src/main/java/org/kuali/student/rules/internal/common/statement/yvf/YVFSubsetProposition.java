/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.rules.internal.common.statement.yvf;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.student.rules.factfinder.dto.FactResultDTO;
import org.kuali.student.rules.factfinder.dto.FactStructureDTO;
import org.kuali.student.rules.internal.common.statement.exceptions.PropositionException;
import org.kuali.student.rules.internal.common.statement.propositions.SubsetProposition;
import org.kuali.student.rules.internal.common.utils.FactUtil;
import org.kuali.student.rules.rulemanagement.dto.YieldValueFunctionDTO;

public class YVFSubsetProposition<E> extends YVFProposition<E> {

	public YVFSubsetProposition(String id, String propositionName, 
			YieldValueFunctionDTO yvf, Map<String, ?> factMap) {
		if (id == null || id.isEmpty()) {
			throw new PropositionException("Proposition id cannot be null");
		} else if (propositionName == null || propositionName.isEmpty()) {
			throw new PropositionException("Proposition name cannot be null");
		} else if (yvf == null) {
			throw new PropositionException("Yield value function cannot be null");
		} else if (factMap == null || factMap.isEmpty()) {
			throw new PropositionException("Fact map cannot be null or empty");
		}

		List<FactStructureDTO> factStructureList = yvf.getFactStructureList();
		FactStructureDTO criteria = factStructureList.get(0);
		FactStructureDTO fact = factStructureList.get(1);
		
		Set<E> criteriaSet = null;
		Set<E> factSet = null;
		
		if (criteria.isStaticFact()) {
			criteriaSet = getSet(criteria.getStaticValueDataType(), criteria.getStaticValue());
		} else {
			String criteriaKey = FactUtil.createCriteriaKey(criteria);
			FactResultDTO criteriaDTO = (FactResultDTO) factMap.get(criteriaKey);
			criteriaSet = getSet(criteriaDTO);
		}

		if (fact.isStaticFact()) {
			factSet = getSet(fact.getStaticValueDataType(), fact.getStaticValue());
		} else {
	    	String factKey = FactUtil.createFactKey(fact);
			FactResultDTO factDTO = (FactResultDTO) factMap.get(factKey);
			factSet = getSet(factDTO);
		}
		
		super.proposition = new SubsetProposition<E>(id, propositionName, criteriaSet, factSet); 
	}
	
}
