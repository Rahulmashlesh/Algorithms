/*
 * Kruskal.java
 * Author: Poorn Pragya
 * Created on: Oct 26th, 2014
 * This program takes set of edges of the graph as input on command line and prints the minimum spanning tree
 * using Kruskal's Algorithm with and without union find data structure
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Kruskal {

	// Represents set of edges of the graph
	Edge[] edges;
	// Represents set of vertices of the graph
	Set<Integer> vertices;

	public Kruskal(int m) {
		edges = new Edge[m];
		vertices = new TreeSet<Integer>();
	}

	// This method sets the vertices and edges data members which are given from
	// command line
	void SetVerticesEdges(String commandLineInput[]) {

		int j = 0;
		for (int i = 0; i < commandLineInput.length; i += 3) {
			int vertex1 = Integer.parseInt(commandLineInput[i]);
			int vertex2 = Integer.parseInt(commandLineInput[i + 1]);
			long w = Long.parseLong(commandLineInput[i + 2]);
			vertices.add(vertex1);
			vertices.add(vertex2);
			edges[j++] = new Edge(vertex1, vertex2, w);
		}

	}

	// This method is used to print the vertices
	void printVertices() {
		System.out.println("Set of vertices are--->");
		for (Integer i : vertices)
			System.out.print(i + " ");
		System.out.println();
	}

	// This method is used to print the edges
	void printEdges(Edge T[]) {

		System.out.println("Set of Edges in minimum spanning tree--->");
		int i;
		for (i = 0; i < T.length-1; i++)
			System.out.print("("+T[i].v1 + "," + T[i].v2 + "), ");
		System.out.print("("+T[i].v1 + "," + T[i].v2 + ")");
	}

	// This method is used to sort the edges in ascending order of weight using
	// merge sort in O(ElogV) time
	void sortEdges() {
		MergeSort msort = new MergeSort();
		msort.MERGE_SORT(edges);
	}

	// This method is used to find minimum spanning tree using Kruskal's
	// algorithm without union find.
	// Returns minimum spanning tree T
	// Takes O(V^3) time
	Edge[] KruskalAlgorithmWithoutUnionFind() {

		/* since min spanning tree will have edges=vertices-1 */
		Edge[] T = new Edge[vertices.size() - 1];
		ArrayList<ArrayList<Integer>> Vs = new ArrayList<ArrayList<Integer>>();

		// Creating initial disjoint sets
		for (Integer i : vertices) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(i);
			Vs.add(temp);
		}

		// sort the edges of E in nondecreasing order of weight
		sortEdges();

		int edge_ptr = 0, c = 0;

		// Continuing till all sets are merged. This will run O(V) times
		while (Vs.size() > 1) {

			Edge E = edges[edge_ptr++];
			// Extracting the vertices of current edge
			Integer v = E.v1;
			Integer w = E.v2;

			ArrayList<Integer> w1 = null;
			ArrayList<Integer> w2 = null;

			// Finding vertex v belongs to which set and assigning the value of
			// set to w1. This runs in O(V^2) time
			for (ArrayList<Integer> s : Vs) {
				for (Integer i : s) {
					if (i.equals(v)) {
						w1 = s;
						break;
					}
				}
			}

			// Finding vertex w belongs to which set and assigning the value of
			// set to w2. This runs in O(V^2) time
			for (ArrayList<Integer> s : Vs) {
				for (Integer i : s) {
					if (i.equals(w)) {
						w2 = s;
						break;
					}
				}
			}

			// Checking if sets w1 and w2 are not same, then union w1 and w2 in
			// VS and assign the edge to T. This takes O(1)
			if (!w1.equals(w2)) {
				Vs.remove(w1);
				Vs.remove(w2);
				w1.addAll(w2);
				Vs.add(w1);
				T[c++] = E;
			}

		}

		return T;
	}

	// This method is used to find minimum spanning tree using Kruskal's
	// algorithm with union find data structure.
	// Returns minimum spanning tree T
	// Takes O(V^2 log V) time
	public Edge[] KruskalAlgorithmWithUnionFind() {
		// since min spanning tree will have edges=vertices-1
		Edge[] T = new Edge[vertices.size() - 1];

		// Hash Table lookup is used to map vertex label to Union Find vertex
		// id.
		// Hash Table is being used for look up as look up is done O(1) time
		Map<Integer, Integer> HashTablelookup = new HashMap<Integer, Integer>();
		int id = 0;
		Object[] a = vertices.toArray();
		int b[] = new int[a.length];
		// Creating set of vertices to pass to Union-Find and initializing the
		// lookup table. Takes O(V) times
		for (int i = 0; i < a.length; i++) {
			b[i] = (Integer) a[i];
			HashTablelookup.put(b[i], id++);
		}

		UnionFind VS = new UnionFind(b);

		// Sorting list of edges using merge sort. Takes O( V^2 logV ) time
		this.sortEdges();

		int k = 0;
		// The outer loop runs till O(V^2) time
		for (int i = 0; i < edges.length; i++) {
			int vertexID1 = HashTablelookup.get(edges[i].v1);// Takes O(1) time since it is a hash table
			int vertexID2 = HashTablelookup.get(edges[i].v2);// Takes O(1) time since it is a hash table

			// Find operation takes O(log V) time
			if (VS.find(vertexID1) != VS.find(vertexID2)) {
				T[k++] = edges[i];// adding edge to minimum spanning tree.
				VS.union(vertexID1, vertexID2);// Union operation takes O(1)
												// time
			}
		}
		return T;
	}

	// This method is used to check if input graph is connected or not
	// If graph is not connected, this method provides recommendations to make
	// the graph connected
	boolean isConnected() {

		ArrayList<ArrayList<Integer>> connectedComponents = new ArrayList<ArrayList<Integer>>();
		for (Integer i : vertices) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(i);
			connectedComponents.add(temp);
		}

		int edge_ptr = 0;
		while (edge_ptr < edges.length) {
			Edge E = edges[edge_ptr++];
			Integer v = E.v1;
			Integer w = E.v2;
			ArrayList<Integer> w1 = null;
			ArrayList<Integer> w2 = null;
			for (ArrayList<Integer> s : connectedComponents) {
				if (s.contains(v)) {
					w1 = s;
					break;
				}
			}

			for (ArrayList<Integer> s : connectedComponents) {
				if (s.contains(w)) {
					w2 = s;
					break;
				}
			}

			if (!w1.equals(w2)) {
				connectedComponents.remove(w1);
				connectedComponents.remove(w2);
				w1.addAll(w2);
				connectedComponents.add(w1);
			}
		}

		if (connectedComponents.size() > 1) {
			System.out
					.println("Input Graph not connected. Kruskal's algorithm works only for connected graph");
			System.out.print("Connected components : {");
			int firstVertex[] = new int[connectedComponents.size()];
			int firstVertexCounter = 0;
			for (ArrayList<Integer> i : connectedComponents) {
				System.out.print("{ ");
				firstVertex[firstVertexCounter++] = i.get(0);
				for (Integer j : i)
					System.out.print(j + " ");
				System.out.print("} ");
			}
			System.out.print("}");
			System.out.println();
			System.out
					.println("Please add edges with weights between vertices of different connected components to connect the graph");
			System.out
					.println("For this particular input graph, you can add edges as follows to make te graph connected:");
			Random rand = new Random();
			for (int i = 0; i < firstVertex.length - 1; i++) {
				System.out.println(firstVertex[i] + " " + firstVertex[i + 1]
						+ " " + rand.nextInt(edges.length * edges.length / 2));
			}
			return false;
		} else
			return true;

	}

	// This method when invoked reads edges from file source and initializes the
	// data members
	void readfile(String path) throws FileNotFoundException {
		File f = new File(path);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
		int j = 0;
		edges = new Edge[20000];
		while (sc.hasNext()) {
			int vertex1 = Integer.parseInt(sc.next());
			int vertex2 = Integer.parseInt(sc.next());
			long w = Long.parseLong(sc.next());
			vertices.add(vertex1);
			vertices.add(vertex2);
			edges[j++] = new Edge(vertex1, vertex2, w);
		}

	}

	// This method is used to check the connectivity of each node in the graph
	void checkNodeConnectivity() {
		Map<Integer, Integer> m = new TreeMap<Integer, Integer>();
		for (int i = 0; i < edges.length; i++) {
			if (m.containsKey(edges[i].v1))
				m.put(edges[i].v1, m.get(edges[i].v1) + 1);
			else
				m.put(edges[i].v1, 1);

			if (m.containsKey(edges[i].v2))
				m.put(edges[i].v2, m.get(edges[i].v2) + 1);
			else
				m.put(edges[i].v2, 1);
		}

		System.out.println("Connectivity of Each node-->");
		for (Integer i : m.keySet()) {
			System.out.println(i + ": " + m.get(i));
		}
	}

	// Main method
	public static void main(String[] args) {

		// Checking the validity of input to be in multiple of 3 entries
		if (args.length % 3 != 0) {
			System.out
					.println("Wrong Input provided! Input to this program should be ORDERED SET of all EDGES of the graph in following format: ");
			System.out.println("edge=(vertex1 <space> vertex2 <space> weight)");
			System.out
					.println("Example: 10 11 2 represents edge between vertex 10 and vertex 11 with weight 2");
			System.out
					.println("i.e the number of entries should be in multiple of 3");
			System.exit(1);
		}

		// Initialing data members edges and vertices
		Kruskal obj = new Kruskal(args.length / 3);
		obj.SetVerticesEdges(args);

		// try {
		// obj.readfile();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }

		// Checking if input graph is connected or not. Since Kruskal's
		// algorithm works only for connected graphs
		if (!obj.isConnected())
			System.exit(1);

		 obj.checkNodeConnectivity();
		// obj.printVertices();

		// Calls to Kruskal's algorithm
		Edge T[] = null;
		System.out.println();
		System.out
				.println("Kruskal Algorithm Without UnionFind Data Structure---->\n");
		long start = System.currentTimeMillis();
		T = obj.KruskalAlgorithmWithoutUnionFind();
		long end = System.currentTimeMillis();
		obj.printEdges(T);
		System.out.println();
		System.out
				.println("Total time take by Kruskal Algorithm without union find:"
						+ (end - start) + " milli secs");

		System.out.println();
		System.out
				.println("Kruskal Algorithm With Union Find Data Structure----->\n");
		start = System.currentTimeMillis();
		T = obj.KruskalAlgorithmWithUnionFind();
		end = System.currentTimeMillis();
		obj.printEdges(T);
		System.out.println();
		System.out
				.println("Total time take by Kruskal Algorithm with union find:"
						+ (end - start) + " milli secs");
	}
}
