package de.uni.freiburg.iig.telematik.jagal.graph.weighted;


import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;

public abstract class AbstractWeightedGraph<V extends Vertex<U>, U> extends AbstractGraph<V, WeightedEdge<V>, U>{
	
	public AbstractWeightedGraph(){
		super();
	}
	
	public AbstractWeightedGraph(String name) throws ParameterException{
		super(name);
	}
	
	public AbstractWeightedGraph(Collection<String> vertexNames) throws ParameterException{
		super(vertexNames);
	}
	
	public AbstractWeightedGraph(String name, Collection<String> vertexNames) throws ParameterException{
		super(name, vertexNames);
	}

//	@Override
//	protected String getEdgeClassName(){
//		return "graph.WeightedEdge";
//	}
	
//	public WeightedEdge<V> addEdge(U sourceElement, U targetElement, double weight) throws VertexNotFoundException{
//		return addEdge(createNewVertex(sourceElement), createNewVertex(targetElement), weight);
//	}
	
	public WeightedEdge<V> addEdge(String sourceVertexName, String targetVertexName, double weight) throws VertexNotFoundException, ParameterException{
		WeightedEdge<V> newEdge = super.addEdge(sourceVertexName, targetVertexName);
		if(newEdge == null){
			//Edge already contained in graph
			return null;
		}
		newEdge.setWeight(weight);
		return newEdge;
	}

}


