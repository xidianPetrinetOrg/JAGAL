package graph.weighted;

import graph.Vertex;
import graph.abstr.AbstractGraph;
import graph.exception.VertexNotFoundException;

import java.util.Collection;

public abstract class AbstractWeightedGraph<V extends Vertex<U>, U> extends AbstractGraph<V, WeightedEdge<V>, U>{
	
	public AbstractWeightedGraph(){
		super();
	}
	
	public AbstractWeightedGraph(String name){
		super(name);
	}
	
	public AbstractWeightedGraph(Collection<V> vertexes){
		super(vertexes);
	}
	
	public AbstractWeightedGraph(String name, Collection<V> vertexes){
		super(name, vertexes);
	}

//	@Override
//	protected String getEdgeClassName(){
//		return "graph.WeightedEdge";
//	}
	
	public WeightedEdge<V> addEdge(U sourceElement, U targetElement, double weight) throws VertexNotFoundException{
		return addEdge(createNewVertex(sourceElement), createNewVertex(targetElement), weight);
	}
	
	public WeightedEdge<V> addEdge(V sourceVertex, V targetVertex, double weight) throws VertexNotFoundException{
		WeightedEdge<V> newEdge = super.addEdge(sourceVertex, targetVertex);
		if(newEdge == null){
			//Edge already contained in graph
			return null;
		}
		newEdge.setWeight(weight);
		return newEdge;
	}

}


