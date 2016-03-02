package janala.solvers.counters.trees;

import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.common.collect.ImmutableList;

import janala.interpreters.Constraint;

public class DummyTreePolicy implements TreePolicy {

	@Override
	public List<Constraint> chooseNextPath(SymbolicTree tree, RandomGenerator rng) {
		return ImmutableList.<Constraint>of();
	}

}
