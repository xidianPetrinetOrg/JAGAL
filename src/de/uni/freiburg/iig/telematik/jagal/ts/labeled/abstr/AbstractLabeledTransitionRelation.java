package de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;

public class AbstractLabeledTransitionRelation<S extends AbstractLTSState<E,O>, E extends AbstractEvent, O extends Object> extends AbstractTransitionRelation<S,O> {

	private static final long serialVersionUID = 3424924052967526567L;
	
	private final String toStringFormat = "(%s, %s, %s)";
	private final String toStringFormatSimple = "%s %s %s";
	
	private E event;
	
	protected AbstractLabeledTransitionRelation(){
		super();
	}
	
	protected AbstractLabeledTransitionRelation(S source, S target){
		super(source, target);
	}
	
	protected AbstractLabeledTransitionRelation(S source, S target, E event){
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
	
	public String toStringElements(){
		return String.format(toStringFormat, source.getElement(), event, target.getElement());
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
		AbstractLabeledTransitionRelation other = (AbstractLabeledTransitionRelation) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		return true;
	}

}
