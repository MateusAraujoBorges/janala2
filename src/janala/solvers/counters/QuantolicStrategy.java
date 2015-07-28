package janala.solvers.counters;

import java.util.ArrayList;

import janala.config.Config;
import janala.solvers.BranchElement;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.Strategy;

/*
 * Problem: How the hell I keep the tree between JVM invocations?
 * 
 */

public class QuantolicStrategy extends Strategy {

	SymbolicTree tree = null;
	Counter counter = Config.instance.getCounter();
	
	@Override
	public int solve(ArrayList<Element> history, int historySize, History solver) {
		int j, to = -1, ret;

		if (tree == null) {
			tree = loadTreeFromFile(Config.instance.symtreeFile);
		}

		for (j = 0; j < historySize; j++) {
			Element tmp = history.get(j);
			BranchElement current;
			if (tmp instanceof BranchElement) {
				current = (BranchElement) tmp;
				if (current.isForceTruth && !current.branch) {
					if ((ret = dfs(history, j, to, solver)) != -1) {
						return ret;
					}
					to = j;
				} else if (current.isForceTruth) {
					to = j;
				}
			}
		}

		if (j >= historySize) {
			j = historySize - 1;
		}

		writeTreeToFile(tree,Config.instance.symtreeFile);
		return dfs(history, j, to, solver);
	}

	private void writeTreeToFile(SymbolicTree tree2, String symtreeFile) {
		
	}

	private SymbolicTree loadTreeFromFile(String symtreeFile) {
		return tree;
	}

	private int dfs(ArrayList<Element> history, int from, int to, History solver) {
		for (int i = from; i > to; i--) {
			Element tmp = history.get(i);
			if (tmp instanceof BranchElement) {
				BranchElement current = (BranchElement) tmp;
				if (!current.done && current.pathConstraintIndex != -1) {
					if (solver.solveAt(current.pathConstraintIndex)) {
						return i;
					}
				}
			}
		}
		return -1;
	}

}
