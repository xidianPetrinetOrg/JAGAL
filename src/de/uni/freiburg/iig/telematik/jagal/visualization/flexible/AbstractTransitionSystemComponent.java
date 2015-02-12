package de.uni.freiburg.iig.telematik.jagal.visualization.flexible;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;

public abstract class AbstractTransitionSystemComponent<G extends AbstractTransitionSystem<S, T, O>,S extends AbstractState<O>, T extends AbstractTransitionRelation<S, O>, O extends Object> extends AbstractGraphComponent<G,S,T,O> {

	private static final long serialVersionUID = -6292113630339758126L;
	
	protected static final String endStateColor = "#f2ddf8";
	protected boolean highlightEndStates = true;

	public AbstractTransitionSystemComponent(G ts) throws Exception {
		super(ts);
	}

	@Override
	protected void setupVisualGraph(G ts) throws Exception {
		super.setupVisualGraph(ts);
		if(highlightEndStates){
			for(S endState: ts.getEndStates()){
				setNodeColor(endStateColor, endState.getName());
			}
		}

		for (S sourceVertex : graph.getStartStates()) {
			setNodeColor(sourceNodeColor, sourceVertex.getName());
		}
		for (S drainVertex : graph.getEndStates()) {
			setNodeColor(drainNodeColor, drainVertex.getName());
		}
//		mxHierarchicalLayout layout = new mxHierarchicalLayout(visualGraph);
//		layout.execute(visualGraph.getDefaultParent());
	}


	@Override
	protected String getShapeForVertex(S graphVertex) {
		if(graph.isEndState(graphVertex.getName()))
			return "shape=doubleEllipse";
		return "shape=ellipse";
	}

}
