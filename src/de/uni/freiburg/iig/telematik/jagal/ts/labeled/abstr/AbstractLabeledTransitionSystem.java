package de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.StateNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.TSException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception.EventNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception.LabeledRelationNotFoundException;



public abstract class AbstractLabeledTransitionSystem<	E extends AbstractEvent, 
														S extends AbstractLTSState<E,O>, 
														R extends AbstractLabeledTransitionRelation<S,E,O>, 
														O extends Object> extends AbstractTransitionSystem<S,R,O>{
	
	protected static final String toStringFormat = "TS = {S, E, T, S_start, S_end}\n  S       = %s\n  S_start = %s\n  S_end   = %s\n  E       = %s\n  T       = %s\n";
	private static final String complexityFormat = "|S| = %s, |T| = %s, |E| = %s";
	
	protected Map<String, E> events = new HashMap<String, E>();
	protected Map<String, Set<R>> eventRelations = new HashMap<String, Set<R>>();
	private Boolean isDFA = true;
	
	protected AbstractLabeledTransitionSystem() {
		super();
	}
	
	protected AbstractLabeledTransitionSystem(String name) {
		super(name);
	}
	
	protected AbstractLabeledTransitionSystem(Collection<String> stateNames) {
		super(stateNames);
	}
	
	protected AbstractLabeledTransitionSystem(String name, Collection<String> stateName) {
		super(name, stateName);
	}
	
	protected AbstractLabeledTransitionSystem(Collection<String> stateNames, Collection<String> eventNames) {
		this(stateNames);
		addEvents(eventNames);
	}
	
	protected AbstractLabeledTransitionSystem(String name, Collection<String> stateNames, Collection<String> eventNames) {
		this(name, stateNames);
		addEvents(eventNames);
	}
	
	/**
	 * Creates a new labeled relation of type from the given source and target states.<br>
	 * This method is abstract because only subclasses know the concrete types of their states and events.
	 * @param sourceState The state where the relation starts
	 * @param targetState The state where the relation ends
	 * @param event The event which triggers the relation
	 * @return A new labeled transition relation.
	 */
	protected abstract R createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws Exception;
	
	/**
	 * Creates a new event with the give nname and label.<br>
	 * This method is abstract because only subclasses know the type <code>E</code> of their events.
	 * @param name The name for the new event
	 * @param label The label for the new event
	 * @return A new event of type <code>E</code> with the given name and label.
	 * @throws ParameterException
	 */
	protected abstract E createNewEvent(String name, String label) ;

	public boolean isDFA(){
		return isDFA;
	}

	public boolean addEvent(String eventName) {
		return addEvent(eventName, eventName);
	}
	
	public boolean addEvent(String eventName, String eventLabel) {
		if(events.containsKey(eventName))
			return false;
		addEvent(createNewEvent(eventName, eventLabel));
		return true;
	}
	
	protected void addEvent(E event){
		events.put(event.getName(), event);
		eventRelations.put(event.getName(), new HashSet<R>());
	}
	
	public boolean addEvents(Collection<String> eventNames) {
		Validate.notNull(eventNames);
		boolean modified = false;
		for(String eventName: eventNames){
			if(addEvent(eventName))
				modified = true;
		}
		return modified;
	}
	
	public Collection<E> getEvents(){
		return Collections.unmodifiableCollection(events.values());
	}
	
	public Set<String> getEventNames(){
		return Collections.unmodifiableSet(events.keySet());
	}
	
	public int getEventCount(){
		return events.size();
	}
	
	public Set<E> getLambdaEvents(){
		Set<E> result = new HashSet<E>();
		for(E event: getEvents()){
			if(event.isLambdaEvent()){
				result.add(event);
			}
		}
		return result;
	}
	
	public Set<E> getNonLambdaEvents(){
		Set<E> result = new HashSet<E>();
		for(E event: getEvents()){
			if(!event.isLambdaEvent()){
				result.add(event);
			}
		}
		return result;
	}
	
	public Set<E> getEventsWithLabel(String label){
		Set<E> result = new HashSet<E>();
		for(E event: events.values()){
			if(event.getLabel().equals(label)){
				result.add(event);
			}
		}
		return result;
	}
	
	public E getEvent(String eventName){
		return events.get(eventName);
	}
	
	/**
	 * Checks, if the graph contains an event equal to the given vertex.
	 * -> This is not a pure reference equality (see {@link Event#equals(Object)}).
	 * @param Event Event to check
	 * @return <code>true</code> if the specified event is present;
     *		<code>false</code> otherwise.
	 */
	public boolean containsEvent(String eventName){
		return events.containsKey(eventName);
	}
	
	public Set<E> getUnusedEvents(){
		Set<E> usedEvents = new HashSet<E>();
		for(R relation: getRelations()){
			usedEvents.add(relation.getEvent());
		}
		Set<E> unusedEvents = new HashSet<E>(events.values());
		unusedEvents.removeAll(usedEvents);
		return unusedEvents;
	}
	
	public boolean hasUnusedEvents(){
		return !getUnusedEvents().isEmpty();
	}
	
	public Set<S> getSourceStates(String eventName) throws EventNotFoundException {
		validateEvent(eventName);
		Set<S> result = new HashSet<S>();
		for(R relation: getRelationsForEvent(eventName)){
			result.add(relation.getSource());
		}
		return result;
	}
	
	public Set<S> getTargetStates(String eventName) throws EventNotFoundException {
		validateEvent(eventName);
		Set<S> result = new HashSet<S>();
		for(R relation: getRelationsForEvent(eventName)){
			result.add(relation.getTarget());
		}
		return result;
	}

	public R addRelation(String sourceStateName, String targetStateName, String eventName) throws StateNotFoundException, EventNotFoundException{
		validateState(sourceStateName);
		validateState(targetStateName);
		validateEvent(eventName);
		
		if(containsRelation(sourceStateName, targetStateName, eventName))
			return null;
        
		R newRelation = super.addRelation(sourceStateName, targetStateName);
		if(newRelation == null){
			// There is another relation between the same states which uses a different event
			// Create a completely new relation.
			newRelation = createNewEdge(getVertex(sourceStateName), getVertex(targetStateName));
			edgeList.add(newRelation);
			getEdgeContainer(sourceStateName).addOutgoingEdge(newRelation);
			getEdgeContainer(targetStateName).addIncomingEdge(newRelation);
		}
		newRelation.setEvent(getEvent(eventName));
		
		if (!this.getState(sourceStateName).addOutgoingEvent(getEvent(eventName))) {
			// In this case there exists another relation from the sourceState
			// with the same event which means the transition system gets non-deterministic.
			isDFA = false;
		}
		getState(targetStateName).addIncomingEvent(getEvent(eventName));
		getState(sourceStateName).addOutgoingEvent(getEvent(eventName));
		eventRelations.get(eventName).add(newRelation);
		return newRelation;
	}
	
	public boolean containsRelation(String sourceStateName, String targetStateName, String eventName) throws StateNotFoundException {
//		System.out.println("check contains: " + sourceStateName + " to " + targetStateName + " via " + eventName);
		if(!super.containsRelation(sourceStateName, targetStateName)){
			return false;
		}
		for(R relation: getOutgoingRelationsFor(sourceStateName)){
			if(relation.getTarget().getName().equals(targetStateName)){
				if(relation.getEvent().getName().equals(eventName)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean containsRelation(String sourceStateName, String targetStateName){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public R addEdge(String sourceVertexName, String targetVertexName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R addRelation(String sourceStateName, String targetStateName) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeRelation(String sourceName, String targetName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeEdge(String sourceVertexName, String targetVertexName) {
		throw new UnsupportedOperationException();
	}
	
	public boolean removeRelation(String sourceName, String targetName, String eventName) throws StateNotFoundException, LabeledRelationNotFoundException, EventNotFoundException {
		try {
			R relationToRemove = getRelation(sourceName, targetName, eventName);
			if(super.removeEdge(relationToRemove)){
				eventRelations.get(eventName).remove(relationToRemove);
				return true;
			} else {
				return false;
			}
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
	}

	private R getRelation(String sourceName, String targetName, String eventName) throws LabeledRelationNotFoundException, StateNotFoundException, EventNotFoundException {
		for (R outgoingRelation : getOutgoingRelationsFor(sourceName, eventName)) {
			if (outgoingRelation.getTarget().getName().equals(targetName))
				return outgoingRelation;
		}
		throw new LabeledRelationNotFoundException(sourceName, targetName, eventName, this);
	}

	public boolean hasRelations(String eventName){
		for(R relation: getRelations()){
			if(relation.getEvent().getName().equals(eventName)){
				return true;
			}
		}
		return false;
	}
	
	public Set<R> getRelationsForEvent(String eventName) throws EventNotFoundException {
		validateEvent(eventName);
		return Collections.unmodifiableSet(eventRelations.get(eventName));
	}
	
	public List<R> getIncomingRelationsFor(String stateName, String eventName) throws StateNotFoundException, EventNotFoundException {
		validateEvent(eventName);
		validateState(stateName);
		
		List<R> incomingRelations =  new ArrayList<R>(getIncomingRelationsFor(stateName));
		incomingRelations.retainAll(getRelationsForEvent(eventName));
		return incomingRelations;
	}
	
	@Override
	public List<R> getIncomingRelationsFor(String stateName) throws StateNotFoundException {
		try {
			return super.getIncomingEdgesFor(stateName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
	}
	
	public List<R> getOutgoingRelationsFor(String stateName, String eventName) throws StateNotFoundException, EventNotFoundException{
		validateEvent(eventName);
		validateState(stateName);
		List<R> outgoingRelations = new ArrayList<R>(getOutgoingRelationsFor(stateName));
		outgoingRelations.retainAll(getRelationsForEvent(eventName));
		return outgoingRelations;
	}
	
	@Override
	public List<R> getOutgoingRelationsFor(String stateName) throws StateNotFoundException {
		try {
			return super.getOutgoingEdgesFor(stateName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
	}
	
	public List<S> getTargetsFor(String stateName, String eventName) throws StateNotFoundException, EventNotFoundException{
		validateEvent(eventName);
		validateState(stateName);
		
		List<S> result = new ArrayList<S>();
		if(hasOutgoingRelations(stateName)){
			for(R relation: getOutgoingRelationsFor(stateName, eventName)){
				result.add(relation.getTarget());
			}
		}
		return result;
	}
	
	public boolean hasOutgoingRelations(String stateName) throws StateNotFoundException{
		try {
			return super.hasOutgoingEdges(stateName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
	}
	
	public boolean removeEvent(String eventName) throws EventNotFoundException{
		validateEvent(eventName);
		try {
			removeEdges(getRelationsForEvent(eventName));
			eventRelations.remove(eventName);
		} catch (VertexNotFoundException e) {
			// Edge vertexes are present, since the edge itself was contained in the ts before.
			e.printStackTrace();
		}
        return events.remove(eventName) != null;
	}
	
	
	
	public boolean acceptsSequence(String... sequence) throws StateNotFoundException{
		Validate.notNull(sequence);
		Validate.notEmpty(sequence);
		
		// Find all possible start states
		Set<S> possibleStartStates = new HashSet<S>();
		for (S startState : getStartStates()) {
			for (R relation : getOutgoingRelationsFor(startState.getName())) {
				if (relation.getEvent().getLabel().equals(sequence[0])) {
					possibleStartStates.add(startState);
					break;
				}
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
	
	private boolean acceptsSequenceRec(S actualState, String... restSequence) throws StateNotFoundException{
		
		if(restSequence.length == 0){
			// This is the trivial case
			return true;
		}
		
		// Check if there is an outgoing relation with the right label
		Set<S> successors = new HashSet<S>();
		for (R relation : getOutgoingRelationsFor(actualState.getName())) {
			if (relation.getEvent().getLabel().equals(restSequence[0])) {
				successors.add(relation.getTarget());
			}
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
	public boolean addStartState(String stateName) {
		if(super.addStartState(stateName)){
			if(isDFA()){
				isDFA = getStartStates().size() == 1;
			}
			return true;
		}
		return false;
	}

	public void validateEvent(String eventName) throws EventNotFoundException {
		if(!containsEvent(eventName))
			throw new EventNotFoundException(eventName, this);
	}
	
	@Override
	public AbstractLabeledTransitionSystem<E,S,R,O> clone(){
		AbstractLabeledTransitionSystem<E,S,R,O> result = (AbstractLabeledTransitionSystem<E,S,R,O>) createNewInstance();
		try{
			for(S ownState: getStates()){
				result.addState(ownState.getName(), ownState.getElement());
				transferContent(ownState, result.getState(ownState.getName()));
			}
			for(S startState: getStartStates()){
				result.addStartState(startState.getName());
			}
			for(S endState: getEndStates()){
				result.addEndState(endState.getName());
			}
			for(E ownEvent: getEvents()){
				result.addEvent(ownEvent.getName());
				transferContent(ownEvent, result.getEvent(ownEvent.getName()));
			}
			for(R ownRelation: getRelations()){
				result.addRelation(ownRelation.getSource().getName(), ownRelation.getTarget().getName(), ownRelation.getEvent().getName());
			}
		} catch (TSException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	@Override
	protected void transferContent(S existingState, S newState){
		newState.setElement(existingState.getElement());
	}
	
	protected void transferContent(E existingEvent, E newEvent){
		newEvent.setLabel(existingEvent.getLabel());
		newEvent.setLambdaEvent(existingEvent.isLambdaEvent());
	}
	
	
	
	@Override
	public String getComplexity() {
		return String.format(complexityFormat, getStateCount(), getRelationCount(), getEventCount());
	}

	@Override
	public String toString(){
		StringBuilder relations = new StringBuilder();
		boolean firstEntry = true;
		for(R relation: getRelations()){
			if(!firstEntry){
				relations.append("            ");
			}
			relations.append(relation.toString());
			relations.append('\n');
			firstEntry = false;
		}
		return String.format(toStringFormat, getVertices(), startStates.keySet(), endStates.keySet(), events.keySet(), relations.toString());
	}
	
}
