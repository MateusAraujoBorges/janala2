package tests.casestudies.tcas;

import janala.Main;

public class Driver {

	public static void main(String[] args) {
		TCAS_v0 tcas = new TCAS_v0();
		int cvs = Main.readInt(0);
		Main.MakeSymbolic(cvs);
		boolean hc = Main.readBool(false);
		Main.MakeSymbolic(hc);
		boolean ttrv = Main.readBool(false);
		Main.MakeSymbolic(ttrv);
		
		int ota = Main.readInt(0);
		Main.MakeSymbolic(ota);
		
		int otar = Main.readInt(0);
		Main.MakeSymbolic(otar);
		
		int otTa = Main.readInt(0);
		Main.MakeSymbolic(otTa);  
		
		int alv = Main.readInt(0);
		Main.MakeSymbolic(alv);
		
		int upS = Main.readInt(0);
		Main.MakeSymbolic(upS);
		
		int dS = Main.readInt(0);
		Main.MakeSymbolic(dS);
		
		int oRAC = Main.readInt(0);
		Main.MakeSymbolic(oRAC);
		
		int oc = Main.readInt(0);
		Main.MakeSymbolic(oc); 
		
		boolean ci = Main.readBool(false);
		Main.MakeSymbolic(ci);
		
		int result = tcas.startTcas(cvs, hc, ttrv, ota, otar, otTa, alv, upS, dS, oRAC, oc, ci);
		System.out.println("result: " + result);
	
	}
}
