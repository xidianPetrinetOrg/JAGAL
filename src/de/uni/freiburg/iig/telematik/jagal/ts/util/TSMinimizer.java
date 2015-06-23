package de.uni.freiburg.iig.telematik.jagal.ts.util;

import java.util.HashSet;

import de.uni.freiburg.iig.telematik.jagal.ts.MarkingStatePairContainer;
import de.uni.freiburg.iig.telematik.jagal.ts.StatePair;
import de.uni.freiburg.iig.telematik.jagal.ts.StatePairContainer;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.StateNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractEvent;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.exception.EventNotFoundException;



public class TSMinimizer {
	
	public static <E extends AbstractEvent, S extends AbstractLTSState<E,O>, R extends AbstractLabeledTransitionRelation<S,E,O>, O> void minimize(AbstractLabeledTransitionSystem<E,S,R,O> ts){
		
		
		if(!ts.isDFA()){
			throw new IllegalArgumentException("This minimization only operates on DFNs");
		}
		MarkingStatePairContainer<S,O> container = new MarkingStatePairContainer<>(StatePairContainer.getStatePairsFrom(ts.getStates()));
		for(StatePair<S,O> statePair: container.getStatePairs()){
			if(   ( ts.isEndState(statePair.state1.getName()) && !ts.isEndState(statePair.state2.getName()))
			   || (!ts.isEndState(statePair.state1.getName()) &&  ts.isEndState(statePair.state2.getName()))){
				container.markPair(StatePair.createStatePair(statePair.state1, statePair.state2));
			}
		}

		boolean newMarkedPairs;
		do{
			newMarkedPairs = false;
			for(StatePair<S,O> statePair: new HashSet<>(container.getUnmarkedStatePairs())){
				for(E event: ts.getEvents()){
					try {
						if(   (!ts.getTargetsFor(statePair.state1.getName(), event.getName()).isEmpty() && !ts.getTargetsFor(statePair.state2.getName(), event.getName()).isEmpty())
						   && (container.isMarked(ts.getTargetsFor(statePair.state1.getName(), event.getName()).get(0), ts.getTargetsFor(statePair.state2.getName(), event.getName()).get(0)))){
							container.markPair(statePair);
							newMarkedPairs = true;
							break;
						}
					} catch (StateNotFoundException | EventNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} while(newMarkedPairs);
		
	//TODO: Merging
//		System.out.println("pairs: "+ container.getUnmarkedStatePairs());
		
	}


	
	public static void main(String[] args) throws Exception{
		LabeledTransitionSystem ts = new LabeledTransitionSystem();
		ts.addState("z0");
		ts.addState("z1");
		ts.addState("z2");
		ts.addState("z3");
		ts.addState("z4");
		ts.addStartState("z0");
		ts.addEndState("z4");
		ts.addEvent("e0");
		ts.addEvent("e1");
		ts.addRelation("z0", "z1", "e0");
		ts.addRelation("z0", "z2", "e1");
		ts.addRelation("z1", "z4", "e0");
		ts.addRelation("z1", "z2", "e1");
		ts.addRelation("z2", "z2", "e1");
		ts.addRelation("z2", "z3", "e0");
		ts.addRelation("z3", "z0", "e1");
		ts.addRelation("z3", "z4", "e0");
		ts.addRelation("z4", "z4", "e0");
		ts.addRelation("z4", "z4", "e1");
		System.out.println(ts);
		TSMinimizer.minimize(ts);
//		MarkingStatePairContainer<State> cont = new MarkingStatePairContainer<State>();
//		System.out.println(cont.getUnmarkedStatePairs());
	}

}
