package tests.casestudies.etg;


//import gov.nasa.jpf.symbc.Symbolic;
//import gov.nasa.jpf.vm.Verify;

public class WBS_V0 {

	//Internal state
//	 @Symbolic("true")
	private int WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE;
//	 @Symbolic("true")
	private int WBS_Node_WBS_BSCU_rlt_PRE1;
//	 @Symbolic("true")
	private int WBS_Node_WBS_rlt_PRE2;

//symbolic.method= etg.WBS.update(sym#sym#sym)	
	
	//Outputs
	private int Nor_Pressure;
	private int Alt_Pressure;
	private int Sys_Mode;

	public WBS_V0() {
		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = 0;
		WBS_Node_WBS_BSCU_rlt_PRE1 = 0;
		WBS_Node_WBS_rlt_PRE2 = 100;
		Nor_Pressure = 0;
		Alt_Pressure = 0;
		Sys_Mode = 0;
	}


	public boolean daikon_pre(int arg0, boolean arg1, boolean arg2){
		 return (this.WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE == this.WBS_Node_WBS_BSCU_rlt_PRE1)
 			 && (this.WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE == this.Nor_Pressure)
 			 && (arg1 == arg2)
 			 && (this.WBS_Node_WBS_rlt_PRE2 == 100)
 			 && (arg1 == false)
 			 && (this.WBS_Node_WBS_rlt_PRE2 >= arg0);
	}

