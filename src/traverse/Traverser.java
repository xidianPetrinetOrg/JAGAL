package traverse;

import graph.Graph;
import graph.Vertex;
import graph.exception.VertexNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

import types.HashList;
import validate.ParameterException;

public class Traverser<V extends Object> implements Iterator<V>{
	
	private Traversable<V> structure = null;
	private TraversalMode mode = null;
	private List<V> visited = new HashList<V>();
	private ArrayBlockingQueue<V> queue = new ArrayBlockingQueue<V>(10);
	private Stack<V> stack = new Stack<V>();
	private List<V> path = new ArrayList<V>();
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
			Set<V> children = null;
			try {
				switch(mode){
					case DEPTHFIRST:
						visited.add(stack.peek());
						indexMap.put(stack.peek(), visited.size()+1);
						path.add(stack.peek());
						children = structure.getChildren(stack.pop());
						for(V child: children){
							if(!visited.contains(child))
								stack.push(child);
						}
						break;
					case BREADTHFIRST:
						visited.add(queue.peek());
						indexMap.put(queue.peek(), visited.size()+1);
						path.add(queue.peek());
						children = structure.getChildren(queue.poll());
						for(V child: children){
							if(!visited.contains(child))
								queue.offer(child);
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
	
	public V lastVisited(){
		return visited.get(visited.size()-1);
	}
	
	private void checkPath(){
		if(!lastNodeAddedChildren && !visited.isEmpty()){
			Set<V> parentsOfLastAddedElement = null;
			try {
				parentsOfLastAddedElement = structure.getParents(lastVisited());
			} catch (VertexNotFoundException e) {
				e.printStackTrace();
			} catch (ParameterException e) {
				e.printStackTrace();
			}
			Set<V> pathelementsToRemove = new HashSet<V>();
			if(path.size() > 1){
			for(int i=path.size()-2; i>=0; i--){
				if(!parentsOfLastAddedElement.contains(path.get(i))){
					pathelementsToRemove.add(path.get(i));
				} else {
					break;
				}
			}
			}
			path.removeAll(pathelementsToRemove);
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public enum TraversalMode {DEPTHFIRST, BREADTHFIRST}
	
	public static void main(String[] args) throws Exception {
		Graph<Integer> g = new Graph<Integer>();
		g.addElement(1);
		g.addElement(2);
		g.addElement(3);
		g.addElement(4);
		g.addElement(5);
		g.addEdge(1, 2);
		g.addEdge(1, 3);
		g.addEdge(2, 4);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		
		Traverser<Vertex<Integer>> t = new Traverser<Vertex<Integer>>(g, g.getVertex(1), TraversalMode.DEPTHFIRST);
		while(t.hasNext()){
			System.out.println(t.next());
		}
	}

}
