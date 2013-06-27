package de.uni.freiburg.iig.telematik.jagal.graph.visualization;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

import de.invation.code.toval.graphic.GraphicUtils;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;

public class VisualVertexSet<V extends Vertex<?>> extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public static final Color defaultVertexColor = Color.orange;
	public static final short defaultVertexDiameter = 26;
	public static final short captionDistance = defaultVertexDiameter;
	
	protected HashMap<V,Point> vertexPointMap = new HashMap<V, Point>();
	protected HashMap<V,Integer> diameterMap = new HashMap<V, Integer>();
	private V fixedVertex = null;
	
	protected static final Dimension defaultDimension = new Dimension(400,400);
	protected Dimension dimension = defaultDimension;
	protected Point center; 
	protected int diameter = 0;
	
	public VisualVertexSet(){
		this(defaultDimension);
	}
	
	public VisualVertexSet(Dimension dimension){
		this.dimension = dimension;
		setPreferredSize(dimension);
		center = new Point(dimension.width/2,dimension.height/2);
		setDiameter();
	}
	
	public VisualVertexSet(Collection<V> vertexes){
		this(vertexes, defaultDimension);
	}
	
	public VisualVertexSet(Collection<V> vertexes, Dimension dimension){
		this(dimension);
		initializeMaps(vertexes);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	protected void initializeMaps(Collection<V> vertexes){
		int n = vertexes.size();
		int c = 0;
		for (Iterator<V> iter=vertexes.iterator(); iter.hasNext();){
			V next = iter.next();
			vertexPointMap.put(next, new Point((int) (center.x + diameter/2 * Math.cos(c * 2 * Math.PI / n)), (int) (center.y + diameter/2 * Math.sin(c * 2 * Math.PI / n))));
			diameterMap.put(next, getVertexDiameter(next));
			c++;
		}
	}
	
	protected int getVertexDiameter(V vertex){
		return defaultVertexDiameter;
	}
	
	protected void setDiameter(){
		diameter = Math.min(dimension.height, dimension.width)-140;
	}
	
	protected void drawVertex(V vertex){
		drawVertex(vertex, defaultVertexColor);
	}
	
	protected void drawVertex(V vertex, Color color){
		Graphics g = getGraphics();
		g.setColor(color);
		GraphicUtils.fillCircle(g, (int) vertexPointMap.get(vertex).getX(), (int) vertexPointMap.get(vertex).getY(), diameterMap.get(vertex));
		g.setColor(Color.black);
		GraphicUtils.drawCircle(g, (int) vertexPointMap.get(vertex).getX(), (int) vertexPointMap.get(vertex).getY(),  diameterMap.get(vertex));
	}
	
	protected V getVertexAt(Point cursor){
		for(V v: vertexPointMap.keySet()){
			Point p = vertexPointMap.get(v);
			if(Math.abs(p.getX()-cursor.getX())<(diameterMap.get(v)/2) && Math.abs(p.getY()-cursor.getY())<(diameterMap.get(v)/2)){
				if(fixedVertex!=v){
					fixedVertex = v;
					return v;
				}
				return null;
			}
		}
		fixedVertex = null;
		return null;
	}
	
	protected void drawCaption(String caption, Point p){
		Graphics g = this.getGraphics();
		double x,s,dx,dy,sinPhi,cosPhi;
		dx = p.x-center.x;
		dy = p.y-center.y;
		x=-captionDistance;
	    s=Math.sqrt(dy*dy+dx*dx);
	    sinPhi=dy/s;
	    cosPhi=dx/s; 		
	    g.setColor(Color.black);
		g.drawString(caption, (int) (p.getX()-x*cosPhi)-caption.length()*4, (int) (p.getY()-x*sinPhi)+5);
	    //g.drawLine((int) (p.getX()-x*cosPhi), (int) (p.getY()-x*sinPhi), (int) (p.getX()-x*cosPhi), (int) (p.getY()-x*sinPhi));
	}

}
