package janala.solvers.counters;

import java.util.List;

import janala.interpreters.Constraint;
import janala.solvers.InputElement;
import name.filieri.antonio.jpf.utils.BigRational;

public interface Counter{

	public BigRational count(List<Constraint> constraints, List<InputElement> inputs);
	
}
