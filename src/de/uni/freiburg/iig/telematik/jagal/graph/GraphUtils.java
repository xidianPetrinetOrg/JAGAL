package de.uni.freiburg.iig.telematik.jagal.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.EdgeNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;

public class GraphUtils {

    private final static ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(10);
    private final static Set<Object> visited = new HashSet<>();

    /**
     * Checks, if there is a closed cycle in the graph with Node&lt;T&gt;
     * baseNode&lt;T&gt;, using inEdge and outEdge.
     *
     * @param <V> Type of the vertex
     * @param <E> Type of the edge
     * @param <U> Type of the graph's elements
     * @param graph The graph to check.
     * @param baseVertexName The base Node&lt;T&gt; for cycle check
     * @param outEdgeVertexName Incoming edge of the base Node&lt;T&gt; in the
     * potential cycle
     * @param inEdgeVertexName Outgoing edge of the base Node&lt;T&gt; in the
     * potential cycle
     * @return <code>true</code> if there is a closed cycle with inEdge and
     * outEdge; <code>false</code> otherwise.
     * @throws
     * de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException
     */
    public static <V extends Vertex<U>, E extends Edge<V>, U> boolean cycleBy(AbstractGraph<V, E, U> graph, String baseVertexName, String inEdgeVertexName, String outEdgeVertexName) throws GraphException {
        if (!graph.containsEdge(inEdgeVertexName, baseVertexName)) {
            throw new EdgeNotFoundException(inEdgeVertexName, baseVertexName, graph);
        }
        if (!graph.containsEdge(baseVertexName, outEdgeVertexName)) {
            throw new EdgeNotFoundException(baseVertexName, outEdgeVertexName, graph);
        }

        visited.clear();
        queue.clear();
        queue.offer(outEdgeVertexName);
        while (!queue.isEmpty()) {
            if (queue.peek().equals(inEdgeVertexName)) {
                return true;
            }
            visited.add(queue.peek());
            // Cast is safe since only objects of type V were added to the queue
            // before.
            for (V vertex : graph.getChildren((String) queue.poll())) {
                if (!queue.contains(vertex.getName()) && !visited.contains(vertex.getName())) {
                    queue.offer(vertex.getName());
                }
            }
        }
        return false;
    }

}
