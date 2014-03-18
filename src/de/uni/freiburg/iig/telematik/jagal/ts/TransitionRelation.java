package de.uni.freiburg.iig.telematik.jagal.ts;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;

public class TransitionRelation extends AbstractTransitionRelation<State, Object> {
	
	public TransitionRelation(){
		super();
	}

	public TransitionRelation(State source, State target){
		super(source, target);
	}
	
}
