package it.unive.lisa.program.cfg.statement.evaluation;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;

/**
 * A right-to-left {@link EvaluationOrder}, evaluating expressions in reversed
 * order.
 * 
 * @author <a href="mailto:luca.negrini@unive.it">Luca Negrini</a>
 */
public class RightToLeftEvaluation implements EvaluationOrder {

	/**
	 * The singleton instance of this class.
	 */
	public static final RightToLeftEvaluation INSTANCE = new RightToLeftEvaluation();

	private RightToLeftEvaluation() {
	}

	@Override
	public <A extends AbstractState<A, H, V>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>> AnalysisState<A, H, V> evaluate(
					Expression[] subExpressions,
					AnalysisState<A, H, V> entryState,
					InterproceduralAnalysis<A, H, V> interprocedural,
					StatementStore<A, H, V> expressions,
					ExpressionSet<SymbolicExpression>[] computed)
					throws SemanticException {
		if (subExpressions.length == 0)
			return entryState;

		AnalysisState<A, H, V> preState = entryState;
		for (int i = computed.length - 1; i >= 0; i--) {
			preState = subExpressions[i].semantics(preState, interprocedural, expressions);
			expressions.put(subExpressions[i], preState);
			computed[i] = preState.getComputedExpressions();
		}

		return preState;
	}
}