package janala.solvers.counters.trees;

import janala.interpreters.Constraint;
import janala.interpreters.SymbolicFalseConstraint;
import janala.solvers.counters.util.BigRational;

public class PrunedNode implements SymbolicCountNode {

  public static final SymbolicCountNode INSTANCE = new PrunedNode();

  private static final long serialVersionUID = 9061980019581486709L;

  @Override
  public BigRational getProbabilityOfSolution() {
    return BigRational.ZERO;
  }

  @Override
  public void setProbabilityOfSolution(BigRational nsolutions) {
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

  @Override
  public String toString() {
    return " -- PRUNED -- ";
  }

  @Override
  public long getNumberChildren() {
    return 0;
  }

  @Override
  public int getNumberVisits() {
    return 0;
  }

  @Override
  public void setNumberChildren(long children) {
    throw new RuntimeException("Invalid Operation!");
  }

  @Override
  public void setNumberVisits(int visits) {
    throw new RuntimeException("Invalid Operation!");
  }
}
