package tests.quantolic;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import janala.interpreters.Constraint;
import janala.interpreters.IntValue;
import janala.interpreters.SymOrInt;
import janala.interpreters.SymbolicInt;
import janala.interpreters.SymbolicIntCompareConstraint;
import janala.interpreters.Value;
import janala.solvers.InputElement;
import janala.solvers.counters.Counter;
import janala.solvers.counters.PCPCounter;
import janala.solvers.counters.PCPVersionTwoCounter;
import janala.solvers.counters.QuantolicStrategy;
import janala.solvers.counters.trees.ConcolicCountNode;
import janala.solvers.counters.trees.ConcolicCountTree;
import janala.solvers.counters.trees.PrunedNode;
import janala.solvers.counters.trees.QuantolicTreePolicy;
import janala.solvers.counters.trees.SymbolicCountNode;
import janala.solvers.counters.trees.SymbolicTree;
import janala.solvers.counters.trees.UnexploredNode;
import janala.solvers.counters.util.BigRational;

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
		assertEquals(tree.getRoot().getProbabilityOfSolution(),new BigRational(9,10));
		assertEquals(tree.getRoot().getLeftChild().getProbabilityOfSolution(),new BigRational(6,10));
		
		SymbolicCountNode n2 = tree.getRoot().getLeftChild().getLeftChild();
		assertEquals(n2.getProbabilityOfSolution(),new BigRational(5,10));
		assertTrue(n2 instanceof ConcolicCountNode);
		assertTrue(n2.getLeftChild() instanceof PrunedNode);
		assertTrue(n2.getRightChild() instanceof UnexploredNode);

		tree.updateAndPrune(nodes2);
		assertEquals(tree.getRoot().getProbabilityOfSolution(),new BigRational(8,10));
		assertEquals(tree.getRoot().getLeftChild().getProbabilityOfSolution(),new BigRational(5,10));
		
		SymbolicCountNode n1 = tree.getRoot().getLeftChild();
		assertTrue(n1 instanceof ConcolicCountNode);
		assertTrue(n1.getLeftChild() instanceof ConcolicCountNode);
		assertTrue(n1.getRightChild() instanceof PrunedNode);

	}

	@Test
	public void testTreeCounting() throws IOException {
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
		
		Counter counter = new PCPVersionTwoCounter();
		tree.count(nodes1, inputs, Collections.<Integer,Value>emptyMap(), counter);
		tree.count(nodes2, inputs, Collections.<Integer,Value>emptyMap(), counter);
		tree.count(nodes3, inputs, Collections.<Integer,Value>emptyMap(), counter);
		tree.count(nodes4, inputs, Collections.<Integer,Value>emptyMap(), counter);
		
		assertEquals(tree.getRoot().getProbabilityOfSolution(), new BigRational(1));
		SymbolicCountNode n_1 = tree.getRoot().getLeftChild();
		SymbolicCountNode n_n1 = tree.getRoot().getRightChild();
		SymbolicCountNode n_2_1 = tree.getRoot().getLeftChild().getLeftChild();
		SymbolicCountNode n_n2_1 = tree.getRoot().getLeftChild().getRightChild();
		SymbolicCountNode n_2_n1 = tree.getRoot().getRightChild().getLeftChild();
		SymbolicCountNode n_n2_n1 = tree.getRoot().getRightChild().getRightChild();
		
		assertEquals(n_1.getProbabilityOfSolution(), new BigRational(147,441));
		assertEquals(n_n1.getProbabilityOfSolution(), new BigRational(294,441));
		assertEquals(n_2_1.getProbabilityOfSolution(), new BigRational(35,441));
		assertEquals(n_n2_1.getProbabilityOfSolution(), new BigRational(112,441));
		assertEquals(n_2_n1.getProbabilityOfSolution(), new BigRational(70,441));
		assertEquals(n_n2_n1.getProbabilityOfSolution(), new BigRational(224,441));
		
	}
	
	//TODO rewrite this with a mocking framework
	@Test
	public void testQuantolicPathSelection() {
		//Same tree as before, but with some extra nodes
		SymOrInt v1 = new SymOrInt("x");
		SymOrInt v2 = new SymOrInt("y");
		SymOrInt v3 = new SymOrInt("z");

		SymOrInt c1 = new SymOrInt(1);
		SymOrInt c2 = new SymOrInt(2);
		SymOrInt c3 = new SymOrInt(3);

		Constraint a1 = new SymbolicIntCompareConstraint(v1, c1, SymbolicIntCompareConstraint.COMPARISON_OPS.GE);
		Constraint a2 = new SymbolicIntCompareConstraint(v2, c2, SymbolicIntCompareConstraint.COMPARISON_OPS.GE);
		Constraint a3 = new SymbolicIntCompareConstraint(v3, c3, SymbolicIntCompareConstraint.COMPARISON_OPS.GE);
		Constraint a4 = new SymbolicIntCompareConstraint(v1, v2, SymbolicIntCompareConstraint.COMPARISON_OPS.GE);

		// first insertion
		List<Constraint> constraints = Lists.newArrayList(a1, a2, a3);
		SymbolicTree tree = new ConcolicCountTree();

		List<SymbolicCountNode> nodes = tree.insertPathIntoTree(constraints);
		checkNodeLayoutAndSetupProbabilities(tree, nodes);

		// second insertion
		Constraint a2_1 = new SymbolicIntCompareConstraint(v2, c2, SymbolicIntCompareConstraint.COMPARISON_OPS.LT);
		List<Constraint> constraints_2 = Lists.newArrayList(a1, a2_1, a3);
		List<SymbolicCountNode> nodes2 = tree.insertPathIntoTree(constraints_2);
		checkNodeLayoutAndSetupProbabilities_take2(tree, nodes2);

		SymbolicCountNode node4 = new UnexploredNode(a4);
		node4.setProbabilityOfSolution(new BigRational(1,10));
		SymbolicCountNode node4_1 = new UnexploredNode(a4);
		node4_1.setProbabilityOfSolution(new BigRational(1,10));
		
		tree.getRoot().getLeftChild().getLeftChild().getLeftChild().setRightChild(node4);
		tree.getRoot().getLeftChild().getRightChild().getLeftChild().setRightChild(node4_1);
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
		 *  |      |       |___________________          
		 *  |      |                      |   |
		 *  |    z<3(p=0.5,not explored)  P   x>y(p=0.1,not explored)    
		 *  |
		 * z>=3(p=0.1)
		 *  |___
		 *  |   |
		 *  P  x>y(p=0.1,not explored)
		 */
		
		RandomGenerator rng = new RandomGenerator() {
			
			double[] values = new double[]{0.3,0.8,0.2,0.9,0.1,0.99,0.5,0.000000001};
			int pos = 0;
			
			@Override
			public void setSeed(long arg0) {}
			@Override
			public void setSeed(int[] arg0) {}
			@Override
			public void setSeed(int arg0) {}
			@Override
			public long nextLong() {return -1;}
			@Override
			public int nextInt(int arg0) {return -1;}
			@Override
			public int nextInt() {return -1;}
			@Override
			public double nextGaussian() {return -1;}
			@Override
			public float nextFloat() {return -1;}
			@Override
			public double nextDouble() {
				return values[pos++];
			}
			@Override
			public void nextBytes(byte[] arg0) {}
			public boolean nextBoolean() {return false;}
		};
		
		QuantolicTreePolicy policy = new QuantolicTreePolicy();
		QuantolicStrategy strategy = new QuantolicStrategy(new Counter() {
			@Override
			public void shutdown() {}
			@Override
			public BigRational probabilityOf(List<Constraint> constraints, List<InputElement> inputs, Map<Integer,Value> syntheticVars) {
				throw new RuntimeException();
			}
		}, tree, policy, rng);
		
		List<Constraint> nextPath = policy.chooseNextPath(tree,rng);
		assertEquals(nextPath.size(), 3);
		assertEquals(nextPath.get(0), a1);
		assertEquals(nextPath.get(1), a2);
		assertEquals(nextPath.get(2), a3.not());
		
		nextPath = policy.chooseNextPath(tree,rng);
		assertEquals(nextPath.size(), 1);
		assertEquals(nextPath.get(0), a1.not());
		
		nextPath = policy.chooseNextPath(tree,rng);
		assertEquals(nextPath.size(), 4);
		assertEquals(nextPath.get(0), a1);
		assertEquals(nextPath.get(1), a2.not());
		assertEquals(nextPath.get(2), a3);
		assertEquals(nextPath.get(3), a4);
	}


	private void checkNodeLayoutAndSetupProbabilities(SymbolicTree tree, List<SymbolicCountNode> nodes) {
		assertEquals(nodes.size(), 4);
		SymbolicCountNode root = tree.getRoot();
		assertEquals(nodes.get(0), root);

		assertTrue(root.getLeftChild() != null);
		assertTrue(root.getRightChild() != null);
		assertTrue(root.getRightChild() instanceof UnexploredNode);
		assertTrue(root.getRightChild().getConstraint().equals(root.getLeftChild().getConstraint().not()));
		root.setProbabilityOfSolution(BigRational.ONE);

		SymbolicCountNode n1 = root.getLeftChild();
		assertEquals(nodes.get(1), n1);
		assertTrue(n1.getLeftChild() != null);
		assertTrue(n1.getRightChild() != null);
		assertTrue(n1.getRightChild() instanceof UnexploredNode);
		assertTrue(n1.getRightChild().getConstraint().equals(n1.getLeftChild().getConstraint().not()));
		assertEquals(n1.getConstraint().toString(), "x-1>=0");
		n1.setProbabilityOfSolution(new BigRational(700, 1000));

		SymbolicCountNode n2 = n1.getLeftChild();
		assertEquals(nodes.get(2), n2);
		assertTrue(n2.getLeftChild() != null);
		assertTrue(n2.getRightChild() != null);
		assertTrue(n2.getRightChild() instanceof UnexploredNode);
		assertTrue(n2.getRightChild().getConstraint().equals(n2.getLeftChild().getConstraint().not()));
		assertEquals(n2.getConstraint().toString(), "y-2>=0");
		n2.setProbabilityOfSolution(new BigRational(600, 1000));

		SymbolicCountNode n3 = n2.getLeftChild();
		assertEquals(nodes.get(3), n3);
		assertTrue(n3.getLeftChild() != null);
		assertTrue(n3.getRightChild() != null);
		assertTrue(n3.getRightChild() instanceof PrunedNode);
		assertTrue(n3.getLeftChild() instanceof PrunedNode);
		assertTrue(n3.getRightChild().equals(n3.getLeftChild()));
		assertEquals(n3.getConstraint().toString(), "z-3>=0");
		n3.setProbabilityOfSolution(new BigRational(100, 1000));
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
		n2_2.setProbabilityOfSolution(new BigRational(100, 1000));
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
		n3_2.setProbabilityOfSolution(new BigRational(100, 1000));
		assertEquals(nodes.get(3), n3_2);
	}
}
