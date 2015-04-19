/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;
import list.*;
import dict.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {


  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
	public static WUGraph minSpanTree(WUGraph g){
		WUGraph newGraph = new WUGraph();
		//Create a new graph T having the same vertices as G, but no edges (yet).
		Object[] vLst = g.getVertices();
		for (int i=0;i<vLst.length;i++){
			newGraph.addVertex(vLst[i]);
		}
		//Make a list of all the edges in G by applying the method getNeighbors;
		Kedge[] edgeLst = new Kedge[2*g.edgeCount()];
		int k = 0;
		for (int i=0;i<vLst.length;i++){
			Neighbors myNeighbors = g.getNeighbors(vLst[i]);
			for (int j=0;j<myNeighbors.neighborList.length;j++){
				edgeLst[k]=new Kedge(vLst[i],myNeighbors.neighborList[j],myNeighbors.weightList[j]);
				k++;
			}
		}
		// quicksort all the edges by their weight;
		quicksort(edgeLst);
		// use disjoint set to find all the edges;
		DisjointSets vertices = new DisjointSets(vLst.length);
		HashTableChained vIndex = new HashTableChained();
		for (int i=0;i<vLst.length;i++){
			vIndex.resize();
			vIndex.insert(vLst[i], Integer.toString(i));
		}
		for (int i=0;i<edgeLst.length;i++){
			if (edgeLst[i]!=null){
				int v1Index = Integer.parseInt((String)(vIndex.find(edgeLst[i].vertex1).value()));
				int v2Index = Integer.parseInt((String)(vIndex.find(edgeLst[i].vertex2).value()));
				int root1 = vertices.find(v1Index);
				int root2 = vertices.find(v2Index);
				if (root1!=root2){
					vertices.union(root1,root2);
					newGraph.addEdge(edgeLst[i].vertex1, edgeLst[i].vertex2, edgeLst[i].weight);
				}
			}
		}

		return newGraph;
	}
	


	public static void quicksort(Kedge[] a) {
		int i = a.length-1;
		while (a[i]==null){
			i--;
		}
		quicksort(a, 0, i);
	}



	/**
	*  Method to swap two ints in an array.
	*  @param a an array of ints.
	*  @param index1 the index of the first int to be swapped.
	*  @param index2 the index of the second int to be swapped.
	**/
	public static void swapReferences(Kedge[] a, int index1, int index2) {
		Kedge tmp = a[index1];
		a[index1] = a[index2];
		a[index2] = tmp;
	}


	/**
	*  This is a generic version of C.A.R Hoare's Quick Sort algorithm.  This
	*  will handle arrays that are already sorted, and arrays with duplicate
	*  keys.
	*
	*  If you think of an array as going from the lowest index on the left to
	*  the highest index on the right then the parameters to this function are
	*  lowest index or left and highest index or right.  The first time you call
	*  this function it will be with the parameters 0, a.length - 1.
	*
	*  @param a       an integer array
	*  @param lo0     left boundary of array partition
	*  @param hi0     right boundary of array partition
	**/
	private static void quicksort(Kedge a[], int lo0, int hi0) {
		int lo = lo0;
		int hi = hi0;
		int mid;
		
		if (hi0 > lo0) {
	   // Arbitrarily establishing partition element as the midpoint of
	   // the array.
			swapReferences(a, lo0, (lo0 + hi0)/2);
			mid = a[(lo0 + hi0) / 2].weight;
	   // loop through the array until indices cross.
			while (lo <= hi) {
	     // find the first element that is greater than or equal to 
	     // the partition element starting from the left Index.
				while((lo < hi0) && (a[lo].weight < mid)) {
					lo++;
				}

	     // find an element that is smaller than or equal to 
	     // the partition element starting from the right Index.
				while((hi > lo0) && (a[hi].weight > mid)) {
					hi--;
				}
	     // if the indices have not crossed, swap them.
				if (lo <= hi) {
					swapReferences(a, lo, hi);
					lo++;
					hi--;
				}
			}
	   // If the right index has not reached the left side of array
	   // we must now sort the left partition.
			if (lo0 < hi) {
				quicksort(a, lo0, hi);
			}
	   // If the left index has not reached the right side of array
	   // must now sort the right partition.
			if (lo < hi0) {
				quicksort(a, lo, hi0);
			}
		}
	}

}
