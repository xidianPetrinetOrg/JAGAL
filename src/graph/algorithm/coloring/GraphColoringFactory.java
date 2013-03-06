package graph.algorithm.coloring;

import graph.Edge;
import graph.Vertex;
import graph.abstr.AbstractGraph;
import validate.ParameterException;

public class GraphColoringFactory {
	
	public static <V extends Vertex<U>, E extends Edge<V>, U> Coloring<V> exactGreedyColoring(AbstractGraph<V, E, U> graph) throws ParameterException{
		GraphColoring coloringAlgorithm = new ExactGreedyRecursive();
		return coloringAlgorithm.determineColoring(graph);
	}

}
