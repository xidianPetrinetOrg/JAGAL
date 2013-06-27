package ts;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;


public class State extends Vertex<Object> {
	private String name = "<->";
	private Set<Event> incomingEvents = new HashSet<Event>();
	private Set<Event> outgoingEvents = new HashSet<Event>();
	
	public State(){
		super();
	}
	
	public State(String name){
		this();
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Set<Event> getIncomingEvents(){
		return Collections.unmodifiableSet(incomingEvents);
	}
	
	public boolean addIncomingEvent(Event e){
		return incomingEvents.add(e);
	}
	
	public Set<Event> getOutgoingEvents(){
		return Collections.unmodifiableSet(outgoingEvents);
	}
	
	public boolean addOutgoingEvent(Event e){
		return outgoingEvents.add(e);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		State other = (State) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return name;
	}
}
