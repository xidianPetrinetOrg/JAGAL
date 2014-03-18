package de.uni.freiburg.iig.telematik.jagal.ts;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;

public enum TSType {
	
	TRANSITION_SYSTEM, LABELED_TRANSITION_SYSTEM;
	
	public static Class<?> getClassType(TSType type) throws ParameterException{
		Validate.notNull(type);
		switch (type){
			case TRANSITION_SYSTEM: return AbstractTransitionSystem.class;
			case LABELED_TRANSITION_SYSTEM: return AbstractLabeledTransitionSystem.class;
			default:	return null;
		}
	}

}
