/*
 * Problem3.java
 * Author: Poorn Pragya
 * This program takes in multiple graphs with same vertex set and different edge set 
 * and produces optimal path with minimal cost in polynomial time using dynamic programming technique.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Problem3 {
	
	//Represents set of input graph
	ArrayList<Graph> listOfGraphs;
	//Represents value infinity
	int infinity;
	//Represents number of vertices
	int V;

	/*
	 * The constructor is used to initialize V and infinity
	 */
	public Problem3(String filename) throws FileNotFoundException {

		this.infinity = 32767;
		listOfGraphs = new ArrayList<Graph>();
		File f = new File(filename);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
		String elements[];
		Set<Integer> vertices = new TreeSet<Integer>();

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.matches("^$"))
				break;
			elements = line.split(" ");
			vertices.add(Integer.parseInt(elements[0]));
			vertices.add(Integer.parseInt(elements[1]));
		}
		V = vertices.size();
	}

	/*
	 * The function is used to create list of graph from input file and adds them to listOfGraph Arraylist. 
	 */
	void createAllGraphs(String filename) throws FileNotFoundException {
		File f = new File(filename);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
		Graph G = new Graph(V);
		
		while (sc.hasNextLine()) {

			String line = sc.nextLine();
			// Checking for a blank line, represents end of current graph and beginning of next graph
			if (line.matches("^$")) {
				listOfGraphs.add(G);
				G = new Graph(V);
				continue;
			}
			
			String element[] = line.split(" ");
			G.addEdge(Integer.parseInt(element[0]), Integer.parseInt(element[1]));
		}

	}

