package janala.solvers.counters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import janala.interpreters.Constraint;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.InputElement;
import janala.solvers.Strategy;
import name.filieri.antonio.jpf.utils.BigRational;

public class DomainCoverageWithoutSolvingStrategy extends Strategy {

	private Counter counter;
	
	public DomainCoverageWithoutSolvingStrategy() throws IOException {
		counter = new PCPVersionTwoCounter();
	}
	
	@Override
	public int solve(ArrayList<Element> history, int historySize, History solver) {
		long start = System.nanoTime();
		List<Constraint> path = solver.getPathConstraint();
		List<InputElement> inputs = solver.getInputs();
		
		BigRational pathProbability = counter.probabilityOf(path, inputs, solver.getSyntheticVars());
		long end = System.nanoTime();
		System.out.println("[quantolic] domain coverage for this path: " + pathProbability.doubleValue());
		System.out.println("[quantolic] full path: " + path);
		System.out.println("[quantolic] time spent calling the counter: " + (end - start) / Math.pow(10, 9));
		return -1;
	}
}