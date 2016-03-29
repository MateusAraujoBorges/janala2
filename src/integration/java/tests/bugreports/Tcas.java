package tests.bugreports;

import catg.CATG;
import janala.Main;

public class Tcas {

	static int is_upward_preferred(boolean inhibit) {
		if (inhibit == true) {
			System.out.println("$$TARGET$$");
			System.out.println("$$P: 10");
			return 1;
		}

		else {
			System.out.println("$$TARGET$$");
			System.out.println("$$P: 11");
			return 0;
		}
	}

	public static void main(String[] argv) {
		
//		boolean a = true;
//		if (a) {
		
		 boolean b = Main.readBool(false);
		 Main.MakeSymbolicBool(b);
//		} 
//		else 
//		{
//			b = CATG.readBool(false);
//		}

		is_upward_preferred(b);
	}

}