package de.uni.freiburg.iig.telematik.jagal.graph;



import java.util.Collection;

import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;

public class Graph<U extends Object> extends AbstractGraph<Vertex<U>, Edge<Vertex<U>>, U> {
	
	public Graph(){
		super();
	}
	
	public Graph(String name){
		super(name);
	}
	
	public Graph(Collection<Vertex<U>> vertexes){
		super(vertexes);
	}
	
	public Graph(String name, Collection<Vertex<U>> vertexes){
		super(name, vertexes);
	}
	
	@Override
	protected Vertex<U> createNewVertex(U element) {
		return new Vertex<U>(element);
	}

	@Override
	protected Edge<Vertex<U>> createNewEdge(Vertex<U> sourceVertex, Vertex<U> targetVertex) {
		return new Edge<Vertex<U>>(sourceVertex, targetVertex);
	}

	public static void main(String[] args) throws Exception {
		Graph<String> g = new Graph<String>();
		Vertex<String> v1 = new Vertex<String>("v1");
		Vertex<String> v2 = new Vertex<String>("v2");
		g.addVertex(v1);
		g.addVertex(v2);
		g.addEdge(v1, v2);
		System.out.println(g);
		g.removeAllEdges(g.getEdges());
		System.out.println(g);
	}

}
