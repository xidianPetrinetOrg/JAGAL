package de.uni.freiburg.iig.telematik.jagal.ts.serialize;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.TSType;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionRelation;

public abstract class LTSSerializer<S extends State, 
									E extends Event,
									T extends LabeledTransitionRelation<S,E>> extends TSSerializer<S,T>{

	public LTSSerializer(AbstractTransitionSystem<S,T> ts) throws ParameterException {
		super(ts);
	}

	@Override
	public TSType acceptedNetType() {
		return TSType.LABELED_TRANSITION_SYSTEM;
	}

}
