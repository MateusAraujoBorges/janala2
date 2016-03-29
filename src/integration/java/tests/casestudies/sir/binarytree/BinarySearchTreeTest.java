package tests.casestudies.sir.binarytree;



import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import janala.Main;
import junit.framework.TestCase;
//import edu.ksu.cis.projects.bogor.kiasan.test.Util;
//import edu.ksu.cis.projects.bogor.kiasan.translator.Method;
//import edu.ksu.cis.projects.bogor.kiasan.util.VerySimpleFormatter;
import tests.SimpleObject;

public class BinarySearchTreeTest extends TestCase {
   public void initLogging() throws Exception{
        Handler fh = new FileHandler("cvcl.log");
        fh.setFormatter(new SimpleFormatter());
        Logger.getLogger("edu.ksu.cis.santos.util.theoremprover").addHandler(fh);
        Logger.getLogger("edu.ksu.cis.santos.util.theoremprover").setLevel(Level.ALL);
        Handler fhvm = new FileHandler("vmtrace.log");
        fhvm.setFormatter(new SimpleFormatter());
        Logger.getLogger("edu.ksu.cis.projects.bogor.kiasan").addHandler(fhvm);
        Logger.getLogger("edu.ksu.cis.projects.bogor.kiasan").setLevel(Level.ALL);
    }
    public void testInsert() throws Exception {
        //initLogging();
//        Util
//            //.testBogorVM(
//            		.testForwardKUnit(
//                "intkey.BinarySearchTree",                
//                "kunitforward.bogor-conf",
//                //"yices-kunit.bogor-conf",
//                new Method("intkey.BinarySearchTree",
//                        "insert",
//                        "(I)V",
//                        false),true,false);                
    }   
// public void testRemove() throws Exception {
////initLogging();
//Util
//   .testBogorVM(
//       "intkey.BinarySearchTree",                
//       "kunit.bogor-conf",
//       new Method("intkey.BinarySearchTree",
//               "remove",
//               "(I)V",
//               false),true,false);                
//}   

// public void testFind() throws Exception {
//	//initLogging();
//	Util
//	   .testKUnit(
//	       "kunit.bogor-conf",
//	       new Method("intkey.BinarySearchTree",
//	               "find",
//	               "(I)I",
//	               false),true,false);                
//	}   
//   public void testFindMax() throws Exception {
////	 initLogging();
//	 Util
//	    .testBogorVM(
//	        "intkey.BinarySearchTree",                
//	        "kunit.bogor-conf",
//	        new Method("intkey.BinarySearchTree",
//	                "findMax",
//	                "()I",
//	                false),true,false);                
//	 }   
//   public void testFindMin() throws Exception {
////		 initLogging();
//		 Util
//		    .testBogorVM(
//		        "intkey.BinarySearchTree",                
//		        "kunit.bogor-conf",
//		        new Method("intkey.BinarySearchTree",
//		                "findMin",
//		                "()I",
//		                false),true,false);                
//		 }   
    
	public static void main(String[] args) {
		final int N = 5;
		BinarySearchTree tree = new BinarySearchTree();
		
	    for (int i = 0; i < N; i++) {
	        int x = Main.readInt(0);
	        Main.MakeSymbolic(x, -100, 100);
	        tree.insert(x);
	    }
	    System.out.println(tree.findMax());
	    System.out.println(tree.findMin());
	}
}
