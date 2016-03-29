package tests;

import catg.CATG;

public class ArraySymbolicLength {

	public static void main(String[] args) {
		int x = CATG.readInt(3);

		int[] a = new int[x];
        a[0] = 3;
        a[1] = 5;
        a[2] = 10;

        int y = CATG.readInt(0);
        
        if (a[y]==5) {
            System.out.println("Hello");
        }
    }
}
