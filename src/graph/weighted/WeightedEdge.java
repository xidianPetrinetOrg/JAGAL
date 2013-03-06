package graph.weighted;

import graph.Edge;
import graph.Vertex;


/**
 * Represents an edge that maintains a weight.
 * 
 * @author Thomas Stocker
 *
 */
public class WeightedEdge<V extends Vertex<? extends Object>> extends Edge<V> implements Comparable<WeightedEdge<V>>{
	
	public static final int DEFAULT_WEIGHT = 1;
	private double weight = DEFAULT_WEIGHT;
	
	public WeightedEdge(){}
	
	public WeightedEdge(V source, V target){
		super(source, target);
	}
	
	public WeightedEdge(V source, V target, double weight){
		this(source, target);
		this.weight = weight;
	}
	
	/**
	 * Returns the weight of this edge.
	 * @return The edge weight.
	 */
	public double getWeight(){
		return weight;
	}
	
	/**
	 * Sets the weight of this edge.
	 * @param weight Edge weight.
	 */
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	public WeightedEdge<V> clone(){
		return new WeightedEdge<V>(source, target, weight);
	}

	@Override
	public int compareTo(WeightedEdge<V> o) {
		if (this == o)
			   return 0;
			if (o == null)
			   return 0;
			if (o.getClass() != getClass())
			   return 0;
			
			if(this.weight<((WeightedEdge<V>) o).getWeight()){
				return 1;
			}
			if(this.weight>((WeightedEdge<V>) o).getWeight()){
				return -1;
			}
			return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		@SuppressWarnings("unchecked")
		WeightedEdge<V> other = (WeightedEdge<V>) obj;
		if (Double.doubleToLongBits(weight) != Double
				.doubleToLongBits(other.weight))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return "("+source+"-"+weight+"->"+target+")";
	}
	
}
