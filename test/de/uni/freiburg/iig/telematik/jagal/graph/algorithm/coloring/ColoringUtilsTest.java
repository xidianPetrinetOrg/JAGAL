package de.uni.freiburg.iig.telematik.jagal.graph.algorithm.coloring;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.EdgeNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;

public class ColoringUtilsTest {

	@Test
	public void testGetElementColoring() throws VertexNotFoundException {
		// Initialising coloring
		Coloring coloring = new Coloring();
		Object test = "test";

		Graph graph = new Graph();
		graph.addVertex("vertex1", test);
		graph.addVertex("vertex2", test);
		graph.addVertex("vertex3", test);

		coloring.setColor(graph.getVertex("vertex1"), 1);
		coloring.setColor(graph.getVertex("vertex2"), 2);
		coloring.setColor(graph.getVertex("vertex3"), 3);

		graph.addEdge("vertex1", "vertex2");
		graph.addEdge("vertex1", "vertex3");

		// vertex.getElement() erzeugt eine null pointer exception, obwohl allen vertices ein Objekt zugewiesen wurde
		ColoringUtils.getElementColoring(graph, coloring);
	}

	@Test
	public void testMaxClique() throws VertexNotFoundException, EdgeNotFoundException {
		// Initialising graph
		Graph<String> g = new Graph<>();
		g.addVertex("1");
		g.addVertex("2");
		g.addVertex("3");
		g.addVertex("4");
		g.addVertex("5");
		g.addVertex("6");
		g.addEdge("1", "2");
		g.addEdge("1", "5");
		g.addEdge("2", "5");
		g.addEdge("4", "2");
		g.addEdge("4", "1");
		g.addEdge("5", "4");

		// Assert that bigget group is [1, 2, 4, 5]
		assertEquals("[1, 2, 4, 5]", ColoringUtils.maxClique(g).toString());

		// Assert that bigget group is [2, 4, 5] after removing one edge
		g.removeEdge("1", "2");
		assertEquals("[2, 4, 5]", ColoringUtils.maxClique(g).toString());
	}
}
