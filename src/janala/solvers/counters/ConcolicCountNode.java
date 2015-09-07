package janala.solvers.counters;

import java.nio.charset.Charset;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import janala.interpreters.Constraint;
import name.filieri.antonio.jpf.utils.BigRational;

public class ConcolicCountNode implements SymbolicCountNode {

	private static final long serialVersionUID = -7018150946471139179L;

	private BigRational probabilityOfSolution;
	private Constraint constraint;
	//private Problem problem;
	private Integer hashcode = null;
	
	private SymbolicCountNode left;
	private SymbolicCountNode right;

	public ConcolicCountNode(Constraint cons) {
		this.constraint = cons;
		probabilityOfSolution = BigRational.MINUS_ONE;
		hashcode = 0;
	}

	@Override
	public BigRational getProbabilityOfSolution() {
		return probabilityOfSolution;
	}

	@Override
	public void setProbabilityOfSolution(BigRational probabilityOfSolution) {
		this.probabilityOfSolution = probabilityOfSolution;
	}

	@Override
	public boolean isCounted() {
		return !probabilityOfSolution.equals(BigRational.MINUS_ONE);
	}

	@Override
	public boolean isEmpty() {
		return isCounted() && probabilityOfSolution.equals(BigRational.ZERO);
	}

	@Override
	public Constraint getConstraint() {
		return constraint;
	}

	private int computeHashCode() {
		HashFunction hf = Hashing.goodFastHash(32);
		return hf.newHasher().putInt(constraint.iid).putInt(constraint.index)
		        .putString(constraint.toString(), Charset.defaultCharset()).hash().asInt();
	}

	@Override
	public int hashCode() {
		if (this.hashcode==null) {
			this.hashcode = computeHashCode();
		}
		return this.hashcode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConcolicCountNode) {
			ConcolicCountNode node = (ConcolicCountNode) obj;
			return node.hashCode() == this.hashCode()
			        && node.getConstraint().toString().equals(this.getConstraint().toString());
		}
		return false;
	};

	@Override
	public String toString() {
		return "count: " + probabilityOfSolution + " cons:" + constraint;
	}
	
	@Override
	public SymbolicCountNode getLeftChild() {
		return left;
	}

	@Override
	public SymbolicCountNode getRightChild() {
		return right;
	}

	@Override
	public void setLeftChild(SymbolicCountNode left) {
		this.left = left;
	}

	@Override
	public void setRightChild(SymbolicCountNode right) {
		this.right = right;
	}
}
