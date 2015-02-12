package de.uni.freiburg.iig.telematik.jagal.visualization.flexible;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedGraph;
import de.uni.freiburg.iig.telematik.jagal.ts.TransitionSystem;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.LabeledTransitionSystem;

public class GraphComponentFactory {

	public static <G extends Graph<T>, T extends Object> GraphComponent<G,T> createGraphComponent(G g) throws Exception {
		GraphComponent<G,T> component = new GraphComponent<G,T>(g);
		component.initialize();
		return component;
	}
	
	public static <G extends WeightedGraph<T>, T extends Object> WeightedGraphComponent<G,T> createWeightedGraphComponent(G g) throws Exception {
		WeightedGraphComponent<G,T> component = new WeightedGraphComponent<G,T>(g);
		component.initialize();
		return component;
	}
	
	public static TransitionSystemComponent createTSComponent(TransitionSystem ts) throws Exception {
		TransitionSystemComponent component = new TransitionSystemComponent(ts);
		component.initialize();
		return component;
	}
	
	public static LabeledTransitionSystemComponent createTSComponent(LabeledTransitionSystem ts) throws Exception {
		LabeledTransitionSystemComponent component = new LabeledTransitionSystemComponent(ts);
		component.initialize();
		return component;
	}

	public static void testGraphComponent() throws Exception {
		Graph<Object> g = new Graph<Object>();
		g.addVertex("v1");
		g.addVertex("v2");
		g.addVertex("v3");
		g.addEdge("v1", "v2");
		g.addEdge("v2", "v3");
		new DisplayFrame(GraphComponentFactory.createGraphComponent(g), true);
	}
	
	public static void testTSComponent() throws Exception {
		TransitionSystem ts = new TransitionSystem();
		ts.addState("s1");
		ts.addState("s2");
		ts.addState("s3");
		ts.addStartState("s1");
		ts.addEndState("s3");
		ts.addRelation("s1", "s2");
		ts.addRelation("s2", "s3");
		new DisplayFrame(GraphComponentFactory.createTSComponent(ts), true);
	}
	
	public static void main(String[] args) throws Exception {
		testTSComponent();
	}
	
}
