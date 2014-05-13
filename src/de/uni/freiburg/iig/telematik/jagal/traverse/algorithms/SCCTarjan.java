package de.uni.freiburg.iig.telematik.jagal.traverse.algorithms;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.traverse.Traversable;


public class SCCTarjan<V extends Object> {

	private Stack<V> stack = new Stack<V>();
	private Map<V, Integer> indexMap = new HashMap<V, Integer>();
	private Map<V, Integer> lowlinkMap = new HashMap<V, Integer>();
	
	private int index = 0;
	private Set<Set<V>> sccs = new HashSet<Set<V>>();
	

	public Set<Set<V>> execute(Traversable<V> graph) throws ParameterException {
		Validate.notNull(graph);
		sccs.clear();
		index = 0;
		stack.clear();
		indexMap.clear();
		lowlinkMap.clear();
		if (graph != null) {
			List<V> VList = new ArrayList<V>(graph.getNodes());
			for (V v : VList) {
				if (!indexMap.containsKey(v)) {
					try {
						executeRecursive(v, graph);
					} catch (VertexNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return sccs;
	}
	  
	private Set<Set<V>> executeRecursive(V v, Traversable<V> graph) throws VertexNotFoundException, ParameterException {
		indexMap.put(v, index);
		lowlinkMap.put(v, index);
		index++;
		stack.push(v);
		for (V n : graph.getChildren(v)) {
			if (!indexMap.containsKey(n)) {
				executeRecursive(n, graph);
				lowlinkMap.put(v,
						Math.min(lowlinkMap.get(v), lowlinkMap.get(n)));
			} else if (stack.contains(n)) {
				lowlinkMap.put(v, Math.min(lowlinkMap.get(v), indexMap.get(n)));
			}
		}
		if (lowlinkMap.get(v) == indexMap.get(v)) {
			V n;
			Set<V> component = new HashSet<V>();
			do {
				n = stack.pop();
				component.add(n);
			} while (n != v);
			sccs.add(component);
		}
		return sccs;
	}

	public static void main(String[] args) throws ParameterException {
		Graph<String> g = new Graph<String>();
		g.addVertex("1");
		g.addVertex("2");
		g.addVertex("3");
		g.addVertex("4");
		try {
			g.addEdge("1", "2");
			g.addEdge("1", "3");
			g.addEdge("1", "4");
			g.addEdge("2", "3");
			g.addEdge("3", "2");
		} catch (VertexNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(g);
		SCCTarjan<Vertex<String>> tarjan = new SCCTarjan<Vertex<String>>();
		System.out.println(tarjan.execute(g));
	}

}