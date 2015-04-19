/* WUGraph.java */

package graph;
import list.*;
import dict.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {
	DList vertexLst;				//the user-level list of vertex;
	DList internalVertexLst;		// the list of keys of the hash table;
	HashTableChained vertices;		//the hash table that restores all the vertices;
	HashTableChained edges;			// the hash table that restores all the edges;
	int numVertices;				//the number of vertices (keep upgraded to reduce the running time)
	int numEdges;					//the number of edges (keep upgraded to reduce the running time)

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph(){
	  vertexLst = new DList();
	  internalVertexLst = new DList();
	  vertices = new HashTableChained();
	  edges = new HashTableChained();
	  numVertices = 0;
	  numEdges = 0;
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount(){
	  return numVertices;
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount(){   
	  return numEdges;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices(){
	  Object[] temp = new Object[vertexLst.length()]; 	//make an array to store all the items
	  DListNode cur;									//iterate through the vertex list and copy;
	  try {
		  cur = (DListNode)(vertexLst.front());
		  for (int i = 0; i < vertexLst.length(); i++){
			  temp[i]=cur.item();
			  cur = (DListNode)cur.next();
		  } 
	  }catch (InvalidNodeException e){
		  e.printStackTrace();
	  }
	  return temp;
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex){
	  if (!this.isVertex(vertex)){				//if the object is vertex, copy;
		  vertexLst.insertBack(vertex);
		  DList newVertex = new DList();
		  internalVertexLst.insertBack(newVertex);
		  vertices.resize();
		  vertices.insert(vertex, newVertex);
		  numVertices++;
	  }
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex){
	  if (isVertex(vertex)){
		  vertexLst.remove(vertex);
		  DList inVertex = (DList)(vertices.find(vertex).value());
		  if (inVertex.length()>0){
			  removeEdge(vertex);
		  }
		  try{
			  inVertex.myNode.remove();
		  } catch (InvalidNodeException e){
			  e.printStackTrace();
		  }
		  vertices.remove(vertex);
		  numVertices--;
  	  }
  	}

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex){
	  Entry found;
	  found = vertices.find(vertex);
	  if (found == null){
		  return false;
	  }else{
		  return true;
	  }
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex){
	  if (this.isVertex(vertex)){
		  DList internalVextex = (DList)(vertices.find(vertex).value());
		  return internalVextex.length();
	  }
	  return 0; 
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex){
	  if (this.degree(vertex)!=0){
		  DList inVertex = (DList)(vertices.find(vertex).value());
		  Object[] neighborList = new Object[degree(vertex)];
		  int[] weightList = new int[degree(vertex)];
		  int i = 0;
		  DListNode cur = (DListNode)(inVertex.front());
		  try{
			  while (cur!=null){
				  Object exV1 = ((Edge)(cur.item())).exV1;
				  Object exV2 = ((Edge)(cur.item())).exV2;
				  int weight = ((Edge)(cur.item())).weight;
				  if (exV1==vertex) {
					  neighborList[i] = exV2;
				  }else{
					  neighborList[i] = exV1;
				  }
				  weightList[i] = weight;
				  i++;
				  if (cur == (DListNode)(inVertex.back())){
					  break;
				  }
				  cur = (DListNode)(cur.next());
			  }
		  }catch(InvalidNodeException e){
			  e.printStackTrace();
		  }
		  Neighbors myNeighbors = new Neighbors();
		  myNeighbors.neighborList = neighborList;
		  myNeighbors.weightList = weightList;
		  return myNeighbors;
	  }
	  return null;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight){
	  if (!isEdge(u, v)){
		  if (isVertex(u)&&isVertex(v)){
			  VertexPair exEdge = new VertexPair(u, v);
			  DList inU = (DList)(vertices.find(u).value());
			  DList inV = (DList)(vertices.find(v).value());
			  Edge newEdge = new Edge(inU, inV, weight, u, v);
			  if (inV!=inU){
				  inU.insertBack(newEdge);
				  inV.insertBack(newEdge);        
			  }else{
				  inU.insertBack(newEdge);
			  }
			  edges.resize();
			  edges.insert(exEdge, newEdge);
			  numEdges++;
		  }     
	  }else{
		  Edge inEdge = findEdge(u, v);
		  inEdge.weight=weight;
	  }
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v){
	  if (isEdge(u, v)){
		  try {
			  if (u==v){
				  findEdge(u, v).myNode1.remove();
			  }else{
				  findEdge(u, v).myNode1.remove();
				  findEdge(u, v).myNode2.remove();
			  }
			  edges.remove(new VertexPair(u, v));
			  numEdges--;
		  }catch(InvalidNodeException e){
			  e.printStackTrace();
		  }
	  }
  }

  public void removeEdge(Object u){
	  Object[] neighborList = getNeighbors(u).neighborList;
	  for (int i=0;i<neighborList.length;i++){
		  removeEdge(u, neighborList[i]);
	  }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v){
	  if (isVertex(u)&&isVertex(v)){
		  VertexPair exEdge = new VertexPair(u, v);
		  if (edges.find(exEdge)!=null){
			  return true;
    	  }
		  return false;
	  }
	  return false;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v){
	  if (isEdge(u, v)){
		  Edge inEdge = findEdge(u, v);
		  return inEdge.weight;
	  }
	  return 0;
  }

  
  // findEdge is to make a new Edge object know (u, v);
  public Edge findEdge(Object u, Object v){
	  if (isEdge(u,v)){
		  VertexPair exEdge = new VertexPair(u, v);
		  return (Edge)(edges.find(exEdge).value());
	  }
	  return null;
  }
  
}
