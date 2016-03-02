package tests.math3.util;

import tests.math3.util.FastMath;

import tests.math3.exception.MathArithmeticException;
import tests.math3.exception.util.LocalizedFormats;


public class ArithmeticUtils {
	 public static int gcd(int p, int q) throws MathArithmeticException {
	        int a = p;
	        int b = q;
	        if (a == 0 ||
	            b == 0) {
	            if (a == Integer.MIN_VALUE ||
	                b == Integer.MIN_VALUE) {
	                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS(),
	                		new Object[]{p, q});
	            }
	            return FastMath.abs(a + b);
	        }

	        long al = a;
	        long bl = b;
	        boolean useLong = false;
	        if (a < 0) {
	            if(Integer.MIN_VALUE == a) {
	                useLong = true;
	            } else {
	                a = -a;
	            }
	            al = -al;
	        }
	        if (b < 0) {
	            if (Integer.MIN_VALUE == b) {
	                useLong = true;
	            } else {
	                b = -b;
	            }
	            bl = -bl;
	        }
	        if (useLong) {
	            if(al == bl) {
	                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS(),
	                		new Object[]{p, q});
	            }
	            long blbu = bl;
	            bl = al;
	            al = mod(blbu,  al);
	            if (al == 0) {
	                if (bl > Integer.MAX_VALUE) {
	                    throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS(),
	                    		new Object[]{p, q});
	                }
	                return (int) bl;
	            }
	            blbu = bl;

	            // Now "al" and "bl" fit in an "int".
	            b = (int) al;
	            a = (int) (mod(blbu,al));
	        }

	        return gcdPositive(a, b);
	    }
	 private static long mod(long a,long b)
	 {
		 long c =  (int) (a / b);
				 return(  a - b * c);
	 }
	 
	    private static int gcdPositive(int a, int b) {
	        if (a == 0) {
	            return b;
	        }
	        else if (b == 0) {
	            return a;
	        }

	        // Make "a" and "b" odd, keeping track of common power of 2.
	        final int aTwos = Integer.numberOfTrailingZeros(a);
	        a >>= aTwos;
	        final int bTwos = Integer.numberOfTrailingZeros(b);
	        b >>= bTwos;
	        final int shift = FastMath.min(aTwos, bTwos);

	        // "a" and "b" are positive.
	        // If a > b then "gdc(a, b)" is equal to "gcd(a - b, b)".
	        // If a < b then "gcd(a, b)" is equal to "gcd(b - a, a)".
	        // Hence, in the successive iterations:
	        //  "a" becomes the absolute difference of the current values,
	        //  "b" becomes the minimum of the current values.
	        while (a != b) {
	            final int delta = a - b;
	            b = Math.min(a, b);
	            a = Math.abs(delta);

	            // Remove any power of 2 in "a" ("b" is guaranteed to be odd).
	            a >>= Integer.numberOfTrailingZeros(a);
	        }

	        // Recover the common power of 2.
	        return a << shift;
	    }
	    public static int mulAndCheck(int x, int y) throws MathArithmeticException {
	        long m = ((long)x) * ((long)y);
	        if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
	            throw new MathArithmeticException();
	        }
	        return (int)m;
	    }
	    public static int addAndCheck(int x, int y)
	            throws MathArithmeticException {
	        long s = (long)x + (long)y;
	        if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
	            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION(), new Object[]{x, y});
	        }
	        return (int)s;
	    }
	    public static int subAndCheck(int x, int y) throws MathArithmeticException {
	        long s = (long)x - (long)y;
	        if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
	            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION(), new Object[]{x, y});
	        }
	        return (int)s;
	    }
}
