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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * @author Mounir Jarra•
 * 
 */
public class NameSpaceRule extends AbstractRule {

	private List<CategorizedProblem> problems = new ArrayList<CategorizedProblem>();

	private boolean enabled = true;

	private Pattern sourcePackage, targetPackage;

	private boolean enableTargetCheck = false;

	private String errorMsg;

	/**
	 * Creates a <code>PackageDependencyRule</code>.
	 * 
	 * @param sourcePackage
	 *            , the package avoided from accessing the target package.
	 * @param targetPackage
	 *            , the inaccessible package from the source package.
	 */
	public NameSpaceRule(String sourcePackage, String targetPackage, String errorMsgTemplate) {
		if (sourcePackage == null || sourcePackage.length() == 0) {
			throw new IllegalArgumentException("sourcePackage must a non null and non empty string instance!");
		}
		if (targetPackage == null || targetPackage.length() == 0) {
			throw new IllegalArgumentException("targetPackage must a non null and non empty string instance!");
		}
		if (errorMsgTemplate == null || errorMsgTemplate.length() == 0) {
			throw new IllegalArgumentException("errorMsgTemplate must a non null and non empty string instance!");
		}

		this.sourcePackage = Pattern.compile(sourcePackage);
		this.targetPackage = Pattern.compile(targetPackage);
		errorMsg = MessageFormat.format(errorMsgTemplate, sourcePackage, targetPackage);
	}

	/**
	 * @return the sourcePackage
	 */
	public String getSourcePackage() {
		return sourcePackage.pattern();
	}

	/**
	 * @return the targetPackage
	 */
	public String getTargetPackage() {
		return targetPackage.pattern();
	}

	/**
	 * @see org.mj.acfj.rule.IRule#getProblems()
	 */
	public List<CategorizedProblem> getProblems() {
		return Collections.unmodifiableList(problems);
	}

	/*
	 * @param problem
	 */
	private void addProblem(CategorizedProblem problem) {
		// TODO Creates and uses a problem registry 
		problems.add(problem);
	}

	/*
	 *
	 */
	protected void clearProblems() {
		problems.clear();
	}

	/*
	 * @param name
	 * 
	 * @param errorMsg
	 */
	private void addProblem(Name name, String errorMsg) {
		int startPosition = name.getStartPosition();
		int endPosition = startPosition + name.getLength();
		int lineNumber = getLineNumber(startPosition);
		int columnNumber = getColumnNumber(startPosition);

		addProblem(new RuleProblem(super.getFileName(), errorMsg, IProblem.ForbiddenReference,
				new String[] { name.getFullyQualifiedName() }, startPosition, endPosition, lineNumber, columnNumber));
	}

	/**
	 * @see org.mj.acfj.rule.IRule#isEnabled()
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @see org.mj.acfj.rule.impl.AbstractRule#visit(org.eclipse.jdt.core.dom.CompilationUnit)
	 */
	@Override
	public boolean visit(CompilationUnit node) {
		clearProblems();
		return super.visit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.CompilationUnit)
	 */
	@Override
	public void endVisit(CompilationUnit node) {
		super.endVisit(node);
		enableTargetCheck = false;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.PackageDeclaration)
	 */
	@Override
	public boolean visit(PackageDeclaration node) {
		String fullyQualifiedName = node.getName().getFullyQualifiedName();
		if (sourcePackage.matcher(fullyQualifiedName).matches()) {
			enableTargetCheck = true;
		}
		return false;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ImportDeclaration)
	 */
	@Override
	public boolean visit(final ImportDeclaration node) {
		if (enableTargetCheck) {
			Name name = node.getName();
			String fullyQualifiedName = name.getFullyQualifiedName();
			if (targetPackage.matcher(fullyQualifiedName).matches()) {
				addProblem(name, errorMsg);
			}
		}
		return false;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SimpleName)
	 */
	@Override
	public boolean visit(SimpleName node) {
		if (enableTargetCheck) {
			ITypeBinding resolveTypeBinding = node.resolveTypeBinding();
			String name = "";
			if (resolveTypeBinding != null) {
				name = resolveTypeBinding.getBinaryName();
				name = (name != null)
						? name
						: "";
			}
			if (targetPackage.matcher(name).matches()) {
				addProblem(node, errorMsg);
			}
			//			LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, ("visit SimpleName - " + name + getPostionStr(node))));
		}
		return false;
	}

	/**
	 * @param node
	 * @return
	 */
	private String getPostionStr(ASTNode node) {
		String positionStr = "";
		if (node != null) {
			int startPosition = node.getStartPosition();
			int endPosition = startPosition + node.getLength() - 1;
			int lineNumber = getLineNumber(startPosition);
			int columnNumber = getColumnNumber(startPosition);
			int endColumnNumber = getColumnNumber(endPosition);
			positionStr = " - (" + lineNumber + ", " + columnNumber + ", " + endColumnNumber + ")";
		}
		return positionStr;
	}

	@Override
	public String toString() {
		return "NameSpaceRule (" + this.sourcePackage + ", " + this.targetPackage + ", " + this.errorMsg + ", " + this.enabled + ")";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourcePackage.pattern() == null)
				? 0
				: sourcePackage.pattern().hashCode());
		result = prime * result + ((targetPackage.pattern() == null)
				? 0
				: targetPackage.pattern().hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NameSpaceRule other = (NameSpaceRule) obj;
		if (sourcePackage == null) {
			if (other.sourcePackage != null)
				return false;
		} else if (!sourcePackage.pattern().equals(other.sourcePackage.pattern()))
			return false;
		if (targetPackage == null) {
			if (other.targetPackage != null)
				return false;
		} else if (!targetPackage.pattern().equals(other.targetPackage.pattern()))
			return false;
		return true;
	}

}
