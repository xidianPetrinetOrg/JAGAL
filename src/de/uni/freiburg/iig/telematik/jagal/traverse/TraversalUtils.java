package de.uni.freiburg.iig.telematik.jagal.traverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.traverse.Traverser.TraversalMode;
import de.uni.freiburg.iig.telematik.jagal.traverse.algorithms.SCCTarjan;

public class TraversalUtils {

        private static final ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(10);
        private static final Set<Object> visited = new HashSet<>();

        /**
         * Returns <code>true</code>, if the given traversable structure is
         * weakly connected.
         *
         * @param <V>
         * @param traversableStructure
         * @return
         */
        public static <V extends Object> boolean isWeaklyConnected(Traversable<V> traversableStructure) {
                Validate.notNull(traversableStructure);
                for (V node : traversableStructure.getNodes()) {
                        Set<V> nodes = new HashSet<>();
                        try {
                                weakConnectivityRec(traversableStructure, node, nodes);
                        } catch (ParameterException | VertexNotFoundException e) {
                                // shouldn't be happening
                                throw new RuntimeException(e);
                        }
                        if (nodes.size() < traversableStructure.getNodes().size()) {
                                return false;
                        }
                        break;
                }
                return true;
        }

        /**
         * Returns <code>true</code>, if the given traversable structure is
         * strongly connected.
         *
         * @param <V>
         * @param traversableStructure
         * @param node
         * @return
         */
        public static <V extends Object> boolean isStronglyConnected(Traversable<V> traversableStructure, V node) {
                int visitedNodes = 0;
                Iterator<V> iter = new Traverser<>(traversableStructure, node, TraversalMode.DEPTHFIRST);
                while (iter.hasNext()) {
                        iter.next();
                        visitedNodes++;
                }
                return visitedNodes == traversableStructure.nodeCount();
        }

        public static <V extends Object> Set<Set<V>> getStronglyConnectedComponents(Traversable<V> traversableStructure) throws ParameterException {
                SCCTarjan<V> tarjan = new SCCTarjan<>();
                return tarjan.execute(traversableStructure);
        }

        public static <V extends Object> Set<V> getSiblings(Traversable<V> traversableStructure, V node) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);
                Set<V> result = new HashSet<>();
                for (V parent : traversableStructure.getParents(node)) {
                        result.addAll(traversableStructure.getChildren(parent));
                }
                result.remove(node);
                return result;
        }

        /**
         * Checks, if <code>queryNode</code> is a predecessor of
         * <code>baseNode</code>.
         *
         * @param <V>
         * @param traversableStructure
         * @param baseNode Basic Node for predecessor search
         * @param queryNode Query Node for predecessor search
         * @return <code>true</code> if <code>queryNode</code> is a predecessor
         * of <code>baseNode</code>; <code>false</code> otherwise.
         * @throws VertexNotFoundException
         * @throws ParameterException
         */
        public static <V extends Object> boolean isPredecessor(Traversable<V> traversableStructure, V queryNode, V baseNode) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);
                visited.clear();
                visited.add(baseNode);
                queue.clear();
                for (V parent : traversableStructure.getParents(baseNode)) {
                        queue.offer(parent);
                }
                while (!queue.isEmpty()) {
                        if (queryNode.equals(queue.peek())) {
                                return true;
                        }
                        //Cast is safe since only objects of type V were added to the queue before.
                        for (V parent : traversableStructure.getParents((V) queue.peek())) {
                                if (!visited.contains(parent) && !queue.contains(parent)) {
                                        queue.add(parent);
                                }
                        }
                        visited.add(queue.poll());
                }
                return false;
        }

        /**
         * Checks, if <code>queryNode&lt;T&gt;</code> is a successor of
         * <code>baseNode&lt;T&gt;</code>.
         *
         * @param <V>
         * @param traversableStructure
         * @param baseNode Basic Node&lt;T&gt; for successor search
         * @param queryNode Query Node&lt;T&gt; for successor search
         * @return <code>true</code> if <code>queryNode&lt;T&gt;</code> is a
         * successor of <code>baseNode&lt;T&gt;</code>; <code>false</code>
         * otherwise.
         * @throws VertexNotFoundException
         * @throws ParameterException
         */
        public static <V extends Object> boolean isSuccessor(Traversable<V> traversableStructure, V queryNode, V baseNode) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);
                visited.clear();
                visited.add(baseNode);
                queue.clear();
                for (V child : traversableStructure.getChildren(baseNode)) {
                        queue.offer(child);
                }
                while (!queue.isEmpty()) {
                        if (queryNode.equals(queue.peek())) {
                                return true;
                        }
                        //Cast is safe since only objects of type V were added to the queue before.
                        for (V child : traversableStructure.getChildren((V) queue.peek())) {
                                if (!visited.contains(child) && !queue.contains(child)) {
                                        queue.add(child);
                                }
                        }
                        visited.add(queue.poll());
                }
                return false;
        }

