/*
 * Heap.java
 * Author: Poorn Pragya
 * Created on: Oct 26th, 2014
 * This class creates a minimum heap along with its operations
 */


public class Heap {

	// Data members to hold the heap
	private CharSet heap[];
	private int size;

	/*
	 * Constructor to initialize the heap
	 */
	public Heap(CharSet data[]) {
		heap = new CharSet[2 * data.length];
		for (int i = 0; i < data.length; i++)
			heap[i] = data[i];
		this.size = data.length;
		BuildHeap();
	}

	/*
	 * This function builds an min heap in O(n) time
	 */
	private void BuildHeap() {
		int n=size-1;
		for (int i = (n-1) / 2; i >= 0; i--)
			Heapify(i);
	}

	/*
	 * This function heapifies a heap that doesn't follow heap property in O(log n) time
	 */
	public void Heapify(int i) {

		int left = 2 * i+1;
		int right = 2 * i +2;
		int minimum=i;

		if (left < size && heap[left].freq < heap[minimum].freq) 
			minimum = left;

		if (right < size && heap[right].freq < heap[minimum].freq) {
			minimum = right;
		}

		if (minimum != i) {
			exchange(i, minimum);
			Heapify(minimum);
		}

	}

	/*
	 * This is swap function to exchange the  ith and jth elements
	 */
	private void exchange(int i, int j) {
		CharSet t = heap[i];
		heap[i] = heap[j];
		heap[j] = t;
	}

	/*
	 * This function extracts the top most element of heap in O(log n) time
	 */
	public CharSet ExtractMin() {
		if(size<0) {
			return  null;
		}
		CharSet Min = heap[0];
		heap[0] = heap[size-1];
		size = size - 1;
		Heapify(0);
		return Min;
	}

	/*
	 * This function is used to insert an element in the heap in O(log n) time
	 */
	public void insert(CharSet key) {
		
		size=size+1;
		int i=size-1;
		while (i>0 && key.freq < heap[(i-1)/2].freq) {
			heap[i]=heap[(i-1)/2];
			i=(i-1)/2;
		}
		heap[i]=key;
		
	}
	
	/*
	 * This function returns the current size size of heap 
	 */
	int size() {
		return size;
	}
	
	/*
	 * This dunction is used to print the content of heap in BFS manner
	 */
	void printHeap() {
		for(int i=0;i<size;i++)
			System.out.print(heap[i].c+":"+heap[i].freq);
	}
	
}
