package tests.casestudies.heap;

public class HeapArrayV0 { // max-heap
   
    int[] heap = new int[10]; // default max size
    int size = 0; // actual number of elements in the heap

    public HeapArrayV0(){
    	for(int i=0; i<heap.length; i++){
    		heap[i] = -1;
    	}
    }
    
    void add(int x) {
        // implement this method to build a max-heap
        // maybe disallow duplicates? use a standard implementation
    	if(x==-1){
    		return; // special node, treated as null
    	}
    	
    	// grow heap if needed
    	if(size >= heap.length - 1){
    		heap = this.resize();
    	}
    	
    	// place element into heap at bottom
    	int index = size;
    	heap[index] = x;
    	size++;
    	bubbleUp();
    }
    
    int[] resize(){
    	int[] array = new int[heap.length*2];
    	
    	for(int i=0; i<array.length; i++){
    		array[i] = -1;
    	}
    	
    	for(int i=0; i<size; i++){
    		array[i] = heap[i];
    	}
    	return array;
    }
    
    void bubbleUp(){
    	int index = this.size-1;
    	while(hasParent(index) && parent(index) < heap[index]){
    		// out of order, swap
    		swap(index, parentIndex(index));
    		index = parentIndex(index);
    	}
    }
    
    boolean hasParent(int i){
    	return i >= 1;
    }
 
    int parent(int i){
    	return heap[parentIndex(i)];
    }
    
    int parentIndex(int i){
    	return (i-1)/2;
    }
    
    void swap(int index1, int index2){
    	int tmp = heap[index1];
    	heap[index1] = heap[index2];
    	heap[index2] = tmp;	
    }
    
    boolean maxHeap() { // see Korat repOk for HeapArray [ISSTA 2002 paper]
        // ...
        
    	// checks that array is non-null
    	if (heap == null) return false;
    	// checks that size is within array bounds
    	if (size < 0 || size > heap.length)
    		return false;
    	for (int i = 0; i < size; i++) {
    	// checks that elements are non-null
    		if (heap[i] == -1) return false;
    	// checks that array is heapified
    		if (i > 0 && heap[i] > parent(i))
    			return false;
    	}
    	// checks that non-heap elements are null
    	for (int i = size; i < heap.length; i++)
    		if (heap[i] != -1) return false;
    	return true;
    }
    
    boolean maxIsFirst() {
        // assume: heap.length > 0
        int max = heap[0];
        for(int i=0; i<size; i++){
            if (heap[i] > max) return false;        	
        }
        return true;
    }
    
    boolean sortedDescending() {
        for (int i = 0; i < size; i++) {
            if (heap[i] > heap[i + 1]) return false;
        }
        return true;
    }
    
    public static void main(String[] a) {
    	HeapArrayV0 ha = new HeapArrayV0();
	 	ha.driver(ha, 0, 0, 0, 0, 0);
  }
  
  public  void driver(HeapArrayV0 ha, int x1, int x2, int x3, int x4, int x5){
      ha.add(x1);
      ha.add(x2);
      ha.add(x3);
      ha.add(x4);
      ha.add(x5);
     
      assert ha.maxIsFirst(); 
  }
}