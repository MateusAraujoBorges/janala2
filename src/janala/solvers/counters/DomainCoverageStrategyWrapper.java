package janala.solvers.counters;

import java.util.ArrayList;
import java.util.List;

import janala.interpreters.Constraint;
import janala.solvers.Element;
import janala.solvers.History;
import janala.solvers.InputElement;
import janala.solvers.Strategy;
import name.filieri.antonio.jpf.utils.BigRational;

/**
 * Logs the coverage of the domain for the current path
 * @author mateus
 *
 */

public class DomainCoverageStrategyWrapper extends Strategy {

	Strategy strategy;
	Counter counter;
	
	public DomainCoverageStrategyWrapper(Strategy strategy) {
		this.strategy = strategy;
		counter = new PCPCounter();
	}
	
	@Override
	public int solve(ArrayList<Element> history, int historySize, History solver) {
		
		List<Constraint> path = solver.getPathConstraint();
		List<InputElement> inputs = solver.getInputs();
		
		BigRational pathProbability = counter.probabilityOf(path, inputs);
		System.out.println("[quantolic] domain coverage for this path: " + pathProbability.doubleValue());
		return strategy.solve(history, historySize, solver);
	}

}
