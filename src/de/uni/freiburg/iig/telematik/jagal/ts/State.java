package de.uni.freiburg.iig.telematik.jagal.ts;


import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;


public class State extends AbstractState<Object> {
	
	public State(String name){
		super(name);
	}
	
	public State(String name, Object element) {
		super(name, element);
	}
	
}
