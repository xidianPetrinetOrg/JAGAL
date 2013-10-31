package de.uni.freiburg.iig.telematik.jagal.ts.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.TransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionRelation;


public class TSUtils {
	
	public static <S extends State> boolean haveSameRelations(S s1, S s2){
		return s1.getIncomingEvents().equals(s2.getIncomingEvents()) && s1.getOutgoingEvents().equals(s2.getOutgoingEvents());
	}
	
	public static <S extends State> Set<S> getStates(Collection<TransitionRelation<S>> relations){
		return getStates(new HashSet<TransitionRelation<S>>(relations));
	}
	
	public static <S extends State> Set<S> getStates(Set<TransitionRelation<S>> relations){
		Set<S> result = new HashSet<S>();
		for(TransitionRelation<S> relation: relations){
			result.add(relation.getSource());
			result.add(relation.getTarget());
		}
		return result;
	}
	
	public static <E extends Event, S extends State> Set<TransitionRelation<S>> convert(Set<LabeledTransitionRelation<S,E>> relations){
		Set<TransitionRelation<S>> result = new HashSet<TransitionRelation<S>>();
		for(LabeledTransitionRelation<S,E> relation: relations){
			result.add((TransitionRelation<S>) relation);
		}
		return result;
	}

}
