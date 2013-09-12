package de.uni.freiburg.iig.telematik.jagal.ts.serialize.serializer;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.State;
import de.uni.freiburg.iig.telematik.jagal.ts.TSType;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.serialize.SerializationException;
import de.uni.freiburg.iig.telematik.jagal.ts.serialize.TSSerializer;

public class LTSSerializer_Petrify<S extends State, 
									   E extends Event,
									   T extends LabeledTransitionRelation<S,E>> extends TSSerializer<S,T>{
	
	private final String RELATION_FORMAT = "%s %s %s";
	public LTSSerializer_Petrify(AbstractTransitionSystem<S,T> petriNet) throws ParameterException {
		super(petriNet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String serialize() throws SerializationException {
		AbstractLabeledTransitionSystem<E,S> ts = (AbstractLabeledTransitionSystem<E,S>) getTransitionSystem();
		StringBuilder builder = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		
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
		for(LabeledTransitionRelation<S,E> relation: ts.getRelations()){
			builder.append(String.format(RELATION_FORMAT, relation.getSource().getName(), relation.getEvent().getName(), relation.getTarget().getName()));
			builder.append(newLine);
		}
		
		// Add start states
		builder.append(".marking {");
		Set<S> startStates = ts.getStartStates();
		int addedStates = 0;
		for(S startState: startStates){
			builder.append(startState.getName());
			if(++addedStates < startStates.size()){
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
