package de.uni.freiburg.iig.telematik.jagal.graph.abstr;

import static org.junit.Assert.*;

import org.junit.Test;

import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.EdgeNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import org.junit.Ignore;

public class AbstractGraphTest {

	@Test
	public void testGetName() {
		Graph graph = new Graph("testgraph");
		assertEquals("testgraph", graph.getName());
	}

	@Test
	public void testGetDefaultName() {
		Graph graph = new Graph("testgraph");
		assertEquals("Graph", graph.getDefaultName());
	}

	@Test
	public void testSetName() {
		Graph graph = new Graph("testgraph");
		assertEquals("testgraph", graph.getName());

		graph.setName("newname");
		assertEquals("newname", graph.getName());
	}

	@Test
	public void testIsEmpty() throws VertexNotFoundException {
		// Test if graph is empty (contains no vertex)
		Graph graph = new Graph("testgraph");
		graph.addVertex("vertex1");
		assertFalse(graph.isEmpty());
		graph.removeVertex("vertex1");
		assertTrue(graph.isEmpty());
	}

	@Test
	public void testIsTrivial() throws VertexNotFoundException {
		// Test if graph is trivial (contains <= 1 vertices)
		Graph graph = new Graph("testgraph");
		graph.addVertex("vertex1");
		assertTrue(graph.isTrivial());
		graph.addVertex("vertex2");
		assertFalse(graph.isTrivial());
		graph.removeVertex("vertex1");
		assertTrue(graph.isTrivial());
		graph.removeVertex("vertex2");
		assertTrue(graph.isTrivial());
	}

	@Test
	public void testGetSources() throws VertexNotFoundException {
		// Source vertexes have no incoming, but at least one outgoing edge
		Graph graph = new Graph("testgraph");
		// No vertices --> no sources
		assertEquals("[]", graph.getSources().toString());

		// No edges --> no sources
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		assertEquals("[]", graph.getSources().toString());

		// v1 outgoing: 1, incoming: 0, source: y
		graph.addEdge("v1", "v2");
		assertEquals("[v1]", graph.getSources().toString());

		// v1 outgoing: 2, incoming: 0, source: y
		graph.addEdge("v1", "v3");
		assertEquals("[v1]", graph.getSources().toString());

		// v1 outgoing: 2, incoming: 0, source: y
		// v4 outgoing: 1, incoming: 0, source: y
		graph.addEdge("v4", "v2");
		assertEquals("[v1, v4]", graph.getSources().toString());

		// v1 outgoing: 2, incoming: 1, source: n
		// v4 outgoing: 1, incoming: 0, source: y
		graph.addEdge("v2", "v1");
		assertEquals("[v4]", graph.getSources().toString());

		// v1 outgoing: 2, incoming: 1, source: n
		// v4 outgoing: 1, incoming: 1, source: n
		graph.addEdge("v1", "v4");
		assertEquals("[]", graph.getSources().toString());
	}

	@Test
	public void testGetDrains() throws VertexNotFoundException {
		// Drain vertexes have no outgoing, but at least one incoming edge
		Graph graph = new Graph("testgraph");
		// No vertices --> no drains
		assertFalse(graph.hasSeparatedVertices());

		// No edges --> no drains
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		assertEquals("[]", graph.getDrains().toString());

		// v1 outgoing: 0, incoming: 1, drain: y
		graph.addEdge("v2", "v1");
		assertEquals("[v1]", graph.getDrains().toString());

		// v1 outgoing: 0, incoming: 2, drain: y
		graph.addEdge("v3", "v1");
		assertEquals("[v1]", graph.getDrains().toString());

		// v1 outgoing: 0, incoming: 2, drain: y
		// v4 outgoing: 0, incoming: 1, drain: y
		graph.addEdge("v2", "v4");
		assertEquals("[v1, v4]", graph.getDrains().toString());

		// v1 outgoing: 1, incoming: 2, drain: n
		// v4 outgoing: 0, incoming: 1, drain: y
		graph.addEdge("v1", "v2");
		assertEquals("[v4]", graph.getDrains().toString());

		// v1 outgoing: 1, incoming: 2, drain: n
		// v4 outgoing: 1, incoming: 1, drain: n
		graph.addEdge("v4", "v1");
		assertEquals("[]", graph.getDrains().toString());
	}

