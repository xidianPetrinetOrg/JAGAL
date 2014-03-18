package de.uni.freiburg.iig.telematik.jagal.traverse;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

import de.invation.code.toval.types.HashList;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;


public class Traverser<V extends Object> implements Iterator<V>{
	
	private Traversable<V> structure = null;
	private TraversalMode mode = null;
	private List<V> visited = new HashList<V>();
	private ArrayBlockingQueue<V> queue = new ArrayBlockingQueue<V>(10);
	private Stack<V> stack = new Stack<V>();
	protected List<V> path = new ArrayList<V>();
	private Map<V, Integer> indexMap = new HashMap<V, Integer>();
	private boolean lastNodeAddedChildren;
	
	public Traverser(Traversable<V> traversableStructure, V startNode, TraversalMode mode){
		structure = traversableStructure;
		this.mode = mode;
		setFirstNode(startNode);
	}
	
	private void setFirstNode(V startNode){
		switch(mode){
			case DEPTHFIRST: stack.push(startNode);
			break;
			case BREADTHFIRST: queue.offer(startNode);
			break;
		}
	}
	
	public Map<V, Integer> getIndexMap(){
		return Collections.unmodifiableMap(indexMap);
	}
	
	public void iterate(){
		while(hasNext()){
			next();
		}
	}

	protected Traversable<V> getStructure() {
		return structure;
	}

	@Override
	public boolean hasNext() {
		switch(mode){
			case DEPTHFIRST: return !stack.isEmpty();
			case BREADTHFIRST: return !queue.isEmpty();
		}
		return false;
	}
	
	public List<V> getPath(){
		return Collections.unmodifiableList(path);
	}
	
	public List<V> getVisitedNodes(){
		return visited;
	}

	@Override
	public V next() {
		if(hasNext()){
			Collection<V> children = null;
			try {
				switch(mode){
					case DEPTHFIRST:
						visited.add(stack.peek());
						indexMap.put(stack.peek(), visited.size()+1);
						addToPath(stack.peek());
						children = structure.getChildren(stack.pop());
						for(V child: children){
							childFoundDF(child);
						}
						break;
					case BREADTHFIRST:
						visited.add(queue.peek());
						indexMap.put(queue.peek(), visited.size()+1);
						addToPath(queue.peek());
						children = structure.getChildren(queue.poll());
						for(V child: children){
							childFoundBF(child);
						}
						break;
					default: return null;
				}
				checkPath();
				lastNodeAddedChildren = !children.isEmpty();
			}catch(VertexNotFoundException e){
				e.printStackTrace();
			} catch (ParameterException e) {
				e.printStackTrace();
			}
			return visited.get(visited.size()-1);
		} 
		return null;
	}
	
	protected void childFoundDF(V child){
		if(!visited.contains(child))
			pushToStack(child);
	}
	
	protected void pushToStack(V node){
		stack.push(node);
	}
	
	protected void childFoundBF(V child){
		if(!visited.contains(child))
			queue.offer(child);
	}
	
	protected void addToPath(V vertex){
		path.add(vertex);
	}
	
	public V lastVisited(){
		return visited.get(visited.size()-1);
	}
	
	protected void checkPath(){
//		System.out.print(path + " -> ");
		if(!lastNodeAddedChildren && !visited.isEmpty()){
			Collection<V> parentsOfLastAddedElement = null;
			try {
				parentsOfLastAddedElement = structure.getParents(lastVisited());
			} catch (VertexNotFoundException e) {
				e.printStackTrace();
			} catch (ParameterException e) {
				e.printStackTrace();
			}
			Set<V> pathElementsToRemove = new HashSet<V>();
			if (path.size() > 1) {
				for (int i = path.size() - 2; i >= 0; i--) {
					if (!parentsOfLastAddedElement.contains(path.get(i))) {
						pathElementsToRemove.add(path.get(i));
					} else {
						break;
					}
				}
			}
			path.removeAll(pathElementsToRemove);
		}
//		System.out.println(path);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public enum TraversalMode {DEPTHFIRST, BREADTHFIRST}
	
	public static void main(String[] args) throws Exception {
		Graph<Integer> g = new Graph<Integer>();
		g.addVertex("1");
		g.addVertex("2");
		g.addVertex("3");
		g.addVertex("4");
		g.addVertex("5");
		g.addEdge("1", "2");
		g.addEdge("1", "3");
		g.addEdge("2", "4");
		g.addEdge("3", "4");
		g.addEdge("3", "5");
		
		Traverser<Vertex<Integer>> t = new Traverser<Vertex<Integer>>(g, g.getVertex("1"), TraversalMode.DEPTHFIRST);
		while(t.hasNext()){
			System.out.println(t.next());
		}
	}

}
