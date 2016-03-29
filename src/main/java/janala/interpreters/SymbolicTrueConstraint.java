package janala.interpreters;

import java.util.Map;

/**
 * Author: Koushik Sen (ksen@cs.berkeley.edu)
 */
public class SymbolicTrueConstraint extends Constraint {
  private static final long serialVersionUID = 7068252973680685143L;
  public static final SymbolicTrueConstraint instance = new SymbolicTrueConstraint();

  public SymbolicTrueConstraint() {
  }

  @Override
  public void accept(ConstraintVisitor v) {
    v.visitSymbolicTrue(this);
  }

  @Override
  public Constraint not() {
    return SymbolicFalseConstraint.instance;
  }

  @Override
  public Constraint substitute(Map<String, Long> assignments) {
    return this;
  }

  @Override
  public String toString() {
    return " TRUE ";
  }

  @Override
  public String toMathString() {
    return "true";
  }
}
