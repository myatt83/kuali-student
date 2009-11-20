/*
 * Copyright 2009 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.orchestration.orch;


import org.kuali.student.common.assembly.client.Data;
import org.kuali.student.common.assembly.client.Metadata;
import org.kuali.student.orchestration.orch.CreditCourseActivityContactHoursHelper.Properties;


public class CreditCourseActivityContactHoursMetadata
{
	// version 2
	public Metadata getMetadata (String type, String state)
	{
		Metadata mainMeta = new Metadata ();
		Metadata childMeta;
		
		// metadata for per
		childMeta = new Metadata ();
		mainMeta.getProperties ().put (Properties.PER.getKey (), childMeta);
		childMeta.setDataType (Data.DataType.STRING);
		childMeta.setWriteAccess (Metadata.WriteAccess.ALWAYS);
		
		// metadata for hrs
		childMeta = new Metadata ();
		mainMeta.getProperties ().put (Properties.HRS.getKey (), childMeta);
		childMeta.setDataType (Data.DataType.INTEGER);
		childMeta.setWriteAccess (Metadata.WriteAccess.ALWAYS);
		
		mainMeta.setWriteAccess (Metadata.WriteAccess.ALWAYS);
		return mainMeta;
	}
}

