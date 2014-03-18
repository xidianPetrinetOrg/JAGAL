package de.uni.freiburg.iig.telematik.jagal.ts.serialize;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.TSType;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;

public abstract class LTSSerializer<S extends AbstractLTSState<E,O>, 
									E extends AbstractEvent,
									T extends AbstractLabeledTransitionRelation<S,E,O>,
									O extends Object> extends TSSerializer<S,T,O>{

	public LTSSerializer(AbstractLabeledTransitionSystem<E,S,O> ts) throws ParameterException {
		super((AbstractTransitionSystem<S, T, O>) ts);
	}

	@Override
	public TSType acceptedNetType() {
		return TSType.LABELED_TRANSITION_SYSTEM;
	}

}
