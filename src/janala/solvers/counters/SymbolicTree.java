package janala.solvers.counters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import janala.interpreters.Constraint;
import janala.interpreters.Value;
import janala.solvers.InputElement;
import name.filieri.antonio.jpf.utils.BigRational;

public interface SymbolicTree extends Serializable {

	/**
	 * @return true if there are no more nodes to be explored.
	 */
	public boolean isDone();

	/**
	 * Update the tree with new constraints. New nodes will be created as
	 * needed. This method also can be used to search for nodes. Intermediary
	 * {@code UnexploredNode}s in the way will be replaced by concrete nodes.
	 * 
	 * @param constraints
	 * @return List with the nodes in the path. First node is the root, last one
	 *         is the leaf.
	 */

	public List<SymbolicCountNode> insertPathIntoTree(List<Constraint> constraints);

	/**
	 * The number of solutions of every uncounted node (checked using
	 * {@code isCounted()} is computed using the {@code counter} object.
	 * @return 
	 */

	public BigRational count(List<SymbolicCountNode> path, List<InputElement> inputs, Map<Integer,Value> syntheticVars, Counter counter);

	/**
	 * Update count estimates and remove nodes without remaining unexplored
	 * paths from the tree. This happens in two steps:
	 * 
	 * - The last element of {@code path} is pruned from the tree. This element
	 * must be counted and have only {@code PrunedNode}s as childs. - Every
	 * other path element, in reverse order, will have their number of solutions/
	 * probability updated, and will be pruned too if this number is equal to zero.
	 * 
	 * @param path
	 */

	public void updateAndPrune(List<SymbolicCountNode> path);

	/**
	 * TODO How to prune nodes with estimates of the number of solutions?
	 * @param path
	 */
	
	public void updateAndPruneApprox(List<SymbolicCountNode> path);
	
	public SymbolicCountNode getRoot();

	/**
	 * Write the tree to the disk
	 * 
	 * @param f
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeToDisk(File f) throws FileNotFoundException, IOException;

	
}
