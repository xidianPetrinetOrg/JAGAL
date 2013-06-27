package de.uni.freiburg.iig.telematik.jagal.graph.algorithm.coloring;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;

public interface GraphColoring {
	
	public <V extends Vertex<U>, E extends Edge<V>, U> Coloring<V> determineColoring(AbstractGraph<V, E, U> graph) throws ParameterException;

}
