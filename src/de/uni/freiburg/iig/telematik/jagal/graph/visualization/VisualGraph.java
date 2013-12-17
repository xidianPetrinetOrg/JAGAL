package de.uni.freiburg.iig.telematik.jagal.graph.visualization;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;

import de.invation.code.toval.graphic.util.GraphicUtils;
import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;

public abstract class VisualGraph<V extends Vertex<U>, E extends Edge<V>, U> extends VisualVertexSet<V>{
		private static final long serialVersionUID = 1L;
		
		public static final short arrowHeadLength = 10;
		public static final double arrowHeadAngle = 15*Math.PI/180;
		public static final Color defaultEdgeColor = Color.black;
		public static final double defaultWeight = 1;
		
		protected AbstractGraph<V,E,U> baseGraph = null;
		
		public VisualGraph(AbstractGraph<V,E,U> baseGraph, Dimension dimension){
			super(baseGraph.vertexes(), dimension);
			this.baseGraph = baseGraph;
		}
		
		public VisualGraph(AbstractGraph<V,E,U> baseGraph){
			this(baseGraph, VisualVertexSet.defaultDimension);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawBaseCircle();
			paintGraph();
		}
		
		protected void drawBaseCircle(){
			getGraphics().setColor(Color.black);
			GraphicUtils.drawCircle(getGraphics(), center, diameter);
		}
		
		protected void paintGraph(){
			for(E edge: baseGraph.getEdges()){
				drawEdge(edge);
			}
			for (Iterator<V> iter=baseGraph.vertexes().iterator(); iter.hasNext();){
				drawVertex(iter.next());
			}
		}
		
		@Override
		protected void drawVertex(V vertex, Color color){
			super.drawVertex(vertex, color);
			drawCaption(vertex.getElement().toString(), vertexPointMap.get(vertex));
		}
		
		protected void drawEdge(E edge){
			drawEdge(edge, defaultEdgeColor);
		}
		
		protected void drawEdge(E edge, Color color){
			if(baseGraph.containsEdge(edge)){
				drawArrow(edge.getSource(), edge.getTarget(), defaultWeight, color);
			}
		}
		
		protected void drawArrow(V source, V target, double weight, Color color){
			Graphics g = getGraphics();
			g.setColor(color);
			GraphicUtils.drawArrowOffset(g, vertexPointMap.get(source),vertexPointMap.get(target), diameterMap.get(source), diameterMap.get(target), weight);
		}
		
	}