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

        private final Stack<V> stack = new Stack<>();
        private final Map<V, Integer> indexMap = new HashMap<>();
        private final Map<V, Integer> lowlinkMap = new HashMap<>();

        private int index = 0;
        private final Set<Set<V>> sccs = new HashSet<>();

        public Set<Set<V>> execute(Traversable<V> graph) throws ParameterException {
                Validate.notNull(graph);
                sccs.clear();
                index = 0;
                stack.clear();
                indexMap.clear();
                lowlinkMap.clear();
                if (graph != null) {
                        List<V> VList = new ArrayList<>(graph.getNodes());
                        for (V v : VList) {
                                if (!indexMap.containsKey(v)) {
                                        try {
                                                executeRecursive(v, graph);
                                        } catch (VertexNotFoundException e) {
                                                throw new RuntimeException(e);
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
                if (lowlinkMap.get(v).equals(indexMap.get(v))) {
                        V n;
                        Set<V> component = new HashSet<>();
                        do {
                                n = stack.pop();
                                component.add(n);
                        } while (n != v);
                        sccs.add(component);
                }
                return sccs;
        }

        public static void main(String[] args) throws ParameterException {
                Graph<String> g = new Graph<>();
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
                        throw new RuntimeException(e);
                }
                System.out.println(g);
                SCCTarjan<Vertex<String>> tarjan = new SCCTarjan<>();
                System.out.println(tarjan.execute(g));
        }

}
