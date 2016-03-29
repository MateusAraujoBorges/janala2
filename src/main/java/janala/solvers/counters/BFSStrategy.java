package janala.solvers.counters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import janala.config.Config;
import janala.interpreters.Constraint;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.InputElement;
import janala.solvers.Solver;
import janala.solvers.Strategy;
import janala.utils.MyLogger;

/**
 * Explore paths with smaller path constraints (by number of clauses) first.
 * 
 * @author mateus
 *
 */

public class BFSStrategy implements Strategy {

  public static class ExplorationData implements Serializable {

    private static final long serialVersionUID = -4131793826629580773L;

    public ExplorationData() {
      rng = new MersenneTwister(Config.instance.seed);
      toExplore = Maps.newHashMap();
      explored = Sets.newTreeSet(LexicograficConstraintComparator.INSTANCE);
      inputs = Sets.newLinkedHashSet(); // preserve iteration order between
                                        // executions
    }

    RandomGenerator rng;
    Map<Integer, Set<List<Constraint>>> toExplore;
    Set<List<Constraint>> explored;
    Set<InputElement> inputs;
  }

  private final static Logger logger = MyLogger.getLogger(BFSStrategy.class.getName());
  private ExplorationData edata;

  public BFSStrategy() {
    try {
      edata = readDataFromFile(Config.instance.bfsFile);
    } catch (FileNotFoundException e) {
      edata = new ExplorationData();
      logger.log(Level.INFO, "BFS data not found - starting exploration from scratch");
    } catch (ClassNotFoundException e) {
      logger.log(Level.SEVERE, "Serialized BFS map class not found ", e);
      throw new RuntimeException(e);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Problems while opening the bfs map file ", e);
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private ExplorationData readDataFromFile(String filename)
      throws FileNotFoundException, IOException, ClassNotFoundException {
    ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(filename)));
    Object tmp = is.readObject();
    is.close();
    if (tmp instanceof ExplorationData) {
      return (ExplorationData) tmp;
    } else {
      throw new RuntimeException("Unexpected class type! " + tmp.getClass().getName());
    }
  }

  private void writeDataToFile(String filename) throws FileNotFoundException, IOException {
    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(filename)));
    os.writeObject(edata);
    os.close();
  }

  @Override
  public int solve(List<Element> trace, int historySize, History history) {
    List<Constraint> pathTaken = history.getPathConstraint();
    if (pathTaken.size() == 0) { // no branches with symbolic variables :P
      logger.log(Level.INFO, "No branches with symbolic variables were found -  Nothing to do here :P");
      return -1;
    }
    edata.explored.add(pathTaken); // store the full path
    logger.log(Level.INFO, "EXPLORED: " + pathTaken);

    // TODO also store inputs in quantolic - probably it will be needed with
    // more complex subjects
    List<InputElement> inputs = history.getInputs();
    edata.inputs.addAll(inputs);
    inputs = Lists.newLinkedList(edata.inputs);
    logger.log(Level.INFO, "Inputs found so far: " + inputs.size());

    Solver solver = history.getSolver();
    solver.setNegateLast(false);

    // add all unexplored paths to the map. this whole mess is not exactly
    // efficient, but it should do the job for small experiments
    storePathsNotTaken(pathTaken);

    // select and solve
    ArrayList<Constraint> nextPath = Lists.newArrayList(selectNextPath());
    while (!nextPath.isEmpty()) {
      edata.explored.add(nextPath); // also store possible unsat paths
      logger.log(Level.INFO, "NEXT: " + nextPath);
      solver.setInputs(inputs);
      solver.setPathConstraint(nextPath);
      solver.setPathConstraintIndex(nextPath.size() - 1);
      boolean solved = solver.solve();

      if (solved) {
        // store state on the disk
        try {
          writeDataToFile(Config.instance.bfsFile);
        } catch (FileNotFoundException e) {
          logger.log(Level.SEVERE, "Problems while opening the file:", e);
          throw new RuntimeException(e);
        } catch (IOException e) {
          logger.log(Level.SEVERE, "Problems while opening the file:", e);
          throw new RuntimeException(e);
        }

        return 0;
      } else {
        nextPath = Lists.newArrayList(selectNextPath());
      }
    }

    logger.log(Level.INFO, "Next path not found. Exploration is done (unless something strange is going on)");
    return -1;
  }

  private List<Constraint> selectNextPath() {
    Map<Integer, Set<List<Constraint>>> pathMap = edata.toExplore;
    if (pathMap.size() > 0) {
      SortedSet<Integer> sortedSet = Sets.newTreeSet(pathMap.keySet());
      Integer lowestLength = sortedSet.first();
      Set<List<Constraint>> constraintSet = pathMap.get(lowestLength);

      if (constraintSet.size() > 0) {
        int choice = edata.rng.nextInt(constraintSet.size());
        Iterator<List<Constraint>> it = constraintSet.iterator();
        for (int i = 0; i < choice; i++) {
          it.next();
        }
        List<Constraint> nextPath = it.next();
        it.remove();
        return nextPath;
      } else { // remove empty set, explore paths with length=n+1
        pathMap.remove(lowestLength);
        return selectNextPath();
      }

    } else {
      return Lists.newArrayList();
    }
  }

  private void storePathsNotTaken(List<Constraint> pathTaken) {
    for (int i = 0; i < pathTaken.size(); i++) {
      Constraint clause = pathTaken.get(i);
      List<Constraint> pathNotTaken = new ArrayList<Constraint>(pathTaken.subList(0, i));
      if (i != 0) {
        edata.explored.add(Lists.newArrayList(pathNotTaken)); // store prefixes
                                                              // of the full
                                                              // path
        logger.log(Level.INFO, "PREFIX:" + pathNotTaken);
      }
      pathNotTaken.add(clause.not());

      if (!edata.explored.contains(pathNotTaken)) { // ignore explored paths
        Integer length = i + 1;
        Set<List<Constraint>> unexploredPath = edata.toExplore.get(length);
        if (unexploredPath == null) {
          // use lexicographic ordering as sorting criteria
          unexploredPath = new TreeSet<List<Constraint>>(LexicograficConstraintComparator.INSTANCE);
          edata.toExplore.put(length, unexploredPath);
        }

        unexploredPath.add(pathNotTaken);
      }
    }
  }

  // we want to compare constraints without worrying about iid and index

  private static final class LexicograficConstraintComparator implements Comparator<List<Constraint>>, Serializable {

    private static final long serialVersionUID = 257347358742439870L;
    static final LexicograficConstraintComparator INSTANCE = new LexicograficConstraintComparator();

    LexicograficConstraintComparator() {
    }

    @Override
    public int compare(List<Constraint> o1, List<Constraint> o2) {
      int sizeComparator = o1.size() - o2.size();
      int smallerSize = sizeComparator <= 0 ? o1.size() : o2.size();

      for (int i = 0; i < smallerSize; i++) {
        Constraint c1 = o1.get(i);
        Constraint c2 = o2.get(i);

        if (c1 == null && c2 == null) {
          return 0;
        } else if (c1 == null) {
          return 1;
        } else if (c2 == null) {
          return -1;
        }

        int ordering = c1.toMathString().compareTo(c2.toMathString());
        if (ordering != 0) {
          return ordering;
        } else {
          continue;
        }
      }
      // lists are equal, or one is a prefix of the other
      return sizeComparator;
    }
  }

}
