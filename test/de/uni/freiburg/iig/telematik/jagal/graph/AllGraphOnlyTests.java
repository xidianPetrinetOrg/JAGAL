package de.uni.freiburg.iig.telematik.jagal.graph;

import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AllAbstrGraphTests;
import de.uni.freiburg.iig.telematik.jagal.graph.algorithm.coloring.AllColoringTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({EdgeTest.class, GraphTest.class, GraphUtilsTest.class, VertexTest.class, AllAbstrGraphTests.class, AllColoringTests.class})
public class AllGraphOnlyTests {
}
