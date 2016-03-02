package janala.solvers.counters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.collect.Lists;

import janala.config.Config;
import janala.interpreters.Constraint;

import java.util.logging.Logger;

import janala.solvers.InputElement;
import janala.solvers.Solver;
import janala.solvers.counters.trees.ConcolicCountTree;
import janala.solvers.counters.trees.SymbolicTree;
import janala.solvers.counters.trees.TreePolicy;
import janala.solvers.counters.util.CountUtils;
import janala.utils.MyLogger;

public class IntermediaryInputGenerator {

	private static final Logger logger = MyLogger.getLogger(IntermediaryInputGenerator.class.getName());

	private SymbolicTree tree;
	private RandomGenerator rng;
	private final TreePolicy policy;
	private final Solver solver;
	private final LinkedList<InputElement> inputs;

	public IntermediaryInputGenerator() {
		this.policy = Config.instance.getPolicy();
		this.solver = Config.instance.getSolver();

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

		try {
			inputs = CountUtils.readInputElementsFromFile(Config.instance.inputElementsFile);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Input elements not found!");
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Problems when reading input elements from file!");
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Problems when reading input elements from file!");
			throw new RuntimeException(e);
		}
	}

	public void genNextInput() {
		ArrayList<Constraint> nextPath = Lists.newArrayList(policy.chooseNextPath(tree, rng));
		solver.setNegateLast(false);
		solver.setInputs(inputs);
		solver.setPathConstraint(nextPath);
		solver.setPathConstraintIndex(nextPath.size() - 1);
		if (solver.solve()) {
			logger.log(Level.INFO, "[IntermediaryInputGenerator] new input successfully generated!");
		} else {
			logger.log(Level.SEVERE, "[IntermediaryInputGenerator] couldn't solve the choosen path!");
		}
		
	}
	
	public static void main(String[] args) {
		IntermediaryInputGenerator iig = new IntermediaryInputGenerator();
		iig.genNextInput();
	}
}
