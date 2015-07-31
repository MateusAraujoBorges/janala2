package tests.quantolic;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import janala.interpreters.Constraint;
import janala.interpreters.IntValue;
import janala.interpreters.SymOrInt;
import janala.interpreters.SymbolicInt;
import janala.interpreters.SymbolicIntCompareConstraint;
import janala.solvers.InputElement;
import janala.solvers.counters.ConcolicCountNode;
import janala.solvers.counters.ConcolicCountTree;
import janala.solvers.counters.Counter;
import janala.solvers.counters.PCPCounter;
import janala.solvers.counters.PrunedNode;
import janala.solvers.counters.SymbolicCountNode;
import janala.solvers.counters.SymbolicTree;
import janala.solvers.counters.UnexploredNode;
import name.filieri.antonio.jpf.utils.BigRational;

public class TreeTests {

	@Test
	public void testConcolicTreeInsertionAndPruning() {
		SymOrInt v1 = new SymOrInt("x");
		SymOrInt v2 = new SymOrInt("y");
		SymOrInt v3 = new SymOrInt("z");

		SymOrInt c1 = new SymOrInt(1);
		SymOrInt c2 = new SymOrInt(2);
		SymOrInt c3 = new SymOrInt(3);

		Constraint a1 = new SymbolicIntCompareConstraint(v1, c1, SymbolicIntCompareConstraint.COMPARISON_OPS.GE);
		Constraint a2 = new SymbolicIntCompareConstraint(v2, c2, SymbolicIntCompareConstraint.COMPARISON_OPS.GE);
		Constraint a3 = new SymbolicIntCompareConstraint(v3, c3, SymbolicIntCompareConstraint.COMPARISON_OPS.GE);

		// first insertion
		List<Constraint> constraints = Lists.newArrayList(a1, a2, a3);
		SymbolicTree tree = new ConcolicCountTree();

		List<SymbolicCountNode> nodes = tree.insertPathIntoTree(constraints);
		checkNodeLayoutAndSetupProbabilities(tree, nodes);

		// everything should be the same if inserted twice...
		nodes = tree.insertPathIntoTree(constraints);
		checkNodeLayoutAndSetupProbabilities(tree, nodes);

		// second insertion
		Constraint a2_1 = new SymbolicIntCompareConstraint(v2, c2, SymbolicIntCompareConstraint.COMPARISON_OPS.LT);
		List<Constraint> constraints_2 = Lists.newArrayList(a1, a2_1, a3);
		List<SymbolicCountNode> nodes2 = tree.insertPathIntoTree(constraints_2);
		checkNodeLayoutAndSetupProbabilities_take2(tree, nodes2);

		// pruning
		/*-
		 * root (p=1)
		 *  |______________
		 *  |              | 
		 * x>=1(p=0.7)  x<1(p=0.3,not explored)
		 *  |______________
		 *  |              |
		 * y>=2(p=0.6)   y<2(p=0.1)
		 *  |              |____________
		 *  |			   |            |
		 *  |______      z>=3(p=0.1)   z<3(p=0.0, not explored)
		 *  |      |
		 *  |    z<3(p=0.5,not explored)
		 *  |
		 * z>=3(p=0.1)
		 * 
		 *   | AFTER PRUNING (a1,a2,a3)
		 *   V
		 * 
		 * root (p=0.9)
		 *  |______________
		 *  |              | 
		 * x>=1(p=0.6)  x<1(p=0.3,not explored)
		 *  |______________
		 *  |              |
		 * y>=2(p=0.5)   y<2(p=0.1)
		 *  |              |____________
		 *  |			   |            |
		 *  |______      z>=3(p=0.1)   z<3(p=0.0, not explored)
		 *  |      |
		 *  |    z<3(p=0.5,not explored)
		 *  |
		 *  P  
		 * 
		 *   | AFTER PRUNING (a1,a2_2,a3)
		 *   V
		 *   
		 * root (p=0.8)
		 *  |______________
		 *  |              | 
		 * x>=1(p=0.5)  x<1(p=0.3,not explored)
		 *  |______________
		 *  |              |
		 * y>=2(p=0.5)     P
		 *  |              x____________
		 *  |			   |            |
		 *  |______        P   z<3(p=0.0, not explored)
		 *  |      |
		 *  |    z<3(p=0.5,not explored)
		 *  |
		 *  P
		 *   
		 * 
		 */

		tree.updateAndPrune(nodes);
		assertEquals(tree.getRoot().getNumberOfSolutions(),new BigRational(9,10));
		assertEquals(tree.getRoot().getLeftChild().getNumberOfSolutions(),new BigRational(6,10));
		
		SymbolicCountNode n2 = tree.getRoot().getLeftChild().getLeftChild();
		assertEquals(n2.getNumberOfSolutions(),new BigRational(5,10));
		assertTrue(n2 instanceof ConcolicCountNode);
		assertTrue(n2.getLeftChild() instanceof PrunedNode);
		assertTrue(n2.getRightChild() instanceof UnexploredNode);

		tree.updateAndPrune(nodes2);
		assertEquals(tree.getRoot().getNumberOfSolutions(),new BigRational(8,10));
		assertEquals(tree.getRoot().getLeftChild().getNumberOfSolutions(),new BigRational(5,10));
		
		SymbolicCountNode n1 = tree.getRoot().getLeftChild();
		assertTrue(n1 instanceof ConcolicCountNode);
		assertTrue(n1.getLeftChild() instanceof ConcolicCountNode);
		assertTrue(n1.getRightChild() instanceof PrunedNode);

	}

