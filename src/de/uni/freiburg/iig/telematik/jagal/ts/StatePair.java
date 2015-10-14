package de.uni.freiburg.iig.telematik.jagal.ts;

import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;


public class StatePair<S extends AbstractState<O>, O> {
	private final String toStringFormat = "(%s|%s)";
	
	public S state1;
	public S state2;
	private final Set<S> stateSet = new HashSet<>();

	public StatePair(S state1, S state2){
		this.state1 = state1;
		this.state2 = state2;
		stateSet.add(state1);
		stateSet.add(state2);
	}
	
	public static <SS extends AbstractState<OO>, OO extends Object> StatePair<SS,OO> createStatePair(SS state1, SS state2){
		return new StatePair<>(state1, state2);
	}
	
	@Override
	public String toString(){
		return String.format(toStringFormat, state1, state2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((stateSet == null) ? 0 : stateSet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		StatePair other = (StatePair) obj;
		if (stateSet == null) {
			if (other.stateSet != null)
				return false;
		} else if (!stateSet.equals(other.stateSet))
			return false;
		return true;
	}
	
}