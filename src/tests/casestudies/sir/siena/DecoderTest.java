package tests.casestudies.sir.siena;

import janala.Main;

public class DecoderTest {

	public static void main(String[] args) {
		final int N = 5;
		
		byte[] input = new byte[N];
	    for (int i = 0; i < N; i++) {
	        int x = Main.readInt(0);
	        Main.MakeSymbolic(x, -128, 127);
	        input[i] = (byte) x;
	    }
	    
	    
	    try {
			SENP.decode(input);
		} catch (SENPInvalidFormat e) {
			System.out.println("Invalid format: " + e.getMessage());
		}
	}
}
