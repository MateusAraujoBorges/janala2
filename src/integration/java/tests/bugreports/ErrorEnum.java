package tests.bugreports;


public class ErrorEnum {
	  public enum BasicBlockClass {
		    BB_NONHEADER;   
		  }
	
	  public static void main(String[] args) {
		 
		/* works:
		 *  BasicBlockClass type;
		  type = BasicBlockClass.BB_NONHEADER;*/

		  
		  BasicBlockClass type []= new BasicBlockClass[1];
		  type[0] = BasicBlockClass.BB_NONHEADER;
	  	  }
}