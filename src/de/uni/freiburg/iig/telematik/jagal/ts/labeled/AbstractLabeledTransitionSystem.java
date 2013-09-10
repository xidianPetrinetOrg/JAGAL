package de.uni.freiburg.iig.telematik.jagal.ts.labeled;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.EventNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;



public abstract class AbstractLabeledTransitionSystem<E extends Event, S extends State> extends AbstractTransitionSystem<S, LabeledTransitionRelation<S, E>>{
	
	protected final String toStringFormat = "TS = {S, E, T, S_start, S_end}\n  S       = %s\n  S_start = %s\n  S_end   = %s\n  E       = %s\n  T       = %s\n";
	
	protected Set<E> events = new HashSet<E>();
	private Boolean isDFA = true;
	
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
	
	/**
	 * Creates a new labeled relation of type from the given source and target states.<br>
	 * This method is abstract because only subclasses know the concrete types of their states and events.
	 * @param sourceState The state where the relation starts
	 * @param targetState The state where the relation ends
	 * @param event The event which triggers the relation
	 * @return A new labeled transition relation.
	 */
	public abstract LabeledTransitionRelation<S, E> createNewTransitionRelation(S sourceState, S targetState, E event) throws ParameterException;
	
	/**
	 * Creates a new event with the give nname and label.<br>
	 * This method is abstract because only subclasses know the type <code>E</code> of their events.
	 * @param name The name for the new event
	 * @param label The label for the new event
	 * @return A new event of type <code>E</code> with the given name and label.
	 * @throws ParameterException
	 */
	public abstract E createNewEvent(String name, String label) throws ParameterException;

	public boolean isDFA(){
		return isDFA;
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
	
	public Set<S> getSourceStates(E event){
		Set<S> result = new HashSet<S>();
		for(LabeledTransitionRelation<S,E> relation: getRelationsFor(event)){
			result.add(relation.getSource());
		}
		return result;
	}
	
	public Set<S> getTargetStates(E event){
		Set<S> result = new HashSet<S>();
		for(LabeledTransitionRelation<S,E> relation: getRelationsFor(event)){
			result.add(relation.getTarget());
		}
		return result;
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
    			isDFA = false;
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
	
	
	
	public boolean acceptsSequence(String... sequence) throws ParameterException{
		Validate.notNull(sequence);
		Validate.notEmpty(sequence);
		
		S actualState = null;
		
		// Find all possible start states
		Set<S> possibleStartStates = new HashSet<S>();
		for(S startState: getStartStates()){
			try {
				for(LabeledTransitionRelation<S, E> relation: getOutgoingRelationsFor(startState)){
					if(relation.getEvent().getLabel().equals(sequence[0])){
						possibleStartStates.add(startState);
						break;
					}
				}
			} catch(VertexNotFoundException e){
				// Should not happen, since we only use start states of the TS
				e.printStackTrace();
			}
		}
		if(possibleStartStates.isEmpty())
			return false;
		
		for(S possibleStartState: possibleStartStates){
			// For every possible start state, check if the sequence can be generated
			if(acceptsSequenceRec(possibleStartState, sequence)){
				return true;
			}
		}
		return false;
	}
	
	private boolean acceptsSequenceRec(S actualState, String... restSequence){
		
		if(restSequence.length == 0){
			// This is the trivial case
			return true;
		}
		
		// Check if there is an outgoing relation with the right label
		Set<S> successors = new HashSet<S>();
		try{ 
			for(LabeledTransitionRelation<S, E> relation: getOutgoingRelationsFor(actualState)){
				if(relation.getEvent().getLabel().equals(restSequence[0])){
					successors.add(relation.getTarget());
				}
			}
		} catch(VertexNotFoundException e){
			// Should not happen, since we only use start states of the TS
			e.printStackTrace();
		}
		
		
		if(successors.isEmpty())
			return false;
		
		for(S successor: successors){
			if(acceptsSequenceRec(successor, Arrays.copyOfRange(restSequence, 1, restSequence.length))){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean addStartState(S state) {
		if(super.addStartState(state)){
			if(isDFA()){
				isDFA = getStartStates().size() == 1;
			}
			return true;
		}
		return false;
	}

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
