package de.uni.freiburg.iig.telematik.jagal.ts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.misc.SetUtils;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;



public class StatePairContainer<S extends AbstractState<O>, O> {
	
	protected Set<StatePair<S,O>> statePairs = new HashSet<StatePair<S,O>>();
	
	public StatePairContainer(){}
	
	public StatePairContainer(StatePair<S,O>... statePairs){
		for(StatePair<S,O> statePair: statePairs){
			addStatePair(statePair);
		}
	}
	
	public StatePairContainer(Collection<StatePair<S,O>> statePairs){
		for(StatePair<S,O> statePair: statePairs){
			addStatePair(statePair);
		}
	}
	
	public Set<StatePair<S,O>> getStatePairs(){
		return Collections.unmodifiableSet(statePairs);
	}
	
	public boolean addStatePair(S state1, S state2){
		return addStatePair(StatePair.createStatePair(state1, state2));
	}
	
	public boolean addStatePair(StatePair<S,O> statePair){
		return statePairs.add(statePair);
	}
	
	public boolean containsStatePair(S state1, S state2){
		return getStatePair(state1, state1) != null;
	}

	public StatePair<S,O> getStatePair(S state1, S state2){
		return getEqualStatePair(new StatePair<S,O>(state1, state2));
	}
	
	protected StatePair<S,O> getEqualStatePair(StatePair<S,O> statePair){
		for(StatePair<S,O> pair: statePairs){
			if(statePair.equals(pair)){
				return pair;
			}
		}
		return null;
	}
	
	public static <S extends AbstractState<O>, O> Set<StatePair<S,O>> getStatePairsFrom(Collection<S> states){
		Set<S> stateSet = new HashSet<S>(states);
		Set<StatePair<S,O>> result = new HashSet<StatePair<S,O>>(); 
		for(List<S> statePair: SetUtils.getKElementarySets(stateSet, 2)){
			result.add(new StatePair<S,O>(statePair.get(0), statePair.get(1)));
		}
		return result;
	}
	
}