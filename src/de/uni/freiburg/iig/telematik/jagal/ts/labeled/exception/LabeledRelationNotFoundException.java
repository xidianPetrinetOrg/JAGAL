package de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception;

import de.uni.freiburg.iig.telematik.jagal.ts.exception.RelationNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;

public class LabeledRelationNotFoundException extends RelationNotFoundException{

	private static final long serialVersionUID = -2454819744638436968L;
	private static final String toStringFormat = "%s does not contain relation (%s -%s-> %s)";
	
	private String eventName = null;

	public <S extends AbstractLTSState<E,O>, E extends AbstractEvent, T extends AbstractLabeledTransitionRelation<S,E,O>, O> LabeledRelationNotFoundException(String sourceName, String targetName, String eventName, AbstractLabeledTransitionSystem<E,S,T,O> ts) {
		super(sourceName, targetName, ts);
		this.eventName = eventName;
	}

	public <S extends AbstractLTSState<E,O>, E extends AbstractEvent, T extends AbstractLabeledTransitionRelation<S,E,O>, O> LabeledRelationNotFoundException(T edge, AbstractLabeledTransitionSystem<E,S,T,O> ts) {
		super(edge, ts);
		this.eventName = edge.getEvent().getName();
	}

	public String getEventName(){
		return eventName;
	}
	
	@Override
	public String getMessage(){
		return String.format(toStringFormat, getTSName(), getSourceName(), getEventName(), getTargetName());
	}

}
