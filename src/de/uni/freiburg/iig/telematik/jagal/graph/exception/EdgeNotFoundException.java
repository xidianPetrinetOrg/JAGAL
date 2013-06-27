package de.uni.freiburg.iig.telematik.jagal.graph.exception;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;



public class EdgeNotFoundException extends GraphException {
	
	private static final long serialVersionUID = 1L;
	private String messagePart = " does not contain edge ";
	private String edge;

	public <V extends Vertex<U>, E extends Edge<V>, U> EdgeNotFoundException(E edge, AbstractGraph<V, E, U> graph){
		super(graph.getName());
		this.edge = edge.toString();
	}
	
	public <V extends Vertex<U>, E extends Edge<V>, U> EdgeNotFoundException(V source, V target, AbstractGraph<V, E, U> graph){
		super(graph.getName());
		this.edge = edge.toString();
		messagePart = " does not contain edge between "+source+" and "+target;
	}
	
	public String getMessage(){
		return getGraph()+messagePart+getEdge();
	}
	
	public String getEdge(){
		return edge;
	}
}