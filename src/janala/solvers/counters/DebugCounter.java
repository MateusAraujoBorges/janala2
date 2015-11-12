package janala.solvers.counters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import janala.interpreters.Constraint;
import janala.solvers.InputElement;
import janala.utils.MyLogger;
import name.filieri.antonio.jpf.utils.BigRational;

public class DebugCounter implements Counter {

	private final static Logger logger = MyLogger.getLogger(DebugCounter.class.getName());
	private final List<Counter> counters;

	public DebugCounter() {
		counters = new ArrayList<Counter>();
		counters.add(new PCPCounter("127.0.0.1", 9990));
		try {
			counters.add(new PCPVersionTwoCounter("127.0.0.1", 9991));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigRational probabilityOf(List<Constraint> constraints, List<InputElement> inputs) {
		List<BigRational> results = new ArrayList<BigRational>();
		List<Long> timeSpent = new ArrayList<Long>();
		logger.log(Level.INFO, "[debugCounter] constraints: " + constraints);

		for (Counter counter : counters) {
			long start = System.nanoTime();
			results.add(counter.probabilityOf(constraints, inputs));
			long end = System.nanoTime();
			timeSpent.add(end - start);
		}

		for (int i = 0; i < counters.size() - 1; i++) {
			if (!results.get(i).equals(results.get(i + 1))) {
				logger.log(Level.SEVERE, "[debugCounter] results do not agree!: " + results.toString());
			}
		}
		for (int i = 0; i < counters.size(); i++) {
			logger.log(Level.INFO, "Time spent by counter {0} : {1} (s)",
			        new Object[] { counters.get(i).getClass().getName(),
			        		timeSpent.get(i) / Math.pow(10, 9) 
			        		});
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
