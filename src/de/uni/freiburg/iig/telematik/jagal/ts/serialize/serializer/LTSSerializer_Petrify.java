package de.uni.freiburg.iig.telematik.jagal.ts.serialize.serializer;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.TSType;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.serialize.LTSSerializer;
import de.uni.freiburg.iig.telematik.jagal.ts.serialize.SerializationException;

public class LTSSerializer_Petrify<	S extends AbstractLTSState<E,O>, 
									E extends AbstractEvent,
									T extends AbstractLabeledTransitionRelation<S,E,O>,
									O extends Object> extends LTSSerializer<S,E,T,O>{
	
	private final String RELATION_FORMAT = "%s %s %s";
	
	public LTSSerializer_Petrify(AbstractLabeledTransitionSystem<E,S,T,O> ts) throws ParameterException {
		super(ts);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String serialize() throws SerializationException {
		AbstractLabeledTransitionSystem<E,S,T,O> ts = (AbstractLabeledTransitionSystem<E,S,T,O>) getTransitionSystem();
		StringBuilder builder = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		
		builder.append("# states: ");
		builder.append(ts.getStateCount());
		builder.append(newLine);
		builder.append("# events: ");
		builder.append(ts.getEventCount());
		builder.append(newLine);
		
		// Add first line: events
		builder.append(".outputs");
		for(E event: ts.getEvents()){
			builder.append(' ');
			builder.append(event.getName());
		}
		builder.append(newLine);
		
		// Add second line: graph type
		builder.append(".state graph");
		builder.append(newLine);
		
		// Add relations
		for(AbstractLabeledTransitionRelation<S,E,O> relation: ts.getRelations()){
			builder.append(String.format(RELATION_FORMAT, relation.getSource().getName(), relation.getEvent().getName(), relation.getTarget().getName()));
			builder.append(newLine);
		}
		
		// Add start states
		builder.append(".marking {");
		Collection<S> startStates = ts.getStartStates();
		int addedStates = 0;
		for(S startState: startStates){
			builder.append(startState.getName());
			if(++addedStates < startStates.size()){
				builder.append(' ');
			}
		}
		builder.append('}');
		builder.append(newLine);
		
		// Add end states
		builder.append(".final {");
		Collection<S> endStates = ts.getEndStates();
		addedStates = 0;
		for (S endState : endStates) {
			builder.append(endState.getName());
			if (++addedStates < endStates.size()) {
				builder.append(' ');
			}
		}
		builder.append('}');
		builder.append(newLine);
		
		return builder.toString();
	}
	

	@Override
	public TSType acceptedNetType() {
		return TSType.TRANSITION_SYSTEM;
	}
}
