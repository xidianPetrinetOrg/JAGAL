package de.uni.freiburg.iig.telematik.jagal.graph.exception;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;



public class EdgeNotFoundException extends GraphException {
	
	
	private static final long serialVersionUID = -817667248805564124L;
	private static final String toStringFormat = "%s does not contain edge (%s -> %s)";
	private String sourceName = null;
	private String targetName = null;

	public <V extends Vertex<U>, E extends Edge<V>, U> EdgeNotFoundException(E edge, AbstractGraph<V, E, U> graph){
		super(graph.getName());
		this.sourceName = edge.getSource().getName();
		this.targetName = edge.getTarget().getName();
	}
	
	public <V extends Vertex<U>, E extends Edge<V>, U> EdgeNotFoundException(String sourceName, String targetName, AbstractGraph<V, E, U> graph){
		super(graph.getName());
		this.sourceName = sourceName;
		this.targetName = targetName;
	}
	
        @Override
	public String getMessage(){
		return String.format(toStringFormat, getGraphName(), getSourceName(), getTargetName());
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getTargetName() {
		return targetName;
	}
	
	
}