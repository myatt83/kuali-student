/*
 * Copyright 2010 The Kuali Foundation
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

package org.kuali.student.loader.organization;

import java.util.List;

/**
 *
 * @author nwright
 */
public class OrgOrgRelationInputModelCache implements OrgOrgRelationInputModel
{

 public OrgOrgRelationInputModelCache (OrgOrgRelationInputModel loaderModel)
 {
  this.loaderModel = loaderModel;
 }

 private OrgOrgRelationInputModel loaderModel;

 public OrgOrgRelationInputModel getLoaderModel ()
 {
  return loaderModel;
 }

 public void setLoaderModel (OrgOrgRelationInputModel loaderModel)
 {
  this.loaderModel = loaderModel;
 }

 private static List<OrgOrgRelation> orgOrgRelations = null;

 @Override
 public List<OrgOrgRelation> getOrgOrgRelations ()
 {
  if (orgOrgRelations != null)
  {
   return orgOrgRelations;
  }
  orgOrgRelations = loaderModel.getOrgOrgRelations ();
  return orgOrgRelations;
 }


}
