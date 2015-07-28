package janala.solvers.counters;

import java.nio.charset.Charset;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import janala.interpreters.Constraint;
import name.filieri.antonio.jpf.utils.BigRational;

public class ConcolicCountNode implements SymbolicCountNode {

	private static final long serialVersionUID = -7018150946471139179L;

	private BigRational nsolutions;
	private Constraint constraint;
	//private Problem problem;
	private boolean alreadyHashed;
	private int hashcode;
	
	private SymbolicCountNode left;
	private SymbolicCountNode right;

	public ConcolicCountNode(Constraint cons) {
		this.constraint = cons;
		nsolutions = BigRational.MINUS_ONE;
		alreadyHashed = false;
		hashcode = 0;
		//problem = toPCPFormat(cons);
	}

//	private Problem toPCPFormat(Constraint cons) {
//		String pc = cons.toString();
//		pc.toString();
//	}

	@Override
	public BigRational getNumberOfSolutions() {
		return nsolutions;
	}

	@Override
	public void setNumberOfSolutions(BigRational nsolutions) {
		this.nsolutions = nsolutions;
	}

	@Override
	public boolean isCounted() {
		return nsolutions != BigRational.MINUS_ONE;
	}

	@Override
	public boolean isEmpty() {
		return isCounted() && nsolutions.equals(BigRational.ZERO);
	}

	@Override
	public Constraint getConstraint() {
		return constraint;
	}

	private int computeHashCode() {
		HashFunction hf = Hashing.goodFastHash(32);
		return hf.newHasher().putInt(constraint.iid).putInt(constraint.index)
		        .putString(constraint.toString(), Charset.defaultCharset()).hashCode();
	}

	@Override
	public int hashCode() {
		if (!alreadyHashed) {
			hashcode = computeHashCode();
			alreadyHashed = true;
		}
		return hashcode;
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
