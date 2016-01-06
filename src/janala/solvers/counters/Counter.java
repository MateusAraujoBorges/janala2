package janala.solvers.counters;

import java.util.List;
import java.util.Map;

import janala.interpreters.Constraint;
import janala.interpreters.Value;
import janala.solvers.InputElement;
import name.filieri.antonio.jpf.utils.BigRational;

public interface Counter{

	public BigRational probabilityOf(List<Constraint> constraints, List<InputElement> inputs, Map<Integer,Value> syntheticVars);
	
	/**
	 * Tell the counter that we won't be using it anymore, and it should clean up the house
	 * (flush caches to disk, etc.). 
	 */
	
	public void shutdown();
	
}