//	public static <V extends Vertex<U>, U> Set<V> getPredecessorsFor(AbstractGraph<V, ?, U> graph, V vertex) throws VertexNotFoundException
        public static <V extends Object> Set<V> getPredecessorsFor(Traversable<V> traversableStructure, V startNode) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);
                Set<V> visitedNodes = new HashSet<>();
                visitedNodes.add(startNode);
                queue.clear();
                for (V parent : traversableStructure.getParents(startNode)) {
                        queue.offer(parent);
                }
                while (!queue.isEmpty()) {
                        //Cast is safe since only objects of type V were added to the queue before.
                        for (V parent : traversableStructure.getParents((V) queue.peek())) {
                                if (!visitedNodes.contains(parent) && !queue.contains(parent)) {
                                        queue.add(parent);
                                }
                        }
                        visitedNodes.add((V) queue.poll());
                }
                visitedNodes.remove(startNode);
                return visitedNodes;
        }

//	public static <V extends Vertex<U>, U> Set<V> getSuccessorsFor(AbstractGraph<V, ?, U> graph, V vertex) throws GraphException
        public static <V extends Object> Set<V> getSuccessorsFor(Traversable<V> traversableStructure, V startNode) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);
                Set<V> visitedNodes = new HashSet<>();
                visitedNodes.add(startNode);
                queue.clear();
                for (V child : traversableStructure.getChildren(startNode)) {
                        queue.offer(child);
                }
                while (!queue.isEmpty()) {
                        //Cast is safe since only objects of type V were added to the queue before.
                        for (V child : traversableStructure.getChildren((V) queue.peek())) {
                                if (!visitedNodes.contains(child) && !queue.contains(child)) {
                                        queue.add(child);
                                }
                        }
                        visitedNodes.add((V) queue.poll());
                }
                visitedNodes.remove(startNode);
                return visitedNodes;
        }

//	public static <V extends Vertex<U>, U> boolean isVertexInCycle(AbstractGraph<V, ?, U> graph, V vertex) throws VertexNotFoundException{
        /**
         * Checks if a vertex is contained in a cycle.<br>
         * In case the given vertex is in a cycle, it is a predecessor of
         * itself.
         *
         * @param <V>
         * @param traversableStructure The graph that contains the vertex.
         * @param node The vertex for which the property is checked.
         * @return <code>true</code> if the given vertex is contained in a
         * cycle;<br>
         * <code>false</code>.
         * @throws VertexNotFoundException If the graph does not contain the
         * vertex.
         * @throws ParameterException
         */
        public static <V extends Object> boolean isNodeInCycle(Traversable<V> traversableStructure, V node) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);
                return isPredecessor(traversableStructure, node, node);
        }

        /**
         * Checks if a traversable structure contains a cycle.
         *
         * @param <V>
         * @param traversableStructure The graph to check for cycles.
         * @return <code>true</code> if the given structure contains at least
         * one cycle, <code>false</code> otherwise.
         * @throws ParameterException
         */
        public static <V extends Object> boolean hasCycle(Traversable<V> traversableStructure) throws ParameterException {
                Validate.notNull(traversableStructure);
                for (V node : traversableStructure.getNodes()) {
                        try {
                                if (isNodeInCycle(traversableStructure, node)) {
                                        return true;
                                }
                        } catch (VertexNotFoundException e) {
                                throw new RuntimeException(e);
                        }
                }
                return false;
        }

