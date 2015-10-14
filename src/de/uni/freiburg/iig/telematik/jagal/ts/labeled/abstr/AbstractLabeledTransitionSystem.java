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
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.TSComplexity;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception.EventNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception.LabeledRelationNotFoundException;



public abstract class AbstractLabeledTransitionSystem<	E extends AbstractEvent, 
														S extends AbstractLTSState<E,O>, 
														R extends AbstractLabeledTransitionRelation<S,E,O>, 
														O extends Object> extends AbstractTransitionSystem<S,R,O>{
	
	private static final long serialVersionUID = -8005788902665552329L;

	protected static final String toStrFormat = "TS = {S, E, T, S_start, S_end}\n  S       = %s\n  S_start = %s\n  S_end   = %s\n  E       = %s\n  T       = %s\n";
	
	protected Map<String, E> events = new HashMap<>();
	protected Map<String, Set<R>> eventRelations = new HashMap<>();
	
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
	 * @param sourceStateName The state where the relation starts
	 * @param targetStateName The state where the relation ends
	 * @param eventName The event which triggers the relation
	 * @return A new labeled transition relation.
         * @throws Exception
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
		if(getStartStates().isEmpty())
			return false;
		if(getStartStates().size() > 1)
			return false;
		for(S state: getStates()){
			try {
				Set<E> relationEvents = new HashSet<>();
				for(R outgoingRelation: getOutgoingRelationsFor(state.getName())){
					if(!relationEvents.add(outgoingRelation.getEvent()))
						return false;
				}
			} catch (StateNotFoundException e) {
				// Cannot happen, since we iterate over TS states
				throw new RuntimeException(e);
			}
		}
		return true;
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
	
	public void renameEvent(String oldName, String newName) throws EventNotFoundException{
		if(containsEvent(newName))
			throw new ParameterException("There is already an event with name \"" + newName + "\". Abort renaming.");
		E existingEvent = getEvent(oldName);
		existingEvent.setName(newName);
		events.put(newName, existingEvent);
		events.remove(oldName);
		eventRelations.put(newName, eventRelations.get(oldName));
		eventRelations.remove(oldName);
	}
	
	protected void addEvent(E event){
		events.put(event.getName(), event);
		eventRelations.put(event.getName(), new HashSet<>());
	}
	
	public final boolean addEvents(Collection<String> eventNames) {
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
		Set<E> result = new HashSet<>();
		for(E event: getEvents()){
			if(event.isLambdaEvent()){
				result.add(event);
			}
		}
		return result;
	}
	
	public Set<E> getNonLambdaEvents(){
		Set<E> result = new HashSet<>();
		for(E event: getEvents()){
			if(!event.isLambdaEvent()){
				result.add(event);
			}
		}
		return result;
	}
	
	public Set<E> getEventsWithLabel(String label){
		Set<E> result = new HashSet<>();
		for(E event: events.values()){
			if(event.getLabel().equals(label)){
				result.add(event);
			}
		}
		return result;
	}
	
	public E getEvent(String eventName) throws EventNotFoundException{
		E event = events.get(eventName);
		if(event == null)
			throw new EventNotFoundException(eventName, this);
		return event;
	}
	
	/**
	 * Checks, if the graph contains an event equal to the given vertex.
	 * -&gt; This is not a pure reference equality (see {@link Event#equals(Object)}).
	 * @param eventName Event to check
	 * @return <code>true</code> if the specified event is present;
     *		<code>false</code> otherwise.
	 */
	public boolean containsEvent(String eventName){
		return events.containsKey(eventName);
	}
	
	public Set<E> getUnusedEvents(){
		Set<E> usedEvents = new HashSet<>();
		for(R relation: getRelations()){
			usedEvents.add(relation.getEvent());
		}
		Set<E> unusedEvents = new HashSet<>(events.values());
		unusedEvents.removeAll(usedEvents);
		return unusedEvents;
	}
	
	public boolean hasUnusedEvents(){
		return !getUnusedEvents().isEmpty();
	}
	
	public Set<S> getSourceStates(String eventName) throws EventNotFoundException {
		validateEvent(eventName);
		Set<S> result = new HashSet<>();
		for(R relation: getRelationsForEvent(eventName)){
			result.add(relation.getSource());
		}
		return result;
	}
	
	public Set<S> getTargetStates(String eventName) throws EventNotFoundException {
		validateEvent(eventName);
		Set<S> result = new HashSet<>();
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
		getState(sourceStateName).addOutgoingEvent(getEvent(eventName));
		getState(targetStateName).addIncomingEvent(getEvent(eventName));
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
	
        @Override
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
				getState(targetName).removeIncomingEvent(getEvent(eventName));
				getState(sourceName).removeOutgoingEvent(getEvent(eventName));
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
		
		List<R> incomingRelations =  new ArrayList<>(getIncomingRelationsFor(stateName));
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
		List<R> outgoingRelations = new ArrayList<>(getOutgoingRelationsFor(stateName));
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
		
		List<S> result = new ArrayList<>();
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
			throw new RuntimeException(e);
		}
        return events.remove(eventName) != null;
	}
	
	
	
	public boolean acceptsSequence(String... sequence) throws StateNotFoundException{
		Validate.notNull(sequence);
		Validate.notEmpty(sequence);
		
		// Find all possible start states
		Set<S> possibleStartStates = new HashSet<>();
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
			if (getEndStates().isEmpty()) {
				return true;
			} else {
				return getEndStates().contains(actualState);
			}
		}
		
		// Check if there is an outgoing relation with the right label
		Set<S> successors = new HashSet<>();
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
		return super.addStartState(stateName);
	}

	public void validateEvent(String eventName) throws EventNotFoundException {
		if(!containsEvent(eventName))
			throw new EventNotFoundException(eventName, this);
	}
	
	
	
	@Override
	public abstract AbstractLabeledTransitionSystem<E,S,R,O> createNewInstance();

	@Override
	public AbstractLabeledTransitionSystem<E,S,R,O> clone(){
		AbstractLabeledTransitionSystem<E,S,R,O> result = createNewInstance();
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
			throw new RuntimeException(e);
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
	
	public TSComplexity getComplexity() {
		return new TSComplexity(getStateCount(), getEventCount(), getRelationCount());
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
		return String.format(toStrFormat, getVertices(), startStates.keySet(), endStates.keySet(), events.keySet(), relations.toString());
	}
	
}
