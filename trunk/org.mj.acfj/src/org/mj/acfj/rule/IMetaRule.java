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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.mj.acfj.rule.impl.AbstractRule;
import org.mj.acfj.rule.impl.MetaRule;

/**
 * @author Mounir Jarra•
 * 
 */
public interface IMetaRule extends Set<AbstractRule>, IRule {

	public static final MetaRule EMPTY_META_RULE = new EmptyMetaRule();

	/**
	 * @author Mounir Jarra•
	 * 
	 */
	public static final class EmptyMetaRule extends MetaRule implements IMetaRule {

		private static final long serialVersionUID = 1L;

		private static final Set<AbstractRule> EMPTY_SET = Collections.emptySet();

		private EmptyMetaRule() {
			super();
		}

		/**
		 * @see java.util.Set#add(java.lang.Object)
		 */
		public boolean add(AbstractRule o) {
			return EMPTY_SET.add(o);
		}

		/**
		 * @see java.util.Set#addAll(java.util.Collection)
		 */
		public boolean addAll(Collection<? extends AbstractRule> c) {
			return EMPTY_SET.addAll(c);
		}

		/**
		 * @see java.util.Set#clear()
		 */
		public void clear() {
			EMPTY_SET.clear();
		}

		/**
		 * @see java.util.Set#contains(java.lang.Object)
		 */
		public boolean contains(Object o) {
			return EMPTY_SET.contains(o);
		}

		/**
		 * @see java.util.Set#containsAll(java.util.Collection)
		 */
		public boolean containsAll(Collection<?> c) {
			return EMPTY_SET.containsAll(c);
		}

		/**
		 * @see java.util.Set#isEmpty()
		 */
		public boolean isEmpty() {
			return EMPTY_SET.isEmpty();
		}

		/**
		 * @see java.util.Set#iterator()
		 */
		public Iterator<AbstractRule> iterator() {
			return EMPTY_SET.iterator();
		}

		/**
		 * @see java.util.Set#remove(java.lang.Object)
		 */
		public boolean remove(Object o) {
			return EMPTY_SET.remove(o);
		}

		/**
		 * @see java.util.Set#removeAll(java.util.Collection)
		 */
		public boolean removeAll(Collection<?> c) {
			return EMPTY_SET.removeAll(c);
		}

		/**
		 * @see java.util.Set#retainAll(java.util.Collection)
		 */
		public boolean retainAll(Collection<?> c) {
			return EMPTY_SET.retainAll(c);
		}

		/**
		 * @see java.util.Set#size()
		 */
		public int size() {
			return EMPTY_SET.size();
		}

		/**
		 * @see java.util.Set#toArray()
		 */
		public Object[] toArray() {
			return EMPTY_SET.toArray();
		}

		/**
		 * @see java.util.Set#toArray(T[])
		 */
		public <T> T[] toArray(T[] a) {
			return EMPTY_SET.toArray(a);
		}

		/**
		 * @see org.mj.acfj.rule.IRule#getProblems()
		 */
		public List<CategorizedProblem> getProblems() {
			return Collections.emptyList();
		}

		/**
		 * @see org.mj.acfj.rule.IRule#isEnabled()
		 */
		public boolean isEnabled() {
			return false;
		}

		/**
		 * @see org.mj.acfj.rule.IRule#isMet()
		 */
		public boolean isMet() {
			return false;
		}

	};

}
