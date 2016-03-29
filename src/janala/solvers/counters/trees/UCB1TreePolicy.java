package janala.solvers.counters.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import janala.interpreters.Constraint;
import janala.utils.MyLogger;
import janala.solvers.counters.util.BigRational;

public class UCB1TreePolicy implements TreePolicy {

	public enum FPU_TYPE {NONE, ONE, PROBABILITY, ONE_VISIT};
	
	// See 3.3 of the MCTS survey for more details
	private static final double KOCSIS_CONSTANT = 1 / Math.sqrt(2);
	private final static Logger logger = MyLogger.getLogger(UCB1TreePolicy.class.getName());

	private final boolean useConstant;
	private final FPU_TYPE fpu; 
	
	public UCB1TreePolicy(boolean useConstant, FPU_TYPE fpu) {
		this.useConstant = useConstant;
		this.fpu = fpu;
	}
	
	@Override
	public List<Constraint> chooseNextPath(SymbolicTree tree, RandomGenerator rng) {
		SymbolicCountNode current = tree.getRoot();
		ArrayList<Constraint> nextPath = Lists.newArrayList();

		while (!current.isEmpty() && !(current instanceof UnexploredNode)) {
			// some assertions
			Preconditions.checkState(current.isCounted(), "Current node wasn't counted!");

			SymbolicCountNode left = current.getLeftChild();
			SymbolicCountNode right = current.getRightChild();

			Preconditions.checkState(!(left instanceof PrunedNode && right instanceof PrunedNode),
			        "We reached a path already taken previously. This shouldn't happen");
			Preconditions.checkState(left.isCounted() || right.isCounted(), "Both child nodes are uncounted!");

			BigRational rewardCurr = current.getProbabilityOfSolution();
			BigRational rewardLeft = left.getProbabilityOfSolution();
			BigRational rewardRight = right.getProbabilityOfSolution();

			// TODO maybe fold this into the counting phase - the same code is
			// repeated at QuantolicPolicy
			// left.nsolutions = (current.nsolutions - right.nsolutions)
			if (!left.isCounted()) {
				left.setProbabilityOfSolution(rewardCurr.minus(rewardRight));
				rewardLeft = left.getProbabilityOfSolution();
			} else if (!right.isCounted()) {
				right.setProbabilityOfSolution(rewardCurr.minus(rewardLeft));
				rewardRight = right.getProbabilityOfSolution();
			}
			Preconditions.checkState(rewardLeft.isPositive() || rewardRight.isPositive(),
			        "Both child nodes have zero probability!");

			if (rewardLeft.isZero()) { // prune
				current.setLeftChild(PrunedNode.INSTANCE);
				nextPath.add(right.getConstraint());
				current = right;
				logger.warning("[ucb1] Current node: " + current + "\n    Left (score=uncomputed): " + left
				        + "\n   Right (score=uncomputed): " + right);
				
			} else if (rewardRight.isZero()) {
				current.setRightChild(PrunedNode.INSTANCE);
				nextPath.add(left.getConstraint());
				current = left;
				logger.warning("[ucb1] Current node: " + current + "\n    Left (score=uncomputed): " + left
				        + "\n   Right (score=uncomputed): " + right);
			} else {
				// compute UCB1 for left/right
				int parentVisits = current.getNumberVisits();
				double leftUCB1 = ucb1(rewardLeft, left.getNumberVisits(), parentVisits);
				double rightUCB1 = ucb1(rewardRight, right.getNumberVisits(), parentVisits);

				logger.warning("[ucb1] Current node: " + current + "\n    Left (score=" + leftUCB1 + "): " + left
				        + "\n   Right (score=" + rightUCB1 + "): " + right);

				boolean chooseLeft;
				if (leftUCB1 == rightUCB1) {
					chooseLeft = rng.nextBoolean();
				} else {
					chooseLeft = leftUCB1 > rightUCB1;
				}

				if (chooseLeft) {
					nextPath.add(left.getConstraint());
					current = left;
				} else {
					nextPath.add(right.getConstraint());
					current = right;
				}
			}
			
		}
		return nextPath;
	}

	public double ucb1(BigRational reward, int timesCovered, int nParentVisits) {
		if (timesCovered == 0) {
			switch(fpu) {
			case NONE: //return max value
				return Double.MAX_VALUE;
			case ONE:
				return 1;
			case PROBABILITY:
				return reward.doubleValue();
			case ONE_VISIT: 
				timesCovered = 1;
			default:
				throw new RuntimeException("Unknown case: " + fpu);
			}
		} else {
			double explorationFactor = Math.sqrt(2.0 * Math.log(nParentVisits) / timesCovered);
			if (useConstant) {
				explorationFactor = explorationFactor * 2 * KOCSIS_CONSTANT;
			}
			return reward.doubleValue() + explorationFactor;
		}
	}
}
