package tests.quantolic;

import static org.junit.Assert.assertEquals;

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
import janala.solvers.counters.Counter;
import janala.solvers.counters.PCPCounter;
import janala.solvers.counters.util.BigRational;

public class CounterTests {

	@Test
	public void testPCPCounter() {
		
		//Input: x1 + x2 >= 0
		// Mathematica result: 
		//   sols = Solve[{x + y >= 0, 10 >= x >= -10, 10 >= y >= -10}, {x, y}, Integers];
		//   Length@sols
		//Out: 231
		
		// Note to self - do not trust WolframAlpha results (returned 230 solutions :P)
		
		SymbolicInt i1 = new SymbolicInt(1);
		i1.constant = 0;
		i1.linear.put(2, 1);
		i1.op = SymbolicInt.COMPARISON_OPS.GE;
		
		IntValue v1 = new IntValue(0);
		v1.symbolic = new SymbolicInt(1);
		InputElement ie1 = new InputElement(1,v1, Range.closed(-10l, 10l));
		IntValue v2 = new IntValue(0);
		v1.symbolic = new SymbolicInt(2);
		InputElement ie2 = new InputElement(2,v2, Range.closed(-10l, 10l));
		
		List<Constraint> constraints = Lists.<Constraint>newArrayList(i1);
		List<InputElement> inputs = Lists.<InputElement>newArrayList(ie1,ie2);
		
		Counter counter = new PCPCounter();
		BigRational result = counter.probabilityOf(constraints, inputs, Collections.<Integer,Value>emptyMap());
		
		assertEquals(result, new BigRational(231,441));
	}
}
