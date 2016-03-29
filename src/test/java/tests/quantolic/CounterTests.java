package tests.quantolic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Assume;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import janala.interpreters.COMPARISON_OPS;
import janala.interpreters.Constraint;
import janala.interpreters.IntValue;
import janala.interpreters.SymbolicInt;
import janala.interpreters.Value;
import janala.solvers.InputElement;
import janala.solvers.counters.PCPVersionTwoCounter;
import janala.solvers.counters.util.BigRational;

public class CounterTests {

	@Test
	public void testPCPCounter() throws Exception {
		
	  PCPVersionTwoCounter counter = null;
    try {
      counter = new PCPVersionTwoCounter();
    } catch (IOException e) {
      System.out.println("Counter server is not running. Test will be skipped!");
      Assume.assumeTrue(false);
    }
	  
		//Input: x1 + x2 >= 0
		// Mathematica result: 
		//   sols = Solve[{x + y >= 0, 10 >= x >= -10, 10 >= y >= -10}, {x, y}, Integers];
		//   Length@sols
		//Out: 231
		
		// Note to self - do not trust WolframAlpha results (returned 230 solutions :P)
		
		SymbolicInt i1 = new SymbolicInt(1);
		i1.getLinear().put(2, (long) 1);
		i1.setOp(COMPARISON_OPS.GE);
		
		IntValue v1 = new IntValue(0);
		v1.symbolic = new SymbolicInt(1);
		InputElement ie1 = new InputElement(1,v1, Range.closed(-10l, 10l));
		IntValue v2 = new IntValue(0);
		v1.symbolic = new SymbolicInt(2);
		InputElement ie2 = new InputElement(2,v2, Range.closed(-10l, 10l));
		
		List<Constraint> constraints = Lists.<Constraint>newArrayList(i1);
		List<InputElement> inputs = Lists.<InputElement>newArrayList(ie1,ie2);
		
		BigRational result = counter.probabilityOf(constraints, inputs, Collections.<Integer,Value>emptyMap());
		
		assertEquals(result, new BigRational(231,441));
	}
}
