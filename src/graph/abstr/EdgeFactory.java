package graph.abstr;

import graph.Edge;
import graph.Vertex;

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