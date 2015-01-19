import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
 * Author: Poorn Pragya
 * Created on September 25, 2014
 * This Program is used to compute the distance between 2 points in 2-D plane that are closest.
 * 
 */

public class CLOSEST_PAIR {

	/*
	 * This function initializes the algorithm by calculating the Px and Py
	 */
	POINT_PAIR CLOSEST_PAIR(final POINT p[]) {
		if (p.length < 1) {
			System.out.println("Invalid input");
			System.exit(0);
		}

		POINT px[] = new POINT[p.length];
		POINT py[] = new POINT[p.length];

		this.MERGE_SORT(p, 'x');
		for (int i = 0; i < p.length; i++)
			px[i] = p[i];

		this.MERGE_SORT(p, 'y');
		for (int i = 0; i < p.length; i++)
			py[i] = p[i];

		return CLOSEST_PAIR_REC(px, py, p.length);
	}

	/*
	 * This function computes the distance between 2 points
	 */
	float DISTANCE(POINT p1, POINT p2) {
		return (float) Math.sqrt(Math.pow((p1.x - p2.x), 2)
				+ Math.pow((p1.y - p2.y), 2));
	}

	/*
	 * This function uses divide and conquer strategy to find min distance
	 */
	POINT_PAIR CLOSEST_PAIR_REC(POINT[] px, POINT[] py, int n) {

		// using brute forece if points are less than 3
		if (px.length <= 3) {
			POINT_PAIR min_pair = new POINT_PAIR();
			min_pair.distance = Float.MAX_VALUE;
			for (int i = 0; i < px.length; i++) {
				for (int j = i + 1; j < px.length; j++) {
					if (DISTANCE(px[i], px[j]) < min_pair.distance) {
						min_pair.distance = DISTANCE(px[i], px[j]);
						min_pair.p1 = px[i];
						min_pair.p2 = px[j];
					}
				}
			}
			return min_pair;
		}

		int mid = px.length / 2;
		POINT mid_point = px[mid];

		// Constructing Qx,Qy,Rx, Ry
		POINT Qx[] = new POINT[mid];
		for (int i = 0; i < mid; i++)
			Qx[i] = px[i];
		this.MERGE_SORT(Qx, 'x');

		POINT Qy[] = new POINT[mid];
		for (int i = 0; i < mid; i++)
			Qy[i] = px[i];
		this.MERGE_SORT(Qx, 'y');

		POINT Rx[] = new POINT[px.length - mid];
		for (int i = mid; i < px.length; i++)
			Rx[i - mid] = px[i];
		this.MERGE_SORT(Rx, 'x');

		POINT Ry[] = new POINT[px.length - mid];
		for (int i = mid; i < px.length; i++)
			Ry[i - mid] = px[i];
		this.MERGE_SORT(Ry, 'y');

		// Recursion step
		POINT_PAIR q = CLOSEST_PAIR_REC(Qx, Qy, mid);
		POINT_PAIR r = CLOSEST_PAIR_REC(Rx, Ry, px.length - mid);

		// Calculating delta
		POINT_PAIR delta;
		if (q.distance < r.distance)
			delta = q;
		else
			delta = r;

		// Constructing Sy
		POINT Sy[] = new POINT[n];
		int j = 0;
		for (int i = 0; i < n; i++) { // O(n) time
			if (Math.abs(py[i].x - mid_point.x) < delta.distance) {
				Sy[j] = py[i];
				j++;
			}
		}

		// This is a proven fact that this loop runs at most 6 times. Hence,
		// O(1)
		float min = delta.distance;
		POINT_PAIR strip_min_pair = delta;
		for (int k = 0; k < j; ++k) {
			for (int m = k + 1; m < j && (Sy[m].y - Sy[k].y) < min; ++m) {
				if (DISTANCE(Sy[k], Sy[m]) < min) {
					min = DISTANCE(Sy[k], Sy[m]);
					strip_min_pair = new POINT_PAIR(Sy[k], Sy[m], min);
				}
			}
		}

		// Computing final delta
		if (delta.distance < strip_min_pair.distance)
			return delta;
		else
			return strip_min_pair;
	}

