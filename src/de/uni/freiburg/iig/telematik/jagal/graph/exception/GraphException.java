package de.uni.freiburg.iig.telematik.jagal.graph.exception;


public abstract class GraphException extends Exception{

	private static final long serialVersionUID = 8025121318188217233L;
	
	protected String graphName = null;
	
	public GraphException(String graph){
		super();
		this.graphName = graph;
	}
	
	public String getGraphName(){
		return graphName;
	}

}
