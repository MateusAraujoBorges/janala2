package tests.math3.trimmed;

public class Fraction {
	public static void main(String[] args) {
		throw new DumbException(Whatever.gen());
	}
}

class DumbException extends RuntimeException {
	public DumbException(Object obj) {
	}
}

class Whatever {
	static Object f = null;
	
	static Object gen() {
		return new Object();
	}
}
