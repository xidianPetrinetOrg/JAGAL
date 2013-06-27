package de.uni.freiburg.iig.telematik.jagal.ts.labeled;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.TransitionRelation;

public class LabeledTransitionRelation<S extends State, E extends Event> extends TransitionRelation<S>{
	
	private final String toStringFormat = "(%s, %s, %s)";
	private final String toStringFormatSimple = "%s %s %s";
	
	private E event;
	
	public LabeledTransitionRelation(){
		super();
	}
	
	public LabeledTransitionRelation(S source, S target){
		super(source, target);
	}
	
	public LabeledTransitionRelation(S source, S target, E event){
		super(source, target);
		this.event = event;
	}
	
	public void setEvent(E event){
		if(event == null)
			throw new NullPointerException();
		this.event = event;
	}
	
	public E getEvent(){
		return event;
	}
	
	public String toSimpleString(){
		return String.format(toStringFormatSimple, source, event, target);
	}
	
	@Override
	public String toString(){
		return String.format(toStringFormat, source, event, target);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		LabeledTransitionRelation other = (LabeledTransitionRelation) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		return true;
	}
	
}
