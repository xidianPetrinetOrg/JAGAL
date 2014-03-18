package de.uni.freiburg.iig.telematik.jagal.ts.labeled;


import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;


public class LTSState extends AbstractLTSState<Event,Object> {
	
//	public LTSState(String name){
//		super(name);
//	}

	public LTSState(String name, Object element) {
		super(name, element);
	}
	
}
