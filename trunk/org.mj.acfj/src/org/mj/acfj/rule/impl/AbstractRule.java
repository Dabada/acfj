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
package org.mj.acfj.rule.impl;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.mj.acfj.ACFJConstants;
import org.mj.acfj.Activator;
import org.mj.acfj.rule.IRule;

/**
 * @author Mounir Jarra•
 *
 */
public abstract class AbstractRule extends ASTVisitor implements IRule, ACFJConstants {

	private static final ILog LOGGER = Activator.getDefault().getLog();

	private CompilationUnit compilationUnit;

	private IResource resource;

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.CompilationUnit)
	 */
	@Override
	public boolean visit(CompilationUnit node) {
		this.compilationUnit = node;
		ITypeRoot typeRoot = compilationUnit.getTypeRoot();
		this.resource = (typeRoot != null)
				? typeRoot.getResource()
				: null;
		//		LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, ("visit CompilationUnit - " + compilationUnit.toString())));
		System.out.println("Scanned file : " + new String(getFileName()));
		return super.visit(node);
	}

	/**
	 * @param position
	 * @return
	 * @see org.eclipse.jdt.core.dom.CompilationUnit#getColumnNumber(int)
	 */
	protected final int getColumnNumber(int position) {
		return this.compilationUnit.getColumnNumber(position);
	}

	/**
	 * Delegate method
	 * 
	 * @see org.eclipse.jdt.core.dom.CompilationUnit#getLineNumber(int)
	 */
	protected final int getLineNumber(int position) {
		return this.compilationUnit.getLineNumber(position);
	}

	/**
	 * Delegate method
	 * 
	 * @see org.eclipse.jdt.core.dom.CompilationUnit#getPosition(int, int)
	 */
	protected final int getPosition(int line, int column) {
		return this.compilationUnit.getPosition(line, column);
	}

	/**
	 * @return
	 */
	protected char[] getFileName() {
		ITypeRoot typeRoot = this.compilationUnit.getTypeRoot();
		String name = resource.getName();
				
		if (typeRoot instanceof org.eclipse.jdt.internal.core.CompilationUnit) {
			return ((org.eclipse.jdt.internal.core.CompilationUnit) typeRoot).getFileName();
		}
		return null;
	}

	/**
	 * @see org.mj.acfj.rule.IRule#isMet()
	 */
	public boolean isMet() {
		return getProblems().isEmpty();
	}

}
