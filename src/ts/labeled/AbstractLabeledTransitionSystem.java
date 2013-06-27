package ts.labeled;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;

import ts.Event;
import ts.EventNotFoundException;
import ts.State;
import ts.abstr.AbstractTransitionSystem;


public abstract class AbstractLabeledTransitionSystem<E extends Event, S extends State> extends AbstractTransitionSystem<S, LabeledTransitionRelation<S, E>>{
	
	protected final String toStringFormat = "TS = {S, E, T, S_start, S_end}\n  S       = %s\n  S_start = %s\n  S_end   = %s\n  E       = %s\n  T       = %s\n";
	
	protected Set<E> events = new HashSet<E>();
	private Boolean isDFN = true;
	
	public AbstractLabeledTransitionSystem() {
		super();
	}
	
	public AbstractLabeledTransitionSystem(String name){
		super(name);
	}
	
	public AbstractLabeledTransitionSystem(Collection<S> states){
		super(states);
	}
	
	public AbstractLabeledTransitionSystem(String name, Collection<S> states){
		super(name, states);
	}
	
	public AbstractLabeledTransitionSystem(Collection<S> states, Collection<E> events){
		this(states);
		this.events.addAll(events);
	}
	
	public AbstractLabeledTransitionSystem(String name, Collection<S> states, Collection<E> events){
		this(name, states);
		this.events.addAll(events);
	}

	public boolean isDFN(){
		return isDFN;
	}

	public boolean addEvent(E event){
		return events.add(event);
	}
	
	public Set<E> getEvents(){
		return Collections.unmodifiableSet(events);
	}
	
	public E getEvent(String name){
		for(E event: events){
			if(event.getName().equals(name)){
				return event;
			}
		}
		return null;
	}
	
	/**
	 * Checks, if the graph contains an event equal to the given vertex.
	 * -> This is not a pure reference equality (see {@link Event#equals(Object)}).
	 * @param Event Event to check
	 * @return <code>true</code> if the specified event is present;
     *		<code>false</code> otherwise.
	 */
	public boolean contains(E event){
		try{
			getEqualEvent(event);
			return true;
		}catch(EventNotFoundException e){
			return false;
		}
	}
	
	/**
	 * Returns the reference of a contained event, that equals to the given event.
	 * @param event
	 * @return
	 */
	public E getEqualEvent(E event) throws EventNotFoundException{
		for(E e: events){
			if(e.equals(event)){
				return e;
			}
		}
		throw new EventNotFoundException(event, this);
	}
	
	public Set<E> getUnusedEvents(){
		Set<E> usedEvents = new HashSet<E>();
		for(LabeledTransitionRelation<S, E> relation: getRelations()){
			usedEvents.add(relation.getEvent());
		}
		Set<E> unusedEvents = new HashSet<E>(events);
		unusedEvents.removeAll(usedEvents);
		return unusedEvents;
	}
	
	public boolean hasUnusedEvents(){
		return !getUnusedEvents().isEmpty();
	}

	public LabeledTransitionRelation<S,E> addRelation(S sourceState, S targetState, E event) throws GraphException{
		if(event==null)
			throw new NullPointerException();
        if(!contains(event))
        	throw new EventNotFoundException(event, this);
		
        
        LabeledTransitionRelation<S,E> testRelation = new LabeledTransitionRelation<S,E>(sourceState, targetState, event);
        // Check if TS already contains the relation
        boolean alreadyContainsRelation = false;
        for(LabeledTransitionRelation<S,E> existingRelation: getRelations()){
        	if(existingRelation.equals(testRelation)){
        		alreadyContainsRelation = true;
        		break;
        	}
        }
        if(!alreadyContainsRelation){
        	LabeledTransitionRelation<S,E> newRelation = super.addRelation(sourceState, targetState);
    		E equalEvent = getEqualEvent(event);
    		newRelation.setEvent(equalEvent);
    		if(!this.getEqualState(sourceState).addOutgoingEvent(equalEvent)){
    			//In this case there exists another relation from the sourceState 
    			//with the same event which means the transition system gets non-deterministic.
    			isDFN = false;
    		}
    		getEqualState(targetState).addIncomingEvent(equalEvent);
    		return newRelation;
        }
        return null;	
	}
	
