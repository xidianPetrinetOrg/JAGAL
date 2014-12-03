package de.uni.freiburg.iig.telematik.jagal.graph.visualization.flexible;

import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;

public class AbstractLabeledTransitionSystemComponent<G extends AbstractLabeledTransitionSystem<E,S,R,O>,E extends AbstractEvent, S extends AbstractLTSState<E, O>, R extends AbstractLabeledTransitionRelation<S, E, O>, O extends Object> extends AbstractTransitionSystemComponent<G, S, R, O> {

	private static final long serialVersionUID = 4091538764969398029L;
	private boolean useEventLabels = false;

	public AbstractLabeledTransitionSystemComponent(G ts) throws Exception {
		super(ts);
	}

	
	public boolean isUseEventLabels() {
		return useEventLabels;
	}


	public void setUseEventLabels(boolean useEventLabels) {
		this.useEventLabels = useEventLabels;
	}


	@Override
	protected String getEdgeLabel(R edge) {
		if(isUseEventLabels())
			return edge.getEvent().getLabel();
		return edge.getEvent().getName();
	}
	
}
