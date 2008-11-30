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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;
import org.mj.acfj.Activator;
import org.mj.acfj.rule.IMetaRule;
import org.mj.acfj.rule.IRule;

/**
 * @author Mounir Jarra•
 * 
 */
public class MetaRule extends AbstractRule implements IMetaRule {

	private static final ILog LOGGER = Activator.getDefault().getLog();

	private HashSet<AbstractRule> rules = new HashSet<AbstractRule>();

	private Map<RuleStatKey, Boolean> rulesStat = new HashMap<RuleStatKey, Boolean>();
	
	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#postVisit(org.eclipse.jdt.core.dom.ASTNode)
	 */
	@Override
	public void postVisit(ASTNode node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.postVisit(node);
			}
		}
		super.postVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#preVisit(org.eclipse.jdt.core.dom.ASTNode)
	 */
	@Override
	public void preVisit(ASTNode node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.preVisit(node);
			}
		}
		super.preVisit(node);
	}

	/**
	 * @see org.mj.acfj.rule.impl.AbstractRule#visit(org.eclipse.jdt.core.dom.CompilationUnit)
	 */
	@Override
	public boolean visit(CompilationUnit node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnnotationTypeDeclaration)
	 */
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration)
	 */
	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnonymousClassDeclaration)
	 */
	@Override
	public boolean visit(AnonymousClassDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ArrayAccess)
	 */
	@Override
	public boolean visit(ArrayAccess node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ArrayCreation)
	 */
	@Override
	public boolean visit(ArrayCreation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ArrayInitializer)
	 */
	@Override
	public boolean visit(ArrayInitializer node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ArrayType)
	 */
	@Override
	public boolean visit(ArrayType node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AssertStatement)
	 */
	@Override
	public boolean visit(AssertStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Assignment)
	 */
	@Override
	public boolean visit(Assignment node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Block)
	 */
	@Override
	public boolean visit(Block node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.BlockComment)
	 */
	@Override
	public boolean visit(BlockComment node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.BooleanLiteral)
	 */
	@Override
	public boolean visit(BooleanLiteral node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.BreakStatement)
	 */
	@Override
	public boolean visit(BreakStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.CastExpression)
	 */
	@Override
	public boolean visit(CastExpression node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.CatchClause)
	 */
	@Override
	public boolean visit(CatchClause node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.CharacterLiteral)
	 */
	@Override
	public boolean visit(CharacterLiteral node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ClassInstanceCreation)
	 */
	@Override
	public boolean visit(ClassInstanceCreation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ConditionalExpression)
	 */
	@Override
	public boolean visit(ConditionalExpression node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ConstructorInvocation)
	 */
	@Override
	public boolean visit(ConstructorInvocation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ContinueStatement)
	 */
	@Override
	public boolean visit(ContinueStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.DoStatement)
	 */
	@Override
	public boolean visit(DoStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.EmptyStatement)
	 */
	@Override
	public boolean visit(EmptyStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.EnhancedForStatement)
	 */
	@Override
	public boolean visit(EnhancedForStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.EnumConstantDeclaration)
	 */
	@Override
	public boolean visit(EnumConstantDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.EnumDeclaration)
	 */
	@Override
	public boolean visit(EnumDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ExpressionStatement)
	 */
	@Override
	public boolean visit(ExpressionStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.FieldAccess)
	 */
	@Override
	public boolean visit(FieldAccess node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.FieldDeclaration)
	 */
	@Override
	public boolean visit(FieldDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ForStatement)
	 */
	@Override
	public boolean visit(ForStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.IfStatement)
	 */
	@Override
	public boolean visit(IfStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ImportDeclaration)
	 */
	@Override
	public boolean visit(ImportDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.InfixExpression)
	 */
	@Override
	public boolean visit(InfixExpression node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Initializer)
	 */
	@Override
	public boolean visit(Initializer node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.InstanceofExpression)
	 */
	@Override
	public boolean visit(InstanceofExpression node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Javadoc)
	 */
	@Override
	public boolean visit(Javadoc node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.LabeledStatement)
	 */
	@Override
	public boolean visit(LabeledStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.LineComment)
	 */
	@Override
	public boolean visit(LineComment node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MarkerAnnotation)
	 */
	@Override
	public boolean visit(MarkerAnnotation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MemberRef)
	 */
	@Override
	public boolean visit(MemberRef node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MemberValuePair)
	 */
	@Override
	public boolean visit(MemberValuePair node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodInvocation)
	 */
	@Override
	public boolean visit(MethodInvocation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodRef)
	 */
	@Override
	public boolean visit(MethodRef node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodRefParameter)
	 */
	@Override
	public boolean visit(MethodRefParameter node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Modifier)
	 */
	@Override
	public boolean visit(Modifier node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.NormalAnnotation)
	 */
	@Override
	public boolean visit(NormalAnnotation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.NullLiteral)
	 */
	@Override
	public boolean visit(NullLiteral node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.NumberLiteral)
	 */
	@Override
	public boolean visit(NumberLiteral node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.PackageDeclaration)
	 */
	@Override
	public boolean visit(PackageDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ParameterizedType)
	 */
	@Override
	public boolean visit(ParameterizedType node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ParenthesizedExpression)
	 */
	@Override
	public boolean visit(ParenthesizedExpression node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.PostfixExpression)
	 */
	@Override
	public boolean visit(PostfixExpression node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.PrefixExpression)
	 */
	@Override
	public boolean visit(PrefixExpression node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.PrimitiveType)
	 */
	@Override
	public boolean visit(PrimitiveType node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.QualifiedName)
	 */
	@Override
	public boolean visit(QualifiedName node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.QualifiedType)
	 */
	@Override
	public boolean visit(QualifiedType node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ReturnStatement)
	 */
	@Override
	public boolean visit(ReturnStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SimpleName)
	 */
	@Override
	public boolean visit(SimpleName node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SimpleType)
	 */
	@Override
	public boolean visit(SimpleType node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SingleMemberAnnotation)
	 */
	@Override
	public boolean visit(SingleMemberAnnotation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SingleVariableDeclaration)
	 */
	@Override
	public boolean visit(SingleVariableDeclaration node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.StringLiteral)
	 */
	@Override
	public boolean visit(StringLiteral node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SuperConstructorInvocation)
	 */
	@Override
	public boolean visit(SuperConstructorInvocation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SuperFieldAccess)
	 */
	@Override
	public boolean visit(SuperFieldAccess node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SuperMethodInvocation)
	 */
	@Override
	public boolean visit(SuperMethodInvocation node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SwitchCase)
	 */
	@Override
	public boolean visit(SwitchCase node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SwitchStatement)
	 */
	@Override
	public boolean visit(SwitchStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.SynchronizedStatement)
	 */
	@Override
	public boolean visit(SynchronizedStatement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TagElement)
	 */
	@Override
	public boolean visit(TagElement node) {

		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;

	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TextElement)
	 */
	@Override
	public boolean visit(TextElement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ThisExpression)
	 */
	@Override
	public boolean visit(ThisExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ThrowStatement)
	 */
	@Override
	public boolean visit(ThrowStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TryStatement)
	 */
	@Override
	public boolean visit(TryStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeDeclaration)
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeDeclarationStatement)
	 */
	@Override
	public boolean visit(TypeDeclarationStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeLiteral)
	 */
	@Override
	public boolean visit(TypeLiteral node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeParameter)
	 */
	@Override
	public boolean visit(TypeParameter node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.VariableDeclarationExpression)
	 */
	@Override
	public boolean visit(VariableDeclarationExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.VariableDeclarationFragment)
	 */
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.VariableDeclarationStatement)
	 */
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.WhileStatement)
	 */
	@Override
	public boolean visit(WhileStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.WildcardType)
	 */
	@Override
	public boolean visit(WildcardType node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				RuleStatKey key = new RuleStatKey(rule, node.getParent());
				Boolean canVisit = rulesStat.get(key);
				if (canVisit == null) {
					// Root node
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else if (canVisit.booleanValue()) {
					key.node = node;
					rulesStat.put(key, rule.visit(node));
				} else {
					// do not visit the rule
					// Forward skip to sub nodes
					key.node = node;
					rulesStat.put(key, false);
					if (false) {
						String msg = "Skip visit child = " + node + " of parent= " + node.getParent();
						LOGGER.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, msg));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.AnnotationTypeDeclaration)
	 */
	@Override
	public void endVisit(AnnotationTypeDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration)
	 */
	@Override
	public void endVisit(AnnotationTypeMemberDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.AnonymousClassDeclaration)
	 */
	@Override
	public void endVisit(AnonymousClassDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ArrayAccess)
	 */
	@Override
	public void endVisit(ArrayAccess node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ArrayCreation)
	 */
	@Override
	public void endVisit(ArrayCreation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ArrayInitializer)
	 */
	@Override
	public void endVisit(ArrayInitializer node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ArrayType)
	 */
	@Override
	public void endVisit(ArrayType node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.AssertStatement)
	 */
	@Override
	public void endVisit(AssertStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.Assignment)
	 */
	@Override
	public void endVisit(Assignment node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.Block)
	 */
	@Override
	public void endVisit(Block node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.BlockComment)
	 */
	@Override
	public void endVisit(BlockComment node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.BooleanLiteral)
	 */
	@Override
	public void endVisit(BooleanLiteral node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.BreakStatement)
	 */
	@Override
	public void endVisit(BreakStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.CastExpression)
	 */
	@Override
	public void endVisit(CastExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.CatchClause)
	 */
	@Override
	public void endVisit(CatchClause node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.CharacterLiteral)
	 */
	@Override
	public void endVisit(CharacterLiteral node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ClassInstanceCreation)
	 */
	@Override
	public void endVisit(ClassInstanceCreation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.CompilationUnit)
	 */
	@Override
	public void endVisit(CompilationUnit node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ConditionalExpression)
	 */
	@Override
	public void endVisit(ConditionalExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ConstructorInvocation)
	 */
	@Override
	public void endVisit(ConstructorInvocation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ContinueStatement)
	 */
	@Override
	public void endVisit(ContinueStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.DoStatement)
	 */
	@Override
	public void endVisit(DoStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.EmptyStatement)
	 */
	@Override
	public void endVisit(EmptyStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.EnhancedForStatement)
	 */
	@Override
	public void endVisit(EnhancedForStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.EnumConstantDeclaration)
	 */
	@Override
	public void endVisit(EnumConstantDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.EnumDeclaration)
	 */
	@Override
	public void endVisit(EnumDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ExpressionStatement)
	 */
	@Override
	public void endVisit(ExpressionStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.FieldAccess)
	 */
	@Override
	public void endVisit(FieldAccess node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.FieldDeclaration)
	 */
	@Override
	public void endVisit(FieldDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ForStatement)
	 */
	@Override
	public void endVisit(ForStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.IfStatement)
	 */
	@Override
	public void endVisit(IfStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ImportDeclaration)
	 */
	@Override
	public void endVisit(ImportDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.InfixExpression)
	 */
	@Override
	public void endVisit(InfixExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.Initializer)
	 */
	@Override
	public void endVisit(Initializer node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.InstanceofExpression)
	 */
	@Override
	public void endVisit(InstanceofExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.Javadoc)
	 */
	@Override
	public void endVisit(Javadoc node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.LabeledStatement)
	 */
	@Override
	public void endVisit(LabeledStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.LineComment)
	 */
	@Override
	public void endVisit(LineComment node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MarkerAnnotation)
	 */
	@Override
	public void endVisit(MarkerAnnotation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MemberRef)
	 */
	@Override
	public void endVisit(MemberRef node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MemberValuePair)
	 */
	@Override
	public void endVisit(MemberValuePair node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MethodDeclaration)
	 */
	@Override
	public void endVisit(MethodDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MethodInvocation)
	 */
	@Override
	public void endVisit(MethodInvocation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MethodRef)
	 */
	@Override
	public void endVisit(MethodRef node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MethodRefParameter)
	 */
	@Override
	public void endVisit(MethodRefParameter node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.Modifier)
	 */
	@Override
	public void endVisit(Modifier node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.NormalAnnotation)
	 */
	@Override
	public void endVisit(NormalAnnotation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.NullLiteral)
	 */
	@Override
	public void endVisit(NullLiteral node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.NumberLiteral)
	 */
	@Override
	public void endVisit(NumberLiteral node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.PackageDeclaration)
	 */
	@Override
	public void endVisit(PackageDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ParameterizedType)
	 */
	@Override
	public void endVisit(ParameterizedType node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ParenthesizedExpression)
	 */
	@Override
	public void endVisit(ParenthesizedExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.PostfixExpression)
	 */
	@Override
	public void endVisit(PostfixExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.PrefixExpression)
	 */
	@Override
	public void endVisit(PrefixExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.PrimitiveType)
	 */
	@Override
	public void endVisit(PrimitiveType node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.QualifiedName)
	 */
	@Override
	public void endVisit(QualifiedName node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.QualifiedType)
	 */
	@Override
	public void endVisit(QualifiedType node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ReturnStatement)
	 */
	@Override
	public void endVisit(ReturnStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SimpleName)
	 */
	@Override
	public void endVisit(SimpleName node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SimpleType)
	 */
	@Override
	public void endVisit(SimpleType node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SingleMemberAnnotation)
	 */
	@Override
	public void endVisit(SingleMemberAnnotation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SingleVariableDeclaration)
	 */
	@Override
	public void endVisit(SingleVariableDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.StringLiteral)
	 */
	@Override
	public void endVisit(StringLiteral node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SuperConstructorInvocation)
	 */
	@Override
	public void endVisit(SuperConstructorInvocation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SuperFieldAccess)
	 */
	@Override
	public void endVisit(SuperFieldAccess node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SuperMethodInvocation)
	 */
	@Override
	public void endVisit(SuperMethodInvocation node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SwitchCase)
	 */
	@Override
	public void endVisit(SwitchCase node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SwitchStatement)
	 */
	@Override
	public void endVisit(SwitchStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.SynchronizedStatement)
	 */
	@Override
	public void endVisit(SynchronizedStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TagElement)
	 */
	@Override
	public void endVisit(TagElement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TextElement)
	 */
	@Override
	public void endVisit(TextElement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ThisExpression)
	 */
	@Override
	public void endVisit(ThisExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ThrowStatement)
	 */
	@Override
	public void endVisit(ThrowStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TryStatement)
	 */
	@Override
	public void endVisit(TryStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TypeDeclaration)
	 */
	@Override
	public void endVisit(TypeDeclaration node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TypeDeclarationStatement)
	 */
	@Override
	public void endVisit(TypeDeclarationStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TypeLiteral)
	 */
	@Override
	public void endVisit(TypeLiteral node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TypeParameter)
	 */
	@Override
	public void endVisit(TypeParameter node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.VariableDeclarationExpression)
	 */
	@Override
	public void endVisit(VariableDeclarationExpression node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.VariableDeclarationFragment)
	 */
	@Override
	public void endVisit(VariableDeclarationFragment node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.VariableDeclarationStatement)
	 */
	@Override
	public void endVisit(VariableDeclarationStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.WhileStatement)
	 */
	@Override
	public void endVisit(WhileStatement node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.WildcardType)
	 */
	@Override
	public void endVisit(WildcardType node) {
		for (AbstractRule rule : rules) {
			if (rule.isEnabled()) {
				rule.endVisit(node);
			}
		}
		super.endVisit(node);
	}

	/**
	 * @see org.mj.acfj.rule.IRule#getProblems()
	 */
	public List<CategorizedProblem> getProblems() {
		ArrayList<CategorizedProblem> result = new ArrayList<CategorizedProblem>();
		for (AbstractRule rule : rules) {
			result.addAll(rule.getProblems());
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * @see org.mj.acfj.rule.IRule#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	//******************************************************************************************
	// Set Implementation
	//******************************************************************************************	

	/**
	 * @param o
	 * @return
	 * @see java.util.HashSet#add(java.lang.Object)
	 */
	public boolean add(AbstractRule o) {
		return rules.add(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends AbstractRule> c) {
		return rules.addAll(c);
	}

	/**
	 * 
	 * @see java.util.HashSet#clear()
	 */
	public void clear() {
		rules.clear();
		rulesStat.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.HashSet#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return rules.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.AbstractCollection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {
		return rules.containsAll(c);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.AbstractSet#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return rules.equals(o);
	}

	/**
	 * @return
	 * @see java.util.AbstractSet#hashCode()
	 */
	public int hashCode() {
		return rules.hashCode();
	}

	/**
	 * @return
	 * @see java.util.HashSet#isEmpty()
	 */
	public boolean isEmpty() {
		return rules.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.HashSet#iterator()
	 */
	public Iterator<AbstractRule> iterator() {
		return rules.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.HashSet#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return rules.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.AbstractSet#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		return rules.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.AbstractCollection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c) {
		return rules.retainAll(c);
	}

	/**
	 * @return
	 * @see java.util.HashSet#size()
	 */
	public int size() {
		return rules.size();
	}

	/**
	 * @return
	 * @see java.util.AbstractCollection#toArray()
	 */
	public Object[] toArray() {
		return rules.toArray();
	}

	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see java.util.AbstractCollection#toArray(T[])
	 */
	public <T> T[] toArray(T[] a) {
		return rules.toArray(a);
	}

	/**
	 * @return
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		return rules.toString();
	}

	/**
	 * @author Mounir Jarra•
	 * 
	 */
	private static class RuleStatKey {
		IRule rule;
		ASTNode node;

		/**
		 * @param rule
		 * @param node
		 */
		RuleStatKey(IRule rule, ASTNode node) {
			super();
			this.rule = rule;
			this.node = node;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((node == null)
					? 0
					: node.hashCode());
			result = prime * result + ((rule == null)
					? 0
					: rule.hashCode());
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
			final RuleStatKey other = (RuleStatKey) obj;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			if (rule == null) {
				if (other.rule != null)
					return false;
			} else if (!rule.equals(other.rule))
				return false;
			return true;
		}

	}

}
