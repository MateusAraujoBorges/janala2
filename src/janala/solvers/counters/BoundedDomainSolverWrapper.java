package janala.solvers.counters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import janala.interpreters.Constraint;
import janala.interpreters.SymbolicAndConstraint;
import janala.interpreters.SymbolicFalseConstraint;
import janala.interpreters.SymbolicInt;
import janala.interpreters.SymbolicIntCompareConstraint;
import janala.interpreters.SymbolicNotConstraint;
import janala.interpreters.SymbolicOrConstraint;
import janala.interpreters.SymbolicStringPredicate;
import janala.interpreters.SymbolicTrueConstraint;
import janala.solvers.InputElement;
import janala.solvers.Solver;

/**
 * Add constraints representing the bounds of the domain of the input variables
 * when solve() is called.
 * 
 * @author mateus
 *
 */

public class BoundedDomainSolverWrapper implements Solver {

	private Solver solver;
	private List<Constraint> domainConstraint;
	private ArrayList<Constraint> pathConstraint;
	private int pathConstraintIndex;

	public BoundedDomainSolverWrapper(Solver solver) {
		this.solver = solver;
		this.domainConstraint = Lists.newArrayList();
		this.pathConstraint = Lists.newArrayList();
		this.pathConstraintIndex = -1;
	}

	@Override
	public void visitSymbolicInt(SymbolicInt c) {
		solver.visitSymbolicInt(c);
	}

	@Override
	public void visitSymbolicOr(SymbolicOrConstraint c) {
		solver.visitSymbolicOr(c);
	}

	@Override
	public void visitSymbolicStringPredicate(SymbolicStringPredicate c) {
		solver.visitSymbolicStringPredicate(c);
	}

	@Override
	public void visitSymbolicAnd(SymbolicAndConstraint c) {
		solver.visitSymbolicAnd(c);
	}

	@Override
	public void visitSymbolicNot(SymbolicNotConstraint c) {
		solver.visitSymbolicNot(c);
	}

	@Override
	public void visitSymbolicTrue(SymbolicTrueConstraint c) {
		solver.visitSymbolicTrue(c);
	}

	@Override
	public void visitSymbolicFalse(SymbolicFalseConstraint c) {
		solver.visitSymbolicFalse(c);
	}

	@Override
	public void visitSymbolicIntCompare(SymbolicIntCompareConstraint c) {
		solver.visitSymbolicIntCompare(c);
	}

	@Override
	public boolean solve() {
		Preconditions.checkState(this.pathConstraintIndex >= 0, "setPathConstraintIndex wasn't called!");

		ArrayList<Constraint> combinedConstraints = Lists.newArrayList();
		combinedConstraints.addAll(domainConstraint);
		combinedConstraints.addAll(pathConstraint);
		solver.setPathConstraint(combinedConstraints);
		solver.setPathConstraintIndex(this.pathConstraintIndex + domainConstraint.size());
		return solver.solve();
	}

	@Override
	public void setNegateLast(boolean option) {
		solver.setNegateLast(option);
	}

	@Override
	public void setInputs(LinkedList<InputElement> inputs) {
		solver.setInputs(inputs);
		this.domainConstraint = generateDomainConstraints(inputs);
	}

	private List<Constraint> generateDomainConstraints(LinkedList<InputElement> inputs) {
		List<Constraint> domainConstraints = Lists.newArrayList();

		for (InputElement input : inputs) {
			Range<Long> bound = input.range;
			
			SymbolicInt lowerBound = new SymbolicInt(input.symbol);
			// TODO change the name of this method (confusing)
			lowerBound = lowerBound.setop(SymbolicInt.COMPARISON_OPS.GE);
			long lo = bound.lowerEndpoint().equals(Integer.MIN_VALUE) ? Integer.MAX_VALUE : bound.lowerEndpoint() * -1; 
			lowerBound.constant = lo;

			SymbolicInt upperBound = new SymbolicInt(input.symbol);
			upperBound = upperBound.setop(SymbolicInt.COMPARISON_OPS.LE);
			upperBound.constant = bound.upperEndpoint() * -1;

			domainConstraints.add(lowerBound);
			domainConstraints.add(upperBound);
		}

		return domainConstraints;
	}

	@Override
	public void setPathConstraint(ArrayList<Constraint> pathConstraint) {
		solver.setPathConstraint(pathConstraint);
		this.pathConstraint = pathConstraint;
	}

	@Override
	public void setPathConstraintIndex(int pathConstraintIndex) {
		Preconditions.checkArgument(pathConstraintIndex >= 0);
		this.pathConstraintIndex = pathConstraintIndex;
		solver.setPathConstraintIndex(pathConstraintIndex);
	}
}
