package janala.solvers.counters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import janala.interpreters.Constraint;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.InputElement;
import janala.solvers.Strategy;
import janala.solvers.counters.util.BigRational;

/**
 * Logs the coverage of the domain for the current path
 * 
 * @author mateus
 *
 */

public class DomainCoverageStrategyWrapper implements Strategy {

  Strategy strategy;
  Counter counter;

  public DomainCoverageStrategyWrapper(Strategy strategy) throws IOException {
    this.strategy = strategy;
    counter = new PCPVersionTwoCounter();
  }

  @Override
  public int solve(List<Element> history, int historySize, History solver) {
    long start = System.nanoTime();
    List<Constraint> path = solver.getPathConstraint();
    List<InputElement> inputs = solver.getInputs();

    BigRational pathProbability = counter.probabilityOf(path, inputs, solver.getSyntheticVars());
    long end = System.nanoTime();
    System.out.println("Path: " + path);
    System.out.println("[quantolic] domain coverage for this path: " + pathProbability.doubleValue() + " at "
        + System.currentTimeMillis());
    System.out.println("[quantolic] time spent calling the counter: " + (end - start) / Math.pow(10, 9));
    return strategy.solve(history, historySize, solver);
  }

}
