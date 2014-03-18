package de.uni.freiburg.iig.telematik.jagal.ts.abstr;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public abstract class AbstractState<O extends Object> extends Vertex<O>{

public static final String DEFAULT_STATE_NAME = "<->";
	
	protected AbstractState(String name){
		super(name);
	}
	
	protected AbstractState(String name, O element){
		super(name, element);
	}

}