//	public static <V extends Vertex<U>, U> ArrayBlockingQueue<ArrayList<V>> getDirectedPathsFor(AbstractGraph<V, ?, U> graph, V sourceVertex, V targetVertex) throws VertexNotFoundException{
        /**
         * Returns a list of paths that lead from sourceVertex to targetVertex.
         *
         * @param <V>
         * @param traversableStructure
         * @param sourceNode The source vertex for the desired paths.
         * @param targetNode The target vertex for the desired paths.
         * @return A list of all paths leading from sourceVertex to
         * targetVertex.
         * @throws VertexNotFoundException If the graph does not contain the
         * given vertexes.
         * @throws ParameterException
         */
        public static <V extends Object> ArrayBlockingQueue<List<V>> getDirectedPathsFor(Traversable<V> traversableStructure, V sourceNode, V targetNode) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);

                return getDirectedPathsFor(traversableStructure, Arrays.asList(sourceNode), targetNode);
        }

//	public static <V extends Vertex<U>, U> ArrayBlockingQueue<List<V>> getDirectedPathsFor(AbstractGraph<V, ?, U> graph, List<V> sourceVertexes, V targetVertex) throws VertexNotFoundException{
        /**
         * Returns a list of paths leading from one source vertex to
         * targetVertex.
         *
         * @param <V>
         * @param traversableStructure The graph that contains the vertexes.
         * @param sourceNodes The source vertexes for the desired paths.
         * @param targetNode The target vertex for the desired paths.
         * @return A list of all paths leading from sourceVertex to
         * targetVertex.
         * @throws VertexNotFoundException If the graph does not contain the
         * given vertexes.
         * @throws ParameterException
         */
        public static <V extends Object> ArrayBlockingQueue<List<V>> getDirectedPathsFor(Traversable<V> traversableStructure, List<V> sourceNodes, V targetNode) throws VertexNotFoundException, ParameterException {
                Validate.notNull(traversableStructure);
                ArrayBlockingQueue<List<V>> finalPaths = new ArrayBlockingQueue<>(10);

                if (sourceNodes.contains(targetNode)) {
                        return finalPaths;
                }

                ArrayBlockingQueue<List<V>> tempPaths = new ArrayBlockingQueue<>(10);
                List<V> firstPath = new ArrayList<>();
                firstPath.add(targetNode);
                tempPaths.offer(firstPath);

                while (!tempPaths.isEmpty()) {
                        Collection<V> parents = traversableStructure.getParents(tempPaths.peek().get(tempPaths.peek().size() - 1));
                        for (V v : parents) {
                                List<V> newPath = new ArrayList<>(tempPaths.peek());
                                if (!tempPaths.peek().contains(v)) {
                                        newPath.add(v);
                                        if (sourceNodes.contains(v)) {
                                                finalPaths.add(newPath);
                                        } else {
                                                tempPaths.offer(newPath);
                                        }
                                }
                        }
                        tempPaths.poll();
                }
                for (List<V> l : finalPaths) {
                        Collections.reverse(l);
                }
                return finalPaths;
        }

        private static <V extends Object> void weakConnectivityRec(Traversable<V> graph, V v, Set<V> nodes) throws ParameterException, VertexNotFoundException {
                Set<V> neighbours = new HashSet<>();
                neighbours.addAll(graph.getChildren(v));
                neighbours.addAll(graph.getParents(v));
                for (V n : neighbours) {
                        if (false == nodes.contains(n)) {
                                nodes.add(n);
                                weakConnectivityRec(graph, n, nodes);
                        }
                }
        }
}
