package de.uni.freiburg.iig.telematik.jagal.visualization.flexible;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.AbstractWeightedGraph;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedEdge;

public abstract class AbstractWeightedGraphComponent<G extends AbstractWeightedGraph<V,U>, 
                                                     V extends Vertex<U>, 
                                                     U> 
                                      extends AbstractGraphComponent<G, V, WeightedEdge<V>, U> {

	private static final long serialVersionUID = -8107668682072069537L;

	public AbstractWeightedGraphComponent(G graph) throws Exception {
		super(graph);
	}
	

}
