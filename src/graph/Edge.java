package graph;
import list.*;

// the class Edge stores the data of Edge in a graph as object;

public class Edge{
	public int weight;
	public DList vertex1;
	public DList vertex2;
	public DListNode myNode1;
	public DListNode myNode2;
	public Object exV1;
	public Object exV2;
	public Edge(DList vertex1, DList vertex2, int weight, Object exV1, Object exV2){
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.weight = weight;
		myNode1 = null;
		myNode2 = null;
		this.exV1 = exV1;
		this.exV2 = exV2;
	}
}