package ts;

import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;
import ts.labeled.AbstractLabeledTransitionSystem;



public class EventNotFoundException extends GraphException {
	
	private static final long serialVersionUID = 1L;
	private String messagePart = " does not contain event ";
	private String event;

	public <S extends State, E extends Event> EventNotFoundException(E event, AbstractLabeledTransitionSystem<E, S> transitionSystem){
		super(transitionSystem.getName());
		this.event = event.toString();
	}
	
	public <S extends State, E extends Event> EventNotFoundException(E event, String eventDescription, AbstractLabeledTransitionSystem<E, S> transitionSystem){
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
