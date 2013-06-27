package de.uni.freiburg.iig.telematik.jagal.graph.abstr;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public class VertexFactory<V extends Vertex<U>, U>{

	private final Class<? extends V> vertexClass;

	public VertexFactory(Class<? extends V> vertexClass){
		System.out.println("v1");
		this.vertexClass = vertexClass;
		System.out.println("v2");
	}

	public V createVertex(){
		try {
			return vertexClass.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Vertex factory failed", ex);
		}
	}
}