package tests.casestudies.tcas;

import janala.Main;

public class Driver {

	public static void main(String[] args) {
		TCAS_v0 tcas = new TCAS_v0();
		
		int hc_hack = Main.readInt(0);
		Main.MakeSymbolic(hc_hack,0,1);
		boolean hc = hc_hack != 0;
		
		int ttrv_hack = Main.readInt(0);
		Main.MakeSymbolic(ttrv_hack,0,1);
		boolean ttrv = ttrv_hack != 0;

		int ci_hack = Main.readInt(0);
		Main.MakeSymbolic(ci_hack,0,1);
		boolean ci = ci_hack != 0;

		int cvs = Main.readInt(0);
		Main.MakeSymbolic(cvs,-10000,10000);
		
		int ota = Main.readInt(0);
		Main.MakeSymbolic(ota,-10000,10000);
		
		int otar = Main.readInt(0);
		Main.MakeSymbolic(otar,-10000,10000);
		
		int otTa = Main.readInt(0);
		Main.MakeSymbolic(otTa,-10000,10000);  
		
		int alv = Main.readInt(0);
		Main.MakeSymbolic(alv,-10000,10000);
		
		int upS = Main.readInt(0);
		Main.MakeSymbolic(upS,-10000,10000);
		
		int dS = Main.readInt(0);
		Main.MakeSymbolic(dS,-10000,10000);
		
		int oRAC = Main.readInt(0);
		Main.MakeSymbolic(oRAC,-10000,10000);
		
		int oc = Main.readInt(0);
		Main.MakeSymbolic(oc,-10000,10000); 
		
		
		int result = tcas.startTcas(cvs, hc, ttrv, ota, otar, otTa, alv, upS, dS, oRAC, oc, ci);
		System.out.println("result: " + result);
	}
}
