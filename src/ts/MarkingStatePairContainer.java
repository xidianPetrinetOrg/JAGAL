package ts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class MarkingStatePairContainer<S extends State> extends StatePairContainer<S> {
	private Set<StatePair<S>> markedPairs = new HashSet<StatePair<S>>();
	private Set<StatePair<S>> unmarkedPairs = new HashSet<StatePair<S>>();
	
	public MarkingStatePairContainer(){
		super();
	}
	
	public MarkingStatePairContainer(StatePair<S>... statePairs){
		super();
		for(StatePair<S> statePair: statePairs){
			addStatePair(statePair);
		}
	}
	
	public MarkingStatePairContainer(Collection<StatePair<S>> statePairs){
		super();
		for(StatePair<S> statePair: statePairs){
			addStatePair(statePair);
		}
	}
	
	public Set<StatePair<S>> getMarkedStatePairs(){
		return Collections.unmodifiableSet(markedPairs);
	}
	
	public Set<StatePair<S>> getUnmarkedStatePairs(){
		return Collections.unmodifiableSet(unmarkedPairs);
	}
	
	
	public void markPair(S state1, S state2){
		StatePair<S> testPair = getStatePair(state1, state2);
		if(testPair != null){
			markedPairs.add(testPair);
			unmarkedPairs.remove(testPair);
		}
	}
	
	public boolean isMarked(S state1, S state2){
		return markedPairs.contains(new StatePair<S>(state1, state2));
	}
	
	public void markPair(StatePair<S> statePair){
		if(statePairs.contains(statePair)){
			markedPairs.add(getEqualStatePair(statePair));
			unmarkedPairs.remove(getEqualStatePair(statePair));
		}
	}

	@Override
	public boolean addStatePair(StatePair<S> statePair){
		boolean isNewElement =  super.addStatePair(statePair);
		if(isNewElement){
			if(unmarkedPairs == null){
				System.out.println("set null");
			}
			unmarkedPairs.add(statePair);
		}
		return isNewElement;
	}
	
	public static void main(String[] args){
		MarkingStatePairContainer<State> c = new MarkingStatePairContainer<State>();
		c.addStatePair(new State("s0"), new State("s5"));
		System.out.println(c.getStatePairs());
		StatePair<State> p1 = new StatePair<State>(new State("z4"), new State("z3"));
		StatePair<State> p2 = new StatePair<State>(new State("z4"), new State("z2"));
		System.out.println(p1.hashCode());
		System.out.println(p2.hashCode());
//		c.addStatePair(p1);
//		System.out.println(c.containsStatePair(new State("s0"), new State("s1")));
		System.out.println(c.containsStatePair(new State("s0"), new State("s5")));
	}
	
}