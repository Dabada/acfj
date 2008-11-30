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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.mj.acfj.ACFJConstants;
import org.mj.acfj.Activator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Mounir Jarra•
 * 
 */
class XMLErrorHandler implements ErrorHandler, ACFJConstants {

	private IFile file;

	public XMLErrorHandler(IFile file) {
		if (file == null) {
			throw new IllegalArgumentException("file parameter can't be null!");
		}
		this.file = file;
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_ERROR);
		LOGGER.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, exception.getLocalizedMessage(), exception));
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_ERROR);
		LOGGER.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, exception.getLocalizedMessage(), exception));
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException exception) throws SAXException {
		addMarker(exception, IMarker.SEVERITY_WARNING);
		LOGGER.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, exception.getLocalizedMessage(), exception));
	}

	/**
	 * @param e
	 * @param severity
	 */
	private void addMarker(SAXParseException e, int severity) {
		BuilderUtils.addMarker(file, RULE_MARKER_TYPE, e.getMessage(), e.getLineNumber(), severity);
	}

}