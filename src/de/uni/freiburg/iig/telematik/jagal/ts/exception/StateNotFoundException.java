package de.uni.freiburg.iig.telematik.jagal.ts.exception;

import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;

public class StateNotFoundException extends TSException {

	private static final long serialVersionUID = -3197197858061603241L;
	private static final String toStringFormat = "%s does not contain state %s";
	
	private final String stateName;

	public <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> 
	StateNotFoundException(String stateName, AbstractTransitionSystem<S,T,O> ts) {
		super(ts.getName());
		this.stateName = stateName;
	}
	
	public <S extends AbstractState<O>, T extends AbstractTransitionRelation<S,O>, O extends Object> 
	StateNotFoundException(VertexNotFoundException graphException) {
		super(graphException);
		this.stateName = graphException.getVertex();
	}
	
        @Override
	public String getMessage(){
		return String.format(toStringFormat, getTSName(), getStateName());
	}
	
	public String getStateName(){
		return stateName;
	}

}
