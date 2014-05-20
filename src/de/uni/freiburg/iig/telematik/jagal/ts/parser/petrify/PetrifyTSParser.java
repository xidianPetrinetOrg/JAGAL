package de.uni.freiburg.iig.telematik.jagal.ts.parser.petrify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.invation.code.toval.file.FileReader;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.parser.ParserException.ErrorCode;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.TransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.StateNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.TSException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.parser.TSParserInterface;

public class PetrifyTSParser implements TSParserInterface{
	
	private static final String PREFIX_DOT = ".";
	private static final String PREFIX_OUTPUTS = ".outputs ";
	private static final String PREFIX_STATE_GRAPH = ".state graph";
	private static final String PREFIX_END = ".end";
	private static final String PREFIX_MARKING = ".marking ";
	private static final String PREFIX_FINAL = ".final ";
	private static final String PREFIX_COMMENT = "#";
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> AbstractTransitionSystem<S, T, O> parse(File file) throws IOException, ParserException, ParameterException {
		AbstractTransitionSystem<S,T,O> ts = createTS(file);
		FileReader reader = new FileReader(file.getAbsolutePath());
		String nextLine = null;
		while((nextLine = reader.readLine()) != null){
			String lineContent = null;
			if(nextLine.startsWith(PREFIX_COMMENT)){
				// Do nothing
			} else if(nextLine.startsWith(PREFIX_DOT)){
				
				if(nextLine.startsWith(PREFIX_OUTPUTS)){
					lineContent = nextLine.replace(PREFIX_OUTPUTS, "");
					insertEvents((AbstractLabeledTransitionSystem) ts, lineContent);
				} else if(nextLine.startsWith(PREFIX_MARKING)){
					lineContent = nextLine.replace(PREFIX_MARKING, "");
					lineContent = lineContent.replace("{", "");
					lineContent = lineContent.replace("}", "");
					setMarking(ts, lineContent);
				} else if(nextLine.startsWith(PREFIX_FINAL)){
					lineContent = nextLine.replace(PREFIX_FINAL, "");
					lineContent = lineContent.replace("{", "");
					lineContent = lineContent.replace("}", "");
					setFinalStates(ts, lineContent);
				} else if(nextLine.isEmpty() || nextLine.startsWith(PREFIX_STATE_GRAPH) || nextLine.startsWith(PREFIX_END)){
					// Do nothing
				} else {
					throw new ParserException(ErrorCode.UNSUPPORTED_FORMAT, "File does not seem to contain state graph content.");
				}
			} else {
				addTransition(ts, nextLine);
			}
		}
		reader.closeFile();
		return ts;
	}
	
	@SuppressWarnings("unchecked")
	private <S extends AbstractState<O>, 
			 T extends AbstractTransitionRelation<S,O>,
			 O> 
	
	AbstractTransitionSystem<S,T,O> createTS(File file) throws IOException, ParserException{
		
		FileReader reader = new FileReader(file.getAbsolutePath());
		String nextLine = null;
		int tokenCount = 0;
		while((nextLine = reader.readLine()) != null){
			if(nextLine.startsWith(PREFIX_COMMENT) || nextLine.startsWith(PREFIX_DOT)){
				continue;
			} else {
				tokenCount = getTokens(nextLine).size();
				break;
			}
		}
		reader.closeFile();
		if(tokenCount == 2){
			return (AbstractTransitionSystem<S, T, O>) new TransitionSystem();
		} if (tokenCount == 3){
			return (AbstractTransitionSystem<S,T,O>) new LabeledTransitionSystem();
		} else {
			throw new ParserException(ErrorCode.UNSUPPORTED_FORMAT, "Cannot determine TS type.");
		}
	}


	@SuppressWarnings("rawtypes")
	private <S extends AbstractState<O>, 
			 T extends AbstractTransitionRelation<S,O>,
			 O> 
	
	void addTransition(AbstractTransitionSystem<S,T,O> ts, String lineContent) throws ParserException {
		
		List<String> tokens = getTokens(lineContent);
		String sourceName = tokens.get(0);
		ensureState(ts, sourceName);
		String targetName = null;
		if(tokens.size() == 3){
			targetName = tokens.get(2);
			ensureState(ts, targetName);
			String eventName = tokens.get(1);
			try {
				((AbstractLabeledTransitionSystem) ts).addRelation(sourceName, targetName, eventName);
			} catch (TSException e) {
				throw new ParserException("Cannot add relation: " + e.getMessage());
			}
		} else {
			targetName = tokens.get(1);
			ensureState(ts, targetName);
			try {
				ts.addRelation(sourceName, targetName);
			} catch (StateNotFoundException e) {
				throw new ParserException("Cannot add relation: " + e.getMessage());
			}
		}
	}

	private <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>,O> void ensureState(AbstractTransitionSystem<S,T,O> ts, String stateName) throws ParameterException {
		if(!ts.containsState(stateName)){
			ts.addState(stateName);
		}
	}

	private <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>,O> void setMarking(AbstractTransitionSystem<S,T,O> ts, String lineContent) throws ParameterException {
		for(String token: getTokens(lineContent)){
			if(!ts.containsState(token))
				throw new ParameterException("Unknown state: " + token);
			ts.addStartState(token);
		}
	}
	
	private <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>,O> void setFinalStates(AbstractTransitionSystem<S,T,O> ts, String lineContent) throws ParameterException {
		for(String token: getTokens(lineContent)){
			if(!ts.containsState(token))
				throw new ParameterException("Unknown state: " + token);
			ts.addEndState(token);
		}
	}

	private <E extends AbstractEvent,
			 S extends AbstractLTSState<E,O>, 
			 T extends AbstractLabeledTransitionRelation<S,E,O>,
			 O> 
	void insertEvents(AbstractLabeledTransitionSystem<E,S,T,O> ts, String lineContent){
		
		for(String token: getTokens(lineContent)){
			ts.addEvent(token);
		}
	}
	
	public List<String> getTokens(String lineContent){
		List<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(lineContent, " ");
		while(tokenizer.hasMoreTokens()){
			result.add(tokenizer.nextToken());
		}
		return result;
	}

}
