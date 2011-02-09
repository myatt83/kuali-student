/*
 * Copyright 2011 The Kuali Foundation
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
package org.kuali.student.dictionary.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.impl.ServiceContractModelCache;
import org.kuali.student.dictionary.model.impl.ServiceContractModelQDoxLoader;
import org.kuali.student.dictionary.model.validation.ServiceContractModelValidator;
import static org.junit.Assert.*;

/**
 *
 * @author nwright
 */
public class HtmlContractWriterTest
{

 public HtmlContractWriterTest ()
 {
 }

 @BeforeClass
 public static void setUpClass () throws Exception
 {
 }

 @AfterClass
 public static void tearDownClass () throws Exception
 {
 }

 @Before
 public void setUp ()
 {
 }

 @After
 public void tearDown ()
 {
 }
 private static final String CORE_DIRECTORY =
                             "C:/svn/student/ks-core/ks-core-api/src/main/java";
//                           "C:/svn/maven-dictionary-generator/trunk/src/main/java/org/kuali/student/core";
 private static final String COMMON_DIRECTORY =
                             "C:/svn/student/ks-common/ks-common-api/src/main/java";
 private static final String LUM_DIRECTORY =
                             "C:/svn/student/ks-lum/ks-lum-api/src/main/java";
 private static final String HTML_DIRECTORY =
                             "C:/svn/maven-dictionary-generator/trunk/target/html";

 private ServiceContractModel getModel ()
 {
  List<String> srcDirs = new ArrayList ();
  srcDirs.add (CORE_DIRECTORY);
  srcDirs.add (COMMON_DIRECTORY);
  srcDirs.add (LUM_DIRECTORY);
  ServiceContractModel instance = new ServiceContractModelQDoxLoader (srcDirs);
  return new ServiceContractModelCache (instance);

 }

 private void validate (ServiceContractModel model)
 {
  Collection<String> errors =
                     new ServiceContractModelValidator (model).validate ();
  if (errors.size () > 0)
  {
   StringBuffer buf = new StringBuffer ();
   buf.append (errors.size () + " errors found while validating the data.");
   int cnt = 0;
   for (String msg : errors)
   {
    cnt ++;
    buf.append ("\n");
    buf.append ("*error*" + cnt + ":" + msg);
   }

   fail (buf.toString ());
  }
 }

 /**
  * Test of getBody method, of class HtmlWriter.
  */
 @Test
 public void testRun ()
 {
  ServiceContractModel model = this.getModel ();
  this.validate (model);
  HtmlContractWriter writer = new HtmlContractWriter (HTML_DIRECTORY, model);
  writer.write ();
 }
}
