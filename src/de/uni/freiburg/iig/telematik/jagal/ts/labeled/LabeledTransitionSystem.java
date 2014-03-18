package de.uni.freiburg.iig.telematik.jagal.ts.labeled;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;



public class LabeledTransitionSystem extends AbstractLabeledTransitionSystem<Event, LTSState, Object> {

	public LabeledTransitionSystem() {
		super();
	}
	
	public LabeledTransitionSystem(String name) throws ParameterException{
		super(name);
	}
	
	public LabeledTransitionSystem(Collection<String> states) throws ParameterException{
		super(states);
	}
	
	public LabeledTransitionSystem(String name, Collection<String> states) throws ParameterException{
		super(name, states);
	}
	
	public LabeledTransitionSystem(Collection<String> states, Collection<String> events) throws ParameterException{
		super(states, events);
	}
	
	public LabeledTransitionSystem(String name, Collection<String> states, Collection<String> events) throws ParameterException{
		super(name, states, events);
	}
	
	@Override
	protected LabeledTransitionRelation createNewEdge(LTSState sourceVertex, LTSState targetVertex) {
		return new LabeledTransitionRelation(sourceVertex, targetVertex);
	}

	@Override
	public LTSState createNewState(String name, Object element) throws ParameterException {
		Validate.notNull(name);
		return new LTSState(name, element);
	}

	@Override
	public LabeledTransitionRelation createNewTransitionRelation(LTSState sourceState, LTSState targetState) throws ParameterException {
		Validate.notNull(sourceState);
		Validate.notNull(targetState);
		return new LabeledTransitionRelation(sourceState, targetState);
	}

	@Override
	public LabeledTransitionRelation createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws ParameterException, VertexNotFoundException {
		validateVertex(sourceStateName);
		validateVertex(targetStateName);
		validateEvent(eventName);
		return new LabeledTransitionRelation(getState(sourceStateName), getState(targetStateName), getEvent(eventName));
	}

	@Override
	public Event createNewEvent(String name, String label) throws ParameterException {
		Validate.notNull(name);
		Validate.notNull(label);
		return new Event(name, label);
	}

	@Override
	public LabeledTransitionSystem createNewInstance() {
		return new LabeledTransitionSystem();
	}
	
	public static void main(String[] args) throws Exception{
		LabeledTransitionSystem ts = new LabeledTransitionSystem();
		ts.addState("s0");
		ts.addState("s1");
		ts.addState("s2");
		ts.addState("s3");
		ts.addState("s4");
		ts.addState("s5");
		ts.addEvent("a");
		ts.addEvent("b");
		ts.addEvent("c");
		ts.addEvent("d");
		ts.addEvent("e");
		ts.addStartState("s0");
		ts.addEndState("s3");
		ts.addEndState("s4");
		ts.addRelation("s0", "s2", "a");
		ts.addRelation("s2", "s3", "b");
		ts.addRelation("s0", "s1", "a");
		ts.addRelation("s1", "s4", "c");
		ts.addRelation("s4", "s5", "d");
		ts.addRelation("s5", "s4", "e");
		
		String[] sequence1 = {"A", "C", "D", "E", "D", "E"};
		System.out.println(ts.acceptsSequence(sequence1));
	}
	
}
