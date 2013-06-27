package de.uni.freiburg.iig.telematik.jagal.ts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.misc.SetUtils;



public class StatePairContainer<S extends State> {
	
	protected Set<StatePair<S>> statePairs = new HashSet<StatePair<S>>();
	
	public StatePairContainer(){}
	
	public StatePairContainer(StatePair<S>... statePairs){
		for(StatePair<S> statePair: statePairs){
			addStatePair(statePair);
		}
	}
	
	public StatePairContainer(Collection<StatePair<S>> statePairs){
		for(StatePair<S> statePair: statePairs){
			addStatePair(statePair);
		}
	}
	
	public Set<StatePair<S>> getStatePairs(){
		return Collections.unmodifiableSet(statePairs);
	}
	
	public boolean addStatePair(S state1, S state2){
		return addStatePair(StatePair.createStatePair(state1, state2));
	}
	
	public boolean addStatePair(StatePair<S> statePair){
		return statePairs.add(statePair);
	}
	
	public boolean containsStatePair(S state1, S state2){
		return getStatePair(state1, state1) != null;
	}

	public StatePair<S> getStatePair(S state1, S state2){
		return getEqualStatePair(new StatePair<S>(state1, state2));
	}
	
	protected StatePair<S> getEqualStatePair(StatePair<S> statePair){
		for(StatePair<S> pair: statePairs){
			if(statePair.equals(pair)){
				return pair;
			}
		}
		return null;
	}
	
	public static <S extends State> Set<StatePair<S>> getStatePairsFrom(Set<S> states){
		Set<StatePair<S>> result = new HashSet<StatePair<S>>(); 
		for(List<S> statePair: SetUtils.getKElementarySets(states, 2)){
			result.add(new StatePair<S>(statePair.get(0), statePair.get(1)));
		}
		return result;
	}
	
}