	@Test
	public void testTreeCounting() {
		// Input: 10 >= x1 >= -10, 10 >= x2 >= -10
		// Tree:
		/*-
		 * root
		 *  |_____________________
		 *  |                    |
		 * x > 3               x <= 3
		 *  |                    |
		 *  |________            |______
		 *  |        |           |     |
		 * y > 5   y <= 5      y > 5  y <= 5
		 */

		/*-
		 * Mathematica result: 
		 * In[19]:= 
		 * a1 = Length@Solve[{x1 > 3, 10 >= x1 >= -10, 10 >= x2 >= -10}, {x1, x2},Integers]
		 * a2 = Length@Solve[{x1 <= 3, 10 >= x1 >= -10, 10 >= x2 >= -10}, {x1, x2},Integers]
		 * a11 = Length@Solve[{x1 > 3 && x2 > 5, 10 >= x1 >= -10, 10 >= x2 >= -10}, {x1,x2}, Integers]
		 * a12 = Length@Solve[{x1 > 3 && x2 <= 5, 10 >= x1 >= -10, 10 >= x2 >= -10}, {x1,x2}, Integers]
		 * a21 = Length@Solve[{x1 <= 3 && x2 > 5, 10 >= x1 >= -10, 10 >= x2 >= -10}, {x1,x2}, Integers]
		 * a22 = Length@Solve[{x1 <= 3 && x2 <= 5, 10 >= x1 >= -10, 10 >= x2 >= -10}, {x1,x2}, Integers]
		 * Out[19]= 147
		 * Out[20]= 294
		 * Out[21]= 35
		 * Out[22]= 112
		 * Out[23]= 70
		 * Out[24]= 224
		 * In[25]:= 147 + 294
		 * Out[25]= 441
		 */

		SymbolicInt i1 = new SymbolicInt(1);
		i1.op = SymbolicInt.COMPARISON_OPS.GT;
		i1.constant = -3; //constant is negative because constraints are represented like 'ax + b > 0'
		
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

		
		List<InputElement> inputs = Lists.<InputElement> newArrayList(ie1, ie2);
		SymbolicTree tree = new ConcolicCountTree();

		List<Constraint> constraints1 = Lists.<Constraint> newArrayList(i1,i2);
		List<Constraint> constraints2 = Lists.<Constraint> newArrayList(i1,in2);
		List<Constraint> constraints3 = Lists.<Constraint> newArrayList(in1,i2);
		List<Constraint> constraints4 = Lists.<Constraint> newArrayList(in1,in2);
		
		List<SymbolicCountNode> nodes1 = tree.insertPathIntoTree(constraints1);
		List<SymbolicCountNode> nodes2 = tree.insertPathIntoTree(constraints2);
		List<SymbolicCountNode> nodes3 = tree.insertPathIntoTree(constraints3);
		List<SymbolicCountNode> nodes4 = tree.insertPathIntoTree(constraints4);
		
		Counter counter = new PCPCounter();
		tree.count(nodes1, inputs, counter);
		tree.count(nodes2, inputs, counter);
		tree.count(nodes3, inputs, counter);
		tree.count(nodes4, inputs, counter);
		
		assertEquals(tree.getRoot().getNumberOfSolutions(), new BigRational(1));
		SymbolicCountNode n_1 = tree.getRoot().getLeftChild();
		SymbolicCountNode n_n1 = tree.getRoot().getRightChild();
		SymbolicCountNode n_2_1 = tree.getRoot().getLeftChild().getLeftChild();
		SymbolicCountNode n_n2_1 = tree.getRoot().getLeftChild().getRightChild();
		SymbolicCountNode n_2_n1 = tree.getRoot().getRightChild().getLeftChild();
		SymbolicCountNode n_n2_n1 = tree.getRoot().getRightChild().getRightChild();
		
		assertEquals(n_1.getNumberOfSolutions(), new BigRational(147,441));
		assertEquals(n_n1.getNumberOfSolutions(), new BigRational(294,441));
		assertEquals(n_2_1.getNumberOfSolutions(), new BigRational(35,441));
		assertEquals(n_n2_1.getNumberOfSolutions(), new BigRational(112,441));
		assertEquals(n_2_n1.getNumberOfSolutions(), new BigRational(70,441));
		assertEquals(n_n2_n1.getNumberOfSolutions(), new BigRational(224,441));
		
	}

