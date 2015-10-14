package de.uni.freiburg.iig.telematik.jagal.graph.exception;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;



public class VertexNotFoundException extends GraphException {
	
	private static final long serialVersionUID = -2531063172019479258L;
	private String messagePart = " does not contain vertex ";
	private final String vertexName;

	public <V extends Vertex<U>, E extends Edge<V>, U> VertexNotFoundException(String vertexName, AbstractGraph<V, E, U> graph){
		super(graph.getName());
		this.vertexName = vertexName;
	}
	
	public <V extends Vertex<U>, E extends Edge<V>, U> VertexNotFoundException(String vertexName, String vertexDescription, AbstractGraph<V, E, U> graph){
		super(graph.getName());
		this.vertexName = vertexName;
		messagePart = " does not contain "+vertexDescription+" ";
	}
	
        @Override
	public String getMessage(){
		return getGraphName()+messagePart+getVertex();
	}
	
	public String getVertex(){
		return vertexName;
	}
}
