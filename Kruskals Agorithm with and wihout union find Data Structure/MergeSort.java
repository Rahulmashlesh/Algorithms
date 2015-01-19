/*
 * MergeSort.java
 * Author: Poorn Pragya
 * Created on: Oct 26th, 2014
 * This class is used in Kruskal's algorithm to sort the edges in ascending order of their weight
 */
public class MergeSort {


	void MERGE_SORT(Edge a[]) {
		int n = a.length;
		if (n < 2)
			return;
		int mid = n / 2;
		Edge[] left = new Edge[mid];
		Edge[] right = new Edge[n - mid];
		for (int i = 0; i < mid; i++)
			left[i] = a[i];
		for (int i = mid; i < n; i++)
			right[i - mid] = a[i];
		MERGE_SORT(left);
		MERGE_SORT(right);
		MERGE(left, right, a);

	}

	/*
	 * This function is a part of merge sort algorithm
	 */
	void MERGE(Edge[] left, Edge[] right, Edge[] a) {
		int nL = left.length;
		int nR = right.length;
		int i = 0, j = 0, k = 0;
		float left_data, right_data;
		while (i < nL && j < nR) {
				left_data = left[i].w;
				right_data = right[j].w;
			if (left_data < right_data) {
				a[k] = left[i];
				i++;
			} else {
				a[k] = right[j];
				j++;
			}
			k++;
		}
		while (i < nL)
			a[k++] = left[i++];
		while (j < nR)
			a[k++] = right[j++];
	}
}
