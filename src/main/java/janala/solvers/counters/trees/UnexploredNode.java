package janala.solvers.counters.trees;

import janala.interpreters.Constraint;
import janala.solvers.counters.util.BigRational;

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
    return "[unexplored] count:" + nsolutions.doubleValue() + " cons:" + constraint;
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