	@Test
	public void testGetSeparatedVertices() throws VertexNotFoundException, EdgeNotFoundException {
		// Separated vertexes have no edges at all
		Graph graph = new Graph("testgraph");
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 0, separated: y
		// v3 outgoing: 0, incoming: 0, separated: y
		// v4 outgoing: 0, incoming: 0, separated: y
		assertEquals("[v1, v2, v3, v4]", graph.getSeparatedVertices().toString());

		// v1 outgoing: 1, incoming: 0, separated: n
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 0, incoming: 0, separated: y
		// v4 outgoing: 0, incoming: 0, separated: y
		graph.addEdge("v1", "v2");
		assertEquals("[v3, v4]", graph.getSeparatedVertices().toString());

		// v1 outgoing: 1, incoming: 0, separated: n
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 1, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 1, separated: n
		graph.addEdge("v3", "v4");
		assertEquals("[]", graph.getSeparatedVertices().toString());

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 0, separated: y
		// v3 outgoing: 1, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 1, separated: n
		graph.removeEdge("v1", "v2");
		assertEquals("[v1, v2]", graph.getSeparatedVertices().toString());

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 2, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 1, separated: n
		graph.addEdge("v3", "v2");
		assertEquals("[v1]", graph.getSeparatedVertices().toString());

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 1, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 0, separated: n
		graph.removeEdge("v3", "v4");
		assertEquals("[v1, v4]", graph.getSeparatedVertices().toString());
	}

	@Test
	public void testHasSeparatedVertices() throws VertexNotFoundException, EdgeNotFoundException {
		// Separated vertexes have no edges at all
		Graph graph = new Graph("testgraph");
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 0, separated: y
		// v3 outgoing: 0, incoming: 0, separated: y
		// v4 outgoing: 0, incoming: 0, separated: y
		assertTrue(graph.hasSeparatedVertices());

		// v1 outgoing: 1, incoming: 0, separated: n
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 0, incoming: 0, separated: y
		// v4 outgoing: 0, incoming: 0, separated: y
		graph.addEdge("v1", "v2");
		assertTrue(graph.hasSeparatedVertices());

		// v1 outgoing: 1, incoming: 0, separated: n
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 1, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 1, separated: n
		graph.addEdge("v3", "v4");
		assertFalse(graph.hasSeparatedVertices());

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 0, separated: y
		// v3 outgoing: 1, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 1, separated: n
		graph.removeEdge("v1", "v2");
		assertTrue(graph.hasSeparatedVertices());

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 2, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 1, separated: n
		graph.addEdge("v3", "v2");
		assertTrue(graph.hasSeparatedVertices());

		// v1 outgoing: 0, incoming: 0, separated: y
		// v2 outgoing: 0, incoming: 1, separated: n
		// v3 outgoing: 1, incoming: 0, separated: n
		// v4 outgoing: 0, incoming: 0, separated: n
		graph.removeEdge("v3", "v4");
		assertTrue(graph.hasSeparatedVertices());
	}

