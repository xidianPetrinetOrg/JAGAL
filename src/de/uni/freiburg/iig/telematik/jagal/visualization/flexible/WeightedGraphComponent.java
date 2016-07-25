package de.uni.freiburg.iig.telematik.jagal.visualization.flexible;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedEdge;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedGraph;

public class WeightedGraphComponent<G extends WeightedGraph<T>, 
                                    T extends Object> 
                                   extends AbstractWeightedGraphComponent<G, Vertex<T>, T> {

	private static final long serialVersionUID = 6935652350923822176L;

	public WeightedGraphComponent(G graph) throws Exception {
		super(graph);
	}

	@Override
	protected String getEdgeLabel(WeightedEdge<Vertex<T>> e) {
		return String.valueOf(e.getWeight());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		WeightedGraph<Object> g = new WeightedGraph<Object>();
		g.addVertex("v1");
		g.addVertex("v2");
		g.addVertex("v3");
		g.addEdge("v1", "v2");
		g.addEdge("v2", "v3");
		new DisplayFrame(new WeightedGraphComponent(g), true);
	}
}
