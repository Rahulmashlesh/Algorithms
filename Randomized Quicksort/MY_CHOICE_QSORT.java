import java.util.Random;
/*
 * Author : Poorn Pragya
 * Created on September 25, 2014
 * This program is used to sort a list of numbers using Randomized Quick Sort Algorithm 
 * in average case O(NlogN) time
 */
public class MY_CHOICE_QSORT {

	// Array to be sorted
	float s[];
	long n;

	public MY_CHOICE_QSORT(int n) {
		this.n = n;
		s = new float[n];
	}

	/*
	 * Fill method is used to generate random data sets
	 */
	void fill() {
		Random r = new Random();
		for (int i = 0; i < n; i++) {
			s[i] =  r.nextInt(500000);
		}
	}

	/*
	 * Partition function is used to get the partition index
	 */
	int PARTITION(int start, int end) {
		int partition_index = start;
		for (int i = start; i < end; i++) {
			if (s[i] <= s[end]) {
				float temp = s[partition_index];
				s[partition_index] = s[i];
				s[i] = temp;
				partition_index++;
			}
		}
		float temp = s[partition_index];
		s[partition_index] = s[end];
		s[end] = temp;
		return partition_index;

	}

	/*
	 * Print function prints the contents of the array
	 */
	void print() {
		for (int i = 0; i < n; i++)
			System.out.print(s[i] + "  ");
	}

	/*
	 * MY_CHOICE_QUICKSORT algorithm is main algorithm for Randomized Quick sort
	 */
	void MY_CHOICE_QUICKSORT(int start, int end) {

		if (start < end) {
			int partition_index = this.RANDOMIZED_PARTITION(start, end);
			this.MY_CHOICE_QUICKSORT(start, partition_index - 1);
			this.MY_CHOICE_QUICKSORT(partition_index + 1, end);
		}
	}

	/*
	 * RANDOMIZED_PARTITION is used to randomize the pivot to implement 
	 * Randomized Quick Sort
	 */
	int RANDOMIZED_PARTITION(int start, int end) {
		Random r =new Random();
		int i=r.nextInt(end-start)+start;
		float temp=s[end];
		s[end]=s[i];
		s[i]=temp;
		return PARTITION(start, end);	
	}
	
	/*
	 * PoisonDistributionDataSets is used to generate data sets using poision distribution function
	 */
	public int PoisonDistributionDataSets(int lambda) {

		float temp = (float) Math.exp(-lambda);
		int k = 0;
		double p = 1;
		do {
			p++;
			double rand = Math.random();
			p = p * rand;
		} while (p > temp);

		return k - 1;
	}
	

	public static void main(String[] args) {
		
		int n=args.length;
		MY_CHOICE_QSORT qsort = new MY_CHOICE_QSORT(n);
		for(int i=0;i<args.length;i++)
			qsort.s[i]=Float.parseFloat(args[i]);
		
		//qsort.fill();
		
		System.out.println("Random list--->");
		qsort.print();
		
		//Record the start time
		long start_time=System.currentTimeMillis();
		qsort.MY_CHOICE_QUICKSORT(0, n-1);
		long end_time=System.currentTimeMillis();
		//Record the end time
		
		System.out.println();
		System.out.println("Sorted List--->");
		qsort.print();
		System.out.println();
		
		System.out.println("Total time required to complete the algorithm="+ (end_time-start_time) + " millisec");

}
}
