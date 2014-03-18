package de.uni.freiburg.iig.telematik.jagal.ts.parser;

import de.invation.code.toval.file.FileFormat;
import de.uni.freiburg.iig.telematik.jagal.ts.serialize.formats.TSFF_Petrify;


public enum TSParsingFormat {
	
	PETRIFY(new TSFF_Petrify());
	
	private FileFormat fileFormat = null;
	
	private TSParsingFormat(FileFormat fileFormat){
		this.fileFormat = fileFormat;
	}
	
	public FileFormat getFileFormat(){
		return fileFormat;
	}

}
