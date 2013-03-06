package ts;

import java.util.Collection;

import ts.abstr.AbstractTransitionSystem;

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

}
