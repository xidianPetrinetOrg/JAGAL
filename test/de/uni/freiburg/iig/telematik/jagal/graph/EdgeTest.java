package de.uni.freiburg.iig.telematik.jagal.graph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EdgeTest {

	Vertex source1 = null;
	Vertex target1 = null;
	Vertex source2 = null;
	Vertex target2 = null;

	@Before
	public void setUp() throws Exception {
		// Setup standard transition and place for Edge
		source1 = new Vertex("p0");
		target1 = new Vertex("t0");
		source2 = new Vertex("p0");
		target2 = new Vertex("t0");
	}

	@Test
	public void testHashcodeSameObject() {
		Edge edge1 = new Edge(source1, target1);
		Edge edge2 = new Edge(source1, target1);

		int edge1hashcode = edge1.hashCode();
		int edge2hashcode = edge2.hashCode();

		int target1hashcode = target1.hashCode();
		int target2hashcode = target2.hashCode();

		int source1hashcode = source1.hashCode();
		int source2hashcode = source2.hashCode();

		assertEquals("Equal object, return equal hashcode test fails", edge1hashcode, edge2hashcode);
		assertEquals("Equal object, return equal hashcode test fails", target1hashcode, target2hashcode);
		assertEquals("Equal object, return equal hashcode test fails", source1hashcode, source2hashcode);

		assertNotEquals("Equal object, return equal hashcode test fails", edge1hashcode, target1hashcode);
		assertNotEquals("Equal object, return equal hashcode test fails", target1hashcode, source2hashcode);
		assertNotEquals("Equal object, return equal hashcode test fails", edge2hashcode, source2hashcode);
	}

	@Test
	public void testHashcodeIsConsistent() {
		Edge edge = new Edge(source1, target1);

		// Repeated calls to hashcode should consistently return the same integer.
		int target_initial_hashcode = target1.hashCode();
		int source_initial_hashcode = source1.hashCode();
		int edge_initial_hashcode = edge.hashCode();

		assertEquals("Consistent hashcode test fails", source_initial_hashcode, source1.hashCode());
		assertEquals("Consistent hashcode test fails", source_initial_hashcode, source1.hashCode());

		assertEquals("Consistent hashcode test fails", target_initial_hashcode, target1.hashCode());
		assertEquals("Consistent hashcode test fails", target_initial_hashcode, target1.hashCode());

		assertEquals("Consistent hashcode test fails", edge_initial_hashcode, edge.hashCode());
		assertEquals("Consistent hashcode test fails", edge_initial_hashcode, edge.hashCode());
	}

	@Test
	public void testEdge() {
		Edge edge = new Edge();
		assertNull(edge.getSource());
		assertNull(edge.getTarget());
	}

	@Test
	public void testEdgeVV() {
		Edge edge = new Edge(source1, target1);
		assertEquals("p0", edge.getSource().getName());
		assertEquals("t0", edge.getTarget().getName());
	}

	@Test
	public void testGetSource() {
		Edge edge = new Edge(source1, target1);
		assertEquals(source1, edge.getSource());
	}

	@Test
	public void testSetSource() {
		Edge edge = new Edge(source1, target1);
		edge.setSource(source2);
		assertEquals(source2, edge.getSource());
	}

	@Test
	public void testGetTarget() {
		Edge edge = new Edge(source1, target1);
		assertEquals(target1, edge.getTarget());
	}

	@Test
	public void testSetTarget() {
		Edge edge = new Edge(source1, target1);
		edge.setSource(target2);
		assertEquals(target2, edge.getSource());
	}

	@Test
	public void testEquals_isReflexive_isSymmetric() {
		Edge edge1 = new Edge(source1, target1);
		Edge edge2 = new Edge(source1, target1);
		assertTrue("Reflexive test fail", edge1.equals(edge2));
		assertTrue("Symmetric test fail", edge2.equals(edge1));
	}

	@Test
	public void testClone() {
		Edge edge1 = new Edge(source1, target1);
		Edge edge2 = edge1.clone();
		Edge edge3 = edge2.clone();
		assertTrue("Clone test edge1 - edge2 fail", edge1.equals(edge2));
		assertTrue("Clone of clone test edge1 - edge3 fail", edge1.equals(edge3));
	}

	@Test
	public void testToString() {
		Edge edge1 = new Edge(source1, target1);
		Edge edge2 = new Edge();
		assertEquals("(p0 -> t0)", edge1.toString());
		assertEquals("(null -> null)", edge2.toString());
	}
}
