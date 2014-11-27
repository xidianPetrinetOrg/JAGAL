package de.uni.freiburg.iig.telematik.jagal.graph.visualization.circle;


import java.awt.Color;
import java.awt.Dimension;

import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.AbstractWeightedGraph;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedEdge;

public abstract class VisualWeightedGraph<V extends Vertex<U>, U> extends VisualGraph<V, WeightedEdge<V>, U> {
	
	private static final long serialVersionUID = 1L;

	public VisualWeightedGraph(AbstractWeightedGraph<V, U> baseGraph, Dimension dimension) {
		super(baseGraph, dimension);
	}
	
	public VisualWeightedGraph(AbstractWeightedGraph<V, U> baseGraph){
		this(baseGraph, VisualVertexSet.defaultDimension);
	}
	
	@Override
	protected void drawEdge(WeightedEdge<V> edge){
		drawEdge(edge, defaultEdgeColor);
	}
	
	@Override
	protected void drawEdge(WeightedEdge<V> edge, Color color){
		if(!baseGraph.containsObject(edge))
			return;
		drawArrow(edge.getSource(), edge.getTarget(), edge.getWeight(), color);	
	}
}
