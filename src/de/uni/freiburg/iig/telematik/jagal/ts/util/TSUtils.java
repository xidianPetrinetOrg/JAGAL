package de.uni.freiburg.iig.telematik.jagal.ts.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;


public class TSUtils {
	
	public static <S extends LTSState> boolean haveSameRelations(S s1, S s2){
		return s1.getIncomingEvents().equals(s2.getIncomingEvents()) && s1.getOutgoingEvents().equals(s2.getOutgoingEvents());
	}
	
	public static <S extends AbstractState<O>, O> Set<S> getStates(Collection<AbstractTransitionRelation<S,O>> relations){
		return getStates(new HashSet<AbstractTransitionRelation<S,O>>(relations));
	}
	
	public static <S extends AbstractState<O>, O> Set<S> getStates(Set<AbstractTransitionRelation<S,O>> relations){
		Set<S> result = new HashSet<S>();
		for(AbstractTransitionRelation<S,O> relation: relations){
			result.add(relation.getSource());
			result.add(relation.getTarget());
		}
		return result;
	}
	
	public static <E extends AbstractEvent, S extends AbstractLTSState<E,O>, O> Set<AbstractTransitionRelation<S,O>> convert(Set<AbstractLabeledTransitionRelation<S,E,O>> relations){
		Set<AbstractTransitionRelation<S,O>> result = new HashSet<AbstractTransitionRelation<S,O>>();
		for(AbstractLabeledTransitionRelation<S,E,O> relation: relations){
			result.add((AbstractTransitionRelation<S,O>) relation);
		}
		return result;
	}

}
