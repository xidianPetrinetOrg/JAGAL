package de.uni.freiburg.iig.telematik.jagal.graph;


public class Vertex<T extends Object> {
	
	protected T element = null;
	
	protected Vertex(){}
	
	public Vertex(T element){
		this.element = element;
	}
	
	public T getElement(){
		return element;
	}
	
	public void setElement(T element){
		this.element = element;
	}
	
	@Override
	public String toString(){
		return element.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
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
		@SuppressWarnings("unchecked")
		Vertex<T> other = (Vertex<T>) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		return true;
	}
	
	public Vertex<T> clone(){
		return new Vertex<T>(element);
	}
	
}
