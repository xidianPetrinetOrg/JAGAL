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

/**
 * 遍历Graph，深度优先/广度优先
 * @param <V> 顶点类型
 */
public class Traverser<V extends Object> implements Iterator<V>{
	/** Traversable接口对象，AbstractGraph实现了该接口，因此Graph可以是此处的变量*/
	private Traversable<V> structure = null;
	/** DEPTHFIRST,BREADTHFIRST */
	private TraversalMode mode = null;
	private final List<V> visited = new HashList<>();
	/** BREADTHFIRST，广度优先，暂存节点 */
	private final ArrayBlockingQueue<V> queue = new ArrayBlockingQueue<>(10);
	/** DEPTHFIRST，深度优先，暂存节点 */
	private final Stack<V> stack = new Stack<>();
	protected List<V> path = new ArrayList<>();
	private final Map<V, Integer> indexMap = new HashMap<>();
	private boolean lastNodeAddedChildren;
	
	/**
	 * 遍历Graph，深度优先/广度优先
	 * @param traversableStructure Traversable接口对象，AbstractGraph实现了该接口，因此Graph可以是此参数的实参
	 * @param startNode 开始节点（顶点）
	 * @param mode   DEPTHFIRST,BREADTHFIRST
	 * @param <V> 顶点类型
	 */
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
				lastNodeAddedChildren = children != null && !children.isEmpty();
			}catch(VertexNotFoundException | ParameterException e){
				throw new RuntimeException(e);
			}
			return visited.get(visited.size()-1);
		} 
		return null;
	}
	
	/** 深度优先，未访问的子节点压入stack暂存*/
	protected void childFoundDF(V child){
		if(!visited.contains(child))
			pushToStack(child);
	}
	
	/** push node to stack */
	protected void pushToStack(V node){
		stack.push(node);
	}
	
	/** 广度优先，未访问的子节点入队列 */
	protected void childFoundBF(V child){
		if(!visited.contains(child))
			queue.offer(child);
	}
	
	/** 存入路径 */
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
			} catch (VertexNotFoundException | ParameterException e) {
				throw new RuntimeException(e);
			}
			Set<V> pathElementsToRemove = new HashSet<>();
			if (path.size() > 1 && parentsOfLastAddedElement != null) {
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
		Graph<Integer> g = new Graph<>();
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
		
		Traverser<Vertex<Integer>> t = new Traverser<>(g, g.getVertex("1"), TraversalMode.DEPTHFIRST);
		while(t.hasNext()){
			System.out.println(t.next());  // 1,3,5,4,2
		}
	}
}
