package de.uni.freiburg.iig.telematik.jagal.ts.labeled;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;



public class LabeledTransitionSystem extends AbstractLabeledTransitionSystem<Event, State> {

	public LabeledTransitionSystem() {
		super();
	}
	
	public LabeledTransitionSystem(String name){
		super(name);
	}
	
	public LabeledTransitionSystem(Collection<State> states){
		super(states);
	}
	
	public LabeledTransitionSystem(String name, Collection<State> states){
		super(name, states);
	}
	
	public LabeledTransitionSystem(Collection<State> states, Collection<Event> events){
		super(states, events);
	}
	
	public LabeledTransitionSystem(String name, Collection<State> states, Collection<Event> events){
		super(name, states, events);
	}
	
	@Override
	protected LabeledTransitionRelation<State, Event> createNewEdge(State sourceVertex, State targetVertex) {
		return new LabeledTransitionRelation<State, Event>(sourceVertex, targetVertex);
	}

	public static void main(String[] args) throws Exception{
		LabeledTransitionSystem ts = new LabeledTransitionSystem();
		State s0 = new State("s0");
		State s1 = new State("s1");
		State s2 = new State("s2");
		State s3 = new State("s3");
		State s4 = new State("s4");
		State s5 = new State("s5");
		ts.addVertex(s0);
		ts.addVertex(s1);
		ts.addVertex(s2);
		ts.addVertex(s3);
		ts.addVertex(s4);
		ts.addVertex(s5);
		Event a = new Event("A");
		Event b = new Event("B");
		Event c = new Event("C");
		Event d = new Event("D");
		Event e = new Event("E");
		ts.addEvent(a);
		ts.addEvent(b);
		ts.addEvent(c);
		ts.addEvent(d);
		ts.addEvent(e);
		ts.addStartState(s0);
		ts.addEndState(s3);
		ts.addEndState(s4);
		ts.addRelation(s0, s2, a);
		ts.addRelation(s2, s3, b);
		ts.addRelation(s0, s1, a);
		ts.addRelation(s1, s4, c);
		ts.addRelation(s4, s5, d);
		ts.addRelation(s5, s4, e);
		
		String[] sequence1 = {"A", "C", "D", "E", "D", "E"};
		System.out.println(ts.acceptsSequence(sequence1));
	}

	@Override
	public State createNewState(String name) throws ParameterException {
		Validate.notNull(name);
		return new State(name);
	}

	@Override
	public LabeledTransitionRelation<State, Event> createNewTransitionRelation(State sourceState, State targetState) throws ParameterException {
		Validate.notNull(sourceState);
		Validate.notNull(targetState);
		return new LabeledTransitionRelation<State, Event>(sourceState, targetState);
	}

	@Override
	public LabeledTransitionRelation<State, Event> createNewTransitionRelation(State sourceState, State targetState, Event event) throws ParameterException {
		Validate.notNull(sourceState);
		Validate.notNull(targetState);
		Validate.notNull(event);
		return new LabeledTransitionRelation<State, Event>(sourceState, targetState, event);
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
	
}
