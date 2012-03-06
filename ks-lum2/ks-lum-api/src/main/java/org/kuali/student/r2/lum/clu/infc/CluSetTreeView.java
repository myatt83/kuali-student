/*
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.r2.lum.clu.infc;

import org.kuali.student.r2.common.infc.HasEffectiveDates;
import org.kuali.student.r2.common.infc.IdEntity;

import java.util.List;

/**
 * Tree representation of clu sets
 *
 * @Version 2.0
 * @Author Sri komandur@uw.edu
 */
public interface CluSetTreeView extends IdEntity, HasEffectiveDates {

    /**
     * Information about an organization acting in administrative capacity for a
     * learning unit.The Organization responsible for this cluset
     *
     * @name Admin Org
     */
    public String getAdminOrg();

    /**
     * Flags if the Clu Set is reusable or was created for one time use for e.g.
     * in ReqComponentInfo
     *
     * @name Is Reusable
     */
    public Boolean getIsReusable();

    /**
     * Flags set to true if CluSet can be referenced by ReqComponents and other
     * entities. If set to false, then the CluSet can be referenced only by
     * other CluSets.
     *
     * @name Is Referenceable
     */
    public Boolean getIsReferenceable();

    /**
     * List of of directly contained CLU Sets. Present for enumerated CLU Sets.
     *
     * @name Clu Sets
     */
    public List<? extends CluSetTreeView> getCluSets();

    /**
     * List of directly contained CLUs. Present for enumerated CLU Sets.
     *
     * @name Clus
     */
    public List<? extends Clu> getClus();

}
