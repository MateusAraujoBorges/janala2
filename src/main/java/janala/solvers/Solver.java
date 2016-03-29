package janala.solvers;

import janala.interpreters.Constraint;
import janala.interpreters.ConstraintVisitor;

import java.util.List;

public interface Solver extends ConstraintVisitor {
  public boolean solve();

  public List<String> getSolution();

  /**
   * If the solver should negate the last constraint before solving it. Default
   * should be true, to avoid compatibility problems with CATG.
   * 
   * @param option
   */
  public void setNegateLast(boolean option);

  public void setInputs(List<InputElement> inputs);

  public void setPathConstraint(List<Constraint> pathConstraint);

  public void setPathConstraintIndex(int pathConstraintIndex);
}
