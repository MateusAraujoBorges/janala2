package janala.solvers.counters;

import java.io.Serializable;

import janala.interpreters.Constraint;
import name.filieri.antonio.jpf.utils.BigRational;

public interface SymbolicCountNode extends Serializable {

	public BigRational getNumberOfSolutions();

	public void setNumberOfSolutions(BigRational nsolutions);

	public boolean isCounted();

	public boolean isEmpty();

	/**
	 * @return The raw constraint.
	 */

	public Constraint getConstraint();
	
	/**
	 * @return The inputs from the constraint
	 */
	
//	public List<InputElement> getInputs();

	/**
	 * @return The parsed constraint.
	 */
//	public Problem getProblem();

	/*
	 * The left node must always be the negation of the right node.
	 */
	public SymbolicCountNode getLeftChild();
	public SymbolicCountNode getRightChild();
	
	public void setLeftChild(SymbolicCountNode left);
	public void setRightChild(SymbolicCountNode right);
}
