package de.uni.freiburg.iig.telematik.jagal.graph;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import java.util.ArrayList;
import org.junit.Ignore;

public class GraphTest {

	Collection<String> vertices = null;
	Vertex vertex_1;
	Vertex vertex_2;
	Vertex vertex_3;

	@Before
	public void setUp() throws Exception {
		// Setup standard nodes
		vertices = new ArrayList<>();
		vertices.add("vertex_1");
		vertices.add("vertex_2");
		vertices.add("vertex_3");
	}

	@Test
	public void testGraph() {
		Graph graph = new Graph();
		assertEquals("Graph", graph.getName());
		assertTrue(graph.isEmpty());
		assertEquals(graph.getVertexCount(), 0);
		assertEquals(graph.nodeCount(), 0);
	}

	@Test
	public void testGraphString() {
		Graph graph = new Graph("Test_Graph");
		assertEquals("Test_Graph", graph.getName());
		assertTrue(graph.isEmpty());
		assertEquals(graph.getVertexCount(), 0);
		assertEquals(graph.nodeCount(), 0);
	}

	@Test
	public void testGraphCollectionOfString() {
		Graph graph = new Graph(vertices);
		assertEquals("Graph", graph.getName());
		assertFalse(graph.isEmpty());
		assertEquals(graph.getVertexCount(), 3);
		assertEquals(graph.nodeCount(), 3);
	}

	@Test
	public void testGraphStringCollectionOfString() {
		Graph graph = new Graph("Test_Graph", vertices);
		assertEquals("Test_Graph", graph.getName());
		assertFalse(graph.isEmpty());
		assertEquals(graph.getVertexCount(), 3);
		assertEquals(graph.nodeCount(), 3);
	}

	@Test
	public void testCreateNewVertexStringT() {
		Graph graph = new Graph();
		graph.createNewVertex("test", vertex_1);
		graph.addVertex("test");
		assertEquals("Graph", graph.getName());
		assertFalse(graph.isEmpty());
		assertEquals(graph.getVertexCount(), 1);
		assertEquals(graph.nodeCount(), 1);
		assertTrue(graph.containsVertex("test"));
		assertTrue(graph.containsVertex("test", vertex_1));
	}

	@Ignore
	public void testCreateNewEdgeVertexOfTVertexOfT() throws VertexNotFoundException {
		fail("adding new edge doesn't make sense as it cannot be used");
		/*
		Graph graph = new Graph();
		Edge edge = graph.createNewEdge(vertex_1, vertex_2);
		graph.addEdge(edge.getSource().toString(), edge.getTarget().toString());
		assertEquals("Graph", graph.getName());
		assertTrue(graph.isEmpty());
		assertEquals(graph.getVertexCount(),0);
		assertEquals(graph.nodeCount(),0);
		assertTrue(graph.containsEdge("null", "null"));
		 */
	}
}
