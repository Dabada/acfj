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

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.mj.acfj.ACFJConstants;

/**
 * @author Mounir Jarra•
 * 
 */
class RuleProblem extends CategorizedProblem implements ACFJConstants {

	private char[] fileName;
	private int id;
	private int startPosition, endPosition, line, column;
	private String[] arguments;
	private String message;

	public static final Object[] EMPTY_VALUES = {};

	public RuleProblem(char[] originatingFileName, String message, int id, String[] stringArguments, int startPosition, int endPosition,
			int line, int column) {

		this.fileName = originatingFileName;
		this.message = message;
		this.id = id;
		this.arguments = stringArguments;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.line = line;
		this.column = column;

		System.out.printf("file=%s, line=%d, column=%d \n\t\t Message=%s\n", String.valueOf(fileName), line, column, message);
	}

	/**
	 * Answer back the original arguments recorded into the problem.
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getArguments() {
		return this.arguments;
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CategorizedProblem#getCategoryID()
	 */
	public int getCategoryID() {
		return CAT_TYPE;
	}

	/**
	 * Answer the type of problem.
	 * 
	 * @see org.eclipse.jdt.core.compiler.IProblem#getID()
	 * @return int
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Returns the marker type associated to this problem.
	 * 
	 * @see org.eclipse.jdt.core.compiler.CategorizedProblem#getMarkerType()
	 */
	public String getMarkerType() {
		return ARCHITECTURE_MARKER_TYPE_PROBLEM;
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CategorizedProblem#getExtraMarkerAttributeNames()
	 */
	@Override
	public String[] getExtraMarkerAttributeNames() {
		return new String[] { IMarker.MESSAGE, IMarker.SEVERITY, IMarker.CHAR_START, IMarker.CHAR_END, IMarker.LINE_NUMBER,
				IJavaModelMarker.CATEGORY_ID, IJavaModelMarker.ID };
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CategorizedProblem#getExtraMarkerAttributeValues()
	 */
	@Override
	public Object[] getExtraMarkerAttributeValues() {
		return new Object[] { getMessage(), IMarker.SEVERITY_ERROR, getSourceStart(), getSourceEnd(), getSourceLineNumber(),
				getCategoryID(), getID() };
	}

	/**
	 * Answer a localized, human-readable message string which describes the problem.
	 * 
	 * @return java.lang.String
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Answer the file name in which the problem was found.
	 * 
	 * @return char[]
	 */
	public char[] getOriginatingFileName() {
		return this.fileName;
	}

	/**
	 * Answer the end position of the problem (inclusive), or -1 if unknown.
	 * 
	 * @return int
	 */
	public int getSourceEnd() {
		return this.endPosition;
	}

	/**
	 * Answer the line number in source where the problem begins.
	 * 
	 * @return int
	 */
	public int getSourceColumnNumber() {
		return this.column;
	}

	/**
	 * Answer the line number in source where the problem begins.
	 * 
	 * @return int
	 */
	public int getSourceLineNumber() {
		return this.line;
	}

	/**
	 * Answer the start position of the problem (inclusive), or -1 if unknown.
	 * 
	 * @return int
	 */
	public int getSourceStart() {
		return this.startPosition;
	}

/*
 * Helper method: checks the severity to see if the Error bit is set. @return boolean
 */
	public boolean isError() {
		return true;
	}

/*
 * Helper method: checks the severity to see if the Error bit is not set. @return boolean
 */
	public boolean isWarning() {
		return false;
	}

	public void setOriginatingFileName(char[] fileName) {
		this.fileName = fileName;
	}

	/**
	 * Set the end position of the problem (inclusive), or -1 if unknown.
	 * 
	 * Used for shifting problem positions.
	 * 
	 * @param sourceEnd
	 *            the new value of the sourceEnd of the receiver
	 */
	public void setSourceEnd(int sourceEnd) {
		this.endPosition = sourceEnd;
	}

	/**
	 * Set the line number in source where the problem begins.
	 * 
	 * @param lineNumber
	 *            the new value of the line number of the receiver
	 */
	public void setSourceLineNumber(int lineNumber) {

		this.line = lineNumber;
	}

	/**
	 * Set the start position of the problem (inclusive), or -1 if unknown.
	 * 
	 * Used for shifting problem positions.
	 * 
	 * @param sourceStart
	 *            the new value of the source start position of the receiver
	 */
	public void setSourceStart(int sourceStart) {
		this.startPosition = sourceStart;
	}

	/**
	 * Answers a readable name for the category which this problem belongs to, or null if none could be found. FOR TESTING PURPOSE
	 * 
	 * @return java.lang.String
	 */
	public String getInternalCategoryMessage() {
		switch (getCategoryID()) {
		case CAT_UNSPECIFIED:
			return "unspecified"; //$NON-NLS-1$
		case CAT_BUILDPATH:
			return "buildpath"; //$NON-NLS-1$
		case CAT_SYNTAX:
			return "syntax"; //$NON-NLS-1$
		case CAT_IMPORT:
			return "import"; //$NON-NLS-1$
		case CAT_TYPE:
			return "type"; //$NON-NLS-1$
		case CAT_MEMBER:
			return "member"; //$NON-NLS-1$
		case CAT_INTERNAL:
			return "internal"; //$NON-NLS-1$
		case CAT_JAVADOC:
			return "javadoc"; //$NON-NLS-1$
		case CAT_CODE_STYLE:
			return "code style"; //$NON-NLS-1$
		case CAT_POTENTIAL_PROGRAMMING_PROBLEM:
			return "potential programming problem"; //$NON-NLS-1$
		case CAT_NAME_SHADOWING_CONFLICT:
			return "name shadowing conflict"; //$NON-NLS-1$
		case CAT_DEPRECATION:
			return "deprecation"; //$NON-NLS-1$
		case CAT_UNNECESSARY_CODE:
			return "unnecessary code"; //$NON-NLS-1$
		case CAT_UNCHECKED_RAW:
			return "unchecked/raw"; //$NON-NLS-1$
		case CAT_NLS:
			return "nls"; //$NON-NLS-1$
		case CAT_RESTRICTION:
			return "restriction"; //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "Pb(" + (this.id & IProblem.IgnoreCategoriesMask) + ") "; //$NON-NLS-1$ //$NON-NLS-2$
		if (this.message != null) {
			s += this.message;
		} else {
			if (this.arguments != null)
				for (int i = 0; i < this.arguments.length; i++)
					s += " " + this.arguments[i]; //$NON-NLS-1$
		}
		return s;
	}

}
