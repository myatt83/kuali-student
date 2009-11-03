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
package org.kuali.student.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Creates the dictionary XML file from the Excel source file
 * <p>
 * This is also
 * @goal excelToXML
 */
public class DictionaryGeneratorFromExcelMojo extends AbstractMojo
{

 public static String DIRECTORY = "src/test/resources/dictionary/";
 public static String DICTIONARY_INPUT_FILE = "type-state configuration.xls";
 public static String ORCHESTRATION_INPUT_FILE = "Orchestration Dictionary.xls";
 public static String XML_FILE = "lu-dictionary-config-generated-excel.xml";

 public DictionaryGeneratorFromExcelMojo ()
 {
  this.dictionaryInputFile = null;
  this.orchestrationInputFile = null;
  this.xmlOutputFile = null;
 }

 /**
  * Path to excel file.
  *
  * @parameter
  */
 private File dictionaryInputFile;

  /**
  * Path to orchestration excel file.
  *
  * @parameter
  */
 private File orchestrationInputFile;

 /**
  * Path to the XML file
  *
  * @parameter
  */
 private File xmlOutputFile;

 @Override
 public void execute ()
  throws MojoExecutionException,
         MojoFailureException
 {
  try
  {
   PrintStream out;
   try
   {
    out = new PrintStream (xmlOutputFile);
   }
   catch (FileNotFoundException ex)
   {
    throw new DictionaryExecutionException ("Could  not create ");
   }
   SpreadsheetReader dictReader = new ExcelSpreadsheetReader (dictionaryInputFile.
    getCanonicalPath ());
   SpreadsheetReader orchReader = new ExcelSpreadsheetReader (orchestrationInputFile.
    getCanonicalPath ());
   DictionarySpreadsheetLoader loader = new DictionarySpreadsheetLoader (dictReader, orchReader);
   DictionarySpreadsheet cache = new DictionarySpreadsheetCache (loader);
   DictionarySpreadsheetWriter instance = new DictionarySpreadsheetWriter (out, cache);
   instance.write ();
   out.close ();
  }
  catch (DictionaryValidationException ex)
  {
   throw new MojoFailureException (ex.getMessage ());
  }
  catch (DictionaryExecutionException ex)
  {
   throw new MojoExecutionException ("Error generating dictionary", ex);
  }
  catch (Exception ex)
  {
   throw new MojoExecutionException ("Unexpected Exception while trying to generate the dictionary", ex);
  }
 }

 public File getDictionaryInputFile ()
 {
  return dictionaryInputFile;
 }

 public void setDictionaryInputlFile (File excelFile)
 {
  this.dictionaryInputFile = excelFile;
 }


 public File getOrchestrationInputFile ()
 {
  return orchestrationInputFile;
 }

 public void setOrchestrationInputlFile (File excelFile)
 {
  this.orchestrationInputFile = excelFile;
 }



 public File getXmlFile ()
 {
  return xmlOutputFile;
 }

 public void setXmlFile (File xmlFile)
 {
  this.xmlOutputFile = xmlFile;
 }

}