	@Test
	public void testHasDrains() throws VertexNotFoundException {
		// Drain vertexes have no outgoing, but at least one incoming edge
		Graph graph = new Graph("testgraph");
		// No vertices --> no drains
		assertFalse(graph.hasDrains());

		// No edges --> no drains
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
//        assertFalse(graph.hasDrains());

		// v1 outgoing: 0, incoming: 1, drain: y
		graph.addEdge("v2", "v1");
		assertTrue(graph.hasDrains());

		// v1 outgoing: 0, incoming: 2, drain: y
		graph.addEdge("v3", "v1");
		assertTrue(graph.hasDrains());

		// v1 outgoing: 0, incoming: 2, drain: y
		// v4 outgoing: 0, incoming: 1, drain: y
		graph.addEdge("v2", "v4");
		assertTrue(graph.hasDrains());

		// v1 outgoing: 1, incoming: 2, drain: n
		// v4 outgoing: 0, incoming: 1, drain: y
		graph.addEdge("v1", "v2");
		assertTrue(graph.hasDrains());

		// v1 outgoing: 1, incoming: 2, drain: n
		// v4 outgoing: 1, incoming: 1, drain: n
		graph.addEdge("v4", "v1");
		assertFalse(graph.hasDrains());
	}

	@Test
	public void testHasSources() throws VertexNotFoundException {
		// Source vertexes have no incoming, but at least one outgoing edge
		Graph graph = new Graph("testgraph");

		// No vertices --> no sources
		assertFalse(graph.hasSources());

		// No edges --> no sources
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
//        assertFalse(graph.hasSources());

		// v1 outgoing: 1, incoming: 0, source: y
		graph.addEdge("v1", "v2");
		assertTrue(graph.hasSources());

		// v1 outgoing: 2, incoming: 0, source: y
		graph.addEdge("v1", "v3");
		assertTrue(graph.hasSources());

		// v1 outgoing: 2, incoming: 0, source: y
		// v4 outgoing: 1, incoming: 0, source: y
		graph.addEdge("v4", "v2");
		assertTrue(graph.hasSources());

		// v1 outgoing: 2, incoming: 1, source: n
		// v4 outgoing: 1, incoming: 0, source: y
		graph.addEdge("v2", "v1");
		assertTrue(graph.hasSources());

		// v1 outgoing: 2, incoming: 1, source: n
		// v4 outgoing: 1, incoming: 1, source: n
		graph.addEdge("v1", "v4");
		assertFalse(graph.hasSources());
	}

	@Test
	public void testGetVertices() throws VertexNotFoundException {
		// A set containing all vertexes of this graph.
		Graph graph = new Graph("testgraph");
		assertEquals("[]", graph.getVertexNames().toString());

		// v1 in_graph: y
		// v2 in_graph: y
		// v3 in_graph: y
		// v4 in_graph: y
		// getVertexNames: [v1, v2, v3, v4]
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		assertEquals("[v1, v2, v3, v4]", graph.getVertices().toString());

		// v1 in_graph: n
		// v2 in_graph: y
		// v3 in_graph: y
		// v4 in_graph: y
		// GetVertices: [v2, v3, v4]
		graph.removeVertex("v1");
		assertEquals("[v2, v3, v4]", graph.getVertices().toString());

		// v1 in_graph: n
		// v2 in_graph: n
		// v3 in_graph: n
		// v4 in_graph: n
		// GetVertices: []
		graph.removeVertex("v2");
		graph.removeVertex("v3");
		graph.removeVertex("v4");
		assertEquals("[]", graph.getVertices().toString());

		// v1 in_graph: n
		// v2 in_graph: n
		// v3 in_graph: y
		// v4 in_graph: n
		// GetVertices: [v3]
		graph.addVertex("v3");
		assertEquals("[v3]", graph.getVertices().toString());
	}

