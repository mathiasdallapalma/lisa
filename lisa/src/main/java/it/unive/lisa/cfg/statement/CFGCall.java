package it.unive.lisa.cfg.statement;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.HeapDomain;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.ValueDomain;
import it.unive.lisa.analysis.callgraph.CallGraph;
import it.unive.lisa.cfg.CFG;
import it.unive.lisa.symbolic.Skip;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.ValueIdentifier;

/**
 * A call to one of the CFG under analysis.
 * 
 * @author <a href="mailto:luca.negrini@unive.it">Luca Negrini</a>
 */
public class CFGCall extends Call implements MetaVariableCreator {

	/**
	 * The target of this call
	 */
	private final CFG target;

	/**
	 * Builds the CFG call. The location where this call happens is unknown (i.e. no
	 * source file/line/column is available).
	 * 
	 * @param cfg        the cfg that this expression belongs to
	 * @param target     the CFG that is targeted by this CFG call.
	 * @param parameters the parameters of this call
	 */
	public CFGCall(CFG cfg, CFG target, Expression... parameters) {
		this(cfg, null, -1, -1, target, parameters);
	}

	/**
	 * Builds the CFG call, happening at the given location in the program. The
	 * static type of this CFGCall is the one return type of the descriptor of
	 * {@code target}.
	 * 
	 * @param cfg        the cfg that this expression belongs to
	 * @param sourceFile the source file where this expression happens. If unknown,
	 *                   use {@code null}
	 * @param line       the line number where this expression happens in the source
	 *                   file. If unknown, use {@code -1}
	 * @param col        the column where this expression happens in the source
	 *                   file. If unknown, use {@code -1}
	 * @param target     the CFG that is targeted by this CFG call
	 * @param parameters the parameters of this call
	 */
	public CFGCall(CFG cfg, String sourceFile, int line, int col, CFG target, Expression... parameters) {
		super(cfg, sourceFile, line, col, target.getDescriptor().getReturnType(), parameters);
		Objects.requireNonNull(target, "The target of a CFG call cannot be null");
		this.target = target;
	}

	/**
	 * Yields the CFG that is targeted by this CFG call.
	 * 
	 * @return the target CFG
	 */
	public CFG getTarget() {
		return target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean isEqualTo(Statement st) {
		if (this == st)
			return true;
		if (getClass() != st.getClass())
			return false;
		if (!super.isEqualTo(st))
			return false;
		CFGCall other = (CFGCall) st;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return super.isEqualTo(other);
	}

	@Override
	public String toString() {
		return target.getDescriptor().getFullName() + "(" + StringUtils.join(getParameters(), ", ") + ")";
	}

	@Override
	public final Identifier getMetaVariable() {
		return new ValueIdentifier("call_ret_value@" + offset);
	}

	@Override
	protected <H extends HeapDomain<H>, V extends ValueDomain<V>> AnalysisState<H, V> callSemantics(
			AnalysisState<H, V> computedState, CallGraph callGraph, SymbolicExpression[] params)
			throws SemanticException {
		// this will contain only the information about the returned metavariable
		AnalysisState<H, V> returned = callGraph.getAbstractResultOf(this, computedState, params);
		// the lub will include the metavariable inside the state
		AnalysisState<H, V> tmp = new AnalysisState<>(computedState.getState().lub(returned.getState()), new Skip());
		
		if (target.getDescriptor().getReturnType().isVoidType())
			// no need to add the meta variable since nothing has been pushed on the stack
			return tmp;
		
		Identifier meta = getMetaVariable();
		getMetaVariables().add((Identifier) returned.getLastComputedExpression());
		getMetaVariables().add(meta);
		return tmp.assign(meta, returned.getLastComputedExpression());
	}
}
