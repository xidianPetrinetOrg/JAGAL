package de.uni.freiburg.iig.telematik.jagal.graph;

 

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;

public class Graph<T extends Object> extends AbstractGraph<Vertex<T>, Edge<Vertex<T>>, T> {
	
	public Graph(){
		super();
	}
	
	public Graph(String name) throws ParameterException{
		super(name);
	}
	
	public Graph(Collection<String> vertexNames) throws ParameterException{
		super(vertexNames);
	}
	
	public Graph(String name, Collection<String> vertexNames) throws ParameterException{
		super(name, vertexNames);
	}
	
	@Override
	protected Vertex<T> createNewVertex(String name, T element) {
		return new Vertex<T>(name, element);
	}

	@Override
	protected Edge<Vertex<T>> createNewEdge(Vertex<T> sourceVertex, Vertex<T> targetVertex) {
		return new Edge<Vertex<T>>(sourceVertex, targetVertex);
	}

	public static void main(String[] args) throws Exception {
		Graph<String> g = new Graph<String>();
		g.addVertex("v1");
		g.addVertex("v2");
		g.addEdge("v1", "v2");
		System.out.println(g);
	}

}
