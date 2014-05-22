package de.uni.freiburg.iig.telematik.jagal.ts.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;


public interface TSParserInterface {

	public <E extends AbstractEvent,
	 		S extends AbstractLTSState<E,O>, 
	 		T extends AbstractLabeledTransitionRelation<S,E,O>,
	 		O extends Object>
	
			AbstractLabeledTransitionSystem<E,S,T,O> parse(File file) throws IOException, ParserException;
}
