package tests.math3.util;

public class FastMath {
	  public static int abs(final int x) {
	        final int i = x >>> 31;
	        return (x ^ (~i + 1)) + i;
	    }

	   public static int min(final int a, final int b) {
	        return (a <= b) ? a : b;
	    }
}


