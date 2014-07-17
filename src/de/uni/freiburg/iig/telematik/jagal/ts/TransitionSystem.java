package de.uni.freiburg.iig.telematik.jagal.ts;

import java.util.Collection;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;


public class TransitionSystem extends AbstractTransitionSystem<State, TransitionRelation, Object> {

	private static final long serialVersionUID = -5760109002670091863L;

	public TransitionSystem() {
		super();
	}
	
	public TransitionSystem(String name) {
		super(name);
	}
	
	public TransitionSystem(Collection<String> states) {
		super(states);
	}
	
	public TransitionSystem(String name, Collection<String> states) {
		super(name, states);
	}

	@Override
	protected TransitionRelation createNewEdge(State sourceVertex, State targetVertex) {
		return new TransitionRelation(sourceVertex, targetVertex);
	}
	
	@Override
	public State createNewState(String name, Object element)  {
		Validate.notNull(name);
		Validate.notNull(element);
		return new State(name, element);
	}

	@Override
	public TransitionRelation createNewTransitionRelation(State sourceState, State targetState)  {
		Validate.notNull(sourceState);
		Validate.notNull(targetState);
		return new TransitionRelation(sourceState, targetState);
	}

	@Override
	public TransitionSystem createNewInstance() {
		return new TransitionSystem();
	}

	public static void main(String[] args) throws Exception{
		TransitionSystem ts = new TransitionSystem();
		ts.addState("s1");
		ts.addState("s2");
		ts.addRelation("s1", "s2");
		System.out.println(ts);
		System.out.println("Contains: "+ts.containsEdge("s1", "s2"));
		ts.removeEdge("s1","s2");
//		ts.removeAllEdges(ts.getRelations());
		System.out.println(ts);
	}
	
}
