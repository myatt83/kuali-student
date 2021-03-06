/**
 * Copyright 2013 The Kuali Foundation Licensed under the
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
 *
 * Created by Charles on 9/22/13
 */
package org.kuali.student.poc.eventproc.event.subclass.event;

import org.kuali.student.poc.eventproc.event.KSEvent;
import org.kuali.student.poc.eventproc.event.KSEventAttributeKey;
import org.kuali.student.poc.eventproc.event.KSEventFactory;
import org.kuali.student.r2.common.exceptions.OperationFailedException;

/**
 * Event to request the recomputation of the CO state
 *
 * @author Kuali Student Team
 */
public class KSRecomputeCOStateEvent extends KSEvent {
    private String coId;
    public KSRecomputeCOStateEvent(String coId) throws OperationFailedException {
        super(KSEventFactory.CO_RECOMPUTE_STATE_EVENT_TYPE);
        this.coId = coId;
    }

    public String getCoId() {
        return coId;
    }

    @Override
    public boolean hasAttribute(KSEventAttributeKey key) {
        return key.isEqualTo(KSEventFactory.EVENT_ATTRIBUTE_KEY_CO_ID);
    }

    @Override
    public String getAttributeValueByKey(KSEventAttributeKey key) {
        if (key.isEqualTo(KSEventFactory.EVENT_ATTRIBUTE_KEY_CO_ID)) {
            return coId;
        }
        throw new RuntimeException("Invalid key name: " + key);
    }
}
