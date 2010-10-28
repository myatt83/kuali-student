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
package org.kuali.student.loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.kuali.student.core.enumerationmanagement.dto.EnumeratedValueInfo;
import org.kuali.student.core.enumerationmanagement.service.EnumerationManagementService;
import org.kuali.student.core.exceptions.AlreadyExistsException;

/**
 *
 * @author nwright
 */
public class EnumeratedValueLoader
{

 private EnumerationManagementService enumerationManagementService;

 public EnumerationManagementService getEnumerationManagementService ()
 {
  return enumerationManagementService;
 }

 public void setEnumerationManagementService (EnumerationManagementService enumerationManagementService)
 {
  this.enumerationManagementService = enumerationManagementService;
 }

 public EnumeratedValueLoader ()
 {
 }
 private Iterator<EnumeratedValue> inputDataSource;

 public Iterator<EnumeratedValue> getInputDataSource ()
 {
  return inputDataSource;
 }

 public void setInputDataSource (Iterator<EnumeratedValue> inputDataSource)
 {
  this.inputDataSource = inputDataSource;
 }

 public List<EnumeratedValueLoadResult> update ()
 {
  List<EnumeratedValueLoadResult> results = new ArrayList (500);
  int row = 0;
  while (inputDataSource.hasNext ())
  {
   EnumeratedValueLoadResult result = new EnumeratedValueLoadResult ();
   results.add (result);
   EnumeratedValue ev = inputDataSource.next ();
   row ++;
   EnumeratedValueInfo info = new EnumeratedValueToEnumeratedValueInfoConverter (ev).convert ();
   result.setRow (row);
   result.setEnumeratedValue (ev);
   result.setEnumeratedValueInfo (info);
   try
   {
    EnumeratedValueInfo createdInfo = enumerationManagementService.addEnumeratedValue (ev.getEnumerationKey (), info);
    result.setEnumeratedValueInfo (createdInfo);
    result.setSuccess (true);
   }
   catch (AlreadyExistsException ex)
   {
    result.setSuccess (false);
    result.setException (ex);
   }
   catch (Exception ex)
   {
    result.setSuccess (false);
    result.setException (ex);
   }
  }
  return results;
 }

 public static EnumeratedValueInputModelFactory getInstance (String excelFile)
 {
  Properties props = new Properties ();
  props.putAll (EnumeratedValueInputModelFactory.getDefaultConfig ());
  props.put (EnumeratedValueInputModelFactory.EXCEL_FILES_DEFAULT_DIRECTORY_KEY, "src/main/"
   + EnumeratedValueInputModelFactory.RESOURCES_DIRECTORY);
  props.put (EnumeratedValueInputModelFactory.SERVICE_HOST_URL, "src/main/"
   + EnumeratedValueInputModelFactory.RESOURCES_DIRECTORY);
  System.out.println ("Current Directory=" + System.getProperty ("user.dir"));
  EnumeratedValueInputModelFactory factory = new EnumeratedValueInputModelFactory ();
  factory.setConfig (props);
  return factory;
 }

 


}
