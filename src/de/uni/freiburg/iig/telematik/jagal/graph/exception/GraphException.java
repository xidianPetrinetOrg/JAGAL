package de.uni.freiburg.iig.telematik.jagal.graph.exception;


public class GraphException extends Exception{

	private static final long serialVersionUID = 1L;
	protected String graph = null;
	
	public GraphException(String graph){
		super();
		this.graph = graph;
	}
	
	public String getGraph(){
		return graph;
	}

}
