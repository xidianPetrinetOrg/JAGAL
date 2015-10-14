package de.uni.freiburg.iig.telematik.jagal.ts.exception;

import de.uni.freiburg.iig.telematik.jagal.graph.exception.EdgeNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;

public class RelationNotFoundException extends TSException {

	private static final long serialVersionUID = -817667248805564124L;
	private static final String toStringFormat = "%s does not contain relation (%s -> %s)";
	
	private String sourceName = null;
	private String targetName = null;

	public <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> 
	RelationNotFoundException(T edge, AbstractTransitionSystem<S,T,O> ts){
		super(ts.getName());
		this.sourceName = edge.getSource().getName();
		this.targetName = edge.getTarget().getName();
	}
	
	public <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> 
	RelationNotFoundException(String sourceName, String targetName, AbstractTransitionSystem<S,T,O> ts){
		super(ts.getName());
		this.sourceName = sourceName;
		this.targetName = targetName;
	}
	
	public <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> 
	RelationNotFoundException(EdgeNotFoundException graphException){
		super(graphException);
		this.sourceName = graphException.getSourceName();
		this.targetName = graphException.getTargetName();
	}
	
        @Override
	public String getMessage(){
		return String.format(toStringFormat, getTSName(), getSourceName(), getTargetName());
	}
	
	public String getSourceName(){
		return sourceName;
	}

	public String getTargetName(){
		return targetName;
	}
	
}
