package de.uni.freiburg.iig.telematik.jagal.ts.abstr;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.EventNotFoundException;


public abstract class AbstractTransitionSystem<S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> extends AbstractGraph<S, T, O> {
	
	private final String toStringFormat = "TS = {S, T, S_start, S_end}\n  S       = %s\n  S_start = %s\n  S_end   = %s\n  T       = %s\n";
	
	protected Map<String, S> endStates = new HashMap<String, S>();
	protected Map<String, S> startStates = new HashMap<String, S>(); 
	
//	private final String ALTERNATIVE_NAME_FORMAT = "s%s";
//	private Map<String, String> alternativeNames = new HashMap<String, String>();
	
	public AbstractTransitionSystem() {
		super();
	}
	
	public AbstractTransitionSystem(String name) throws ParameterException{
		super(name);
	}
	
	public AbstractTransitionSystem(Collection<String> stateNames) throws ParameterException{
		super(stateNames);
	}
	
	public AbstractTransitionSystem(String name, Collection<String> stateNames) throws ParameterException{
		super(name, stateNames);
	}
	
	/**
	 * Creates a new state of type <code>S</code> with the given name.<br>
	 * This method is abstract because only subclasses know the type <code>S</code> of their states.
	 * @param name The name for the new state.
	 * @return A new state of type <code>S</code> with the given name.
	 */
	protected abstract S createNewState(String name, O element) throws ParameterException;
	
	/**
	 * Creates a new relation of type <code>T</code> from the given source and target states.
	 * This method is abstract because only subclasses know the type <code>T</code> of their relations.
	 * @param sourceState The state where the relation starts
	 * @param targetState The state where the relation ends
	 * @return A new relation of type <code>T</code>.
	 */
	public abstract T createNewTransitionRelation(S sourceState, S targetState) throws ParameterException;
	
	/**
	 * Creates a new transition system instance.<br>
	 * This method is abstract because only subclasses know their own type.
	 * @return A new transition system instance.
	 */
	public abstract AbstractTransitionSystem<S,T,O> createNewInstance();
	
	@Override
	protected S createNewVertex(String name, O element) throws ParameterException{
		return createNewState(name, element);
	}

	@Override
	protected String getDefaultName() {
		return "Transition system";
	}

	public boolean addState(String stateName) throws ParameterException{
		return super.addVertex(stateName);
	}
	
	public boolean addState(String stateName, O element) throws ParameterException{
		return super.addVertex(stateName, element);
	}
	
//	public boolean addState(String stateName, boolean generateCanonicalName){
//		boolean success = super.addVertex(stateName);
//		if(generateCanonicalName && success){
//			String canonicalName = String.format(ALTERNATIVE_NAME_FORMAT, alternativeNames.keySet().size());
//			alternativeNames.put(stateName, canonicalName);
//		}
//		return success;
//	}
	
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
	
	public boolean removeState(String stateName){
		try {
			super.removeVertex(stateName);
//			alternativeNames.remove(state);
			removeStartState(stateName);
			removeEndState(stateName);
			return true;
		} catch (GraphException e) {
			// State is not part of the transition system
			return false;
		}
	}
	
	@Override
	protected T createNewEdge(S sourceVertex, S targetVertex) throws ParameterException {
		return createNewTransitionRelation(sourceVertex, targetVertex);
	}

	@Override
	public boolean removeVertex(String stateName) throws GraphException {
		if(super.removeVertex(stateName)){
			removeStartState(stateName);
			removeEndState(stateName);
			return true;
		}
		return false;
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
	
	public boolean hasSeparatedStates(){
		return hasSeparatedVertices();
	}
	
	public Set<S> getSeparatedStates(){
		return getSeparatedVertices();
	}

//	@Override
//	public T addEdge(Object sourceObject, Object targetObject) {
//		throw new UnsupportedOperationException();
//	}
//	
//	@Override
//	public boolean addAllElements(Collection<Object> elements) {
//		throw new UnsupportedOperationException();
//	}
//	
//	@Override
//	public boolean addElement(Object element) {
//		throw new UnsupportedOperationException();
//	}

	@Override
	public boolean containsObject(Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAllObjects(Collection<? extends Object> elements) {
		throw new UnsupportedOperationException();
	}

	public T addRelation(String sourceStateName, String targetStateName) throws VertexNotFoundException, ParameterException {
		return super.addEdge(sourceStateName, targetStateName);
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
	
	public List<T> getIncomingRelationsFor(String stateName) throws VertexNotFoundException{
		return super.getIncomingEdgesFor(stateName);
	}
	
	public List<S> getSourcesFor(String stateName) throws VertexNotFoundException, EventNotFoundException{
		List<S> result = new ArrayList<S>();
		if(hasIncomingEdges(stateName)){
			for(AbstractTransitionRelation<S,O> relation: getIncomingRelationsFor(stateName)){
				result.add(relation.getSource());
			}
		}
		return result;
	}
	
	public List<T> getOutgoingRelationsFor(String stateName) throws VertexNotFoundException{
		return super.getOutgoingEdgesFor(stateName);
	}
	
	public List<S> getTargetsFor(String stateName) throws VertexNotFoundException, EventNotFoundException{
		List<S> result = new ArrayList<S>();
		if(hasOutgoingEdges(stateName)){
			for(AbstractTransitionRelation<S,O> relation: getOutgoingRelationsFor(stateName)){
				result.add(relation.getTarget());
			}
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
		}catch(ParameterException e){
			e.printStackTrace();
		} catch (VertexNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	protected void transferContent(S existingState, S newState){
		newState.setElement(existingState.getElement());
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
