package de.uni.freiburg.iig.telematik.jagal.ts;

import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;



public class EventNotFoundException extends GraphException {
	
	private static final long serialVersionUID = 1L;
	private String messagePart = " does not contain event ";
	private String event;

	public <S extends AbstractLTSState<E,O>, E extends Event, O extends Object> EventNotFoundException(E event, AbstractLabeledTransitionSystem<E,S,O> transitionSystem){
		super(transitionSystem.getName());
		this.event = event.toString();
	}
	
	public <S extends AbstractLTSState<E,O>, E extends Event, O extends Object> EventNotFoundException(E event, String eventDescription, AbstractLabeledTransitionSystem<E,S,O> transitionSystem){
		super(transitionSystem.getName());
		this.event = event.toString();
		messagePart = " does not contain "+eventDescription+" ";
	}
	
	public String getMessage(){
		return getGraph()+messagePart+getEvent();
	}
	
	public String getEvent(){
		return event;
	}
}
