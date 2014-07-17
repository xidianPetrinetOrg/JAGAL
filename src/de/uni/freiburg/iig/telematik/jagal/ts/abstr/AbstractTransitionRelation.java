package de.uni.freiburg.iig.telematik.jagal.ts.abstr;

import java.io.Serializable;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;

public abstract class AbstractTransitionRelation<S extends AbstractState<O>, O extends Object> extends Edge<S> implements Serializable {

	private static final long serialVersionUID = 8676428132800423438L;

	public AbstractTransitionRelation(){
		super();
	}

	public AbstractTransitionRelation(S source, S target){
		super(source, target);
	}
	
}
