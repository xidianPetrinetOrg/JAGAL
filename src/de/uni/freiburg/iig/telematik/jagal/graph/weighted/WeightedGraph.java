package de.uni.freiburg.iig.telematik.jagal.graph.weighted;


import java.util.Collection;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public class WeightedGraph<U extends Object> extends AbstractWeightedGraph<Vertex<U>, U>{
	
	public WeightedGraph(){
		super();
	}
	
	public WeightedGraph(String name){
		super(name);
	}
	
	public WeightedGraph(Collection<Vertex<U>> vertexes){
		super(vertexes);
	}
	
	public WeightedGraph(Collection<Vertex<U>> vertexes, String name){
		super(name, vertexes);
	}
	
	
	@Override
	protected Vertex<U> createNewVertex(U element) {
		return new Vertex<U>(element);
	}
	
	@Override
	protected WeightedEdge<Vertex<U>> createNewEdge(Vertex<U> sourceVertex, Vertex<U> targetVertex) {
		return new WeightedEdge<Vertex<U>>(sourceVertex, targetVertex);
	}

	public static void main(String[] args) throws Exception{
		WeightedGraph<String> g = new WeightedGraph<String>();
		g.addVertex(new Vertex<String>("A"));
		g.addVertex(new Vertex<String>("B"));
		g.addEdge(new Vertex<String>("A"), new Vertex<String>("B"), 0.5);
		System.out.println(g);
	}

}


