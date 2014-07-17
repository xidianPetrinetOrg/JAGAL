package de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;

public abstract class AbstractLTSState<E extends AbstractEvent, O extends Object> extends AbstractState<O> {

	private static final long serialVersionUID = 624171513114845628L;
	
	private Set<E> incomingEvents = new HashSet<E>();
	private Set<E> outgoingEvents = new HashSet<E>();
	
	protected AbstractLTSState(String name){
		super(name);
	}
	
	protected AbstractLTSState(String name, O element){
		super(name, element);
	}
	
	public Set<E> getIncomingEvents(){
		return Collections.unmodifiableSet(incomingEvents);
	}
	
	public boolean addIncomingEvent(E e){
		return incomingEvents.add(e);
	}
	
	public Set<E> getOutgoingEvents(){
		return Collections.unmodifiableSet(outgoingEvents);
	}
	
	public boolean addOutgoingEvent(E e){
		return outgoingEvents.add(e);
	}
	
	public boolean removeIncomingEvent(E e){
		return incomingEvents.remove(e);
	}
	
	public boolean removeOutgoingEvent(E e){
		return outgoingEvents.remove(e);
	}

}
