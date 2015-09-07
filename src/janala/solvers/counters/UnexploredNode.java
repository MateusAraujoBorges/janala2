package janala.solvers.counters;

import janala.interpreters.Constraint;
import name.filieri.antonio.jpf.utils.BigRational;

public class UnexploredNode implements SymbolicCountNode {

	private final Constraint constraint;
	private BigRational nsolutions;
	private static final long serialVersionUID = 6686134494504599192L;
 
	public UnexploredNode(Constraint constraint) {
		this.constraint = constraint;
		this.nsolutions = BigRational.MINUS_ONE;
	}
	
	@Override
	public BigRational getProbabilityOfSolution() {
		return nsolutions;
	}

	@Override
	public void setProbabilityOfSolution(BigRational nsolutions) {
		this.nsolutions = nsolutions;
	}

	@Override
	public boolean isCounted() {
		return !nsolutions.equals(BigRational.MINUS_ONE);
	}

	@Override
	public boolean isEmpty() {
		return isCounted() && nsolutions.equals(BigRational.ZERO);
	}

	@Override
	public Constraint getConstraint() {
		return constraint;
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
	
	@Override
	public String toString() {
		return "[unexplored] count:" + nsolutions + " cons:" + constraint;
	}
}