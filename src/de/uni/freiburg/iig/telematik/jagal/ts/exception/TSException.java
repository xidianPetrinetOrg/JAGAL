package de.uni.freiburg.iig.telematik.jagal.ts.exception;

import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;

public abstract class TSException extends Exception{

	private static final long serialVersionUID = -3500067527424826908L;
	
	protected String tsName = null;
	protected GraphException graphException = null;
	
	public TSException(String tsName){
		super();
		this.tsName = tsName;
	}
	
	public TSException(GraphException graphException){
		super();
		this.tsName = graphException.getGraphName();
		this.graphException = graphException;
		setStackTrace(graphException.getStackTrace());
	}
	
	public String getTSName(){
		return tsName;
	}
}
