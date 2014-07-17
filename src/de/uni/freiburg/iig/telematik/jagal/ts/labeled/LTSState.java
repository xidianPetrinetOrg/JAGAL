package de.uni.freiburg.iig.telematik.jagal.ts.labeled;


import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;


public class LTSState extends AbstractLTSState<Event,Object> {
	
	private static final long serialVersionUID = 5336258082573485517L;

	public LTSState(String name, Object element) {
		super(name, element);
	}
	
}
