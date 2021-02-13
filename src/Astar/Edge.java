package Astar;
/**
 * Edge links two nodes.
 */
public class Edge {
	Node from;
	Node to;
	int distance;
	
	//Los dos nodos y la distancia entre ellos
	public Edge(Node from, Node to, int distance){
		this.from = from;
		this.to = to;
		this.distance = distance;
	}
	
	public int hashCode() {
		return from.hashCode() + to.hashCode();
	}
	
	public boolean equals(Object o){
		if (o instanceof Edge){
			Edge e = (Edge) o;
			return e.from.equals(from) && e.to.equals(to);
		} else {
			return false;
		}
	}
	
	public String toString(){
		return from + "-" + to + "-" + distance;
	}
}
