package de.uni.freiburg.iig.telematik.jagal.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;

public class GraphUtilsTest {

	@Test
	public void testCycleBy() throws GraphException {
		Graph graph = new Graph();

		// Create Graph
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		graph.addVertex("v5");
		graph.addEdge("v1", "v2");
		graph.addEdge("v2", "v3");
		graph.addEdge("v3", "v4");
		graph.addEdge("v4", "v5");
		graph.addEdge("v5", "v1");

		// There should exist a cyrcle between v1, v2, v3, v4 and v5
		assertTrue(GraphUtils.cycleBy(graph, "v1", "v5", "v2"));
		assertTrue(GraphUtils.cycleBy(graph, "v2", "v1", "v3"));
		assertTrue(GraphUtils.cycleBy(graph, "v3", "v2", "v4"));
		assertTrue(GraphUtils.cycleBy(graph, "v4", "v3", "v5"));
		assertTrue(GraphUtils.cycleBy(graph, "v5", "v4", "v1"));

		// Destroy cyrcle
		graph.removeEdge("v3", "v4");
		assertFalse(GraphUtils.cycleBy(graph, "v1", "v5", "v2"));
		assertFalse(GraphUtils.cycleBy(graph, "v5", "v4", "v1"));

		// Create new cyrcle between v1, v2 and v3
		graph.addEdge("v3", "v1");
		assertTrue(GraphUtils.cycleBy(graph, "v1", "v3", "v2"));
		assertTrue(GraphUtils.cycleBy(graph, "v2", "v1", "v3"));
		assertTrue(GraphUtils.cycleBy(graph, "v3", "v2", "v1"));

		// Create a new cyrcle between two nodes
		graph.addEdge("v3", "v2");
		assertTrue(GraphUtils.cycleBy(graph, "v2", "v3", "v3"));
		assertTrue(GraphUtils.cycleBy(graph, "v3", "v2", "v2"));
	}

}
