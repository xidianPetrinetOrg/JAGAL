package de.uni.freiburg.iig.telematik.jagal.ts.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;


public interface TSParserInterface {

	public <S extends AbstractState<O>,
			T extends AbstractTransitionRelation<S,O>,
			O extends Object>
	
			AbstractTransitionSystem<S,T,O> parse(File file) throws IOException, ParserException, ParameterException;
}
