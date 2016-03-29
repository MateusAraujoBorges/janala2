package janala.solvers.counters.trees;

import java.io.Serializable;

import janala.interpreters.Constraint;
import janala.solvers.counters.util.BigRational;

public interface SymbolicCountNode extends Serializable {

	public BigRational getProbabilityOfSolution();

	public void setProbabilityOfSolution(BigRational nsolutions);

	public boolean isCounted();

	public boolean isEmpty();

	/**
	 * @return The raw constraint.
	 */

	public Constraint getConstraint();

	/**
	 * @return The inputs from the constraint
	 */

	// public List<InputElement> getInputs();

	/**
	 * @return The parsed constraint.
	 */
	// public Problem getProblem();

	/*
	 * The left node must always be the negation of the right node.
	 */
	public SymbolicCountNode getLeftChild();

	public SymbolicCountNode getRightChild();

	public void setLeftChild(SymbolicCountNode left);

	public void setRightChild(SymbolicCountNode right);

	// MCTS stuff

	//TODO maybe getNumberOfLeaves() is a better name
	public long getNumberChildren();

	public int getNumberVisits();

	public void setNumberChildren(long children);

	public void setNumberVisits(int visits);

}
