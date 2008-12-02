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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.mj.acfj.rule.RuleRegistry;
import org.mj.acfj.rule.impl.MetaRule;

/**
 * @author Mounir Jarra•
 * 
 */
public class ACFJCompilationParticipant extends CompilationParticipant {

	private static final String ACFJ_PROJECT_NATURE = Activator.PLUGIN_ID + ".nature";

	private static final ILog LOGGER = Activator.getDefault().getLog();

	private static int count = 0;

	/**
	 * 
	 */
	public ACFJCompilationParticipant() {
		System.out.println("ACFJCompilationParticipant = " + ++count);
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CompilationParticipant#reconcile(org.eclipse.jdt.core.compiler.ReconcileContext)
	 */
	@Override
	public synchronized void reconcile(final ReconcileContext context) {
		super.reconcile(context);
		context.putProblems("org.mj.acfj.ArchitectureProblem", null);
		try {
			CompilationUnit ast = context.getAST3();
			if (ast == null) {
				return;
			}
			IJavaProject javaProject = ast.getTypeRoot().getJavaProject();
			IProject project = javaProject.getProject();

			//			List<CategorizedProblem> problems = scanForUnsatisfiedRules(project, ast);
			//			context.putProblems(ACFJConstants.ARCHITECTURE_MARKER_TYPE_PROBLEM, problems.toArray(new CategorizedProblem[problems.size()]));

		} catch (JavaModelException e) {
			LOGGER.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
		}
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CompilationParticipant#aboutToBuild(org.eclipse.jdt.core.IJavaProject)
	 */
	@Override
	public int aboutToBuild(IJavaProject project) {
		return super.aboutToBuild(project);
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CompilationParticipant#buildStarting(org.eclipse.jdt.core.compiler.BuildContext[], boolean)
	 */
	@Override
	public void buildStarting(BuildContext[] files, boolean isBatch) {
		super.buildStarting(files, isBatch);
		for (BuildContext context : files) {
			IFile file = context.getFile();
			IProject project = file.getProject();
			CompilationUnit ast = createAstFromFile(file);
			List<CategorizedProblem> problems = scanForUnsatisfiedRules(project, ast);
			context.recordNewProblems(problems.toArray(new CategorizedProblem[problems.size()]));
		}
	}

	/**
	 * @param file
	 * @return
	 */
	private CompilationUnit createAstFromFile(IFile file) {
		ICompilationUnit cu = JavaCore.createCompilationUnitFrom(file);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(cu);
		parser.setResolveBindings(true);
		CompilationUnit ast = (CompilationUnit) parser.createAST(new NullProgressMonitor());
		return ast;
	}

	/**
	 * @param project
	 * @param ast
	 * @return
	 */
	private List<CategorizedProblem> scanForUnsatisfiedRules(IProject project, CompilationUnit ast) {
		MetaRule projectMetaRule = RuleRegistry.getSingleton().getProjectMetaRule(project);
		ast.accept(projectMetaRule);
		List<CategorizedProblem> problems = projectMetaRule.getProblems();
		return problems;
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CompilationParticipant#cleanStarting(org.eclipse.jdt.core.IJavaProject)
	 */
	@Override
	public void cleanStarting(IJavaProject project) {
		super.cleanStarting(project);
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CompilationParticipant#isActive(org.eclipse.jdt.core.IJavaProject)
	 */
	@Override
	public boolean isActive(IJavaProject javaProject) {
		boolean active = false;
		IProject project = javaProject.getProject();
		String[] natureIds = null;

		try {
			natureIds = project.getDescription().getNatureIds();
			for (int i = 0; i < natureIds.length; i++) {
				if (ACFJ_PROJECT_NATURE.equals(natureIds[i])) {
					active = true;
					break;
				}
			}
		} catch (CoreException e) {
			// if no project's natures are accessible disables this compilation participant
			active = false;
		}

		LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "ACFJ isActive on " + javaProject.getElementName() + " = " + active));
		if (active) {
			MetaRule projectMetaRule = RuleRegistry.getSingleton().getProjectMetaRule(project);
			LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "ACFJ : found " + projectMetaRule.size() + " active Rules"));
		}
		return active;
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CompilationParticipant#isAnnotationProcessor()
	 */
	@Override
	public boolean isAnnotationProcessor() {
		return super.isAnnotationProcessor();
	}

	/**
	 * @see org.eclipse.jdt.core.compiler.CompilationParticipant#processAnnotations(org.eclipse.jdt.core.compiler.BuildContext[])
	 */
	@Override
	public void processAnnotations(BuildContext[] files) {
		super.processAnnotations(files);
	}

}
