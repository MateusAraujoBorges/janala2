package janala.solvers.counters;

import java.util.Arrays;

import janala.interpreters.Constraint;
import janala.interpreters.ConstraintVisitor;
import janala.interpreters.SymbolicAndConstraint;
import janala.interpreters.SymbolicFalseConstraint;
import janala.interpreters.SymbolicInt;
import janala.interpreters.SymbolicIntCompareConstraint;
import janala.interpreters.SymbolicNotConstraint;
import janala.interpreters.SymbolicOrConstraint;
import janala.interpreters.SymbolicStringPredicate;
import janala.interpreters.SymbolicTrueConstraint;

public class PCPVisitor implements ConstraintVisitor {

	StringBuilder query = new StringBuilder();

	public static String toPCPFormat(Constraint cons) {
		PCPVisitor pcpv = new PCPVisitor();
		cons.accept(pcpv);
		return pcpv.query.toString();
	}

	@Override
	public void visitSymbolicInt(SymbolicInt c) {
		switch (c.op) {
		case EQ:
			query.append("(= ");
			break;
		case NE:
			query.append("(!= ");
			break;
		case LE:
			query.append("(<= ");
			break;
		case LT:
			query.append("(< ");
			break;
		case GE:
			query.append("(>= ");
			break;
		case GT:
			query.append("(> ");
			break;
		default:
			throw new RuntimeException("Unknown op: " + c.op);
		}

		int[] mapKeys = c.linear.keys();
		int[] copyKeys = new int[mapKeys.length];
		System.arraycopy(mapKeys, 0, copyKeys, 0, mapKeys.length);
		Arrays.sort(copyKeys); // force deterministic iteration order

		for (int varId : copyKeys) {
			query.append("(+ ");
			long factor = c.linear.get(varId);

			query.append("(* x");
			query.append(varId);
			query.append(" ");
			query.append(factor);
			query.append(") ");
		}

		query.append(c.constant);

		for (int i = 0; i < copyKeys.length; i++) {
			query.append(")");
		}

		query.append(" 0)");

	}

	@Override
	public void visitSymbolicOr(SymbolicOrConstraint c) {
		query.append("(or ");
		for (Constraint clause : c.constraints) {
			clause.accept(this);
			query.append(' ');
		}
		query.append(") ");
	}

	@Override
	public void visitSymbolicStringPredicate(SymbolicStringPredicate c) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void visitSymbolicAnd(SymbolicAndConstraint c) {
		query.append("(and ");
		for (Constraint clause : c.constraints) {
			clause.accept(this);
			query.append(' ');
		}
		query.append(")");
	}

	@Override
	public void visitSymbolicNot(SymbolicNotConstraint c) {
		query.append("(not ");
		c.constraint.accept(this);
		query.append(")");
	}

	@Override
	public void visitSymbolicTrue(SymbolicTrueConstraint c) {
		query.append("true");
	}

	@Override
	public void visitSymbolicFalse(SymbolicFalseConstraint c) {
		query.append("false");
	}

	@Override
	public void visitSymbolicIntCompare(SymbolicIntCompareConstraint c) {
		throw new RuntimeException("Not implemented yet.");
	}

}
