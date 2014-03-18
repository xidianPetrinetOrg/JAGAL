package de.uni.freiburg.iig.telematik.jagal.ts.serialize.formats;

import java.nio.charset.Charset;

import de.invation.code.toval.file.FileFormat;


public class TSFF_Petrify  extends FileFormat{

	@Override
	public String getFileExtension() {
		return "sg";
	}
	
	@Override
	public String getFileFooter() {
		return ".end";
	}

	@Override
	public String getName() {
		return "Petrify";
	}

	@Override
	public boolean supportsCharset(Charset charset) {
		return true;
	}

}
