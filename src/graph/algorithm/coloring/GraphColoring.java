package graph.algorithm.coloring;

import de.invation.code.toval.validate.ParameterException;
import graph.Edge;
import graph.Vertex;
import graph.abstr.AbstractGraph;

public interface GraphColoring {
	
	public <V extends Vertex<U>, E extends Edge<V>, U> Coloring<V> determineColoring(AbstractGraph<V, E, U> graph) throws ParameterException;

}
