package de.uni.freiburg.iig.telematik.jagal.visualization.flexible;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionSystem;

public class LabeledTransitionSystemComponent extends AbstractLabeledTransitionSystemComponent<LabeledTransitionSystem, Event, LTSState, LabeledTransitionRelation, Object>{

	private static final long serialVersionUID = -8724078881365954213L;

	public LabeledTransitionSystemComponent(LabeledTransitionSystem ts) throws Exception {
		super(ts);
	}

}
