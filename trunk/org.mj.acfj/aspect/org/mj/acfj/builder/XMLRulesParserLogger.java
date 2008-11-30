/**
 * Copyright (c) 2008, Mounir Jarra•
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    3. All advertising materials mentioning features or use of this software
 *       must display the following acknowledgement:
 *			This product includes software developed by Mounir Jarra•
 *      	and its contributors.
 *    4. Neither the name Mounir Jarra• nor the names of its contributors may 
 *       be used to endorse or promote products derived from this software 
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY MOUNIR JARRAì ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MOUNIR JARRAì BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package org.mj.acfj.builder;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.mj.acfj.rule.IRule;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Mounir Jarra•
 * 
 */
@Aspect
public final class XMLRulesParserLogger extends DefaultHandler {

	//
	// Pointcut
	//

	@Pointcut("this(org.mj.acfj.builder.XMLRulesContentHandler)")
	void thisXMLRulesContentHandler() {
	}

	@Pointcut("this(org.mj.acfj.builder.XMLErrorHandler)")
	void thisXMLErrorHandler() {
	}

	//
	// Advices 
	//

	@Before("thisXMLRulesContentHandler() && execution(public void startDocument())")
	@Override
	public void startDocument() throws SAXException {
		System.out.printf("startDocument \n");
	}

	@Before("thisXMLRulesContentHandler() && execution(public void endDocument())")
	@Override
	public void endDocument() throws SAXException {
		System.out.printf("endDocument \n");
	}

	@Before("thisXMLRulesContentHandler() && execution(public void startElement(String, String, String, Attributes))"
			+ " && args(uri, localName, name, atts)")
	@Override
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		System.out.printf("\tstartElement - localName=%-20s name=%-20s atts=(%-10s, %-5s,%-10s )  uri=%-20s \n", localName, name, atts
				.getLocalName(0), atts.getValue(0), atts.getType(0), uri);
	}

	@Before("thisXMLRulesContentHandler() && execution(public void endElement(String, String, String))" + " && args(uri, localName, name)")
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		System.out.printf("\tendElement   - localName=%-20s name=%-20s  uri=%-20s \n", localName, name, uri);
	}

	@Before("thisXMLRulesContentHandler() && execution(public void characters(char[], int, int)) && args(ch, start, length)")
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String tagValue = new String(ch, start, length);
		System.out.printf("\tcharacters - ch=%-50s (%d, %d) \n", tagValue, start, length);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
	}

	@Override
	public void setDocumentLocator(Locator locator) {
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

	@Before("thisXMLErrorHandler() && execution(public void error(SAXParseException)) && args(e)")
	@Override
	public void error(SAXParseException e) throws SAXException {
		super.error(e);
		System.err.println(e.getLocalizedMessage());
	}

	@Before("thisXMLErrorHandler() && execution(public void fatalError(SAXParseException)) && args(e)")
	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		super.fatalError(e);
		System.err.println(e.getLocalizedMessage());
	}

	@Before("thisXMLErrorHandler() && execution(public void warning(SAXParseException)) && args(e)")
	@Override
	public void warning(SAXParseException e) throws SAXException {
		super.warning(e);
		System.out.println(e.getLocalizedMessage());
	}

	@After("initialization(public org.mj.acfj.rule.impl.NameSpaceRule.new(..)) && this(rule)")
	public void createdNameSpaceRule(IRule rule) {
		System.out.println("new " + rule);
	}

}
