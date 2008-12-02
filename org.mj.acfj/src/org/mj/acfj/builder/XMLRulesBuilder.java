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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.mj.acfj.ACFJConstants;
import org.mj.acfj.Activator;
import org.mj.acfj.rule.RuleRegistry;
import org.osgi.framework.Bundle;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Mounir Jarra•
 *
 */
public class XMLRulesBuilder extends IncrementalProjectBuilder implements ACFJConstants {

	private static volatile SAXParserFactory parserFactory;

	/**
	 * @param bundle
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private static SAXParserFactory getSAXParserFactory(Bundle bundle) throws IOException, SAXException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		InputStream iss = null;
		iss = new BufferedInputStream(FileLocator.openStream(bundle, new Path(RULES_XSD_RELATIVE_PATH_FILE_NAME), false));
		SchemaFactory schemaFactory = SchemaFactory.newInstance(SCHEMA_LANGUAGE);
		factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(iss) }));
		factory.setNamespaceAware(true);
		factory.setFeature("http://xml.org/sax/features/string-interning", true);
		return factory;
	}

	/**
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static SAXParser getParser(Bundle bundle) throws ParserConfigurationException, SAXException, IOException {
		if (parserFactory == null) {
			parserFactory = getSAXParserFactory(bundle);
		}
		return parserFactory.newSAXParser();
	}

	/**
	 * @param resource
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws CoreException
	 */
	void checkXML(IFile file) throws ParserConfigurationException, SAXException, IOException, CoreException {
		IProject project = file.getProject();
		loadProjectRules(project, file);
	}

	/**
	 * @param project
	 * @param file
	 * @throws CoreException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static synchronized void loadProjectRules(IProject project, IFile file) throws CoreException, ParserConfigurationException,
			SAXException, IOException {
		BuilderUtils.deleteMarkers(file, RULE_MARKER_TYPE);
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		SAXParser parser = getParser(bundle);
		XMLReader reader = parser.getXMLReader();
		reader.setErrorHandler(new XMLErrorHandler(file));
		reader.setContentHandler(new XMLRulesContentHandler(project, file));
		InputStream iss = new BufferedInputStream(file.getContents());
		reader.parse(new InputSource(iss));
	}

	/**
	 * @author Mounir Jarra•
	 * 
	 */
	private class DeltaVisitor implements IResourceDeltaVisitor {

		/**
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			if (resource.getType() == IResource.FILE && ARCHITECTURE_RULES_FILE_NAME.equalsIgnoreCase(resource.getName())) {
				try {
					IFile file = (IFile) resource;
					switch (delta.getKind()) {
					case IResourceDelta.ADDED:
						checkXML(file);
						break;
					case IResourceDelta.REMOVED:
						IProject project = resource.getProject();
						RuleRegistry.getSingleton().clearProjectMetaRule(project);
						break;
					case IResourceDelta.CHANGED:
						checkXML(file);
						break;
					}
				} catch (Exception e) {
					Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e);
					LOGGER.log(status);
					throw new CoreException(status);
				}
			}

			return true;
		}
	}

	/**
	 * @author Mounir Jarra•
	 * 
	 */
	private class ResourceVisitor implements IResourceVisitor {

		/**
		 * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
		 */
		public boolean visit(IResource resource) throws CoreException {
			if (resource.getType() == IResource.FILE && ARCHITECTURE_RULES_FILE_NAME.equalsIgnoreCase(resource.getName())) {
				try {
					IFile file = (IFile) resource;
					checkXML(file);
				} catch (Exception e) {
					Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e);
					LOGGER.log(status);
					throw new CoreException(status);
				}
			}
			//return true to continue visiting children.
			return true;
		}
	}

	/**
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	/**
	 * @param monitor
	 * @throws CoreException
	 */
	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		try {
			getProject().accept(new ResourceVisitor());
		} catch (CoreException e) {
			LOGGER.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			throw e;
		}
	}

	/**
	 * @param delta
	 * @param monitor
	 * @throws CoreException
	 */
	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		try {
			delta.accept(new DeltaVisitor());
		} catch (CoreException e) {
			LOGGER.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			throw e;
		}
	}
}
