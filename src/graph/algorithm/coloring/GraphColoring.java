package graph.algorithm.coloring;

import graph.Edge;
import graph.Vertex;
import graph.abstr.AbstractGraph;
import validate.ParameterException;

public interface GraphColoring {
	
	public <V extends Vertex<U>, E extends Edge<V>, U> Coloring<V> determineColoring(AbstractGraph<V, E, U> graph) throws ParameterException;

}
