package janala.solvers.counters;

import name.filieri.antonio.jpf.utils.BigRational;

public interface Counter{

	public BigRational count();
	public SymbolicCountNode readPartialTree();
}
