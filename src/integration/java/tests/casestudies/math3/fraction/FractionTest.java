package tests.casestudies.math3.fraction;

import janala.Main;
import tests.casestudies.math3.fraction.Fraction;

public class FractionTest {


public static void main(String[] args) {
	
	int a = Main.readInt(1);
	int b = Main.readInt(1);
	Main.MakeSymbolic(a);
	Main.MakeSymbolic(b);
	
	int c = Main.readInt(1);
	int d = Main.readInt(1);
	Main.MakeSymbolic(c);
	Main.MakeSymbolic(d);
	/*long a = Main.readLong(1);

    Main.MakeSymbolicLong(a,Integer.MIN_VALUE,Integer.MAX_VALUE);
    
	long b = Main.readLong(1);
    Main.MakeSymbolicLong(b,Integer.MIN_VALUE,Integer.MAX_VALUE);
    */
	//int c =catg.CATG.readInt(1);
	//int d =catg.CATG.readInt(1);
//ArithmeticUtils.gcdPositive(a, b);

   Fraction first = new Fraction(a, b);
  
   Fraction second = new Fraction(c,d);
    //first.abs();
  //  System.out.println (first.getDenominator());
  // 
  // first.getNumerator();
 System.out.println (first.subtract(second).intValue());
  //  first.multiply(second);
  //  first.divide(second);
  //  first.add(second);
  //  first.negate();

    
   // Fraction.getReducedFraction(4, 2);
   // System.out.println(first.intValue());   
}
	}
