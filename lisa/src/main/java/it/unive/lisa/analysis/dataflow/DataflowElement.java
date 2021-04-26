package it.unive.lisa.analysis.dataflow;

import it.unive.lisa.analysis.SemanticEvaluator;
import it.unive.lisa.analysis.representation.DomainRepresentation;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.ValueExpression;
import java.util.Collection;

/**
 * An element of the dataflow domain, that is associated to an
 * {@link Identifier} through {@link #getIdentifier()}. A domain element
 * implements standard dataflow
 * {@link #gen(Identifier, ValueExpression, ProgramPoint, DataflowDomain)} and
 * {@link #kill(Identifier, ValueExpression, ProgramPoint, DataflowDomain)}
 * operations.
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
	 * Yields the {@link Identifier} this element is associated with.
	 * 
	 * @return the identifier
	 */
	Identifier getIdentifier();

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
	 */
	Collection<E> gen(Identifier id, ValueExpression expression, ProgramPoint pp, D domain);

	/**
	 * The dataflow <i>kill</i> operation, yielding the {@link Identifier}s that
	 * are killed by the assignment of the given {@code expression} to the given
	 * {@code id}.
	 * 
	 * @param id         the {@link Identifier} being assigned
	 * @param expression the expressions that is being assigned to {@code id}
	 * @param pp         the program point where this operation happens
	 * @param domain     the {@link DataflowDomain} that is being used to track
	 *                       instances of this element
	 * 
	 * @return the collection of identifiers that are killed by the assignment
	 */
	Collection<Identifier> kill(Identifier id, ValueExpression expression, ProgramPoint pp, D domain);

	/**
	 * Yields a {@link DomainRepresentation} of the information contained in
	 * this domain's instance.
	 * 
	 * @return the representation
	 */
	DomainRepresentation representation();
}