	private void checkNodeLayoutAndSetupProbabilities(SymbolicTree tree, List<SymbolicCountNode> nodes) {
		assertEquals(nodes.size(), 4);
		SymbolicCountNode root = tree.getRoot();
		assertEquals(nodes.get(0), root);

		assertTrue(root.getLeftChild() != null);
		assertTrue(root.getRightChild() != null);
		assertTrue(root.getRightChild() instanceof UnexploredNode);
		assertTrue(root.getRightChild().getConstraint().equals(root.getLeftChild().getConstraint().not()));
		root.setNumberOfSolutions(BigRational.ONE);

		SymbolicCountNode n1 = root.getLeftChild();
		assertEquals(nodes.get(1), n1);
		assertTrue(n1.getLeftChild() != null);
		assertTrue(n1.getRightChild() != null);
		assertTrue(n1.getRightChild() instanceof UnexploredNode);
		assertTrue(n1.getRightChild().getConstraint().equals(n1.getLeftChild().getConstraint().not()));
		assertEquals(n1.getConstraint().toString(), "x-1>=0");
		n1.setNumberOfSolutions(new BigRational(700, 1000));

		SymbolicCountNode n2 = n1.getLeftChild();
		assertEquals(nodes.get(2), n2);
		assertTrue(n2.getLeftChild() != null);
		assertTrue(n2.getRightChild() != null);
		assertTrue(n2.getRightChild() instanceof UnexploredNode);
		assertTrue(n2.getRightChild().getConstraint().equals(n2.getLeftChild().getConstraint().not()));
		assertEquals(n2.getConstraint().toString(), "y-2>=0");
		n2.setNumberOfSolutions(new BigRational(600, 1000));

		SymbolicCountNode n3 = n2.getLeftChild();
		assertEquals(nodes.get(3), n3);
		assertTrue(n3.getLeftChild() != null);
		assertTrue(n3.getRightChild() != null);
		assertTrue(n3.getRightChild() instanceof PrunedNode);
		assertTrue(n3.getLeftChild() instanceof PrunedNode);
		assertTrue(n3.getRightChild().equals(n3.getLeftChild()));
		assertEquals(n3.getConstraint().toString(), "z-3>=0");
		n3.setNumberOfSolutions(new BigRational(100, 1000));
	}

	private void checkNodeLayoutAndSetupProbabilities_take2(SymbolicTree tree, List<SymbolicCountNode> nodes) {
		assertEquals(nodes.size(), 4);
		SymbolicCountNode root = tree.getRoot();
		assertEquals(nodes.get(0), root);

		assertTrue(root.getLeftChild() != null);
		assertTrue(root.getRightChild() != null);
		assertTrue(root.getRightChild() instanceof UnexploredNode);
		assertTrue(root.getRightChild().getConstraint().equals(root.getLeftChild().getConstraint().not()));

		SymbolicCountNode n1 = root.getLeftChild();
		assertTrue(n1.getLeftChild() != null);
		assertTrue(n1.getRightChild() != null);
		assertTrue(n1.getRightChild() instanceof ConcolicCountNode);
		assertTrue(n1.getRightChild().getConstraint().equals(n1.getLeftChild().getConstraint().not()));
		assertEquals(n1.getConstraint().toString(), "x-1>=0");
		assertEquals(nodes.get(1), n1);

		SymbolicCountNode n2 = n1.getLeftChild();
		assertTrue(n2.getLeftChild() != null);
		assertTrue(n2.getRightChild() != null);
		assertTrue(n2.getRightChild() instanceof UnexploredNode);
		assertTrue(n2.getRightChild().getConstraint().equals(n2.getLeftChild().getConstraint().not()));
		assertEquals(n2.getConstraint().toString(), "y-2>=0");

		SymbolicCountNode n2_2 = n1.getRightChild();
		assertTrue(n2_2.getLeftChild() != null);
		assertTrue(n2_2.getRightChild() != null);
		assertTrue(n2_2.getRightChild() instanceof UnexploredNode);
		assertTrue(n2_2.getRightChild().getConstraint().equals(n2_2.getLeftChild().getConstraint().not()));
		assertEquals(n2_2.getConstraint().toString(), "y-2<0");
		n2_2.setNumberOfSolutions(new BigRational(100, 1000));
		assertEquals(nodes.get(2), n2_2);

		SymbolicCountNode n3 = n2.getLeftChild();
		assertTrue(n3.getLeftChild() != null);
		assertTrue(n3.getRightChild() != null);
		assertTrue(n3.getRightChild() instanceof PrunedNode);
		assertTrue(n3.getLeftChild() instanceof PrunedNode);
		assertTrue(n3.getRightChild().equals(n3.getLeftChild()));
		assertEquals(n3.getConstraint().toString(), "z-3>=0");

		SymbolicCountNode n3_2 = n2_2.getLeftChild();
		assertTrue(n3_2.getLeftChild() != null);
		assertTrue(n3_2.getRightChild() != null);
		assertTrue(n3_2.getRightChild() instanceof PrunedNode);
		assertTrue(n3_2.getLeftChild() instanceof PrunedNode);
		assertTrue(n3_2.getRightChild().equals(n3_2.getLeftChild()));
		assertEquals(n3_2.getConstraint().toString(), "z-3>=0");
		n3_2.setNumberOfSolutions(new BigRational(100, 1000));
		assertEquals(nodes.get(3), n3_2);
	}
}
