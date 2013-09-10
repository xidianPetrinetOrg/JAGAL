package de.uni.freiburg.iig.telematik.jagal.ts;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;


public class TransitionSystem extends AbstractTransitionSystem<State, TransitionRelation<State>> {
	
	public TransitionSystem() {
		super();
	}
	
	public TransitionSystem(String name){
		super(name);
	}
	
	public TransitionSystem(Collection<State> states){
		super(states);
	}
	
	public TransitionSystem(String name, Collection<State> states){
		super(name, states);
	}

	@Override
	protected TransitionRelation<State> createNewEdge(State sourceVertex, State targetVertex) {
		return new TransitionRelation<State>(sourceVertex, targetVertex);
	}

	public static void main(String[] args) throws Exception{
		TransitionSystem ts = new TransitionSystem();
		ts.addVertex(new State("s1"));
		ts.addVertex(new State("s2"));
		TransitionRelation<State> relation1 = new TransitionRelation<State>(new State("s1"), new State("s2"));
		ts.addRelation(new State("s1"), new State("s2"));
		System.out.println(ts);
		System.out.println("Contains: "+ts.containsEdge(relation1));
		ts.removeEdge(relation1);
//		ts.removeAllEdges(ts.getRelations());
		System.out.println(ts);
	}

	@Override
	public State createNewState(String name) throws ParameterException {
		Validate.notNull(name);
		return new State(name);
	}

	@Override
	public TransitionRelation<State> createNewTransitionRelation(State sourceState, State targetState) throws ParameterException {
		Validate.notNull(sourceState);
		Validate.notNull(targetState);
		return new TransitionRelation<State>(sourceState, targetState);
	}

	@Override
	public TransitionSystem createNewInstance() {
		return new TransitionSystem();
	}

}
