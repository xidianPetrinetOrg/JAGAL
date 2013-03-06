package ts.labeled;

import java.util.Collection;

import ts.Event;
import ts.State;


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
		ts.addVertex(new State("s1"));
		ts.addVertex(new State("s2"));
		ts.addEvent(new Event("e1"));
		ts.addStartState(new State("s1"));
		ts.addEndState(new State("s2"));
		LabeledTransitionRelation<State, Event> relation1 = new LabeledTransitionRelation<State, Event>(new State("s1"), new State("s2"), new Event("e1"));
//		LabeledTransitionRelation<State, Event> relation2 = new LabeledTransitionRelation<State, Event>(new State("s1"), new State("s2"), new Event("e1"));
		ts.addRelation(new State("s1"), new State("s2"), new Event("e1"));
		System.out.println(ts);
		ts.containsEdge(relation1);
		System.out.println("Contains: "+ts.containsEdge(relation1));
		ts.removeEdge(relation1);
		System.out.println(ts);
//		Set<LabeledTransitionRelation<State, Event>> set = new HashSet<LabeledTransitionRelation<State, Event>>();
//		set.add(relation1);
//		System.out.println(set);
//		System.out.println("contains: "+set.contains(relation2));
//		set.remove(relation2);
//		System.out.println(set);
	}
	
}
