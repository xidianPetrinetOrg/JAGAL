package graph;

import graph.abstr.AbstractGraph;
import graph.exception.EdgeNotFoundException;
import graph.exception.GraphException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;


public class GraphUtils {
	
	private static ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(10);
	private static Set<Object> visited = new HashSet<Object>();
	
	/**
	 * Checks, if there is a closed cycle in the graph with Node<T> {@link baseNode<T>},
	 * using {@link inEdge} and {@link outEdge}.
	 * 
     *@param baseNode<T> The base Node<T> for cycle check
     * @param outEdge Incoming edge of the base Node<T> in the potential cycle
     * @param inEdge Outgoing edge of the base Node<T> in the potential cycle
     * @return <code>true</code> if there is a closed cycle with {@link inEdge} and {@link outEdge};
     *		<code>false</code> otherwise.
     */
	@SuppressWarnings("unchecked")
	public static <V extends Vertex<U>, E extends Edge<V>, U> boolean cycleBy(AbstractGraph<V, E, U> graph, V baseVertex, E inEdge, E outEdge) throws GraphException {
		if(!graph.containsEdge(inEdge))
			throw new EdgeNotFoundException(inEdge, graph);
		if(!graph.containsEdge(outEdge))
			throw new EdgeNotFoundException(outEdge, graph);
		
		visited.clear();
		queue.clear();
		queue.offer(outEdge.getTarget());
		while(!queue.isEmpty()){
			if(queue.peek() == inEdge.getSource()){
				return true;
			}
			visited.add(queue.peek());
			//Cast is safe since only objects of type V were added to the queue before.
			for(V v: graph.getChildren((V) queue.poll())){
				if(!queue.contains(v) && !visited.contains(v)){
					queue.offer(v);
				}
			}
		}
		return false;
	}

}
