package edu.xidian;

import org.junit.Test;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedGraph;
import de.uni.freiburg.iig.telematik.jagal.visualization.flexible.GraphComponent;
import de.uni.freiburg.iig.telematik.jagal.visualization.flexible.WeightedGraphComponent;

public class WeightedDisplayTest {

	public static void main(String[] args) throws VertexNotFoundException {
			// WeightedGraph<U> where U is the vertex element type
			WeightedGraph<Integer> gw = new WeightedGraph<Integer>();
			// Add some vertices
			gw.addVertex("A");
			gw.addVertex("B");
			gw.addVertex("C");
			gw.addVertex("D");
			gw.addVertex("E");
			// Add some edges
			gw.addEdge("A", "B");
			gw.addEdge("B", "C");
			gw.addEdge("B", "D", 2.0);
			gw.addEdge("C", "A", 3.4);
			gw.addEdge("C", "D");
			gw.addEdge("D", "E");
			gw.addEdge("E", "B");
			// Let's take a look at the result
			System.out.println(gw);
			WeightedGraphComponent<WeightedGraph<Integer>, Integer> jpanle1;
			try {
				jpanle1 = new WeightedGraphComponent<>(gw);
				jpanle1.initialize();
				new DisplayFrame(jpanle1, true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

}
