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
package org.mj.acfj;

import org.eclipse.core.runtime.ILog;

/**
 * @author Mounir Jarra•
 * 
 */
public interface ACFJConstants {

	/**
	 * ID of this project nature
	 */
	public static final String NATURE_ID = "org.mj.acfj.nature"; //$NON-NLS-1$

	public static final String BUILDER_ID = "org.mj.acfj.builder"; //$NON-NLS-1$

	//
	//
	//

	public static final String RULES_XSD_RELATIVE_PATH_FILE_NAME = "./xsd/rules.xsd"; //$NON-NLS-1$

	public static final String ARCHITECTURE_RULES_FILE_NAME = "architecture.rules"; //$NON-NLS-1$

	public static final String SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$

	//
	//
	//

	public static final String RULE_MARKER_TYPE = "org.mj.acfj.ruleProblem"; //$NON-NLS-1$

	public static final String ARCHITECTURE_MARKER_TYPE_PROBLEM = "org.mj.acfj.ArchitectureProblem"; //$NON-NLS-1$

	public static final ILog LOGGER = Activator.getDefault().getLog();

}
