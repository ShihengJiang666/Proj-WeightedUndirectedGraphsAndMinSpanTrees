package graphalg;

public class Kedge{
	public int weight;
	public Object vertex1;
	public Object vertex2;

	public Kedge(Object vertex1, Object vertex2, int weight){
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.weight = weight;
	}
}
