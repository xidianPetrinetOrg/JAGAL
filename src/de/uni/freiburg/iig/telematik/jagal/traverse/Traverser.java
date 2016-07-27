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
 * 遍历有向图Graph，深度优先/广度优先<br>
 * 实现了Iterator<V>, 调用hasNetxt(),next()完成遍历图<br>
 * 构造函数传入Traversable接口对象，AbstractGraph实现了该接口，因此Graph可以是构造函数参数的实参<br>
 * <pre>
 * DEPTHFIRST深度优先遍历算法：
 * stack.push(startNode);
 * do {
 *   node = stack.pop();
 *   visited.add(node);
 *   for(child: children of node)  { if(visited不含child,即子节点未访问，才压入栈) stack.puch(child); }
 *   【option,记录节点访问顺序】
 *   indexMap.push(node,visited.size()); // 源码中，map的值（访问顺序）从2开始，2,3,4,...,改为不加1，从1开始更自然
 *   【option, path.push(node);  checkPath();path访问路径，检查，显示】
 * } while(node != null)
 * 
 * BREADTHFIRST，广度优先遍历，把上述的stack替换为queue即可
 * </pre>
 * @param <V> 顶点类型
 */
public class Traverser<V extends Object> implements Iterator<V>{
	/** Traversable接口对象，AbstractGraph实现了该接口，因此构造函数的参数traversableStructure，可以设Graph为实参*/
	private Traversable<V> structure = null;
	/** DEPTHFIRST,BREADTHFIRST */
	private TraversalMode mode = null;
	/** 记录顶点访问序列 */
	private final List<V> visited = new HashList<>();
	/** BREADTHFIRST，广度优先，暂存节点 */
	private final ArrayBlockingQueue<V> queue = new ArrayBlockingQueue<>(10);
	/** DEPTHFIRST，深度优先，暂存节点 */
	private final Stack<V> stack = new Stack<>();
	/** 访问路径  */
	protected List<V> path = new ArrayList<>();
	/** 顶点访问顺序，key: Vertex, Value：顶点访问序号 */
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
					case DEPTHFIRST:  // 访问栈顶元素，其子节点压入堆栈
						visited.add(stack.peek());
						indexMap.put(stack.peek(), visited.size()); // 源码中，访问顺序从2开始，2,3,4,...,改为不加1，从1开始更自然
						addToPath(stack.peek());
						children = structure.getChildren(stack.pop());
						for(V child: children){
							childFoundDF(child);
						}
						break;
					case BREADTHFIRST: // 访问队首元素，其子接点入队尾
						visited.add(queue.peek());
						indexMap.put(queue.peek(), visited.size()); // 源码中，访问顺序从2开始，2,3,4,...,改为不加1，从1开始更自然
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
	
	/**
	 * 访问路径，检查，显示
	 */
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
	
	public void debug() {
		System.out.println("visited: " + visited);
		System.out.println("idexMap: " + indexMap);
		System.out.println("stack: " + stack);
		System.out.println("path: " + stack);
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
			t.debug();
			System.out.println(t.next());  // 1,3,5,4,2
		}
		
		t.debug();
	}
}
