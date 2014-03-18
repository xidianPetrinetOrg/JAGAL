package de.uni.freiburg.iig.telematik.jagal.ts.serialize;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractState;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.abstr.AbstractTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.serialize.formats.TSSerializationFormat;
import de.uni.freiburg.iig.telematik.jagal.ts.serialize.serializer.LTSSerializer_Petrify;

public class TSSerialization {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <S extends AbstractState<O>, 
	   			   T extends AbstractTransitionRelation<S,O>,
	   			   O extends Object>

	TSSerializer<S,T,O> 

	getSerializer(AbstractTransitionSystem<S,T,O> ts, TSSerializationFormat format) throws ParameterException, SerializationException{
		
		switch(format){
		case PETRIFY:
			if(ts instanceof AbstractLabeledTransitionSystem)
				return new LTSSerializer_Petrify((AbstractLabeledTransitionSystem) ts);
			throw new SerializationException(SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, ts.getClass());
		default:
			throw new SerializationException(SerializationException.ErrorCode.UNSUPPORTED_FORMAT, format);
		}
	}
	
	public static <S extends AbstractState<O>, 
	   			   T extends AbstractTransitionRelation<S,O>,
	   			   O extends Object>

	String

	serialize(AbstractTransitionSystem<S,T,O> ts, TSSerializationFormat format) 
			throws SerializationException, ParameterException{

		Validate.notNull(ts);
		Validate.notNull(format);
		
		StringBuilder builder = new StringBuilder();
		builder.append(format.getFileFormat().getFileHeader());
		builder.append(getSerializer(ts, format).serialize());
		builder.append(format.getFileFormat().getFileFooter());
		
		return builder.toString();
	}
	
	public static <S extends AbstractState<O>, 
	   			   T extends AbstractTransitionRelation<S,O>,
	   			   O extends Object>
	
	void 
	
	serialize(AbstractTransitionSystem<S,T,O> ts, TSSerializationFormat format, String fileName, String path) 
			throws SerializationException, ParameterException, IOException{
		
		Validate.notNull(ts);
		Validate.notNull(format);
		Validate.notNull(path);
		Validate.notNull(fileName);
		
		//Check if path and file name are valid
		File cPath = new File(path);
		if(!cPath.exists())
			cPath.mkdirs();
		if(!cPath.isDirectory())
			throw new IOException(path + " is not a valid path!");
		if(fileName.isEmpty())
			throw new ParameterException(ErrorCode.EMPTY);
		
		TSSerializer<S,T,O> serializer = getSerializer(ts, format);
		File file = new File(String.format("%s%s.%s", path, fileName, format.getFileFormat().getFileExtension()));
		FileWriter output;
		if(file.exists()) 
			file.delete();
		file.createNewFile();
		output = new FileWriter(file, true);
		output.write(format.getFileFormat().getFileHeader());
		output.write(serializer.serialize());
		output.write(format.getFileFormat().getFileFooter());
		output.close();
	}
	
}
