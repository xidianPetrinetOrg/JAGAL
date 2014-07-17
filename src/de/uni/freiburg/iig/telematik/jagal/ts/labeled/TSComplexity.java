package de.uni.freiburg.iig.telematik.jagal.ts.labeled;

import java.io.Serializable;

public class TSComplexity implements Serializable {
	
	private static final long serialVersionUID = 2341126394227035338L;

	private static final String complexityFormat = "|S| = %s, |E| = %s, |T| = %s";

	public int numStates = -1;
	public int numEvents = -1;
	public int numTransitions = -1;
	
	public TSComplexity(int numStates, int numEvents, int numTransitions) {
		super();
		this.numStates = numStates;
		this.numEvents = numEvents;
		this.numTransitions = numTransitions;
	}
	
	@Override
	public String toString(){
		return String.format(complexityFormat, numStates, numEvents, numTransitions);
	}
	
}
