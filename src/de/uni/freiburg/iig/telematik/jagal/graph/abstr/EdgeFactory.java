package de.uni.freiburg.iig.telematik.jagal.graph.abstr;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public class EdgeFactory<V extends Vertex<U>, E extends Edge<V>, U>{

	private final Class<? extends E> edgeClass;

	public EdgeFactory(Class<? extends E> edgeClass){
		this.edgeClass = edgeClass;
	}

	public E createEdge(){
		try {
			return edgeClass.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Edge factory failed", ex);
		}
	}
}