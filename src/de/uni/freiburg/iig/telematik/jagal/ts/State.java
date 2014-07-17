package de.uni.freiburg.iig.telematik.jagal.ts;


import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;


public class State extends AbstractState<Object> {
	
	private static final long serialVersionUID = 3148194296398299428L;

	public State(String name){
		super(name);
	}
	
	public State(String name, Object element) {
		super(name, element);
	}
	
}
