package tests.casestudies.sortedlist;

import java.util.HashSet;
import java.util.Set;

public class SortedList {
	
   Node header;

   static class Node {
       Node next;
       int elem;
   }

   boolean repOk() {
       if (!acyclic()) return false;
       if (!sorted()) return false;
       return true;
   }

   boolean unique() {
       // assume: ayclic()
       Node curr = header;
       while (curr != null) {
           if (curr.next != null) {
               if (curr.elem == curr.next.elem) return false;
           }
           curr=curr.next;
       }
       return true;
   }

   boolean sorted() {
       // assume: ayclic()
       Node curr = header;
       while (curr != null) {
           if (curr.next != null) {
               if (curr.elem > curr.next.elem) return false;
           }
           curr = curr.next;
       }
       return true;
   }

   boolean checkMin() {
       // assume: ayclic()
       Node curr = header;
       if(curr==null){return true;}
       
       int min = curr.elem;
       while (curr != null) {
           if (min>curr.elem){return false;}
           curr = curr.next;
       }
       return true;
   }
   
   boolean checkMax() {
       // assume: ayclic()
       Node curr = header;
       if(curr==null){return true;}
       
       int max = curr.elem;
       while (curr != null) {
           if (max<curr.elem){max = curr.elem;}
           if(curr.next==null){
        	   if(curr.elem==max){
        		   return true;
        	   }else{
        		   return false;
        	   }
           }
           curr = curr.next;
       }
       return true;
   }
   
   boolean acyclic() {
       Set<Node> visited = new HashSet<Node>();
       Node curr = header;
       while (curr != null) {
           if (!visited.add(curr)) return false;
           curr = curr.next;
       }
       return true;
   }

   void add(int x) {
       // precondition: repOk()
       // postcondition: repOk() and x is inserted in order (according to"<=")

	   if(header==null){
	 		  header = new Node();
	 		  header.elem = x;
	 		  header.next = null;
		   }else if(x <= header.elem){
			   Node n = new Node();
			   n.elem = x;
			   n.next = header;
			   header = n;
		   }else{
			   Node curr = header;
			   while(curr.next!=null && x > curr.next.elem){
				   curr = curr.next;
			   }
			   
			   if(curr.next==null){
				   Node n = new Node();
				   n.elem = x;
				   n.next = null;
				   curr.next = n;
			   }else{
				   Node n = new Node();
				   n.elem = x;
				   n.next = curr.next;
				   curr.next = n;
			   }
		   }
   }
   
   public static void main(String[] a) {
	 	 SortedList sl = new SortedList();
	 	 sl.driver(sl, 1, 3, 2, 1, 1);
   }
   
   public  void driver(SortedList sl, int x1, int x2, int x3, int x4, int x5){
       sl.add(x1);
       sl.add(x2);
       sl.add(x3);
       sl.add(x4);
       sl.add(x5);	   
	   
       assert sl.acyclic(); 
       assert sl.checkMin();
       assert sl.checkMax();
       assert sl.sorted(); 
       assert x1 == x4 ? !sl.unique() : true; 
   }   
}