	@Override
	public LabeledTransitionRelation<S, E> addEdge(S sourceVertex, S targetVertex) {
		throw new UnsupportedOperationException("Use addRelation(S, S, E) instead.");
	}

	public LabeledTransitionRelation<S, E> addRelation(S sourceState, S targetState) {
		throw new UnsupportedOperationException("Use addRelation(S, S, E) instead.");
	}
	
	public boolean hasRelations(E event){
		for(LabeledTransitionRelation<S, E> relation: getRelations()){
			if(relation.getEvent().equals(event)){
				return true;
			}
		}
		return false;
	}
	
	public Set<LabeledTransitionRelation<S, E>> getRelationsFor(E event){
		Set<LabeledTransitionRelation<S, E>> result = new HashSet<LabeledTransitionRelation<S, E>>();
		for(LabeledTransitionRelation<S, E> relation: getRelations()){
			if(relation.getEvent().equals(event)){
				result.add(relation);
			}
		}
		return result;
	}
	
	public List<LabeledTransitionRelation<S, E>> getIncomingRelationsFor(S state, E event) throws VertexNotFoundException{
		List<LabeledTransitionRelation<S, E>> incomingRelations = super.getIncomingEdgesFor(state);
		if(incomingRelations.isEmpty())
			return incomingRelations;
		List<LabeledTransitionRelation<S, E>> result = new ArrayList<LabeledTransitionRelation<S, E>>();
		for(LabeledTransitionRelation<S, E> relation: incomingRelations){
			if(relation.getEvent().equals(event)){
				result.add(relation);
			}
		}
		return result;
	}
	
	public List<LabeledTransitionRelation<S, E>> getOutgoingRelationsFor(S state, E event) throws VertexNotFoundException{
		List<LabeledTransitionRelation<S, E>> outgoingRelations = super.getOutgoingEdgesFor(state);
		if(outgoingRelations.isEmpty())
			return outgoingRelations;
		List<LabeledTransitionRelation<S, E>> result = new ArrayList<LabeledTransitionRelation<S, E>>();
		for(LabeledTransitionRelation<S, E> relation: outgoingRelations){
			if(relation.getEvent().equals(event)){
				result.add(relation);
			}
		}
		return result;
	}
	
	public List<S> getTargetsFor(S state, E event) throws VertexNotFoundException, EventNotFoundException{
		S equalState = getEqualState(state);
		E equalEvent = getEqualEvent(event);
		
		List<S> result = new ArrayList<S>();
		if(hasOutgoingEdges(equalState)){
			for(LabeledTransitionRelation<S, E> relation: getOutgoingRelationsFor(equalState)){
				if(relation.getEvent().equals(equalEvent)){
					result.add(relation.getTarget());
				}
			}
		}
		return result;
	}
	
	public boolean removeEvent(E event) throws EventNotFoundException{
		E equalEvent = getEqualEvent(event);
		try {
			removeAllEdges(getRelationsFor(equalEvent));
		} catch (VertexNotFoundException e) {
			// Edge vertexes are present, since the edge itself was contained in the ts before.
			e.printStackTrace();
		}
        return events.remove(equalEvent);
	}
	
//	public boolean removeRelation(LabeledTransitionRelation<S, E> relation){
//		if(!contains(relation.getEvent()))
//			return false;
//		if(!contains(relation.getSource()))
//	}
//	
//	public boolean contains(LabeledTransitionRelation<S, E> relation){
//		
//	}
	
	protected void validateEvent(E event) throws ParameterException{
		Validate.notNull(event);
		if(!contains(event))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown event");
	}
	
	@Override
	public String toString(){
		StringBuilder relations = new StringBuilder();
		boolean firstEntry = true;
		for(LabeledTransitionRelation<S, E> relation: getRelations()){
			if(!firstEntry){
				relations.append("            ");
			}
			relations.append(relation.toString());
			relations.append('\n');
			firstEntry = false;
		}
		return String.format(toStringFormat, getVertexes(), startStates, endStates, events, relations.toString());
	}
	
}
