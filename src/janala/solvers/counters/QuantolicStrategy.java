package janala.solvers.counters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import janala.config.Config;
import janala.interpreters.Constraint;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.InputElement;
import janala.solvers.Solver;
import janala.solvers.Strategy;
import janala.utils.MyLogger;
import name.filieri.antonio.jpf.utils.BigRational;

public class QuantolicStrategy extends Strategy {

	private final Counter counter;
	private SymbolicTree tree;
	private RandomGenerator rng;

	public QuantolicStrategy() {
		this.counter = Config.instance.getCounter();
		
		try {
			this.rng = readRNGFromFile(Config.instance.rngFile);
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
			tree = readTreeFromFile(Config.instance.symtreeFile);
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
	
	public QuantolicStrategy(Counter counter, SymbolicTree tree, RandomGenerator rng) {
		this.counter = counter;
		this.rng = rng;
		this.tree = tree;
	}

	private final static Logger logger = MyLogger.getLogger(QuantolicStrategy.class.getName());

	/**
	 * return 0 if a path was successfully found and solved, -1 otherwise. The
	 * return value was supposed to be the index of the history where we are
	 * branching, but in "quantolic" mode we aren't limited to choose branches
	 * which we traversed in the current execution.
	 */

	@Override
	public int solve(ArrayList<Element> trace, int historySize, History history) {

		List<Constraint> pathTaken = getExecutionPathConstraint(history);
		if (pathTaken.size() == 0) { //no branches with symbolic variables :P
			logger.log(Level.INFO,"No branches with symbolic variables were found -  Nothing to do here :P");
			return -1; 
		}
		
		LinkedList<InputElement> inputs = history.getInputs();
		Solver solver = history.getSolver();
		solver.setNegateLast(false);
//		logger.log(Level.INFO, tree.toString());

//		System.out.println(pathTaken);
		// insert,count,prune
		List<SymbolicCountNode> treePath = tree.insertPathIntoTree(pathTaken);
//		System.out.println(tree.toString());
		tree.count(treePath, inputs, history.getSyntheticVars(), counter);
//		System.out.println(tree.toString());
		tree.updateAndPrune(treePath);
//		System.out.println(tree.toString());

		BigRational coverage = BigRational.ONE.minus(tree.getRoot().getProbabilityOfSolution());
		System.out.println("[quantolic] current cumulative domain coverage (including this path): " + coverage.doubleValue());
		
		// select and solve
		ArrayList<Constraint> nextPath = chooseNextPath();
		logger.log(Level.INFO,"[quantolic] next path: {0}", nextPath);
		while (!nextPath.isEmpty() && !tree.isDone()) {
			solver.setInputs(inputs);
			solver.setPathConstraint(nextPath);
			solver.setPathConstraintIndex(nextPath.size() - 1);
			boolean solved = solver.solve();

			if (solved) {
				// store tree and RNG state on the disk
				try {
					writeTreeToFile(Config.instance.symtreeFile);
					writeRNGToFile(Config.instance.rngFile);
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
				logger.warning("Infeasible path detected with solutions. Something strange is going on, maybe?");
				nextPath = chooseNextPath();
			}
		} 

		logger.info("Next path not found. Tree exploration status: " + (tree.isDone() ? " done " : " not done "));
		return -1;
	}

	/**
	 * Walk through the symbolic tree and select nodes based on their
	 * probabilities/number of solutions.
	 * 
	 * @return ArrayList with the selected path condition. The ArrayList return
	 *         type is due to the signature of {@code Solver.setPathConstraint}.
	 */

	public ArrayList<Constraint> chooseNextPath() {
		SymbolicCountNode current = tree.getRoot();
		ArrayList<Constraint> nextPath = Lists.newArrayList();

		while (!current.isEmpty() && !(current instanceof PrunedNode || current instanceof UnexploredNode)) {
			Preconditions.checkState(current.isCounted(), "Current node wasn't counted!");

			SymbolicCountNode left = current.getLeftChild();
			SymbolicCountNode right = current.getRightChild();

			Preconditions.checkState(!(left instanceof PrunedNode && right instanceof PrunedNode),
			        "We reached a path already taken previously. This shouldn't happen");
			Preconditions.checkState(left.isCounted() || right.isCounted(), "Both child nodes are uncounted!");
			
			BigRational nSolCurr = current.getProbabilityOfSolution();
			BigRational nSolLeft = left.getProbabilityOfSolution();
			BigRational nSolRight = right.getProbabilityOfSolution();

			// left.nsolutions = (current.nsolutions - right.nsolutions)
			if (!left.isCounted()) {
				left.setProbabilityOfSolution(nSolCurr.minus(nSolRight));
				nSolLeft = left.getProbabilityOfSolution();
			} else if (!right.isCounted()) {
				right.setProbabilityOfSolution(nSolCurr.minus(nSolLeft));
				nSolRight = right.getProbabilityOfSolution();
			}
			
			Preconditions.checkState(nSolLeft.isPositive() || nSolRight.isPositive(), "Both child nodes have zero probability!");
			
			if (nSolLeft.isZero()) { //prune
				current.setLeftChild(PrunedNode.INSTANCE);
			} else if (nSolRight.isZero()) {
				current.setRightChild(PrunedNode.INSTANCE);
			}
			
			BigRational leftProb = nSolLeft.div(nSolCurr);
			BigRational randomRoll = BigRational.valueOf(rng.nextDouble());

			if (randomRoll.compareTo(leftProb) < 0) { // leftProb < randomRoll (we use '<' in case leftProb == 0)
				nextPath.add(left.getConstraint());
				current = left;
			} else {
				nextPath.add(right.getConstraint());
				current = right;
			}
		}

		return nextPath;
	}

	private List<Constraint> getExecutionPathConstraint(History solver) {
		return solver.getPathConstraint();
	}

	private void writeTreeToFile(String symtreeFile) throws FileNotFoundException, IOException {
		File f = new File(symtreeFile);
		f.createNewFile(); // create new file if it not exists already
		tree.writeToDisk(f);
	}

	private SymbolicTree readTreeFromFile(String symtreeFile)
	        throws FileNotFoundException, ClassNotFoundException, IOException {
		return ConcolicCountTree.readFromDisk(new File(symtreeFile));
	}

	private RandomGenerator readRNGFromFile(String rngFile)
	        throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(rngFile)));
		Object tmp = is.readObject();
		is.close();
		if (tmp instanceof MersenneTwister) {
			return (MersenneTwister) tmp;
		} else {
			throw new RuntimeException("Unexpected rng type! " + tmp.getClass().getName());
		}
	}

	private void writeRNGToFile(String rngFile) throws FileNotFoundException, IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(rngFile)));
		os.writeObject(rng);
		os.close();
	}

}