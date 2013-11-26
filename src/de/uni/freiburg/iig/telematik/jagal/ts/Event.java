package de.uni.freiburg.iig.telematik.jagal.ts;


public class Event {
	
	protected String name;
	protected String label = "";
	
	private boolean isLambdaEvent = false;
	
	public Event(String name){
		setName(name);
		setLabel(name);
	}
	
	public Event(String name, String label){
		setName(name);
		setLabel(label);
	}
	
	public Event(String name, String label, boolean isLambdaEvent){
		this(name, label);
		setLambdaEvent(isLambdaEvent);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public boolean isLambdaEvent() {
		return isLambdaEvent;
	}

	public void setLambdaEvent(boolean isLambdaEvent) {
		this.isLambdaEvent = isLambdaEvent;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	public Event clone(){
		return new Event(getName(), getLabel(), isLambdaEvent());
	}
	
	public Event clone(int index){
		return new Event(getName() + index, getLabel(), isLambdaEvent());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
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
