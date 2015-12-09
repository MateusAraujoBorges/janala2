package janala.solvers.counters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import janala.interpreters.Constraint;
import janala.interpreters.SymbolicTrueConstraint;
import janala.solvers.InputElement;
import janala.utils.MyLogger;
import name.filieri.antonio.jpf.utils.BigRational;

public class ConcolicCountTree implements SymbolicTree {

	private static final long serialVersionUID = -5739239625633749868L;
	private final static Logger logger = MyLogger.getLogger(ConcolicCountTree.class.getName());
	
	private final SymbolicCountNode root;
	private final boolean mergeIdenticalConstraints;

	public ConcolicCountTree() {
		root = new ConcolicCountNode(SymbolicTrueConstraint.instance);
		root.setProbabilityOfSolution(BigRational.ONE);
		mergeIdenticalConstraints = true;
	}

	@Override
	public boolean isDone() {
		return root.isEmpty();
	}

	/**
	 * Precondition: All empty nodes should be pruned prior to inserting new
	 * ones.
	 * 
	 * @param constraints
	 * @return
	 */

	@Override
	public List<SymbolicCountNode> insertPathIntoTree(List<Constraint> constraints) {
		Preconditions.checkArgument(constraints.size() > 0, "Empty lists are not allowed");

		if (mergeIdenticalConstraints) {
			Set<Constraint> set = Sets.newLinkedHashSet(constraints);
			logger.log(Level.INFO,"[concolictree] Merging constraints: (size) {0} -> {1} ",
					new Object[]{ constraints.size(), set.size()});
			logger.log(Level.INFO,"[concolictree] Merging constraints:\n\t old: {0} \n\t new: {1}",
					new Object[]{constraints,set});
			constraints = ImmutableList.copyOf(set);
		}
		
		List<SymbolicCountNode> path = Lists.newArrayList();
		SymbolicCountNode current = root;
		SymbolicCountNode next = root;
		path.add(current);
		// TODO check toString performance of Constraints; caching should help.
		for (Constraint cons : constraints) {
			Preconditions.checkState(!current.isEmpty(), "Trying to insert into an empty/pruned node!");
			Preconditions.checkState(!(current instanceof UnexploredNode), "A unexplored node wasn't replaced!");

			// check left, then right. We always insert both left and right
			// nodes at once, so it should never be the case that left != null
			// and right == null, or vice-versa.
			Preconditions.checkState((current.getLeftChild() != null && current.getRightChild() != null)
			        || (current.getLeftChild() == null && current.getRightChild() == null));

			/*-
			 * two (disjunct) possibilities: 
			 * 1. left is null -> insert C on left, not(C) on right
			 * 2. left is not null -> check if left.cons = cons, or if right.cons = cons, 
			 *    and keep going (replacing UnexploredNodes if needed)
			 */

			if (current.getLeftChild() == null) {
				SymbolicCountNode newLeftNode = new ConcolicCountNode(cons);
				SymbolicCountNode newRightNode = new UnexploredNode(cons.not());
				current.setLeftChild(newLeftNode);
				current.setRightChild(newRightNode);
				current = newLeftNode;
			} else {
				SymbolicCountNode left = current.getLeftChild();
				SymbolicCountNode right = current.getRightChild();
				boolean isLeft = false;

				if (!left.isEmpty() && left.getConstraint().equals(cons)) {
					next = current.getLeftChild();
					isLeft = true;
				} else if (!right.isEmpty() && right.getConstraint().equals(cons)) {
					next = current.getRightChild();
				} else {
					Preconditions.checkArgument(false, "Can't find the next node in the path!");
				}

				if (next instanceof UnexploredNode) {
					UnexploredNode unode = (UnexploredNode) next;
					SymbolicCountNode newNode = new ConcolicCountNode(cons);
					newNode.setProbabilityOfSolution(unode.getProbabilityOfSolution());

					if (isLeft) {
						current.setLeftChild(newNode);
					} else {
						current.setRightChild(newNode);
					}

					current = newNode;
				} else {
					current = next;
				}
			}
			path.add(current);
		}
		// "plug" the paths in the leaf
		current.setLeftChild(PrunedNode.INSTANCE);
		current.setRightChild(PrunedNode.INSTANCE);
		return path;
	}

