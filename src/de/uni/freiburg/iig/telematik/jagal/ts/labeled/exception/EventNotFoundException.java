package de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception;

import de.uni.freiburg.iig.telematik.jagal.ts.exception.TSException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;



public class EventNotFoundException extends TSException {
	
	private static final long serialVersionUID = -4259274129546118573L;
	private static final String toStringFormat = "%s does not contain event %s";
	
	private String eventName;

	public <E extends AbstractEvent, S extends AbstractLTSState<E,O>, R extends AbstractLabeledTransitionRelation<S,E,O>, O extends Object>
	EventNotFoundException(String eventName, AbstractLabeledTransitionSystem<E,S,R,O> transitionSystem){
		super(transitionSystem.getName());
		this.eventName = eventName;
	}
	
	public String getMessage(){
		return String.format(toStringFormat, getTSName(), getEventName());
	}
	
	public String getEventName(){
		return eventName;
	}
}
