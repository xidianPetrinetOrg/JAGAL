package de.uni.freiburg.iig.telematik.jagal.ts;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;

public class TransitionRelation<S extends State> extends Edge<S> {
	
	public TransitionRelation(){
		super();
	}

	public TransitionRelation(S source, S target){
		super(source, target);
	}
	
}
