package de.uni.freiburg.iig.telematik.jagal.ts;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;

public class TransitionRelation extends AbstractTransitionRelation<State, Object> {
	
	private static final long serialVersionUID = 2463763607811901236L;

	public TransitionRelation(){
		super();
	}

	public TransitionRelation(State source, State target){
		super(source, target);
	}
	
}
