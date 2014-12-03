package de.uni.freiburg.iig.telematik.jagal.visualization.flexible;

import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.TransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.TransitionSystem;

public class TransitionSystemComponent extends AbstractTransitionSystemComponent<TransitionSystem, State, TransitionRelation, Object> {

	private static final long serialVersionUID = -2904997258654606729L;

	public TransitionSystemComponent(TransitionSystem ts) throws Exception {
		super(ts);
	}
	
}
