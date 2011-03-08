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
package org.kuali.student.dictionary.writer.service;

import java.io.Serializable;
import java.util.List;
import org.kuali.student.dictionary.DictionaryExecutionException;
import org.kuali.student.dictionary.model.MessageStructure;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.XmlType;
import org.kuali.student.dictionary.model.util.ModelFinder;
import org.kuali.student.dictionary.writer.JavaClassWriter;

/**
 *
 * @author nwright
 */
public class PureJavaInfcBeanWriter extends JavaClassWriter
{

 private ServiceContractModel model;
 private String directory;
 private String rootPackage;
 private String service;
 private XmlType type;
 private ModelFinder finder;

 public PureJavaInfcBeanWriter (ServiceContractModel model,
                           String directory,
                           String rootPackage,
                           String service,
                           XmlType type)
 {
  super (directory, calcPackage (service, rootPackage), calcClassName (type.
   getName ()));
  this.model = model;
  this.finder = new ModelFinder (model);
  this.directory = directory;
  this.rootPackage = rootPackage;
  this.service = service;
  this.type = type;
 }

 public static String calcPackage (String service, String rootPackage)
 {
    if (service.contains (","))
  {
   service = "common";
  }
  return PureJavaInfcServiceWriter.calcPackage (service, rootPackage);
 }

 public static String calcClassName (String name)
 {
  if (name.endsWith ("Info"))
  {
   name = name.substring (0, name.length () - "Info".length ());
  }
  name = name + "Bean";
  return GetterSetterNameCalculator.calcInitUpper (name);

 }

 /**
  * Write out the entire file
  * @param out
  */
 public void write ()
 {
  indentPrintln ("public class " + calcClassName (type.getName ()));
  incrementIndent ();
  indentPrint (" implements "
   + PureJavaInfcInfcWriter.calcClassName (type.getName ()));
  importsAdd (PureJavaInfcInfcWriter.calcPackage (service, rootPackage)
   + "." + PureJavaInfcInfcWriter.calcClassName (type.getName ()));
  this.importsAdd (Serializable.class.getName ());
  indentPrintln (", Serializable");
  openBrace ();

  indentPrintln ("");
  indentPrintln ("private static final long serialVersionUID = 1L;");

  List<MessageStructure> list =
   finder.findMessageStructures (type.getName ());
  if (list.size () == 0)
  {
   throw new DictionaryExecutionException ("xmlType " + type.getName ()
    + " has no fields defined in the message structure tab");
  }
  for (MessageStructure ms : list)
  {
   String realType = stripList (PureJavaInfcInfcWriter.calcClassName (ms.getType ()));
   String fieldType = this.calcFieldTypeToUse (ms.getType (), realType);
   String name = initLower (ms.getShortName ());
   indentPrintln ("");
   indentPrintln ("private " + fieldType + " " + name + ";");
   indentPrintln ("");
   indentPrintln ("/**");
   indentPrintWrappedComment ("Set " + ms.getName ());
   indentPrintln ("*");
   indentPrintln ("* Type: " + ms.getType ());
   indentPrintln ("*");
   indentPrintWrappedComment (ms.getDescription ());
   indentPrintln ("*/");
   indentPrintln ("@Override");
   indentPrintln ("public void " + calcSetter (ms) + "(" + fieldType + " "
    + name + ")");
   openBrace ();
   indentPrintln ("this." + name + " = " + name + ";");
   closeBrace ();

   indentPrintln ("");
   indentPrintln ("/**");
   indentPrintWrappedComment ("Get " + ms.getName ());
   indentPrintln ("*");
   indentPrintln ("* Type: " + ms.getType ());
   indentPrintln ("*");
   indentPrintWrappedComment (ms.getDescription ());
   indentPrintln ("*/");
   indentPrintln ("@Override");
   indentPrintln ("public " + fieldType + " " + calcGetter (ms) + "()");
   openBrace ();
   indentPrintln ("return this." + name + ";");
   closeBrace ();
   indentPrint ("");

   indentPrint ("");
  }
  indentPrintln ("");
  closeBrace ();

  this.writeJavaClassAndImportsOutToFile ();
  this.getOut ().close ();
 }

  private String stripList (String str)
 {
  return GetterSetterNameCalculator.stripList (str);
 }

  private String initLower (String str)
 {
  if (str == null)
  {
   return null;
  }
  if (str.isEmpty ())
  {
   return str;
  }
  if (str.length () == 1)
  {
   return str.toLowerCase ();
  }
  return str.substring (0, 1).toLowerCase () + str.substring (1);
 }


 private String calcGetter (MessageStructure ms)
 {
  return new GetterSetterNameCalculator (ms, this, model).calcGetter ();
 }

 private String calcSetter (MessageStructure ms)
 {
  return new GetterSetterNameCalculator (ms, this, model).calcSetter ();
 }

 private String calcFieldTypeToUse (String type, String realType)
 {
  XmlType t = finder.findXmlType (this.stripList (type));
  String pckName = calcPackage (t.getService (), rootPackage);
  return MessageStructureTypeCalculator.calculate (this, model, type, realType,
                                                   pckName);
 }

}
