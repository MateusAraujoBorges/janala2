package janala.solvers.counters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import janala.interpreters.Constraint;
import janala.solvers.InputElement;
import name.filieri.antonio.jpf.utils.BigRational;

public class DebugCounter implements Counter {

	private final List<Counter> counters;
	
	public DebugCounter() {
		counters = new ArrayList<Counter>();
		counters.add(new PCPCounter("127.0.0.1",9990));
		try {
			counters.add(new PCPVersionTwoCounter());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public BigRational probabilityOf(List<Constraint> constraints, List<InputElement> inputs) {
		List<BigRational> results = new ArrayList<BigRational>();
		for (Counter counter : counters) {
			results.add(counter.probabilityOf(constraints, inputs));
		}
		
		for (int i = 0; i < counters.size() - 1; i++) {
			if (!results.get(i).equals(results.get(i+1))) {
				System.out.println("[debugCounter] results do not agree!: " + results.toString());
			}
		}
		
		return results.get(0);
	}

	@Override
	public void shutdown() {
		for (Counter counter : counters) {
			counter.shutdown();
		}
	}
}
