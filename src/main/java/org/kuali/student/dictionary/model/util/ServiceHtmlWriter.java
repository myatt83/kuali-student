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

import org.kuali.student.dictionary.model.Service;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.ServiceMethod;
import org.kuali.student.dictionary.model.ServiceMethodError;
import org.kuali.student.dictionary.model.ServiceMethodParameter;
import org.kuali.student.dictionary.writer.HtmlWriter;

/**
 *
 * @author nwright
 */
public class ServiceHtmlWriter
{

 private Service service;
 private HtmlWriter writer;
 private ServiceContractModel model;
 private ModelFinder finder;

 public ServiceHtmlWriter (Service service, String directory,
                           ServiceContractModel model)
 {
  this.service = service;
  this.writer = new HtmlWriter (directory, service.getName () + ".html",
                                service.getName ());
  this.model = model;
  this.finder = new ModelFinder (this.model);
 }

 public void write ()
 {
  this.writeStyleSheet ();
  writer.writeTag ("h1", service.getName ());

  writer.indentPrintln ("<table id=\"serviceMetaTable\">");
  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Name");
  writer.writeTag ("td", "id=serviceName colspan=2", service.getKey ());
  writer.indentPrintln ("</tr>");
  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Version");
  writer.writeTag ("td", "id=serviceVersion colspan=2", service.getVersion ());
  writer.indentPrintln ("</tr>");
  writer.indentPrintln ("</table>");

  writer.indentPrintln (service.getComments ());
  writer.indentPrintln ("<hr>");

  writer.indentPrintln ("<h3><a name=\"ListOfOperations\"></a>Operations</h3>");
  writer.indentPrintln ("<ul>");
  for (ServiceMethod method : finder.getServiceMethodsInService (
    service.getKey ()))
  {
   writer.indentPrint ("<li>");
   writer.print ("<a href=\"#" + method.getService () + "-" + method.getName ()
                 + "\">" + method.getName () + "</a>");
   writer.print ("</li>");
  }
  writer.indentPrintln ("</ul>");
  for (ServiceMethod method : finder.getServiceMethodsInService (
    service.getKey ()))
  {
   this.writeMethod (method);
  }
  writer.writeHeaderBodyAndFooterOutToFile ();
 }

 public void writeMethod (ServiceMethod method)
 {
  writer.indentPrintln ("<p>");
  writer.indentPrintln ("<a name=\"" + method.getService () + "-"
                        + method.getName () + "\"></a>");
  writer.indentPrintln ("<p>");
  writer.indentPrintln ("<table class=\"methodTable\">");

  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Method");
  writer.writeTag ("th", "class=h colspan=3 class=\"methodName\"",
                   method.getName ());
  writer.indentPrintln ("</tr>");

  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Description");
  writer.writeTag ("td", "colspan=3 class=\"methodDesc\"",
                   method.getDescription ());
  writer.indentPrintln ("</tr>");

  if (method.getParameters ().isEmpty ())
  {
   writer.indentPrintln ("<tr>");
   writer.writeTag ("th", "class=h", "Parameters");
   writer.writeTag ("td", "class=\"methodParamType\"", "None");
   writer.writeTag ("td", "class=\"methodParamName\"", "None");
   writer.writeTag ("td", "class=\"methodParamDesc\"", "No Parameters");
   writer.indentPrintln ("</tr>");
  }
  else
  {
   boolean firstTime = true;
   for (ServiceMethodParameter param : method.getParameters ())
   {
    writer.indentPrintln ("<tr>");
    if (firstTime)
    {
     writer.writeTag ("th", "class=h rowspan=" + method.getParameters ().size (),
                      "Parameters");
     firstTime = false;
    }
    writer.writeTag ("td", "class=\"methodParamType\"", param.getType ()); // TODO wrap in link to type
    writer.writeTag ("td", "class=\"methodParamName\"", param.getName ());
    writer.writeTag ("td", "class=\"methodParamDesc\"", param.getDescription ());
    writer.indentPrintln ("</tr>");
   }
  }
  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Return");
  writer.writeTag ("td", "colspan=2", method.getReturnValue ().getType ()); // TODO wrap in link to type
  writer.writeTag ("td", "class=\"methodReturnDesc\"",
                   method.getReturnValue ().getDescription ());
  writer.indentPrintln ("</tr>");
  writer.indentPrintln ("</tr>");

  if (method.getErrors ().isEmpty ())
  {
   writer.indentPrintln ("<tr>");
   writer.writeTag ("th", "class=h", "Errors");
   writer.writeTag ("td", "class=\"methodErrorType\" colspan=2", "NONE");
   writer.writeTag ("td", "class=\"methodErrorDesc\"", "No Errors");
   writer.indentPrintln ("</tr>");
  }
  else
  {
   boolean firstTime = true;
   for (ServiceMethodError error : method.getErrors ())
   {
    writer.indentPrintln ("<tr>");
    if (firstTime)
    {
     writer.writeTag ("th", "class=h rowspan=" + method.getErrors ().size (),
                      "Errors");
     firstTime = false;
    }
    writer.writeTag ("td", "class=\"methodErrorType\" colspan=2",
                     error.getType ()); // TODO wrap in link to type
    writer.writeTag ("td", "class=\"methodErrorDesc\"", error.getDescription ());
    writer.indentPrintln ("</tr>");
   }
  }
  writer.indentPrintln ("</table>");
  writer.indentPrintln ("<p>");
  writer.indentPrintln ("<a href=\"#ListOfOperations\">Back to Operations</a>");
  writer.indentPrintln ("<p>");
 }

 public void writeStyleSheet ()
 {
  writer.indentPrintln ("<style type=\"text/css\">");
  writer.indentPrintln ("");
  writer.indentPrintln ("table#serviceMetaTable {");
  writer.indentPrintln ("border-collapse:collapse;");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:95%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#serviceMetaTable th.h {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background-color:#eeeeee;");
  writer.indentPrintln ("width:15%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#serviceMetaTable td#serviceName {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:85%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#serviceMetaTable td#serviceVersion {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:70%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#serviceMetaTable td#serviceVersionHistory {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:15%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("</style>");


  writer.indentPrintln ("<style type=\"text/css\">");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable {");
  writer.indentPrintln ("border-collapse:collapse;");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:95%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.d {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable th.h {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background-color:#eeeeee;");
  writer.indentPrintln ("width:15%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.methodName {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background-color:#f2f2f2;");
  writer.indentPrintln ("color:#222222;");
  writer.indentPrintln ("text-align:center;");
  writer.indentPrintln ("width:85%;");
  writer.indentPrintln ("font-weight:bold;");
  writer.indentPrintln ("font-style:italic;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.methodDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:85%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.methodParamType {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:21%;");
  writer.indentPrintln ("font-style:italic;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table.methodTable td.methodParamName {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:21%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table.methodTable td.methodParamDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:43%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table.methodTable td.methodReturnType {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:42%;");
  writer.indentPrintln ("font-style:italic;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.methodReturnDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:43%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.methodErrors {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:85%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.methodErrorType {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:42%;");
  writer.indentPrintln ("font-style:italic;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table.methodTable td.methodErrorDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:43%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.capabilityDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.usecaseDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.methodTable td.commentsDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("</style>");

 }
}
