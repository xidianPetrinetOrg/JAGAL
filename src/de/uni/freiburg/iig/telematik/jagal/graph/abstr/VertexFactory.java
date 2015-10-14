package de.uni.freiburg.iig.telematik.jagal.graph.abstr;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public class VertexFactory<V extends Vertex<U>, U>{

	private final Class<? extends V> vertexClass;

	public VertexFactory(Class<? extends V> vertexClass){
		this.vertexClass = vertexClass;
	}

	public V createVertex(){
		try {
			return vertexClass.newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new RuntimeException("Vertex factory failed", ex);
		}
	}
}