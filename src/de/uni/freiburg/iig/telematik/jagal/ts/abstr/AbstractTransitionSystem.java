package de.uni.freiburg.iig.telematik.jagal.ts.abstr;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.EdgeNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.RelationNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.StateNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception.EventNotFoundException;


public abstract class AbstractTransitionSystem<S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> extends AbstractGraph<S, T, O> {
	
	private static final String toStringFormat = "TS = {S, T, S_start, S_end}\n  S       = %s\n  S_start = %s\n  S_end   = %s\n  T       = %s\n";
	private static final String complexityFormat = "|S| = %s, |T| = %s";
	
	protected Map<String, S> endStates = new HashMap<String, S>();
	protected Map<String, S> startStates = new HashMap<String, S>(); 
	
	public AbstractTransitionSystem() {
		super();
	}
	
	public AbstractTransitionSystem(String name) {
		super(name);
	}
	
	public AbstractTransitionSystem(Collection<String> stateNames) {
		super(stateNames);
	}
	
	public AbstractTransitionSystem(String name, Collection<String> stateNames) {
		super(name, stateNames);
	}
	
	/**
	 * Creates a new state of type <code>S</code> with the given name.<br>
	 * This method is abstract because only subclasses know the type <code>S</code> of their states.
	 * @param name The name for the new state.
	 * @return A new state of type <code>S</code> with the given name.
	 */
	protected abstract S createNewState(String name, O element) ;
	
	/**
	 * Creates a new relation of type <code>T</code> from the given source and target states.
	 * This method is abstract because only subclasses know the type <code>T</code> of their relations.
	 * @param sourceState The state where the relation starts
	 * @param targetState The state where the relation ends
	 * @return A new relation of type <code>T</code>.
	 */
	public abstract T createNewTransitionRelation(S sourceState, S targetState) ;
	
	/**
	 * Creates a new transition system instance.<br>
	 * This method is abstract because only subclasses know their own type.
	 * @return A new transition system instance.
	 */
	public abstract AbstractTransitionSystem<S,T,O> createNewInstance();
	
	@Override
	protected S createNewVertex(String name, O element) {
		return createNewState(name, element);
	}

	@Override
	protected String getDefaultName() {
		return "Transition system";
	}

	public boolean addState(String stateName) {
		return super.addVertex(stateName);
	}
	
	public boolean addState(String stateName, O element) {
		return super.addVertex(stateName, element);
	}
	
	public S getState(String name){
		return super.getVertex(name);
	}
	
	public boolean containsState(String stateName){
		return getState(stateName) != null;
	}
	
	public int getStateCount(){
		return super.getVertexCount();
	}
	
	public Collection<S> getStates(){
		return super.getVertices();
	}
	
	public Set<String> getStateNames(){
		return super.getVertexNames();
	}
	
	public boolean isStartState(String stateName){
		return startStates.containsKey(stateName);
	}
	
	public boolean addStartState(String stateName){
		if(containsVertex(stateName)){
			startStates.put(stateName, getState(stateName));
			return true;
		}
		return false;
	}
	
	public boolean removeStartState(String stateName){
		return startStates.remove(stateName) != null;
	}
	
	public Set<String> getStartStateNames(){
		return Collections.unmodifiableSet(startStates.keySet());
	}
	
	public Collection<S> getStartStates(){
		return Collections.unmodifiableCollection(startStates.values());
	}
	
	public boolean isEndState(String stateName){
		return endStates.containsKey(stateName);
	}
	
	public boolean addEndState(String stateName){
		if(containsVertex(stateName)){
			endStates.put(stateName, getState(stateName));
			return true;
		}
		return false;
	}
	
	@Override
	protected T createNewEdge(S sourceVertex, S targetVertex)  {
		return createNewTransitionRelation(sourceVertex, targetVertex);
	}
	
	public boolean removeState(String stateName){
		try {
			super.removeVertex(stateName);
			removeStartState(stateName);
			removeEndState(stateName);
			return true;
		} catch (GraphException e) {
			// State is not part of the transition system
			return false;
		}
	}
	
