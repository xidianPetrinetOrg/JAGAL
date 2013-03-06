package graph.visualization;

import graph.Vertex;
import graph.exception.VertexNotFoundException;
import graph.weighted.AbstractWeightedGraph;
import graph.weighted.WeightedEdge;

import java.awt.Color;
import java.awt.Dimension;

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
		try{
			drawArrow(baseGraph.getEqualVertex(edge.getSource()), baseGraph.getEqualVertex(edge.getTarget()), edge.getWeight(), color);	
		} catch (VertexNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
