package ts.util;

import java.util.HashSet;

import ts.Event;
import ts.MarkingStatePairContainer;
import ts.State;
import ts.StatePair;
import ts.StatePairContainer;
import ts.labeled.AbstractLabeledTransitionSystem;
import ts.labeled.LabeledTransitionSystem;


public class TSMinimizer {
	
	public static <E extends Event, S extends State> void minimize(AbstractLabeledTransitionSystem<E, S> ts){
		
		
		if(!ts.isDFN()){
			throw new IllegalArgumentException("This minimization only operates on DFNs");
		}
		MarkingStatePairContainer<S> container = new MarkingStatePairContainer<S>(StatePairContainer.getStatePairsFrom(ts.getStates()));
		for(StatePair<S> statePair: container.getStatePairs()){
			if(   ( ts.isEndState(statePair.state1) && !ts.isEndState(statePair.state2))
			   || (!ts.isEndState(statePair.state1) &&  ts.isEndState(statePair.state2))){
				container.markPair(StatePair.createStatePair(statePair.state1, statePair.state2));
			}
		}

		boolean newMarkedPairs;
		do{
			newMarkedPairs = false;
			for(StatePair<S> statePair: new HashSet<StatePair<S>>(container.getUnmarkedStatePairs())){
				for(E event: ts.getEvents()){
					try {
						if(   (!ts.getTargetsFor(statePair.state1, event).isEmpty() && !ts.getTargetsFor(statePair.state2, event).isEmpty())
						   && (container.isMarked(ts.getTargetsFor(statePair.state1, event).get(0), ts.getTargetsFor(statePair.state2, event).get(0)))){
							container.markPair(statePair);
							newMarkedPairs = true;
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} while(newMarkedPairs);
		
	//TODO: Merging
		System.out.println("pairs: "+ container.getUnmarkedStatePairs());
		
	}


	
	public static void main(String[] args) throws Exception{
		State z0 = new State("z0");
		State z1 = new State("z1");
		State z2 = new State("z2");
		State z3 = new State("z3");
		State z4 = new State("z4");
		Event e0 = new Event("0");
		Event e1 = new Event("1");
		LabeledTransitionSystem ts = new LabeledTransitionSystem();
		ts.addState(z0);
		ts.addState(z1);
		ts.addState(z2);
		ts.addState(z3);
		ts.addState(z4);
		ts.addStartState(z0);
		ts.addEndState(z4);
		ts.addEvent(e0);
		ts.addEvent(e1);
		ts.addRelation(z0, z1, e0);
		ts.addRelation(z0, z2, e1);
		ts.addRelation(z1, z4, e0);
		ts.addRelation(z1, z2, e1);
		ts.addRelation(z2, z2, e1);
		ts.addRelation(z2, z3, e0);
		ts.addRelation(z3, z0, e1);
		ts.addRelation(z3, z4, e0);
		ts.addRelation(z4, z4, e0);
		ts.addRelation(z4, z4, e1);
		System.out.println(ts);
		TSMinimizer.minimize(ts);
//		MarkingStatePairContainer<State> cont = new MarkingStatePairContainer<State>();
//		System.out.println(cont.getUnmarkedStatePairs());
	}

}