	public boolean removeRelation(String sourceName, String targetName) throws StateNotFoundException, RelationNotFoundException {
		try {
			return super.removeEdge(sourceName, targetName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		} catch (EdgeNotFoundException e) {
			throw new RelationNotFoundException(e);
		}
	}
	
	public Set<T> getRelationsFor(String stateName){
		return super.getEdgesFor(stateName);
	}
	
	protected T getRelation(String sourceName, String targetName) throws StateNotFoundException, RelationNotFoundException {
		try {
			return super.getEdge(sourceName, targetName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		} catch (EdgeNotFoundException e) {
			throw new RelationNotFoundException(e);
		}
	}

	@Override
	public boolean removeVertex(String stateName) {
		throw new UnsupportedOperationException("Use removeState() instead.");
	}

	public boolean removeEndState(String stateName){
		return endStates.remove(stateName) != null;
	}
	
	public Set<String> getEndStateNames(){
		return Collections.unmodifiableSet(endStates.keySet());
	}
	
	public Collection<S> getEndStates(){
		return Collections.unmodifiableCollection(endStates.values());
	}
	
	public Set<S> getNonLeafEndStates(){
		Set<S> nonLeafEndStates = new HashSet<S>(endStates.values());
		for(S endState: endStates.values()){
			try {
				if(getOutgoingRelationsFor(endState.getName()).isEmpty())
					nonLeafEndStates.remove(endState);
			} catch (StateNotFoundException e) {
				e.printStackTrace();
			}
		}
		return nonLeafEndStates;
	}
	
	public Set<String> getNonLeafEndStateNames(){
		Set<String> result = new HashSet<String>();
		for(S nonLeafEndState: getNonLeafEndStates()){
			result.add(nonLeafEndState.getName());
		}
		return result;
	}
	
	
	public boolean hasSeparatedStates(){
		return hasSeparatedVertices();
	}
	
	public Set<S> getSeparatedStates(){
		return getSeparatedVertices();
	}

	@Override
	public boolean containsObject(Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAllObjects(Collection<? extends Object> elements) {
		throw new UnsupportedOperationException();
	}

	public T addRelation(String sourceStateName, String targetStateName) throws StateNotFoundException {
		try {
			return super.addEdge(sourceStateName, targetStateName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
	}
	
	public boolean containsRelation(String sourceStateName, String targetStateName){
		return super.containsEdge(sourceStateName, targetStateName);
	}
	
	public List<T> getRelations(){
		return super.getEdges();
	}
	
	public int getRelationCount(){
		return super.getEdgeCount();
	}
	
	public List<T> getIncomingRelationsFor(String stateName) throws StateNotFoundException {
		try {
			return super.getIncomingEdgesFor(stateName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
	}
	
	public List<S> getSourcesFor(String stateName) throws EventNotFoundException, StateNotFoundException{
		List<S> result = new ArrayList<S>();
		try {
			if(hasIncomingEdges(stateName)){
				for(AbstractTransitionRelation<S,O> relation: getIncomingRelationsFor(stateName)){
					result.add(relation.getSource());
				}
			}
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
		return result;
	}
	
	public List<T> getOutgoingRelationsFor(String stateName) throws StateNotFoundException{
		try {
			return super.getOutgoingEdgesFor(stateName);
		} catch(VertexNotFoundException e){
			throw new StateNotFoundException(e);
		}
	}
	
	public List<S> getTargetsFor(String stateName) throws StateNotFoundException{
		List<S> result = new ArrayList<S>();
		try {
			if(hasOutgoingEdges(stateName)){
				for(AbstractTransitionRelation<S,O> relation: getOutgoingRelationsFor(stateName)){
					result.add(relation.getTarget());
				}
			}
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
		return result;
	}
	
//	public void setAlternativeName(S state, String name){
//		alternativeNames.put(state, name);
//	}
//	
//	public String getAlternativeNameFor(S state){
//		return alternativeNames.get(state);
//	}
//	
//	public Set<String> getAlternativeNamesFor(Collection<S> states){
//		Set<String> result = new HashSet<String>();
//		for(S state: states){
//			result.add(getAlternativeNameFor(state));
//		}
//		return result;
//	}
//	
//	public boolean existsAlternativeNameFor(S state){
//		return alternativeNames.keySet().contains(state);
//	}
//	
//	public boolean existAlternativeNamesForAllStates(Collection<S> states){
//		for(S state: states){
//			if(!existsAlternativeNameFor(state)){
//				return false;
//			}
//		}
//		return true;
//	}
//	
//	public boolean existAlternativeNamesForAllStates(){
//		return alternativeNames.keySet().containsAll(getVertexes());
//	}
//	
//	public Collection<String> getAlternativeNames(){
//		return Collections.unmodifiableCollection(alternativeNames.values());
//	}
	
	public AbstractTransitionSystem<S,T,O> clone(){
		AbstractTransitionSystem<S,T,O> result = createNewInstance();
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
			for(T ownRelation: getRelations()){
				result.addRelation(ownRelation.getSource().getName(), ownRelation.getTarget().getName());
			}
		} catch (StateNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	protected void transferContent(S existingState, S newState){
		newState.setElement(existingState.getElement());
	}
	
	public void validateState(String stateName) throws StateNotFoundException{
		try {
			super.validateVertex(stateName);
		} catch (VertexNotFoundException e) {
			throw new StateNotFoundException(e);
		}
	}
	
	public String getComplexity(){
		return String.format(complexityFormat, getStateCount(), getRelationCount());
	}
	
	public void printComplexity(){
		System.out.println(getComplexity());
	}
	
	@Override
	public String toString(){
		StringBuilder relations = new StringBuilder();
		for(T relation: getRelations()){
			relations.append(relation.toString());
			relations.append('\n');
		}
		return String.format(toStringFormat, getVertices(), startStates.keySet(), endStates.keySet(), relations.toString());
	}
	
}
