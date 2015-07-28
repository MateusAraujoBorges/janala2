package janala.solvers.counters;

import janala.interpreters.Constraint;
import janala.interpreters.SymbolicFalseConstraint;
import name.filieri.antonio.jpf.utils.BigRational;

public class PrunedNode implements SymbolicCountNode {

	public static final SymbolicCountNode INSTANCE = new PrunedNode();

	private static final long serialVersionUID = 9061980019581486709L;

	@Override
	public BigRational getNumberOfSolutions() {
		return BigRational.ZERO;
	}

	@Override
	public void setNumberOfSolutions(BigRational nsolutions) {
		throw new RuntimeException("Invalid operation!");
	}

	@Override
	public boolean isCounted() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Constraint getConstraint() {
		return SymbolicFalseConstraint.instance;
	}

	@Override
	public SymbolicCountNode getLeftChild() {
		throw new RuntimeException("Invalid Operation!");
	}

	@Override
	public SymbolicCountNode getRightChild() {
		throw new RuntimeException("Invalid Operation!");
	}

	@Override
	public void setLeftChild(SymbolicCountNode left) {
		throw new RuntimeException("Invalid Operation!");
	}

	@Override
	public void setRightChild(SymbolicCountNode right) {
		throw new RuntimeException("Invalid Operation!");
	}

}
