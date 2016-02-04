package tests.quantolic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import gnu.trove.map.hash.TIntLongHashMap;
import janala.interpreters.Constraint;
import janala.interpreters.IntValue;
import janala.interpreters.SymbolicInt;
import janala.interpreters.Value;
import janala.solvers.InputElement;
import janala.solvers.counters.ConcolicCountTree;
import janala.solvers.counters.PCPVersionTwoCounter;
import janala.solvers.counters.SymbolicCountNode;

public class TreeAndCounterTests {

	@Test
	public void repeatedConstraintsTest() {
		PCPVersionTwoCounter counter = null;
		try {
			counter = new PCPVersionTwoCounter();
		} catch (IOException e) {
			System.out.println("Counter server is not running. Test will be skipped!");
			Assume.assumeTrue(false);
		}
		ConcolicCountTree tree = new ConcolicCountTree();
		RandomGenerator rng = new MersenneTwister(0);

		SymbolicInt i1 = new SymbolicInt(1);
		i1.op = SymbolicInt.COMPARISON_OPS.GT;
		i1.constant = -3; // constant is negative because constraints are
		                  // represented like 'ax + b > 0'

		SymbolicInt i2 = new SymbolicInt(2);
		i2.op = SymbolicInt.COMPARISON_OPS.GT;
		i2.constant = -5;

		IntValue v1 = new IntValue(0);
		v1.symbolic = new SymbolicInt(1);
		InputElement ie1 = new InputElement(1, v1, Range.closed(-10l, 10l));
		IntValue v2 = new IntValue(0);
		v1.symbolic = new SymbolicInt(2);
		InputElement ie2 = new InputElement(2, v2, Range.closed(-10l, 10l));

		List<InputElement> inputs = Lists.<InputElement> newArrayList(ie1, ie2);
		List<Constraint> constraints = ImmutableList.<Constraint> of(i1, i1, i2, i2);

		List<SymbolicCountNode> path = tree.insertPathIntoTree(constraints);
		tree.count(path, inputs, Collections.<Integer,Value>emptyMap(), counter);
		tree.updateAndPrune(path);
		System.out.println(tree);
	}

	/**
	 * This tests shows that the 'equals()' of trove maps (the 'linear' field
	 * from SymbolicInt) is completely broken.
	 */

	@Test
	public void collisionTest() {
		SymbolicInt i1 = new SymbolicInt(3);
		i1.op = SymbolicInt.COMPARISON_OPS.GT;
		i1.linear.put(7, -1);
		i1.iid = 532508;
		i1.index = 24;

		SymbolicInt i2 = new SymbolicInt(1);
		i2.op = SymbolicInt.COMPARISON_OPS.GT;
		i2.linear.put(9, -1);
		i2.iid = 532508;
		i2.index = 28;

		System.out.println(i1.hashCode());
		System.out.println(i2.hashCode());
		Assert.assertFalse(i1.equals(i2));
	}
	
	
	@Test
	public void troveMapBehaviorTest() {
		TIntLongHashMap m1 = new TIntLongHashMap();
		m1.put(7, -1);
		m1.put(3, 1);
		
		TIntLongHashMap m2 = new TIntLongHashMap();
		m2.put(9, -1);
		m2.put(1, 1);
		
		//m1 is definitively not equal to m2 
		assertTrue(m1.equals(m2));
		System.out.println("The implementation of 'equals' for TIntLongHashMap doesn't make any sense");
	}
}
