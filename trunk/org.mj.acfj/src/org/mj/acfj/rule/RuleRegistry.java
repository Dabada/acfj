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
package org.mj.acfj.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.resources.IProject;
import org.mj.acfj.rule.impl.AbstractRule;
import org.mj.acfj.rule.impl.MetaRule;

/**
 * A <code>RuleRegistry</code> maintains a mapping between a <code>IJavaProject</code> and a <code>IRuleSet</code>
 * 
 * @author Mounir Jarra•
 * 
 */
public final class RuleRegistry {

	private Map<IProject, MetaRule> map = new HashMap<IProject, MetaRule>();

	private static volatile RuleRegistry singleton;

	private static Lock instanceLock = new ReentrantLock();

	/**
	 * Singleton class
	 */
	private RuleRegistry() {
	}

	/**
	 * @return the unique instance of <code>RuleRegistry</code>.
	 */
	public static RuleRegistry getSingleton() {
		instanceLock.lock();
		try {
			if (singleton == null) {
				singleton = new RuleRegistry();
			}
		} finally {
			instanceLock.unlock();
		}
		return singleton;
	}

	/**
	 * @param project
	 * @param rule
	 * @return
	 */
	public boolean addRule(IProject project, AbstractRule rule) {
		MetaRule projectMetaRule;
		if (map.containsKey(project)) {
			projectMetaRule = map.get(project);
		} else {
			projectMetaRule = new MetaRule();
			map.put(project, projectMetaRule);
		}
		boolean alreadyAdd = projectMetaRule.contains(rule);
		if (!alreadyAdd) {
			projectMetaRule.add(rule);
			alreadyAdd = true;
		}
		return alreadyAdd;
	}

	/**
	 * @param project
	 * @return
	 */
	public MetaRule getProjectMetaRule(IProject project) {
		MetaRule projectMetaRule = map.get(project);
		if (projectMetaRule == null) {
			return IMetaRule.EMPTY_META_RULE;
		}
		return projectMetaRule;
	}
	
	/**
	 * @param project
	 */
	public void clearProjectMetaRule(IProject project) {
		IMetaRule projectMetaRule = map.get(project);
		if (projectMetaRule != null) {
			projectMetaRule.clear();
		}
	}
}
