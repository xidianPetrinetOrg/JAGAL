package de.uni.freiburg.iig.telematik.jagal.visualization.flexible;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;

public abstract class AbstractGraphComponent<G extends AbstractGraph<V,E,U>,V extends Vertex<U>,E extends Edge<V>,U> extends JPanel {
	
	private static final long serialVersionUID = -5919436015610195204L;
	
	protected static final String sourceNodeColor = "#f6f5b1";
	protected static final String drainNodeColor = "#f2ddf8";
	
	private JComponent graphPanel = null;

	protected mxGraph visualGraph = null;
	protected G graph = null;
	protected Map<String, Object> vertices = new HashMap<String, Object>(); 

	public AbstractGraphComponent(G graph) throws Exception {
		initialize(graph);
	}
	
	protected void initialize(G graph) throws Exception{
		setupVisualGraph(graph);
		setLayout(new BorderLayout(20, 0));
		add(getGraphPanel(), BorderLayout.CENTER);
		setPreferredSize(getGraphPanel().getPreferredSize());
	}

	protected void setupVisualGraph(G graph) throws Exception{
		Validate.notNull(graph);
		visualGraph = new mxGraph();
		Object parent = visualGraph.getDefaultParent();

		visualGraph.getModel().beginUpdate();
		try{
			for (String vertexName : graph.getVertexNames()) {
				Object vertex = visualGraph.insertVertex(parent, vertexName, vertexName, 0, 0, 40, 40, "shape=ellipse");
				vertices.put(vertexName, vertex);
			}
			for (String vertexName : graph.getVertexNames()) {
				for (E outgoingEdge : graph.getOutgoingEdgesFor(vertexName)) {
					String followerVertexName = outgoingEdge.getTarget().getName();
					mxCell insertedEdge = (mxCell) visualGraph.insertEdge(parent, vertexName + "-" + followerVertexName, getEdgeLabel(outgoingEdge), vertices.get(vertexName), vertices.get(followerVertexName));
					Object[] cells = new Object[1];
					cells[0] = insertedEdge;
					visualGraph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff", cells);
				}
			}
		}
		finally{
			visualGraph.getModel().endUpdate();
		}
		
		for(V sourceVertex: graph.getSources()){
			setNodeColor(sourceNodeColor, sourceVertex.getName());
		}
		for(V drainVertex: graph.getDrains()){
			setNodeColor(drainNodeColor, drainVertex.getName());
		}
		
		mxHierarchicalLayout layout = new mxHierarchicalLayout(visualGraph);
		layout.execute(visualGraph.getDefaultParent());
	}
	
	protected String getEdgeLabel(E edge){
		return "";
	}
	
	private JComponent getGraphPanel() {
		if(graphPanel == null){
			graphPanel = new mxGraphComponent(visualGraph);
		}
		return graphPanel;
	}
	
	public void setNodeColor(String color, String... nodeNames){
		Object[] cells = new Object[nodeNames.length];
		for(int i=0; i<nodeNames.length; i++)
			cells[i] = vertices.get(nodeNames[i]);
		visualGraph.setCellStyles(mxConstants.STYLE_FILLCOLOR, color, cells);
	}
	

}
