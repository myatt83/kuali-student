/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.lum.lu.ui.krms.tree.node;

import org.kuali.rice.krms.dto.PropositionEditor;
import org.kuali.rice.krms.tree.node.RuleEditorTreeNode;
import org.kuali.student.lum.lu.ui.krms.dto.LUPropositionEditor;

import java.io.Serializable;

/**
 * abstract data class for the rule tree {@link org.kuali.rice.core.api.util.tree.Node}s
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class LUPropositionEditNode implements Serializable, RuleEditorTreeNode {

    private static final long serialVersionUID = -5650654824214437325L;

    private LUPropositionEditor proposition;

    public LUPropositionEditNode(LUPropositionEditor proposition){
        this.proposition = proposition;
    }

    public LUPropositionEditor getProposition() {
        return proposition;
    }

    public void setProposition(LUPropositionEditor proposition) {
        this.proposition = proposition;
    }

}
