package de.uni.freiburg.iig.telematik.jagal.ts.serialize;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.TSType;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;

public abstract class TSSerializer<	S extends AbstractState<O>, 
								   	T extends AbstractTransitionRelation<S,O>,
									O extends Object>{
	
	protected AbstractTransitionSystem<S,T,O> transitionSystem = null;
	
	public TSSerializer(AbstractTransitionSystem<S,T,O> ts) throws ParameterException{
		validateTransitionSystem(ts);
	}
	
	private void validateTransitionSystem(AbstractTransitionSystem<S,T,O> ts) throws ParameterException{
		Validate.notNull(ts);
		Class<?> requiredClassType = TSType.getClassType(acceptedNetType());
		if(!(requiredClassType.isAssignableFrom(ts.getClass()))){
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "The serializer requires nets of type \""+requiredClassType+"\"\n The given net is of type \""+ts.getClass()+"\"");
		}
		this.transitionSystem = ts;
	}
	
	public AbstractTransitionSystem<S,T,O> getTransitionSystem(){
		return transitionSystem;
	}
	
	public abstract TSType acceptedNetType();
	
	public abstract String serialize() throws SerializationException;
			
}
