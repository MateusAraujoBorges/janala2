package tests.casestudies.math3.fraction;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.math.BigInteger;

import tests.casestudies.math3.exception.MathArithmeticException;
import tests.casestudies.math3.exception.NullArgumentException;
import tests.casestudies.math3.exception.util.LocalizedFormats;
import tests.casestudies.math3.util.ArithmeticUtils;

/**
 * Representation of a rational number.
 *
 * implements Serializable since 2.0
 *
 * @since 1.1
 */
public class Fraction
    extends Number
   {

//    /** A fraction representing "2 / 1". */
//    public static final Fraction TWO = new Fraction(2, 1);
//
//    /** A fraction representing "1". */
//    public static final Fraction ONE = new Fraction(1, 1);
//
//    /** A fraction representing "0". */
    public static final Fraction ZERO = new Fraction(0, 1);
//
//    /** A fraction representing "4/5". */
//    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
//
//    /** A fraction representing "1/5". */
//    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
//
//    /** A fraction representing "1/2". */
//    public static final Fraction ONE_HALF = new Fraction(1, 2);
//
//    /** A fraction representing "1/4". */
//    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
//
//    /** A fraction representing "1/3". */
//    public static final Fraction ONE_THIRD = new Fraction(1, 3);
//
//    /** A fraction representing "3/5". */
//    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
//
//    /** A fraction representing "3/4". */
//    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
//
//    /** A fraction representing "2/5". */
//    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
//
//    /** A fraction representing "2/4". */
//    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
//
//    /** A fraction representing "2/3". */
//    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
//
//    /** A fraction representing "-1 / 1". */
//    public static final Fraction MINUS_ONE = new Fraction(-1, 1);

    /** Serializable version identifier */
    private static final long serialVersionUID = 3698073679419233275L;

    /** The default epsilon used for convergence. */
    //private static final double DEFAULT_EPSILON = 1e-5;

    /** The denominator. */
    private final int denominator;

    /** The numerator. */
    private final int numerator;
    //TODO this is just a ugly fix - If problem with Integer.MAX_VALUE/MIN_VALUE is fixed please replace
    private static int min = -2147483647;
    private static int max =2147483646;


    
    /**
     * Create a fraction from an int.
     * The fraction is num / 1.
     * @param num the numerator.
     */
    public Fraction(int num) {
        this(num, 1);
    }

    /**
     * Create a fraction given the numerator and denominator.  The fraction is
     * reduced to lowest terms.
     * @param num the numerator.
     * @param den the denominator.
     * @throws MathArithmeticException if the denominator is {@code zero}
     */
    public Fraction(int num, int den) {
   // 	System.out.println(num +"" + den);
        if (den == 0) {
        	System.out.println("den==0");
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION(),
            		new Object[]{num, den});
            
        }
        if (den < 0) {
        	System.out.println("den<0");

            if (num >= max ||
                den <= -min) {

                throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION(),
                		new Object[]{num, den});
            }
            num = -num;
           den = -den;
        }
        // reduce numerator and denominator by greatest common denominator.
        final int d = ArithmeticUtils.gcd(num, den);
        if (d > 1) {
        	System.out.println("d<1");

            num /= d;
            den /= d;
        }

        // move sign to numerator.
        if (den < 0) {
        	System.out.println("d<0");

            num = -num;
            den = -den;
        }
        this.numerator   = num;
        this.denominator = den;
    }
    public Fraction subtract(Fraction fraction) {
        return addSub(fraction, false /* subtract */);
    }

    /**
     * Subtract an integer from the fraction.
     * @param i the {@code integer} to subtract.
     * @return this - i
     */
    public Fraction subtract(final int i) {
        return new Fraction(numerator - i * denominator, denominator);
    }

 
    /**
     * Return the additive inverse of this fraction.
     * @return the negation of this fraction.
     */
    public Fraction negate() {
        if (numerator<=min) {
        	System.out.println("neg");

            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION(), new Object[]{numerator, denominator});
        }
        return new Fraction(-numerator, denominator);
    }

    /**
     * Gets the fraction as an {@code int}. This returns the whole number part
     * of the fraction.
     * @return the whole number fraction part
     */
    @Override
    public int intValue() {
        return numerator/denominator;
    }
 
    public Fraction add(final int i) {
        return new Fraction(numerator + i * denominator, denominator);
    }
    
    public Fraction add(Fraction fraction) {
        return addSub(fraction, true /* add */);
    }
    
    private Fraction addSub(Fraction fraction, boolean isAdd) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION());
        }
        // zero is identity for addition.
        System.out.println("sub");
        if (numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }
        // if denominators are randomly distributed, d1 will be 1 about 61%
        // of the time.
        int d1 = ArithmeticUtils.gcd(denominator, fraction.denominator);
        if (d1==1) {
            // result is ( (u*v' +/- u'v) / u'v')
            int uvp = ArithmeticUtils.mulAndCheck(numerator, fraction.denominator);
            int upv = ArithmeticUtils.mulAndCheck(fraction.numerator, denominator);
            return new Fraction
                (isAdd ? ArithmeticUtils.addAndCheck(uvp, upv) :
                 ArithmeticUtils.subAndCheck(uvp, upv),
                 ArithmeticUtils.mulAndCheck(denominator, fraction.denominator));
        }
        // the quantity 't' requires 65 bits of precision; see knuth 4.5.1
        // exercise 7.  we're going to use a BigInteger.
        // t = u(v'/d1) +/- v(u'/d1)
        BigInteger uvp = BigInteger.valueOf(numerator)
        .multiply(BigInteger.valueOf(fraction.denominator/d1));
        BigInteger upv = BigInteger.valueOf(fraction.numerator)
        .multiply(BigInteger.valueOf(denominator/d1));
        BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
        // but d2 doesn't need extra precision because
        // d2 = gcd(t,d1) = gcd(t mod d1, d1)
        int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
        int d2 = (tmodd1==0)?d1:ArithmeticUtils.gcd(tmodd1, d1);

        // result is (t/d2) / (u'/d1)(v'/d2)
        BigInteger w = t.divide(BigInteger.valueOf(d2));
        if (w.bitLength() > 31) {
            throw new MathArithmeticException(LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY(),
                                              new Object[]{w});
        }
        return new Fraction (w.intValue(),
                ArithmeticUtils.mulAndCheck(denominator/d1,
                        fraction.denominator/d2));
    }

    public Fraction divide(Fraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION());
        }
        if (fraction.numerator == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY(),
            		new Object[]{fraction.numerator, fraction.denominator});
        }
        return multiply(fraction.reciprocal());
    }

    public Fraction multiply(final int i) {
        return multiply(new Fraction(i));
    }
    
    public Fraction multiply(Fraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION());
        }
        if (numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        }
        // knuth 4.5.1
        // make sure we don't overflow unless the result *must* overflow.
        int d1 = ArithmeticUtils.gcd(numerator, fraction.denominator);
        int d2 = ArithmeticUtils.gcd(fraction.numerator, denominator);
        return getReducedFraction
        (ArithmeticUtils.mulAndCheck(numerator/d1, fraction.numerator/d2),
                ArithmeticUtils.mulAndCheck(denominator/d2, fraction.denominator/d1));
    }

    public Fraction abs() {
        Fraction ret;
        if (numerator >= 0) {
            ret = this;
        } else {
            ret = negate();
        }
        return ret;
    }
    public static Fraction getReducedFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION(),
            		new Object[]{numerator, denominator});
        }
        if (numerator==0) {
            return ZERO; // normalize zero.
        }
        // allow 2^k/-2^31 as a valid fraction (where k>0)
        if (denominator<=min && (numerator&1)==0) {
            numerator/=2; denominator/=2;
        }
        if (denominator < 0) {
            if (numerator<=min ||
                    denominator<=min) {
                throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION(),
                		new Object[]{numerator, denominator});
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        // simplify fraction.
        int gcd = ArithmeticUtils.gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return new Fraction(numerator, denominator);
    }
    
    public Fraction reciprocal() {
        return new Fraction(denominator, numerator);
    }
    /**
     * Divide the fraction by an integer.
     * @param i the {@code integer} to divide by.
     * @return this * i
     */
    public Fraction divide(final int i) {
        return divide(new Fraction(i));
    }


	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getNumerator() {
	        return numerator;
	    }
    /**
     * Access the denominator.
     * @return the denominator.
     */
    public int getDenominator() {
        return denominator;
    }

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}


}
