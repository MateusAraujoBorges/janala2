package tests.math3.fraction;

import janala.Main;
import tests.math3.fraction.Fraction;

public class FractionTest {


public static void main(String[] args) {
	long a = Main.readLong(1);
    Main.MakeSymbolicLong(a,Integer.MIN_VALUE,Integer.MAX_VALUE);
    
	long b = Main.readLong(1);
    Main.MakeSymbolicLong(b,Integer.MIN_VALUE,Integer.MAX_VALUE);
    
	//int c =catg.CATG.readInt(1);
	//int d =catg.CATG.readInt(1);

    Fraction first = new Fraction((int)a, (int)b);
   // Fraction second = new Fraction(c,d);
    //first.abs();
    System.out.println (first.getDenominator());
  //  first.getNumerator();
  //  first.subtract(second);
  //  first.multiply(second);
  //  first.divide(second);
  //  first.add(second);
  //  first.negate();

    
   // Fraction.getReducedFraction(4, 2);
   // System.out.println(first.intValue());
    
    
    
}
	}
