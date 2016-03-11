package de.uni.freiburg.iig.telematik.jagal.graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

public class VertexTest {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashcodeSameObject() {
		Vertex<Object> vertex1 = new Vertex<>("vertex");
		Vertex<Object> vertex2 = new Vertex<>("vertex");

		Object element = "test";
		Vertex<Object> vertex3 = new Vertex<>("vertex", element);
		Vertex<Object> vertex4 = new Vertex<>("vertex", element);

		int vertex1hashcode = vertex1.hashCode();
		int vertex2hashcode = vertex2.hashCode();
		int vertex3hashcode = vertex3.hashCode();
		int vertex4hashcode = vertex4.hashCode();

		assertEquals("Equal object, return equal hashcode test fails", vertex1hashcode, vertex2hashcode);
		assertEquals("Equal object, return equal hashcode test fails", vertex3hashcode, vertex4hashcode);

		assertNotEquals("Equal object, return equal hashcode test fails", vertex1hashcode, vertex3hashcode);
		assertNotEquals("Equal object, return equal hashcode test fails", vertex2hashcode, vertex4hashcode);
	}

	@Test
	public void testHashcodeIsConsistent() {
		Vertex<Object> vertex1 = new Vertex<>("vertex");
		Vertex<Object> vertex2 = new Vertex<>("vertex");

		// Repeated calls to hashcode should consistently return the same integer.
		int vertex1_initial_hashcode = vertex1.hashCode();
		int vertex2_initial_hashcode = vertex2.hashCode();

		assertEquals("Consistent hashcode test fails", vertex1_initial_hashcode, vertex1.hashCode());
		assertEquals("Consistent hashcode test fails", vertex1_initial_hashcode, vertex1.hashCode());

		assertEquals("Consistent hashcode test fails", vertex2_initial_hashcode, vertex2.hashCode());
		assertEquals("Consistent hashcode test fails", vertex2_initial_hashcode, vertex2.hashCode());
	}

	@Test
	public void testVertexString() {
		Vertex<Object> vertex = new Vertex<>("vertex");
		assertEquals("vertex", vertex.getName());
	}

	@Test
	public void testVertexStringT() {
		Object element = "test";
		Vertex<Object> vertex = new Vertex<>("vertex", element);
		assertEquals("vertex", vertex.getName());
		assertEquals(element, vertex.getElement());
		assertEquals("test", vertex.getElement().toString());
	}

	@Test
	public void testGetElement() {
		Object element = "test";
		Object element2;
		Vertex<Object> vertex = new Vertex<>("vertex", element);
		element2 = vertex.getElement();
		assertEquals(element, element2);
	}

	@Test
	public void testSetElement() {
		Vertex<Object> vertex = new Vertex<>("vertex");
		Object element = "test";
		vertex.setElement(element);
		assertEquals(element, vertex.getElement());
	}

	@Test
	public void testGetName() {
		Vertex<Object> vertex = new Vertex<>("vertex");
		assertEquals("vertex", vertex.getName());
	}

	@Test
	public void testSetName() {
		Vertex<Object> vertex = new Vertex<>("vertex");
		vertex.setName("new_vertex");
		assertEquals("new_vertex", vertex.getName());
	}

	@Test
	public void testToString() {
		Vertex<Object> vertex = new Vertex<>("vertex");
		assertEquals("vertex", vertex.toString());
	}

	@Test
	public void testEquals_isReflexive_isSymmetric() {
		Vertex<Object> vertex1 = new Vertex<>("vertex1");
		Vertex<Object> vertex2 = new Vertex<>("vertex1");
		assertTrue("Reflexive test fail", vertex1.equals(vertex2));
		assertTrue("Symmetric test fail", vertex2.equals(vertex1));
	}

	@Test
	public void testClone() {
		Vertex<Object> vertex1 = new Vertex<>("vertex");
		Vertex<Object> vertex2 = vertex1.clone();
		Vertex<Object> vertex3 = vertex2.clone();
		assertTrue("Clone test vertex1 - vertex2 fail", vertex1.equals(vertex2));
		assertTrue("Clone of clone test vertex1 - vertex3 fail", vertex1.equals(vertex3));
	}

}
