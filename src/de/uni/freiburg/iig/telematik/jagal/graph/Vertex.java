package de.uni.freiburg.iig.telematik.jagal.graph;


public class Vertex<T extends Object> {
	
	public static final String toStringFormat = "[%s - %s]";
	
	protected String name = null;
	protected T element = null;

	protected Vertex(String name){
		this.name = name;
	}
	
	public Vertex(String name, T element){
		this.name = name;
		this.element = element;
	}
	
	public T getElement(){
		return element;
	}
	
	public void setElement(T element){
		this.element = element;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
//		return String.format(toStringFormat, name, element);
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

        @Override
	public Vertex<T> clone(){
		return new Vertex<>(name, element);
	}
	
}
