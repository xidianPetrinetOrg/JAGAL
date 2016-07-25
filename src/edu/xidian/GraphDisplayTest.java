package edu.xidian;

import org.junit.Test;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.graph.weighted.WeightedGraph;
import de.uni.freiburg.iig.telematik.jagal.visualization.flexible.GraphComponent;
import de.uni.freiburg.iig.telematik.jagal.visualization.flexible.WeightedGraphComponent;

public class GraphDisplayTest {

	public static void main(String[] args) throws VertexNotFoundException {
			// Graph<U> where U is the type of the vertex elements
			Graph<Integer> g = new Graph<Integer>();
			// Add some vertices
			g.addVertex("A", 4);
			g.addVertex("B", 3);
			g.addVertex("C", 1);
			g.addVertex("D");
			g.getVertex("D").setElement(2);
			g.addVertex("E", 8);
			// Add some edges
			g.addEdge("A", "B");
			g.addEdge("B", "C");
			g.addEdge("B", "D");
			g.addEdge("C", "A");
			g.addEdge("C", "D");
			g.addEdge("D", "E");
			g.addEdge("E", "B");
			// Let's take a look at the result
			System.out.println(g);
			System.out.println(g.getVertex("E").getElement());
			
			// display
			GraphComponent<Graph<Integer>, Integer> jpanle;
			try {
				jpanle = new GraphComponent<Graph<Integer>, Integer> (g);
				jpanle.initialize();
				new DisplayFrame(jpanle, true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

}
