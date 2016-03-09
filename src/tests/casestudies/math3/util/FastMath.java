package tests.casestudies.math3.util;

public class FastMath {
	  public static int abs(final int x) {
	        final int i = x >>> 31;
	        System.out.println("abs");
	        return (x ^ (~i + 1)) + i;
	    }

	   public static int min(final int a, final int b) {
	        System.out.println("min");
	        return (a <= b) ? a : b;
	    }
}


