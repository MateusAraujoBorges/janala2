package tests.casestudies.math3.util;

import tests.casestudies.math3.exception.MathArithmeticException;
import tests.casestudies.math3.exception.util.LocalizedFormats;
import tests.casestudies.math3.util.FastMath;


public class ArithmeticUtils {
	
	private static int min = -2147483647;
	private static int max =2147483646;
	
	 public static int gcd(int p, int q) throws MathArithmeticException {
		 System.out.println("gcd "+p + " "+q);
	        int a = p;
	        int b = q;
	        
	        if (a == 0 ||
	            b == 0) {
	            if (a <= min ||
	                b <= min) {
	                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS(),
	                		new Object[]{p, q});
	            }
	            return FastMath.abs(a + b);
	        }


	        long al = a;
	        long bl = b;
	        boolean useLong = false;
	        if (a < 0) {
	            if( a<=min) {
	                useLong = true;
	            } else {
	                a = -a;
	            }
	            al = -al;
	        }
	        if (b < 0) {
	            if ( b<=min) {
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
	                if (bl > max) {
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
		 int c =  (int) (a / b);
				 return((  a - b * c));
	 }
	 
	    public static int gcdPositive(int a, int b) {
	       
	    	
	    	
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
	            a >>= 1;//Integer.numberOfTrailingZeros(a);
	        }

	        // Recover the common power of 2.
	        return a << shift;
	    }
	    public static int mulAndCheck(int x, int y) throws MathArithmeticException {
	        long m = ((long)x) * ((long)y);
	        if (m < min || m > max) {
	            throw new MathArithmeticException();
	        }
	        return (int)m;
	    }
	    public static int addAndCheck(int x, int y)
	            throws MathArithmeticException {
	        long s = (long)x + (long)y;
	        if (s < min || s > max) {
	            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION(), new Object[]{x, y});
	        }
	        return (int)s;
	    }
	    public static int subAndCheck(int x, int y) throws MathArithmeticException {
	        long s = (long)x - (long)y;
	        if (s < min || s > max) {
	            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION(), new Object[]{x, y});
	        }
	        return (int)s;
	    }
}
