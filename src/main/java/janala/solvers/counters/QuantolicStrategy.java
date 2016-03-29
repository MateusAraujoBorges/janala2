package janala.solvers.counters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.collect.Lists;

import janala.config.Config;
import janala.interpreters.Constraint;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.InputElement;
import janala.solvers.Solver;
import janala.solvers.Strategy;
import janala.solvers.counters.trees.ConcolicCountTree;
import janala.solvers.counters.trees.SymbolicCountNode;
import janala.solvers.counters.trees.SymbolicTree;
import janala.solvers.counters.trees.TreePolicy;
import janala.solvers.counters.util.CountUtils;
import janala.utils.MyLogger;
import janala.solvers.counters.util.BigRational;

public class QuantolicStrategy implements Strategy {

  private final Counter counter;
  private SymbolicTree tree;
  private RandomGenerator rng;
  private final TreePolicy policy;

  public QuantolicStrategy() {
    this.counter = Config.instance.getCounter();
    this.policy = Config.instance.getPolicy();

    try {
      this.rng = CountUtils.readRNGFromFile(Config.instance.rngFile);
    } catch (FileNotFoundException e) {
      rng = new MersenneTwister(Config.instance.seed);
      logger.log(Level.INFO, "RNG file not found. Creating new RNG from scratch");
    } catch (ClassNotFoundException e) {
      logger.log(Level.SEVERE, "Serialized RNG Class not found", e);
      throw new RuntimeException(e);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Problems while opening the file:", e);
      throw new RuntimeException(e);
    }

    try {
      tree = CountUtils.readTreeFromFile(Config.instance.symtreeFile);
    } catch (FileNotFoundException e) {
      tree = new ConcolicCountTree();
      logger.log(Level.INFO, "Tree file not found. Creating new symbolic tree from scratch");
    } catch (ClassNotFoundException e) {
      logger.log(Level.SEVERE, "Serialized Tree Class not found", e);
      throw new RuntimeException(e);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Problems while opening the file:", e);
      throw new RuntimeException(e);
    }
  }

  public QuantolicStrategy(Counter counter, SymbolicTree tree, TreePolicy policy, RandomGenerator rng) {
    this.counter = counter;
    this.rng = rng;
    this.tree = tree;
    this.policy = policy;
  }

  private final static Logger logger = MyLogger.getLogger(QuantolicStrategy.class.getName());

  /**
   * return 0 if a path was successfully found and solved, -1 otherwise. The
   * return value was supposed to be the index of the history where we are
   * branching, but in "quantolic" mode we aren't limited to choose branches
   * which we traversed in the current execution.
   */

  @Override
  public int solve(List<Element> trace, int historySize, History history) {

    List<Constraint> pathTaken = getExecutionPathConstraint(history);
    if (pathTaken.size() == 0) { // no branches with symbolic variables :P
      logger.log(Level.INFO, "No branches with symbolic variables were found -  Nothing to do here :P");
      return -1;
    }

    List<InputElement> inputs = history.getInputs();
    Solver solver = history.getSolver();
    solver.setNegateLast(false);
    // logger.log(Level.INFO, tree.toString());

    // System.out.println(pathTaken);
    // insert,count,prune
    List<SymbolicCountNode> treePath = tree.insertPathIntoTree(pathTaken);
    // System.out.println(tree.toString());
    BigRational probability = tree.count(treePath, inputs, history.getSyntheticVars(), counter);
    // System.out.println(tree.toString());
    tree.updateAndPrune(treePath);
    // System.out.println(tree.toString());

    BigRational coverage = BigRational.ONE.minus(tree.getRoot().getProbabilityOfSolution());
    System.out.println("[quantolic] domain coverage for this path: " + probability.doubleValue() + " at "
        + System.currentTimeMillis());
    logger.log(Level.SEVERE,
        "[quantolic] current cumulative domain coverage (including this path): " + coverage.doubleValue());

    // select and solve
    ArrayList<Constraint> nextPath = Lists.newArrayList(policy.chooseNextPath(tree, rng));
    logger.log(Level.INFO, "[quantolic] next path: {0}", nextPath);

    if (!nextPath.isEmpty() && !tree.isDone()) {
      solver.setInputs(inputs);
      solver.setPathConstraint(nextPath);
      solver.setPathConstraintIndex(nextPath.size() - 1);
      boolean solved = solver.solve();

      if (solved) {
        // store tree and RNG state on the disk
        try {
          CountUtils.writeTreeToFile(tree, Config.instance.symtreeFile);
          CountUtils.writeRNGToFile(rng, Config.instance.rngFile);
          counter.shutdown();
        } catch (FileNotFoundException e) {
          logger.log(Level.SEVERE, "Problems while opening the file:", e);
          throw new RuntimeException(e);
        } catch (IOException e) {
          logger.log(Level.SEVERE, "Problems while opening the file:", e);
          throw new RuntimeException(e);
        }

        logger.log(Level.FINEST, tree.toString());

        return 0;
      } else {
        logger.severe("[quantolicStrategy] Infeasible path detected with solutions. Something strange is going on");
        logger.severe("[quantolicStrategy] Path: " + nextPath);
        logger.severe("[quantolicStrategy] inputs: " + inputs);
        throw new RuntimeException("Infeasible path detected with solutions.");
      }
    }

    logger.info("Next path not found. Tree exploration status: " + (tree.isDone() ? " done " : " not done "));
    return -1;
  }

  private List<Constraint> getExecutionPathConstraint(History solver) {
    return solver.getPathConstraint();
  }
}