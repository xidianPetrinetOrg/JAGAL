package de.uni.freiburg.iig.telematik.jagal.ts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;


public class MarkingStatePairContainer<S extends AbstractState<O>, O> extends StatePairContainer<S,O> {
	private final Set<StatePair<S,O>> markedPairs = new HashSet<>();
	private final Set<StatePair<S,O>> unmarkedPairs = new HashSet<>();
	
	public MarkingStatePairContainer(){
		super();
	}

        public static <S extends AbstractState<O>, O> MarkingStatePairContainer<S, O> newInstance(StatePair<S, O>... statePairs) {
                MarkingStatePairContainer<S, O> container = new MarkingStatePairContainer<>();
                for (StatePair<S, O> statePair : statePairs) {
                        container.addStatePair(statePair);
                }
                return container;
        }

        public static <S extends AbstractState<O>, O> MarkingStatePairContainer<S, O> newInstance(Collection<StatePair<S, O>> statePairs) {
                MarkingStatePairContainer<S, O> container = new MarkingStatePairContainer<>();
                for (StatePair<S, O> statePair : statePairs) {
                        container.addStatePair(statePair);
                }
                return container;
        }
	
	public Set<StatePair<S,O>> getMarkedStatePairs(){
		return Collections.unmodifiableSet(markedPairs);
	}
	
	public Set<StatePair<S,O>> getUnmarkedStatePairs(){
		return Collections.unmodifiableSet(unmarkedPairs);
	}
	
	
	public void markPair(S state1, S state2){
		StatePair<S,O> testPair = getStatePair(state1, state2);
		if(testPair != null){
			markedPairs.add(testPair);
			unmarkedPairs.remove(testPair);
		}
	}
	
	public boolean isMarked(S state1, S state2){
		return markedPairs.contains(new StatePair<>(state1, state2));
	}
	
	public void markPair(StatePair<S,O> statePair){
		if(statePairs.contains(statePair)){
			markedPairs.add(getEqualStatePair(statePair));
			unmarkedPairs.remove(getEqualStatePair(statePair));
		}
	}

	@Override
	public boolean addStatePair(StatePair<S,O> statePair){
		boolean isNewElement =  super.addStatePair(statePair);
		if(isNewElement){
			if(unmarkedPairs == null){
				System.out.println("set null");
			} else {
				unmarkedPairs.add(statePair);
			}
		}
		return isNewElement;
	}
	
	public static void main(String[] args){
		MarkingStatePairContainer<State,Object> c = new MarkingStatePairContainer<>();
		c.addStatePair(new State("s0"), new State("s5"));
		System.out.println(c.getStatePairs());
		StatePair<State,Object> p1 = new StatePair<>(new State("z4"), new State("z3"));
		StatePair<State,Object> p2 = new StatePair<>(new State("z4"), new State("z2"));
		System.out.println(p1.hashCode());
		System.out.println(p2.hashCode());
//		c.addStatePair(p1);
//		System.out.println(c.containsStatePair(new State("s0"), new State("s1")));
		System.out.println(c.containsStatePair(new State("s0"), new State("s5")));
	}
	
}