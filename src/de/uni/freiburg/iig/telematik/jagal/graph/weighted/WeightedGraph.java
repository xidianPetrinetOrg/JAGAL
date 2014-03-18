package de.uni.freiburg.iig.telematik.jagal.graph.weighted;


import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public class WeightedGraph<U extends Object> extends AbstractWeightedGraph<Vertex<U>, U>{
	
	public WeightedGraph(){
		super();
	}
	
	public WeightedGraph(String name) throws ParameterException{
		super(name);
	}
	
	public WeightedGraph(Collection<String> vertexNames) throws ParameterException{
		super(vertexNames);
	}
	
	public WeightedGraph(Collection<String> vertexNames, String name) throws ParameterException{
		super(name, vertexNames);
	}
	
	@Override
	protected Vertex<U> createNewVertex(String name, U element) {
		return new Vertex<U>(name, element);
	}
	
	@Override
	protected WeightedEdge<Vertex<U>> createNewEdge(Vertex<U> sourceVertex, Vertex<U> targetVertex) {
		return new WeightedEdge<Vertex<U>>(sourceVertex, targetVertex);
	}

	public static void main(String[] args) throws Exception{
		WeightedGraph<String> g = new WeightedGraph<String>();
		g.addVertex("A");
		g.addVertex("B");
		g.addEdge("A", "B", 0.5);
		System.out.println(g);
	}

}