	@Override
	public void count(List<SymbolicCountNode> path, List<InputElement> inputs, Counter counter) { 
		List<Constraint> clauses = Lists.newArrayList();
		
		for (SymbolicCountNode node : path) {
//			if (!clauses.equals(SymbolicTrueConstraint.instance)) {
				clauses.add(node.getConstraint());
//			}
			
			if (node.isCounted()) {
//				logger.log(Level.INFO, "[ConcolicCountTree] Node already counted: {}", node);
			} else {
//				logger.log(Level.INFO, "[ConcolicCountTree] Counting node: {}", node);
				List<Constraint> pc = ImmutableList.copyOf(clauses);
				BigRational result = counter.probabilityOf(pc, inputs);				
				node.setProbabilityOfSolution(result);
			}
		}
	}

	@Override
	public void updateAndPrune(List<SymbolicCountNode> path) {
		SymbolicCountNode last = path.get(path.size() - 1);
		BigRational explored = last.getProbabilityOfSolution();

		SymbolicCountNode toBePruned = last;

		for (int i = path.size() - 1; i >= 0; i--) {
			SymbolicCountNode current = path.get(i);
			Preconditions.checkState(current.isCounted(), "The node wasn't 'counted'!");

			BigRational currentNSolutions = current.getProbabilityOfSolution();
			currentNSolutions = currentNSolutions.minus(explored);
			Preconditions.checkState(!currentNSolutions.isNegative(),
			        "The operation would result in a negative number of solutions");

			current.setProbabilityOfSolution(currentNSolutions);
			if (current.isEmpty()) { // Last node will always be pruned
				Preconditions.checkState(toBePruned != null,
				        "There must not be any 'jumps' in the sequence of pruned nodes");
				toBePruned = current;
			} else if (toBePruned != null) { //Prune the node
				if (current.getLeftChild().equals(toBePruned)) {
					current.setLeftChild(PrunedNode.INSTANCE);
				} else if (current.getRightChild().equals(toBePruned)) {
					current.setRightChild(PrunedNode.INSTANCE);
				} else {
					Preconditions.checkState(false,"Didn't found node to be pruned!");
				}
				toBePruned = null;
			}
		}
	}

	@Override
	public void updateAndPruneApprox(List<SymbolicCountNode> path) {
		throw new RuntimeException("Not Implemented yet");
	}

	@Override
	public SymbolicCountNode getRoot() {
		return root;
	}

	@Override
	public void writeToDisk(File f) throws FileNotFoundException, IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f));
		os.writeObject(this);
		os.close();
	}

	public static ConcolicCountTree readFromDisk(File f)
	        throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(f));
		Object tmp = is.readObject();
		is.close();
		if (tmp instanceof ConcolicCountTree) {
			return (ConcolicCountTree) tmp;
		} else {
			throw new RuntimeException("Unexpected tree type! " + tmp.getClass().getName());
		}
	}

	@Override
	public String toString() {
		SymbolicCountNode current = root;
		Deque<SymbolicCountNode> stack = new ArrayDeque<SymbolicCountNode>();
		Deque<Integer> depthStack = new ArrayDeque<Integer>();
		stack.push(current);
		depthStack.push(0);
		
		StringBuffer sb = new StringBuffer();
		while (!stack.isEmpty()) {
			current = stack.pop();
			int depth = depthStack.pop();
			for (int i = 0; i < depth; i++) {
				sb.append(" > ");
			}
			if (!current.isCounted() && current instanceof ConcolicCountNode) {
				sb.append("!!!uncounted!!!! ");
			}
			sb.append(current.toString());
			sb.append("\n");
			if (current instanceof UnexploredNode || current instanceof PrunedNode) {
				continue;
			} else {
				stack.push(current.getRightChild());
				stack.push(current.getLeftChild());
				depthStack.push(depth + 1);
				depthStack.push(depth + 1);
			}
		}
		return sb.toString();
	}
	
}