	public void update(int PedalPos, boolean AutoBrake, boolean Skid) {
		/** automated generated pre-statements begin **/
		int arg0 = PedalPos;
		boolean arg1 = AutoBrake;
		boolean arg2 = Skid;
		int prevar0 = this.WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE;
		int prevar1 = this.WBS_Node_WBS_BSCU_rlt_PRE1;
		int prevar2 = this.Nor_Pressure;
		int prevar3 = this.Alt_Pressure;
		int prevar4 = this.Sys_Mode;
		int prevar5 = this.WBS_Node_WBS_rlt_PRE2;
		/** automated generated pre-statements end **/ 


		if(!daikon_pre(arg0, arg1, arg2)){
//			Verify.ignoreIf(true);
			throw new RuntimeException("Ignore branches that violate the invariant");
		}
		int WBS_Node_WBS_AS_MeterValve_Switch;
		int WBS_Node_WBS_AccumulatorValve_Switch;
		int WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch;
		boolean WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator;
		int WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1;
		int WBS_Node_WBS_BSCU_Command_Switch;
		boolean WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6;
		int WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay;
		int WBS_Node_WBS_BSCU_Switch2;
		int WBS_Node_WBS_BSCU_Switch3;
		int WBS_Node_WBS_BSCU_Unit_Delay1;
		int WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
		int WBS_Node_WBS_SelectorValve_Switch;
		int WBS_Node_WBS_SelectorValve_Switch1;
		int WBS_Node_WBS_Unit_Delay2;

	   WBS_Node_WBS_Unit_Delay2 = WBS_Node_WBS_rlt_PRE2;
	   WBS_Node_WBS_BSCU_Unit_Delay1 = WBS_Node_WBS_BSCU_rlt_PRE1;
	   WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay = WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE;

	   WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator = (WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay == 0);

	   if ((PedalPos == 0)) {
		      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
		   } else {
			   if ((PedalPos == 1)) {
			      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 1;
			   }  else {
				   if ((PedalPos == 2)) {
				      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 2;
				   } else {
					   if ((PedalPos == 3)) {
					      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 3;
					   } else {
						   if ((PedalPos == 4)) {
						      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 4;
						   }  else {
						      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
						   }
					   }
				   }
			   }
		   }

	   if ((AutoBrake &&
		         WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator)) {
		      WBS_Node_WBS_BSCU_Command_Switch = 1;
		   }  else {
		      WBS_Node_WBS_BSCU_Command_Switch = 0;
		   }

	   WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6 = ((((!(WBS_Node_WBS_BSCU_Unit_Delay1 == 0)) &&
	         (WBS_Node_WBS_Unit_Delay2 <= 0)) &&
	         WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator) ||
	         (!WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator));

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      if (Skid)
	         WBS_Node_WBS_BSCU_Switch3 = 0;
	      else
	         WBS_Node_WBS_BSCU_Switch3 = 4;
	   }
	   else {
	      WBS_Node_WBS_BSCU_Switch3 = 4;
	    }

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 0;
	   }  else {
	      WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 5;
	    }

	   if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1)) {
	      WBS_Node_WBS_SelectorValve_Switch1 = 0;
	   }
	   else {
	      WBS_Node_WBS_SelectorValve_Switch1 = 5;
	   }

	   if ((!WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6)) {
	      WBS_Node_WBS_AccumulatorValve_Switch = 0;
	   }  else {
		   if ((WBS_Node_WBS_SelectorValve_Switch1 >= 1)) {
		      WBS_Node_WBS_AccumulatorValve_Switch = WBS_Node_WBS_SelectorValve_Switch1;
		   }
		   else {
		      WBS_Node_WBS_AccumulatorValve_Switch = 5;
		   }
	   }

	   if ((WBS_Node_WBS_BSCU_Switch3 == 0)) {
	      WBS_Node_WBS_AS_MeterValve_Switch = 0;
	   }  else {
		   if ((WBS_Node_WBS_BSCU_Switch3 == 1))  {
		      WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 4);
		   }  else {
			   if ((WBS_Node_WBS_BSCU_Switch3 == 2))  {
			      WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 2);
			   }  else {
				   if ((WBS_Node_WBS_BSCU_Switch3 == 3)) {
				      WBS_Node_WBS_AS_MeterValve_Switch = ((WBS_Node_WBS_AccumulatorValve_Switch / 4) * 3);
				   }  else {
					   if ((WBS_Node_WBS_BSCU_Switch3 == 4)) {
					      WBS_Node_WBS_AS_MeterValve_Switch = WBS_Node_WBS_AccumulatorValve_Switch;
					   }  else {
					      WBS_Node_WBS_AS_MeterValve_Switch = 0;
					   }
				   }
			   }
		   }
	   }

	   if (Skid) {
	      WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = 0;
	   }  else {
	      WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = (WBS_Node_WBS_BSCU_Command_Switch+WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1);
	   }

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      Sys_Mode = 1;
	   }  else {
	      Sys_Mode = 0;
	   }

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      WBS_Node_WBS_BSCU_Switch2 = 0;
	   }  else {
		   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 0) &&
		         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 1))) {
		      WBS_Node_WBS_BSCU_Switch2 = 0;
		   } else {
			   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 1) &&
			         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 2)))  {
			      WBS_Node_WBS_BSCU_Switch2 = 1;
			   }  else {
				   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 2) &&
				         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 3))) {
				      WBS_Node_WBS_BSCU_Switch2 = 2;
				   } else {
					   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 3) &&
					         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 4)))  {
					      WBS_Node_WBS_BSCU_Switch2 = 3;
					   } else {
					      WBS_Node_WBS_BSCU_Switch2 = 4;
					   }
				   }
			   }
		   }
	   }

	   if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1))  {
	      WBS_Node_WBS_SelectorValve_Switch = WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
	   }  else {
	      WBS_Node_WBS_SelectorValve_Switch = 0;
	   }

	   if ((WBS_Node_WBS_BSCU_Switch2 == 0)) {
	      Nor_Pressure = 0;
	   }  else {
		   if ((WBS_Node_WBS_BSCU_Switch2 == 1)) {
		      Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 4);
		   }  else {
			   if ((WBS_Node_WBS_BSCU_Switch2 == 2)) {
			      Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 2);
			   }  else {
				   if ((WBS_Node_WBS_BSCU_Switch2 == 3)) {
				      Nor_Pressure = ((WBS_Node_WBS_SelectorValve_Switch / 4) * 3);
				   } else {
					   if ((WBS_Node_WBS_BSCU_Switch2 == 4)) {
					      Nor_Pressure = WBS_Node_WBS_SelectorValve_Switch;
					   } else {
					      Nor_Pressure = 0;
					   }
				   }
			   }
		   }
	   }

	   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 0)) {
	      Alt_Pressure = 0;
	   }  else {
		   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 1)) {
		      Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 4);
		   }  else {
			   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 2)) {
			      Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 2);
			   } else {
				   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 3)) {
				      Alt_Pressure = ((WBS_Node_WBS_AS_MeterValve_Switch / 4) * 3);
				   } else {
					   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 4)) {
					      Alt_Pressure = WBS_Node_WBS_AS_MeterValve_Switch;
					   } else {
					      Alt_Pressure = 0;
					   }
				   }
			   }
		   }
	   }

	   WBS_Node_WBS_rlt_PRE2 = Nor_Pressure;

	   WBS_Node_WBS_BSCU_rlt_PRE1 = WBS_Node_WBS_BSCU_Switch2;

	   WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = Sys_Mode;

	   assert (this.WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE == prevar0);
	   
	}


	public static void main(String[] args) {
		WBS_V0 wBS = new WBS_V0();

		if(args.length!=3){
		    System.out.println("wrong arguments");
		    wBS.update(0, true, true);
		    return;
		}

		int PedalPos = Integer.valueOf(args[0]);
		boolean AutoBrake = Boolean.valueOf(args[1]);
		boolean Skid = Boolean.valueOf(args[2]);

		wBS.update(PedalPos, AutoBrake, Skid);
	}

	// public static void main(String[] args) {
	// 	WBS wBS = new WBS();

	// 	int PedalPos = 0;
	// 	boolean AutoBrake = true;
	// 	boolean Skid = true;

	// 	wBS.update(PedalPos, AutoBrake, Skid);
	// }
}