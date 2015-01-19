/*
 * UnionFind.java
 * Author: Poorn Pragya
 * Created on: Oct 26th, 2014
 *  This program defines a data structure that supports two operation union and find.
 *  Union takes O(1) time. Find takes O(logN) time
 *  Algorithm reference : Wikipedia http://en.wikipedia.org/wiki/Disjoint-set_data_structure
 */
public class UnionFind {

	Vertex v[];
	
	public UnionFind(int a[]) {
		v=new Vertex[a.length];
		MakeDisjointSet(a);
		
	}

	// This method creates a disjoint set
	private void MakeDisjointSet(int a[]) {
		for(int i=0;i<a.length;i++ ) {
			v[i]=new Vertex();
			v[i].id=i;
			v[i].parent=null;
		}
	}

	// This operation is used to merge to given disjoint sets. Takes O(1) time
	void union(int a, int b) {
		Vertex v1=v[find(a)];
		Vertex v2=v[find(b)];
		v2.parent=v1;
	}

	// This method used to find the root element of a given set to which input entry belongs to. This takes O(log N) time
	 int find(int a) {
		 Vertex v1=v[a];
		 int level_count=0;
		 while(v1.parent !=null) {
			 v1=v1.parent;
			 level_count++;
		 }
		 if(level_count >1) 
			 compress_path(a,v1.id);
		 return v1.id;

	 }

	 // This method is used to compress the path to reduce access time of find operation be compressing the path 
	private void compress_path(int a, int root) {
		Vertex v1=v[a];
		while(v1.id!=root) {
			Vertex temp=v1.parent;
			v1.parent=v[root];
			v1=temp;
		}
	}
}