	@Test
	public void testGetVertexNames() throws VertexNotFoundException {
		// A set containing all names of the vertices of this graph.
		Graph graph = new Graph("testgraph");
		assertEquals("[]", graph.getVertexNames().toString());

		// v1 in_graph: y
		// v2 in_graph: y
		// v3 in_graph: y
		// v4 in_graph: y
		// getVertexNames: [v1, v2, v3, v4]
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		assertEquals("[v1, v2, v3, v4]", graph.getVertexNames().toString());

		// v1 in_graph: n
		// v2 in_graph: y
		// v3 in_graph: y
		// v4 in_graph: y
		// getVertexNames: [v2, v3, v4]
		graph.removeVertex("v1");
		assertEquals("[v2, v3, v4]", graph.getVertexNames().toString());

		// v1 in_graph: n
		// v2 in_graph: n
		// v3 in_graph: n
		// v4 in_graph: n
		// getVertexNames: []
		graph.removeVertex("v2");
		graph.removeVertex("v3");
		graph.removeVertex("v4");
		assertEquals("[]", graph.getVertexNames().toString());

		// v1 in_graph: n
		// v2 in_graph: n
		// v3 in_graph: y
		// v4 in_graph: n
		// getVertexNames: [v3]
		graph.addVertex("v3");
		assertEquals("[v3]", graph.getVertexNames().toString());
	}

	@Test
	public void testGetVertexCount() throws VertexNotFoundException {
		Graph graph = new Graph("testgraph");
		assertEquals(0, graph.getVertexCount());

		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.addVertex("v4");
		assertEquals(4, graph.getVertexCount());

		graph.removeVertex("v1");
		assertEquals(3, graph.getVertexCount());

		graph.removeVertex("v2");
		graph.removeVertex("v3");
		graph.removeVertex("v4");
		assertEquals(0, graph.getVertexCount());

		graph.addVertex("v3");
		assertEquals(1, graph.getVertexCount());

	}

	@Test
	public void testAddVertexString() {
		// Adds a vertex with the given name to the graph if it does not already contain a vertex with the same name.
		Graph graph = new Graph("testgraph");
		graph.addVertex("v1");
		assertTrue(graph.containsVertex("v1"));

		// Adding v1 again shouldn't change the circumstance that v1 belongs to the graph
		graph.addVertex("v1");
		assertTrue(graph.containsVertex("v1"));

		// Trying to add another vertex
		graph.addVertex("v2");
		assertTrue(graph.containsVertex("v2"));
	}

	@Test
	public void testAddVertexStringU() {
		// Adds a vertex + object with the given name to the graph if it does not already contain a vertex with the same name.
		Graph graph = new Graph("testgraph");
		Object test1 = "teststrin2";
		Object test2 = "teststring2";
		graph.addVertex("v1", test1);
		assertTrue(graph.containsVertex("v1"));

		// Adding v1 with the same object again shouldn't change the circumstance that v1 belongs to the graph
		graph.addVertex("v1", test1);
		assertTrue(graph.containsVertex("v1"));

		// Adding v1 with another object again shouldn't change the circumstance that v1 belongs to the graph
		graph.addVertex("v1", test2);
		assertTrue(graph.containsVertex("v1"));

		// Trying to add another vertex
		graph.addVertex("v2", test1);
		assertTrue(graph.containsVertex("v2"));
	}

	@Ignore
	public void testAddVertices() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testContainsElement() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testContainsVertexV() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testContainsVertexString() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testContainsVertexStringU() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testCreateNewVertex() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testCreateNewEdge() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetVertexString() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetVertexObject() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testContainsObject() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testContainsAllObjects() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testVertexes() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveVertex() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testInDegreeOf() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testOutDegreeOf() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testIsSource() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testIsDrain() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testIsSeparated() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testIsBipartite() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetEdges() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetEdgeCount() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetEdge() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testContainsEdge() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testAddEdge() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetEdgeContainer() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testPrintEdgeContainers() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetEdgesFor() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testHasOutgoingEdges() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetOutgoingEdgesFor() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testHasIncomingEdges() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetIncomingEdgesFor() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveEdgeE() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveEdgeStringString() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveEdges() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveOutgoingEdgesFor() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testRemoveIncomingEdgesFor() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetParentsString() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetChildrenString() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetNeighbors() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetNodes() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetParentsV() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetChildrenV() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testToString() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetElementSet() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetElementSetCollectionOfV() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetElementList() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testGetElementListCollectionOfV() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testNodeCount() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testValidateVertex() {
		fail("Not yet implemented");
	}
}
