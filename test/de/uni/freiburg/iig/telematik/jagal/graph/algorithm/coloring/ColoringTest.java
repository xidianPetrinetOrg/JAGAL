package de.uni.freiburg.iig.telematik.jagal.graph.algorithm.coloring;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ColoringTest {

	@Test
	public void testColoring() {
		// Assert that object exists
		Coloring coloring = new Coloring();
		assertNotNull(coloring);
	}

	@Test
	public void testSetGetColor() {
		Coloring coloring = new Coloring();
		Color key = new Color(255, 0, 1);

		// assertNull as key is not set
		assertNull(coloring.getColor(key));

		// assert that getColor returns "1" as configured in the set function
		coloring.setColor(key, 1);
		assertSame(1, coloring.getColor(key));

		// reassign "0" as color and assert the new return from getColor
		coloring.setColor(key, 0);
		assertSame(0, coloring.getColor(key));
	}

	@Test
	public void testUncolor() {
		// Initialising coloring
		Coloring coloring = new Coloring();

		Color key1 = new Color(1, 2, 3);
		Color key2 = new Color(4, 5, 6);

		coloring.setColor(key1, 1);
		coloring.setColor(key2, 1);

		// getColor(key1) should return 1 as getColor(key2)
		assertSame(1, coloring.getColor(key1));
		assertSame(1, coloring.getColor(key2));

		// getColor(key1) should return null after uncoloring
		coloring.uncolor(key1);
		assertSame(null, coloring.getColor(key1));

		// getColor(key2) should still return 1
		assertSame(1, coloring.getColor(key2));
	}

	@Test
	public void testIsColored() {
		Coloring coloring = new Coloring();
		Color key1 = new Color(1, 2, 3);
		Color key2 = new Color(4, 5, 6);

		// Both keys should not be related to coloring
		assertFalse(coloring.isColored(key1));
		assertFalse(coloring.isColored(key2));

		// key1 should be colored as set before, whereas key2 is still uncolored
		coloring.setColor(key1, 1);
		assertTrue(coloring.isColored(key1));
		assertFalse(coloring.isColored(key2));

		// Both keys should be related to coloring
		coloring.setColor(key2, 2);
		assertTrue(coloring.isColored(key1));
		assertTrue(coloring.isColored(key2));
	}

	@Test
	public void testChromaticNumber() {
		// TODO: Test is not working, as coloring.chromaticNumber() returns 3 consistently, although keys getting uncolored from coloring
		Coloring coloring = new Coloring();
		Color key1 = new Color(1, 2, 3);
		Color key2 = new Color(4, 5, 6);
		Color key3 = new Color(7, 8, 9);

		coloring.setColor(key1, 1);
		coloring.setColor(key2, 2);
		coloring.setColor(key3, 3);
		assertEquals(3, coloring.chromaticNumber());

		coloring.uncolor(key2);
		assertEquals(2, coloring.chromaticNumber());

		coloring.uncolor(key1);
		assertEquals(1, coloring.chromaticNumber());

		coloring.uncolor(key3);
		assertEquals(0, coloring.chromaticNumber());
	}

	@Test
	public void testIsComplete() {
		//Initialising colours
		Coloring coloring = new Coloring();
		Collection<Color> keys = new ArrayList<>();
		Color key1 = new Color(1, 2, 3);
		Color key2 = new Color(4, 5, 6);
		Color key3 = new Color(7, 8, 9);
		Color key4 = new Color(10, 11, 12);
		keys.add(key1);
		keys.add(key2);
		keys.add(key3);

		// coloring should be complete
		coloring.setColor(key1, 1);
		coloring.setColor(key2, 2);
		coloring.setColor(key3, 3);
		assertTrue(coloring.isComplete(keys));

		// coloring should not be complete
		coloring.uncolor(key2);
		assertFalse(coloring.isComplete(keys));

		// coloring should be complete again after adding key2 again with a different integer value
		coloring.setColor(key2, 5);
		assertTrue(coloring.isComplete(keys));

		// coloring should be complete nevertheless it contains key4 additionally
		coloring.setColor(key4, 4);
		assertTrue(coloring.isComplete(keys));

		// coloring should not be complete, as key1 is going to be removed
		coloring.uncolor(key1);
		assertFalse(coloring.isComplete(keys));

	}

	@Test
	public void testGetColorGroups() {
		// Initialising coloring
		Map<Integer, Set<Color>> colorGroups = new HashMap<>();
		Coloring coloring = new Coloring();
		Color key1 = new Color(1, 2, 3);
		Color key2 = new Color(4, 5, 6);
		Color key3 = new Color(7, 8, 9);
		coloring.setColor(key1, 1);
		coloring.setColor(key2, 2);
		coloring.setColor(key3, 3);

		// Initialising colorGroups
		Set<Color> keys1 = new HashSet<>();
		keys1.add(key1);
		Set<Color> keys2 = new HashSet<>();
		keys2.add(key2);
		Set<Color> keys3 = new HashSet<>();
		keys3.add(key3);
		colorGroups.put(1, keys1);
		colorGroups.put(2, keys2);
		colorGroups.put(3, keys3);

		// coloring and colorGroups should be equal
		assertEquals(coloring.getColorGroups(), colorGroups);

		// Remove key2 from coloring and assert unequality
		coloring.uncolor(key2);
		assertNotEquals(coloring.getColorGroups(), colorGroups);

		// Remove key2 from colorGroups and assert equality
		colorGroups.remove(2, keys2);
		assertEquals(coloring.getColorGroups(), colorGroups);

	}

	@Test
	public void testGetUncoloredKeys() {
		// Initialising coloring
		Coloring coloring = new Coloring();
		Color key1 = new Color(1, 2, 3);
		Color key2 = new Color(4, 5, 6);
		Color key3 = new Color(7, 8, 9);
		coloring.setColor(key1, 1);

		// Initialising set of keys for comparison 
		Set<Color> keys = new HashSet<>();
		keys.add(key1);
		keys.add(key2);
		keys.add(key3);

		// Assert that key2 and key3 are uncolored
		assertEquals("[" + key2 + ", " + key3 + "]", coloring.getUncoloredKeys(keys).toString());

		// Allocate key2 and assert that only key3 is still uncolored 
		coloring.setColor(key2, 2);
		assertEquals("[" + key3 + "]", coloring.getUncoloredKeys(keys).toString());

		// Uncolor key1 and assert that key1 and key3 are uncolored
		coloring.uncolor(key1);
		assertEquals("[" + key1 + ", " + key3 + "]", coloring.getUncoloredKeys(keys).toString());
	}

	@Test
	public void testToString() {
		// Initialising coloring
		Coloring coloring = new Coloring();

		Color key1 = new Color(1, 2, 3);
		Color key2 = new Color(4, 5, 6);
		Color key3 = new Color(7, 8, 9);

		coloring.setColor(key1, 1);
		coloring.setColor(key2, 2);
		coloring.setColor(key3, 3);

		// Assert correctness of coloring.toString()
		assertEquals("GraphColoring {" + "\n" + key1 + ":" + " " + "1" + "\n" + key2 + ":" + " " + "2" + "\n" + key3 + ":" + " " + "3" + "\n" + "}" + "\n", coloring.toString());

		// Assert correctness of coloring.toString() after removing key2 and key3
		coloring.uncolor(key2);
		coloring.uncolor(key3);
		assertEquals("GraphColoring {" + "\n" + key1 + ":" + " " + "1" + "\n" + "}" + "\n", coloring.toString());

		// Assert correctness of coloring.toString() after adding key3 again
		coloring.setColor(key3, 3);
		assertEquals("GraphColoring {" + "\n" + key1 + ":" + " " + "1" + "\n" + key3 + ":" + " " + "3" + "\n" + "}" + "\n", coloring.toString());
	}
}
