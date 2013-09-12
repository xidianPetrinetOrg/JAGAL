package de.uni.freiburg.iig.telematik.jagal.ts.serialize.formats;

import de.invation.code.toval.file.FileFormat;


public enum TSSerializationFormat {
	
	PETRIFY(new TSFF_Petrify());
	
	private FileFormat fileFormat = null;
	
	private TSSerializationFormat(FileFormat fileFormat){
		this.fileFormat = fileFormat;
	}
	
	public FileFormat getFileFormat(){
		return fileFormat;
	}

}
