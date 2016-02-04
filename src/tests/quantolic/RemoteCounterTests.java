package tests.quantolic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import janala.interpreters.Constraint;
import janala.interpreters.IntValue;
import janala.interpreters.SymbolicInt;
import janala.interpreters.Value;
import janala.solvers.InputElement;
import janala.solvers.counters.PCPVersionTwoCounter;
import name.filieri.antonio.jpf.utils.BigRational;

public class RemoteCounterTests {

	@Test
	public void testConstraintTranslation() {
		SymbolicInt i1 = new SymbolicInt(1);
		i1.op = SymbolicInt.COMPARISON_OPS.GT;
		i1.constant = -3; // constant is negative because constraints are
		                  // represented like 'ax + b > 0'

		SymbolicInt in1 = new SymbolicInt(1);
		in1.op = SymbolicInt.COMPARISON_OPS.LE;
		in1.constant = -3;

		SymbolicInt i2 = new SymbolicInt(2);
		i2.op = SymbolicInt.COMPARISON_OPS.GT;
		i2.constant = -5;

		SymbolicInt in2 = new SymbolicInt(2);
		in2.op = SymbolicInt.COMPARISON_OPS.LE;
		in2.constant = -5;

		IntValue v1 = new IntValue(0);
		v1.symbolic = new SymbolicInt(1);
		InputElement ie1 = new InputElement(1, v1, Range.closed(-10l, 10l));
		IntValue v2 = new IntValue(0);
		v1.symbolic = new SymbolicInt(2);
		InputElement ie2 = new InputElement(2, v2, Range.closed(-10l, 10l));

		List<InputElement> inputs = Lists.<InputElement>newArrayList(ie1, ie2);
		List<Constraint> constraints = Lists.<Constraint>newArrayList(i1,in1,i2,in2);
		
		PCPVersionTwoCounter counter = new PCPVersionTwoCounter(new PCPVersionTwoCounter.ConnectionManager() {
			@Override
			public BigRational count(String query) {
				String[] expected = new String[] { "(declare-var x1 (Int -10 10))", 
						"(declare-var x2 (Int -10 10))",
		                "(assert (> (+ (* x1 1) -3) 0) (<= (+ (* x1 1) -3) 0) (> (+ (* x2 1) -5) 0) (<= (+ (* x2 1) -5) 0))",
						"(count)"};

				int i = 0;
				for (String line : query.split("\n")) {
					assertEquals(expected[i], line.trim());
					i++;
				}

				return BigRational.ZERO;
			}

			@Override
			public void close() throws IOException {
			}
		});

		counter.probabilityOf(constraints, inputs, Collections.<Integer,Value>emptyMap());
	}
}
