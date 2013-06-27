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
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.EventNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.TransitionRelation;


public abstract class AbstractTransitionSystem<S extends State, T extends TransitionRelation<S>> extends AbstractGraph<S, T, Object> {
	
	private final String toStringFormat = "TS = {S, T, S_start, S_end}\n  S       = %s\n  S_start = %s\n  S_end   = %s\n  T       = %s\n";
	
	protected Set<S> endStates = new HashSet<S>();
	protected Set<S> startStates = new HashSet<S>(); 
	
	private final String ALTERNATIVE_NAME_FORMAT = "s%s";
	private Map<S, String> alternativeNames = new HashMap<S, String>();
	
	public AbstractTransitionSystem() {
		super();
	}
	
	public AbstractTransitionSystem(String name){
		super(name);
	}
	
	public AbstractTransitionSystem(Collection<S> states){
		super(states);
	}
	
	public AbstractTransitionSystem(String name, Collection<S> states){
		super(name, states);
	}
	
	
	/**
	 * This method is never called.
	 */
	@Override
	protected S createNewVertex(Object element) {
		return null;
	}

	@Override
	protected String getDefaultName() {
		return "Transition system";
	}

	public boolean addState(S state){
		return addState(state, false);
	}
	
	public boolean addState(S state, boolean generateCanonicalName){
		boolean success = super.addVertex(state);
		if(generateCanonicalName && success){
			alternativeNames.put(state, String.format(ALTERNATIVE_NAME_FORMAT, alternativeNames.keySet().size()));
		}
		return success;
	}
	
	public Set<S> getStates(){
		return super.getVertexes();
	}
	
	public S getEqualState(S state) throws VertexNotFoundException{
		return super.getEqualVertex(state);
	}
	
	public boolean isStartState(S state){
		return startStates.contains(state);
	}
	
	public boolean addStartState(S state){
		if(super.contains(state)){
			return startStates.add(state);
		}
		return false;
	}
	
	public boolean removeStartState(S state){
		return startStates.remove(state);
	}
	
	public Set<S> getStartStates(){
		return Collections.unmodifiableSet(startStates);
	}
	
	public boolean isEndState(S state){
		return endStates.contains(state);
	}
	
	public boolean addEndState(S state){
		if(super.contains(state)){
			return endStates.add(state);
		}
		return false;
	}
	
	public boolean removeEndState(S state){
		return endStates.remove(state);
	}
	
	public Set<S> getEndStates(){
		return Collections.unmodifiableSet(endStates);
	}
	
	public boolean hasSeparatedStates(){
		return hasSeparatedVertexes();
	}
	
	public Set<S> getSeparatedStates(){
		return getSeparatedVertexes();
	}

	@Override
	public T addEdge(Object sourceObject, Object targetObject) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAllElements(Collection<Object> elements) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addElement(Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsObject(Object element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAllObjects(Collection<? extends Object> elements) {
		throw new UnsupportedOperationException();
	}

	public T addRelation(S sourceState, S targetState) throws VertexNotFoundException {
		return super.addEdge(sourceState, targetState);
	}
	
	public boolean containsRelation(S sourceState, S targetState){
		return super.containsEdge(sourceState, targetState);
	}
	
	public List<T> getRelations(){
		return super.getEdges();
	}
	
	public List<T> getIncomingRelationsFor(S state) throws VertexNotFoundException{
		return super.getIncomingEdgesFor(state);
	}
	
	public List<T> getOutgoingRelationsFor(S state) throws VertexNotFoundException{
		return super.getOutgoingEdgesFor(state);
	}
	
	public List<S> getTargetsFor(S state) throws VertexNotFoundException, EventNotFoundException{
		S equalState = getEqualState(state);
		
		List<S> result = new ArrayList<S>();
		if(hasOutgoingEdges(equalState)){
			for(TransitionRelation<S> relation: getOutgoingRelationsFor(equalState)){
				result.add(relation.getTarget());
			}
		}
		return result;
	}
	
	public void setAlternativeName(S state, String name){
		alternativeNames.put(state, name);
	}
	
	public String getAlternativeNameFor(S state){
		return alternativeNames.get(state);
	}
	
	public Set<String> getAlternativeNamesFor(Collection<S> states){
		Set<String> result = new HashSet<String>();
		for(S state: states){
			result.add(getAlternativeNameFor(state));
		}
		return result;
	}
	
	public boolean existsAlternativeNameFor(S state){
		return alternativeNames.keySet().contains(state);
	}
	
	public boolean existAlternativeNamesForAllStates(Collection<S> states){
		for(S state: states){
			if(!existsAlternativeNameFor(state)){
				return false;
			}
		}
		return true;
	}
	
	public boolean existAlternativeNamesForAllStates(){
		return alternativeNames.keySet().containsAll(getVertexes());
	}
	
	public Collection<String> getAlternativeNames(){
		return Collections.unmodifiableCollection(alternativeNames.values());
	}
	
	@Override
	public String toString(){
		StringBuilder relations = new StringBuilder();
		for(T relation: getRelations()){
			relations.append(relation.toString());
			relations.append('\n');
		}
		return String.format(toStringFormat, getVertexes(), startStates, endStates, relations.toString());
	}
	
}
