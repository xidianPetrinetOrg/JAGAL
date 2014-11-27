package de.uni.freiburg.iig.telematik.jagal.graph.visualization.flexible;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedGraph;

public class WeightedGraphComponent<G extends WeightedGraph<T>, T extends Object> extends AbstractWeightedGraphComponent<G, Vertex<T>, T> {

	private static final long serialVersionUID = 6935652350923822176L;

	public WeightedGraphComponent(G graph) throws Exception {
		super(graph);
	}

}
