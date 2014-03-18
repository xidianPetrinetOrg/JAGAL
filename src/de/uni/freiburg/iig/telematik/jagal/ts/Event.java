package de.uni.freiburg.iig.telematik.jagal.ts;

import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;


public class Event extends AbstractEvent{
	
	public Event(String name){
		super(name);
	}
	
	public Event(String name, String label){
		super(name, label);
	}
	
	public Event(String name, String label, boolean isLambdaEvent){
		super(name, label, isLambdaEvent);
	}
	
	@Override
	public Event clone(){
		return new Event(getName(), getLabel(), isLambdaEvent());
	}
	
	@Override
	public Event clone(int index){
		return new Event(getName() + index, getLabel(), isLambdaEvent());
	}

}
