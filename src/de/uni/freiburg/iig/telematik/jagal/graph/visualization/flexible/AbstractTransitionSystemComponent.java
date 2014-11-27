package de.uni.freiburg.iig.telematik.jagal.graph.visualization.flexible;

import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;

public abstract class AbstractTransitionSystemComponent<G extends AbstractTransitionSystem<S, T, O>,S extends AbstractState<O>, T extends AbstractTransitionRelation<S, O>, O extends Object> extends AbstractGraphComponent<G,S,T,O> {

	private static final long serialVersionUID = -6292113630339758126L;

	public AbstractTransitionSystemComponent(G ts) throws Exception {
		super(ts);
	}

	@Override
	protected void setupVisualGraph(G graph) throws Exception {
		super.setupVisualGraph(graph);
	}

	
	
}
