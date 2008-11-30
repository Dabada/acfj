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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.mj.acfj.ACFJConstants;
import org.mj.acfj.Activator;
import org.mj.acfj.rule.RuleRegistry;
import org.mj.acfj.rule.impl.NameSpaceRule;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author Mounir Jarra•
 * 
 */
class XMLRulesContentHandler implements ContentHandler, IRulesXMLFileConstants, ACFJConstants {

	private static final int NAID = -1;

	private String currentTag;
	private boolean isInNameSpace, isInTemplageMsg;
	private boolean currentNameSpaceEnabled, currentTargetEnabled;

	private String currentSource, currentTarget, currentTargetMsg;
	private int currentId = NAID, currentIdRef = NAID;

	private IProject project;
	private IFile file;

	private Map<Integer, String> msgRefMap = new HashMap<Integer, String>();

	public XMLRulesContentHandler(IProject project, IFile file) {
		if (project == null) {
			throw new IllegalArgumentException("project parameter can't be null");
		}
		if (file == null) {
			throw new IllegalArgumentException("file parameter can't be null");
		}
		this.project = project;
		this.file = file;
	}

	/**
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
	}

	/**
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		RuleRegistry.getSingleton().clearProjectMetaRule(project);
	}

	/**
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		System.out.println("End");
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		currentTag = localName.intern();
		if (TAG_MSG_TEMPLATE == currentTag) {
			isInTemplageMsg = true;
		} else if (TAG_NAMESPACE == currentTag) {
			currentNameSpaceEnabled = Boolean.valueOf(getAttributeValue(ATT_ENABLED, atts));
			isInNameSpace = true;
		} else if (TAG_AVOID == currentTag) {
			currentTargetEnabled = Boolean.valueOf(getAttributeValue(ATT_ENABLED, atts));
		} else if (TAG_MSG == currentTag && isInTemplageMsg) {
			currentId = new Integer(getAttributeValue(ATT_ID, atts));
		} else if (TAG_MSG_REF == currentTag) {
			currentIdRef = new Integer(getAttributeValue(ATT_ID_REF, atts));
			currentTargetMsg = msgRefMap.get(currentIdRef);
			if (currentTargetMsg == null) {
				String message = "No message with id=" + currentIdRef + " is defined in file " + ARCHITECTURE_RULES_FILE_NAME + " !";
				currentTargetMsg = message;
				LOGGER.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message));
			}
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
		String tagValue = new String(ch, start, length);
		if (isInNameSpace && currentNameSpaceEnabled) {
			if (currentTag == TAG_CURRENT) {
				currentSource = tagValue;
			}
			if (currentTargetEnabled) {
				if (currentTag == TAG_TARGET) {
					currentTarget = tagValue;
				} else if (currentTag == TAG_MSG) {
					currentTargetMsg = tagValue;
				}
			}
		} else if (isInTemplageMsg) {
			if (currentTag == TAG_MSG) {
				msgRefMap.put(currentId, tagValue);
			}
		}
	}

	/**
	 * @param atts
	 */
	private String getAttributeValue(String attributeName, Attributes atts) {
		int enabledIdx = atts.getIndex(attributeName);
		String enabledStrVal = atts.getValue(enabledIdx);
		return enabledStrVal;
	}

	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name) throws SAXException {
		String endTagName = localName.intern();
		if (TAG_AVOID == endTagName && currentNameSpaceEnabled && currentTargetEnabled) {
			NameSpaceRule createdNameSpaceRule = createNameSpaceRule(currentSource, currentTarget, currentTargetMsg);
			RuleRegistry.getSingleton().addRule(project, createdNameSpaceRule);
			currentTargetMsg = null;
		} else if (TAG_NAMESPACE == endTagName) {
			isInNameSpace = false;
		} else if (TAG_MSG_TEMPLATE == endTagName) {
			isInTemplageMsg = false;
		} else if (TAG_MSG == endTagName) {
			if (isInTemplageMsg) {
				currentId = NAID;
			}
		} else if (TAG_MSG_REF == endTagName) {
			currentIdRef = NAID;
		}
	}

	static NameSpaceRule createNameSpaceRule(String source, String target, String targetMsg) {
		return new NameSpaceRule(source, target, targetMsg);
	}

	/**
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// Do nothing
		// System.out.printf("\tstartPrefixMapping - prefix=%-20s uri=%-20s \n",
		// prefix, uri);
	}

	/**
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		// Do nothing
		// System.out.printf("\tstartPrefixMapping - prefix=%-20s \n", prefix);
	}

	/**
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	/**
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data) throws SAXException {
		// Do nothing
	}

	/**
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException {
		// Do nothing
		// System.out.printf("skippedEntity - ch=%-50s \n", name);
	}

}