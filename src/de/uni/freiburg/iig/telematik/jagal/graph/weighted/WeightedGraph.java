package de.uni.freiburg.iig.telematik.jagal.graph.weighted;


import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public class WeightedGraph<T extends Object> extends AbstractWeightedGraph<Vertex<T>, T>{
	
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
	protected Vertex<T> createNewVertex(String name, T element) {
		return new Vertex<>(name, element);
	}
	
	@Override
	protected WeightedEdge<Vertex<T>> createNewEdge(Vertex<T> sourceVertex, Vertex<T> targetVertex) {
		return new WeightedEdge<>(sourceVertex, targetVertex);
	}

	public static void main(String[] args) throws Exception{
		WeightedGraph<String> g = new WeightedGraph<>();
		g.addVertex("A");
		g.addVertex("B");
		g.addEdge("A", "B", 0.5);
		System.out.println(g);
	}

}