	/*
	 * This function is used by ClOSEST_PAIR algorithm to sort the list in
	 * O(N*log N) time
	 */
	void MERGE_SORT(POINT a[], char flag) {
		int n = a.length;
		if (n < 2)
			return;
		int mid = n / 2;
		POINT[] left = new POINT[mid];
		POINT[] right = new POINT[n - mid];
		for (int i = 0; i < mid; i++)
			left[i] = a[i];
		for (int i = mid; i < n; i++)
			right[i - mid] = a[i];
		MERGE_SORT(left, flag);
		MERGE_SORT(right, flag);
		MERGE(left, right, a, flag);

	}

	/*
	 * This function is a part of merge sort algorithm
	 */
	void MERGE(POINT[] left, POINT[] right, POINT[] a, char flag) {
		int nL = left.length;
		int nR = right.length;
		int i = 0, j = 0, k = 0;
		float left_data, right_data;
		while (i < nL && j < nR) {
			// Flag='x' means sort based on x coordinate
			if (flag == 'x') {
				left_data = left[i].x;
				right_data = right[j].x;
			} else {
				left_data = left[i].y;
				right_data = right[j].y;
			}
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

	/*
	 * Function to print the points
	 */
	void printPoints(POINT p[]) {

		System.out.println();
		for (int i = 0; i < p.length; i++)
			System.out.print("(" + p[i].x + "," + p[i].y + ")" + "   ");
		// System.out.print(p[i].x + " " + p[i].y + " ");
	}

	/*
	 * Function to print the point pair
	 */
	void printPointPair(POINT_PAIR p) {
		System.out.println();
		System.out.print("(" + p.p1.x + "," + p.p1.y + ")" + "   ");
		System.out.print("      ");
		System.out.print("(" + p.p2.x + "," + p.p2.y + ")" + "   ");
		System.out.println();
		System.out.print("Distance=" + p.distance);

	}

	/*
	 * Function to generate unique Randoom numbers
	 */
	ArrayList<Integer> unique_random(int n) {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			numbers.add(i + 1);
		}
		Collections.shuffle(numbers);
		return numbers;
	}

	/*
	 * This function generates datasets for the algorithm
	 */
	void fill(POINT p[], int n) {
		ArrayList<Integer> l = unique_random(n);
		Random r = new Random(n);
		for (int i = 0; i < n; i++) {
			int x = l.get(i);
			int y = r.nextInt();
			p[i] = new POINT(x, y);
		}
	}
	
	float bruteForce(POINT P[], int n)
	{
	    float min = Float.MAX_VALUE;
	    for (int i = 0; i < n; ++i)
	        for (int j = i+1; j < n; ++j)
	            if (DISTANCE(P[i], P[j]) < min)
	                min = DISTANCE(P[i], P[j]);
	    return min;
	}
	 

	// Main Method
	public static void main(String args[]) {

		//POINT a[] = new POINT[args.length / 2];
		 POINT a[] = new POINT[Integer.parseInt(args[0])];

//		int j = 0;
//		for (int i = 1; i < args.length; i += 2)
//			a[j++] = new POINT(Integer.parseInt(args[i - 1]),
//					Integer.parseInt(args[i]));

		CLOSEST_PAIR obj = new CLOSEST_PAIR();
		obj.fill(a, Integer.parseInt(args[0]));
		// obj.printPoints(a);
		long start_time1 = System.currentTimeMillis();
		System.out.println("Brute Force min="+obj.bruteForce(a, a.length));
		long end_time1 = System.currentTimeMillis();
		System.out.println();
		System.out.println("Time taken for Computation="
				+ (end_time1 - start_time1) + " milli seconds");
		
		long start_time = System.currentTimeMillis();
		POINT_PAIR p = obj.CLOSEST_PAIR(a);
		long end_time = System.currentTimeMillis();

		obj.printPointPair(p);

		System.out.println();
		System.out.println("Time taken for Computation="
				+ (end_time - start_time) + " milli seconds");

	}

}
