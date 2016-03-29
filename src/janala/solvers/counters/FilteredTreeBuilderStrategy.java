package janala.solvers.counters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import janala.config.Config;
import janala.interpreters.Constraint;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.Strategy;
import janala.solvers.counters.trees.ConcolicCountTree;
import janala.solvers.counters.trees.SymbolicCountNode;
import janala.solvers.counters.trees.SymbolicTree;
import janala.solvers.counters.util.CountUtils;
import janala.utils.MyLogger;
import janala.solvers.counters.util.BigRational;

/**
 * Do not solve anything; just add new paths to the tree
 * 
 * @author mateus
 */

public class FilteredTreeBuilderStrategy extends Strategy {

	private static final Logger logger = MyLogger.getLogger(FilteredTreeBuilderStrategy.class.getName());

	private final Counter counter;
	private SymbolicTree tree;

	private final String setsFilename;
	private final String inputElementsFilename;
	private Set<String> pathsCovered;

	@SuppressWarnings("unchecked")
	public FilteredTreeBuilderStrategy() {
		this.counter = Config.instance.getCounter();
		this.setsFilename = Config.instance.setFile;
		this.inputElementsFilename = Config.instance.inputElementsFile;

		try {
			pathsCovered = (Set<String>) CountUtils.readSetFromFile(setsFilename);
		} catch (FileNotFoundException e) {
			pathsCovered = new HashSet<String>();
			logger.log(Level.WARNING, "[pathfilter] File with covered paths wasn't found - creating one from scratch");
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "[pathfilter] Serialized Set class not found", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "[pathfilter] Problems while opening the file", e);
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

	@Override
	public int solve(ArrayList<Element> trace, int historySize, History history) {
		List<Constraint> path = history.getPathConstraint();

		StringBuilder sb = new StringBuilder();
		for (Constraint cons : path) {
			sb.append(cons.toMathString());
		}

		boolean isNewPath = pathsCovered.add(sb.toString());
		if (isNewPath) {
			List<SymbolicCountNode> treePath = tree.insertPathIntoTree(path);
			BigRational probability = tree.count(treePath, history.getInputs(), history.getSyntheticVars(), counter);
			tree.updateAndPrune(treePath);

			System.out.println("[filteredTree] domain coverage for this path: " + probability.doubleValue() + " at "
			        + System.currentTimeMillis());
			logger.log(Level.SEVERE, "[filteredTree] current cumulative domain coverage (including this path): "
			        + BigRational.ONE.minus(tree.getRoot().getProbabilityOfSolution()).doubleValue());

			try {
				CountUtils.writeTreeToFile(tree, Config.instance.symtreeFile);
				CountUtils.writeSetToFile(pathsCovered, setsFilename);
				CountUtils.writeInputElementsToFile(history.getInputs(), inputElementsFilename);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "[pathfilter] error while writing set to file", e);
				throw new RuntimeException(e);
			}

			return 0;
		} else {
			System.out.println("[pathfilter] Path filtered!");
			logger.log(Level.WARNING, "[pathfilter] Path filtered! ");
			return -1;
		}
	}
}