/*
 * This function is used print all the input graphs
 */
	void printAllGraphs() {
		for (Graph G1 : listOfGraphs) {
			System.out.println();
			G1.printAdj();
		}
	}

	/*
	 * This method is used to reverse a string
	 */
	String stringReverse(String s) {
		String rev = "";
		for (int i = s.length() - 1; i >= 0; i--)
			rev = rev + s.charAt(i);
		return rev;
	}

	/*
	 * This method is used to return the shortest path and length of path 
	 * using BFS technique
	 */
	shortestPath BFS(Graph G, int s, int t) {
		
		boolean visited[] = new boolean[V];
		for (int i = 0; i < V; i++)
			visited[i] = false;
		int prev[] = new int[V];
		prev[s] = -1;
		int flag = 0;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		visited[s] = true;
		queue.addLast(s);

		outer: while (!queue.isEmpty()) {

			s = queue.getFirst();
			queue.removeFirst();

			for (Integer i : G.adj[s]) {
				if (!visited[i]) {
					visited[i] = true;
					queue.addLast(i);
					prev[i] = s;
					if (i == t) {
						flag = 1;
						break outer;
					}

				}
			}
		}
		if (flag == 1) {
			int j = 0;
			Integer i = t;
			ArrayList<Integer> rev_path1 = new ArrayList<Integer>();
			rev_path1.add(i);
			while (prev[i] != -1) {
				j++;
				i = prev[i];
				rev_path1.add(i);
			}
			
			
			String rev_path2="";
			for(int k=rev_path1.size()-1;k>=0;k--)
				rev_path2=rev_path2+rev_path1.get(k)+" ";
				
			return new shortestPath(rev_path2, j);
		} else
			return new shortestPath(null, infinity);
	}

	/*
	 * This method returns the common graph between Graph G1 and Graph G2
	 */
	Graph commonEdgeGraph(Graph G1, Graph G2) {

		int[][] adjMatrix1 = new int[V][V];
		int[][] adjMatrix2 = new int[V][V];
		int[][] adjMatrix3 = new int[V][V];

		for (int i = 0; i < V; i++)
			for (Integer j : G1.adj[i])
				adjMatrix1[i][j] = 1;

		for (int i = 0; i < V; i++)
			for (Integer j : G2.adj[i])
				adjMatrix2[i][j] = 1;

		for (int i = 0; i < V; i++)
			for (int j = 0; j < V; j++)
				if (adjMatrix1[i][j] == 1 && adjMatrix2[i][j] == 1)
					adjMatrix3[i][j] = 1;

		Graph G3 = new Graph(V);
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				if (adjMatrix3[i][j] == 1) {
					G3.addEdge(i, j);
				}
			}
		}
		return (G3);
	}

	/*
	 * This methods calculates common path between graphs between index i and index j in
	 * losOfGraph 
	 */
	Graph commonEdgeGraph_ij(int i, int j) {

		if (i == j)
			return listOfGraphs.get(i);

		Graph G = commonEdgeGraph(this.listOfGraphs.get(i),
				this.listOfGraphs.get(i + 1));
		for (int k = i + 2; k <= j; k++) {
			G = commonEdgeGraph(G, this.listOfGraphs.get(k));
		}
		return G;
	}

	/*
	 * This method is used to return min of v1 and v2
	 */
	int min(int v1, int v2) {
		return v1 < v2 ? v1 : v2;
	}

	/*
	 * This method is used to calculate the Optimal solution using the recurrance relation
	 * OPT_SOL [i]= min {(i+1) * shortestCommonPath (0,i) + min {OPT_SOL [j] + (i-j) * shortestCommonPath (j+1,i) + K} 
	 * Where, 0<=i<(number of graphs) and 0<=j<i.  
	 */
	void recurranceRelation(int s, int t, int K) {

		int OPT_SOL[] = new int[this.listOfGraphs.size()];
		String path[] = new String[listOfGraphs.size()];

		for (int i = 0; i < this.listOfGraphs.size(); i++) {

			TreeSet<Integer> Min = new TreeSet<Integer>();
			int min = infinity;

			for (int j = 0; j < i; j++) {

				Graph G = commonEdgeGraph_ij(j + 1, i);
				shortestPath shortestCommonPath_ij = BFS(G, s, t);
				int v = OPT_SOL[j] + (i - j) * shortestCommonPath_ij.dist + K;

				if (v < min) {
					path[i] = shortestCommonPath_ij.path;
					min = v;
				}
				Min.add(v);
			}

			Graph G1 = commonEdgeGraph_ij(0, i);
			shortestPath shortestCommonPath_0i = BFS(G1, s, t);

			if (Min.size() == 0) {
				OPT_SOL[i] = (i + 1) * shortestCommonPath_0i.dist;
				path[i] = shortestCommonPath_0i.path;
			} else {
				OPT_SOL[i] = min((i + 1) * shortestCommonPath_0i.dist,
						Min.first());
				if ((i + 1) * shortestCommonPath_0i.dist < min) {
					for (int m = 0; m <= i; m++)
						path[m] = shortestCommonPath_0i.path;
					min = (i + 1) * shortestCommonPath_0i.dist;
				}
			}
		}

		System.out.println("Optimal Cost: "+ OPT_SOL[OPT_SOL.length-1]);
		System.out.println("Optimal path-->");
		for (int i = 0; i < OPT_SOL.length; i++) {
			System.out.println(path[i] + " ");
			
		}
	}

	/*
	 * Main method
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {

		if (args.length != 1) {
			System.out
					.println("Enter filename to read list of graphs, each graph separated by a blank line");
			System.exit(1);
		}

		Problem3 obj = new Problem3(args[0]);
		obj.createAllGraphs(args[0]);
		//System.out.println("Entered Graph-->");
		//obj.printAllGraphs();
		
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Starting vertex in the entered graph: ");
		int s = sc.nextInt();
		System.out.println("Enter Ending vertex in the entered graph: ");
		int t = sc.nextInt();
		System.out.println("Enter Cost for path change(K): ");
		int K = sc.nextInt();
		

		System.out.println();
		System.out.println("Solution for part 1--->");
		int x=obj.listOfGraphs.size()-1;
		shortestPath p = obj.BFS(
				obj.commonEdgeGraph_ij(0, x ), s, t);
		System.out.println("Common Path Cost: "+p.dist);
		System.out.println("Common Path: "+p.path);
		

		System.out.println();
		System.out.println("Solution for part 2--->");
		obj.recurranceRelation(s, t, K);

	}

}
