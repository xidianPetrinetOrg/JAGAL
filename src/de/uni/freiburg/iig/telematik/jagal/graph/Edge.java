package de.uni.freiburg.iig.telematik.jagal.graph;


public class Edge<V extends Vertex<? extends Object>> {
	
	protected V source;
	protected V target;
	
	public Edge(){}
	
	public Edge(V source, V target){
		this.source = source;
		this.target = target;
	}
	
	public V getSource(){
		return source;
	}
	
	public void setSource(V source){
		this.source = source;
	}
	
	public V getTarget(){
		return target;
	}
	
	public void setTarget(V target){
		this.target = target;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		@SuppressWarnings("rawtypes")
		Edge other = (Edge) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	public Edge<V> clone(){
		return new Edge<V>(source, target);
	}

	public String toString(){
		return "("+source+" -> "+target+")";
	}

}
