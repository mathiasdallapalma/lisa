package it.unive.lisa.analysis.dataflow;

import it.unive.lisa.analysis.ScopeToken;
import it.unive.lisa.analysis.SemanticDomain;
import it.unive.lisa.analysis.SemanticEvaluator;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.representation.DomainRepresentation;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.ValueExpression;
import java.util.Collection;

/**
 * An element of the dataflow domain, that contains a collection of
 * {@link Identifier}s in its definition. A domain element implements standard
 * dataflow gen (
 * {@link #gen(Identifier, ValueExpression, ProgramPoint, DataflowDomain)},
 * {@link #gen(ValueExpression, ProgramPoint, DataflowDomain)}) and kill
 * ({@link #kill(Identifier, ValueExpression, ProgramPoint, DataflowDomain)},
 * {@link #kill(ValueExpression, ProgramPoint, DataflowDomain)}) operations.
 * 
 * @author <a href="mailto:luca.negrini@unive.it">Luca Negrini</a>
 * 
 * @param <D> the concrete type of {@link DataflowDomain} that contains
 *                instances of this element
 * @param <E> the concrete type of {@link DataflowElement}
 */
public interface DataflowElement<D extends DataflowDomain<D, E>, E extends DataflowElement<D, E>>
		extends SemanticEvaluator {

	/**
	 * Yields all the {@link Identifier}s that are involved in the definition of
	 * this element.
	 * 
	 * @return the identifiers
	 */
	Collection<Identifier> getInvolvedIdentifiers();

	/**
	 * The dataflow <i>gen</i> operation, yielding the dataflow elements that
	 * are generated by the assignment of the given {@code expression} to the
	 * given {@code id}.
	 * 
	 * @param id         the {@link Identifier} being assigned
	 * @param expression the expressions that is being assigned to {@code id}
	 * @param pp         the program point where this operation happens
	 * @param domain     the {@link DataflowDomain} that is being used to track
	 *                       instances of this element
	 * 
	 * @return the collection of dataflow elements that are generated by the
	 *             assignment
	 * 
	 * @throws SemanticException if an error occurs during the computation
	 */
	Collection<E> gen(Identifier id, ValueExpression expression, ProgramPoint pp, D domain) throws SemanticException;

	/**
	 * The dataflow <i>gen</i> operation, yielding the dataflow elements that
	 * are generated by evaluating the given non-assigning {@code expression}.
	 * 
	 * @param expression the expressions that is being evaluated
	 * @param pp         the program point where this operation happens
	 * @param domain     the {@link DataflowDomain} that is being used to track
	 *                       instances of this element
	 * 
	 * @return the collection of dataflow elements that are generated by the
	 *             expression
	 * 
	 * @throws SemanticException if an error occurs during the computation
	 */
	Collection<E> gen(ValueExpression expression, ProgramPoint pp, D domain) throws SemanticException;

	/**
	 * The dataflow <i>kill</i> operation, yielding the dataflow elements that
	 * are killed by the assignment of the given {@code expression} to the given
	 * {@code id}.
	 * 
	 * @param id         the {@link Identifier} being assigned
	 * @param expression the expressions that is being assigned to {@code id}
	 * @param pp         the program point where this operation happens
	 * @param domain     the {@link DataflowDomain} that is being used to track
	 *                       instances of this element
	 * 
	 * @return the collection of dataflow elements that are killed by the
	 *             assignment
	 * 
	 * @throws SemanticException if an error occurs during the computation
	 */
	Collection<E> kill(Identifier id, ValueExpression expression, ProgramPoint pp, D domain) throws SemanticException;

	/**
	 * The dataflow <i>kill</i> operation, yielding the dataflow elements that
	 * are killed by evaluating the given non-assigning {@code expression}.
	 * 
	 * @param expression the expressions that is being evaluated
	 * @param pp         the program point where this operation happens
	 * @param domain     the {@link DataflowDomain} that is being used to track
	 *                       instances of this element
	 * 
	 * @return the collection of dataflow elements that are killed by the
	 *             expression
	 * 
	 * @throws SemanticException if an error occurs during the computation
	 */
	Collection<E> kill(ValueExpression expression, ProgramPoint pp, D domain) throws SemanticException;

	/**
	 * Yields a {@link DomainRepresentation} of the information contained in
	 * this domain's instance.
	 * 
	 * @return the representation
	 */
	DomainRepresentation representation();

	/**
	 * Push a scope to the dataflow element.
	 * 
	 * @param token the scope to be pushed
	 * 
	 * @return the element with the pushed scope
	 * 
	 * @throws SemanticException if the scope cannot be pushed
	 * 
	 * @see SemanticDomain#pushScope(ScopeToken)
	 */
	E pushScope(ScopeToken token) throws SemanticException;

	/**
	 * Pop a scope to the dataflow element.
	 * 
	 * @param token the scope to be popped
	 * 
	 * @return the element with the popped scope
	 * 
	 * @throws SemanticException if the scope cannot be popped
	 * 
	 * @see SemanticDomain#popScope(ScopeToken)
	 */
	E popScope(ScopeToken token) throws SemanticException;

	@Override
	default boolean tracksIdentifiers(Identifier id) {
		return canProcess(id);
	}

	@Override
	default boolean canProcess(SymbolicExpression expression) {
		return expression.getRuntimeTypes().anyMatch(t -> !t.isPointerType() && !t.isInMemoryType());
	}
}
