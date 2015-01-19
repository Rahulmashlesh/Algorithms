/*
 * Graph.java
 * Author: Poorn Pragya
 * This class is used to represent a graph in adjacency list format
 */


import java.util.LinkedList;
public class Graph {
	//Data Member to store number of vertices
	int V;
	// Adjacency list
	LinkedList<Integer> adj[];

	/*
	 * Constructor to inititalzie a graph
	 */
	@SuppressWarnings("unchecked")
	public Graph(int V) {
		this.V = V;
		adj = new LinkedList[V];
		for (int i = 0; i < V; i++)
			adj[i] = new LinkedList<Integer>();
	}

	/*
	 * Method Add Edge is used to add an edge in the graph
	 */
	void addEdge(Integer v, Integer w) {
		if (!adj[v].contains(w))
			adj[v].addLast(w);

		if (!adj[w].contains(v))
			adj[w].addLast(v);
	}

	/*
	 * This method is used print the graph
	 */
	void printAdj() {
		for (int i = 0; i < V; i++) {
			System.out.println(i + "-->" + adj[i].toString());
		}

	}
}
