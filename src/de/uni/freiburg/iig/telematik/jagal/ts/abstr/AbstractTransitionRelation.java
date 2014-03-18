package de.uni.freiburg.iig.telematik.jagal.ts.abstr;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;

public abstract class AbstractTransitionRelation<S extends AbstractState<O>, O extends Object> extends Edge<S> {
	
	public AbstractTransitionRelation(){
		super();
	}

	public AbstractTransitionRelation(S source, S target){
		super(source, target);
	}
	
}
