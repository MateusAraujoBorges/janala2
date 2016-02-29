package janala.solvers.counters.trees;

import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import janala.interpreters.Constraint;

/**
 * A TreePolicy dictates how tree nodes are chosen for further exploration.
 * 
 * @author mateus
 *
 */

public interface TreePolicy {

	/**
	 * @param tree
	 * 			Symbolic tree to be used
	 * @param rng
	 * 			random number generator
	 * @return
	 */

	public List<Constraint> chooseNextPath(SymbolicTree tree, RandomGenerator rng);

}
