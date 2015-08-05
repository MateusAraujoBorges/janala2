package janala.solvers.counters;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import gnu.trove.iterator.TIntIterator;
import janala.interpreters.Constraint;
import janala.interpreters.ConstraintVisitor;
import janala.interpreters.SymbolicAndConstraint;
import janala.interpreters.SymbolicFalseConstraint;
import janala.interpreters.SymbolicInt;
import janala.interpreters.SymbolicIntCompareConstraint;
import janala.interpreters.SymbolicNotConstraint;
import janala.interpreters.SymbolicOrConstraint;
import janala.interpreters.SymbolicStringPredicate;
import janala.interpreters.SymbolicTrueConstraint;

public class VarCollectorVisitor implements ConstraintVisitor {

	Set<Integer> varIdsFound = Sets.newHashSet();
	
	public static Set<Integer> collectVariableIDs(List<Constraint> constraints) {
		VarCollectorVisitor visitor = new VarCollectorVisitor();
		Set<Integer> varIDs = Sets.newHashSet();
		for (Constraint cons : constraints) {
			visitor.varIdsFound.clear();
			cons.accept(visitor);
			varIDs.addAll(visitor.varIdsFound);
		}
		return varIDs;
	}
	
	@Override
	public void visitSymbolicInt(SymbolicInt c) {
		TIntIterator it = c.linear.keySet().iterator();
		while (it.hasNext()) {
			int var = it.next();
			this.varIdsFound.add(var);
		}
	}

	@Override
	public void visitSymbolicOr(SymbolicOrConstraint c) {
		for (Constraint cons : c.constraints) {
			cons.accept(this);
		}
	}

	@Override
	public void visitSymbolicStringPredicate(SymbolicStringPredicate c) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void visitSymbolicAnd(SymbolicAndConstraint c) {
		for (Constraint cons : c.constraints) {
			cons.accept(this);
		}
	}

	@Override
	public void visitSymbolicNot(SymbolicNotConstraint c) {
		c.constraint.accept(this);
	}

	@Override
	public void visitSymbolicTrue(SymbolicTrueConstraint c) {}

	@Override
	public void visitSymbolicFalse(SymbolicFalseConstraint c) {}

	@Override
	public void visitSymbolicIntCompare(SymbolicIntCompareConstraint c) {
		throw new RuntimeException("Not implemented yet");
	}
}