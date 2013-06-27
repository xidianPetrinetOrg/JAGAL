package graph.algorithm.coloring;

import de.invation.code.toval.validate.ParameterException;
import graph.Edge;
import graph.Vertex;
import graph.abstr.AbstractGraph;

public class GraphColoringFactory {
	
	public static <V extends Vertex<U>, E extends Edge<V>, U> Coloring<V> exactGreedyColoring(AbstractGraph<V, E, U> graph) throws ParameterException{
		GraphColoring coloringAlgorithm = new ExactGreedyRecursive();
		return coloringAlgorithm.determineColoring(graph);
	}

}
