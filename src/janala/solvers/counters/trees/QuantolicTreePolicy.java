package janala.solvers.counters.trees;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import janala.interpreters.Constraint;
import janala.solvers.counters.util.BigRational;

/**
 * This class chooses the next path by performing a random walk in the symbolic
 * tree.
 * 
 * @author mateus
 */

public class QuantolicTreePolicy implements TreePolicy {

	/**
	 * Walk through the symbolic tree and select nodes based on their
	 * probabilities/number of solutions.
	 * 
	 * @return List with the selected path condition. 
	 */

	@Override
	public List<Constraint> chooseNextPath(SymbolicTree tree, RandomGenerator rng) {
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

			Preconditions.checkState(nSolLeft.isPositive() || nSolRight.isPositive(),
			        "Both child nodes have zero probability!");

			if (nSolLeft.isZero()) { // prune
				current.setLeftChild(PrunedNode.INSTANCE);
			} else if (nSolRight.isZero()) {
				current.setRightChild(PrunedNode.INSTANCE);
			}

			BigRational leftProb = nSolLeft.div(nSolCurr);
			BigRational randomRoll = BigRational.valueOf(rng.nextDouble());

			if (randomRoll.compareTo(leftProb) < 0) { // leftProb < randomRoll
			                                          // (we use '<' in case
			                                          // leftProb == 0)
				nextPath.add(left.getConstraint());
				current = left;
			} else {
				nextPath.add(right.getConstraint());
				current = right;
			}
		}

		return nextPath;
	}

}
