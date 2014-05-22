package de.uni.freiburg.iig.telematik.jagal.ts.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.parser.ParserException.ErrorCode;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.parser.petrify.PetrifyTSParser;

public class TSParser {
	

	public static synchronized <E extends AbstractEvent,
	 							S extends AbstractLTSState<E,O>, 
	 							T extends AbstractLabeledTransitionRelation<S,E,O>,
	 							O>
	
								AbstractLabeledTransitionSystem<E,S,T,O>
	
	parse(File file) throws IOException, ParserException{
		validateFile(file);
		TSParsingFormat format = guessFormat(file);
		if(format == null)
			throw new ParserException(ErrorCode.UNKNOWN_FILE_EXTENSION);
		TSParserInterface parser = getParser(file, format);
		return parser.<E,S,T,O>parse(file);
	}
	
	public static synchronized <E extends AbstractEvent,
	 							S extends AbstractLTSState<E,O>, 
	 							T extends AbstractLabeledTransitionRelation<S,E,O>,
	 							O>
	
								AbstractLabeledTransitionSystem<E,S,T,O>
	
	parse(String fileName) throws IOException, ParserException, ParameterException {
		Validate.notNull(fileName);
		return TSParser.<E,S,T,O>parse(prepareFile(fileName));
	}
	
	public static synchronized <E extends AbstractEvent,
								S extends AbstractLTSState<E,O>, 
								T extends AbstractLabeledTransitionRelation<S,E,O>,
								O>
	
								AbstractLabeledTransitionSystem<E,S,T,O>
	
	parse(File file, TSParsingFormat format) throws IOException, ParserException, ParameterException {
		validateFile(file);
		Validate.notNull(format);
		TSParserInterface parser = getParser(file, format);
		return parser.<E,S,T,O>parse(file);
	}
	
	public static synchronized <E extends AbstractEvent,
	 							S extends AbstractLTSState<E,O>, 
	 							T extends AbstractLabeledTransitionRelation<S,E,O>,
	 							O>
	
								AbstractLabeledTransitionSystem<E,S,T,O>
	
	parse(String fileName, TSParsingFormat format) throws IOException, ParserException, ParameterException {
		Validate.notNull(fileName);
		return TSParser.<E,S,T,O>parse(prepareFile(fileName), format);
	}
	
	private static File prepareFile(String fileName) throws IOException{
		File file = new File(fileName);
		validateFile(file);
		return file;
	}
	
	private static void validateFile(File file) throws IOException{
		if(!file.exists())
			throw new IOException("I/O Error on opening file: File does not exist!");
		if(file.isDirectory())
			throw new IOException("I/O Error on opening file: File is a directory!");
		if(!file.canRead())
			throw new IOException("I/O Error on opening file: Unable to read file!");
	}

	public static synchronized TSParserInterface getParser(File file, TSParsingFormat format) throws ParserException {
		switch(format){
		case PETRIFY: return new PetrifyTSParser();
		}
		throw new ParserException(ErrorCode.UNSUPPORTED_FORMAT);
	}
	
	public static TSParsingFormat guessFormat(File file){
		for(TSParsingFormat format: TSParsingFormat.values()){
			if(file.getName().endsWith(format.getFileFormat().getFileExtension())){
				return format;
			}
		}
		return null;
	}
}
