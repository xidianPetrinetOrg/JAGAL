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

/**
 * 如果两个顶点可以相互通达，则称两个顶点强连通(strongly connected)。如果有向图G的每两个顶点都强连通，称G是一个强连通图。
 * 有向图的极大强连通子图，称为强连通分量(strongly connected components)。
 * Tarjan算法是用来求有向图的强连通分量的。求有向图的强连通分量的Tarjan算法是以其发明者Robert Tarjan命名的。
 * Robert Tarjan还发明了求双连通分量的Tarjan算法。
 * Tarjan算法是基于对图深度优先搜索的算法，每个强连通分量为搜索树中的一棵子树。
 * 搜索时，把当前搜索树中未处理的节点加入一个堆栈，回溯时可以判断栈顶到栈中的节点是否为一个强连通分量。
 * 定义DFN(u)为节点u搜索的次序编号(时间戳)，Low(u)为u或u的子树能够追溯到的最早的栈中节点的次序号。
 * 当DFN(u)=Low(u)时，以u为根的搜索子树上所有节点是一个强连通分量。
 * 
 * 伪代码：
   tarjan(u) {
     DFN[u]=Low[u]=++Index // 为节点u设定次序编号和Low初值
     Stack.push(u) //将节点u压入栈中
     for each(u,v) in E //枚举每一条边，u-->v
       if (v is not visted) // 如果节点v未被访问过
          tarjan(v) // 继续向下找
          Low[u]=min(Low[u],Low[v]) 
       else if (v in S) // 如果节点v还在栈内
          Low[u]=min(Low[u],DFN[v])
     if (DFN[u]==Low[u]) // 如果节点u是强连通分量的根
     repeat {
        v=S.pop // 将v退栈，为该强连通分量中一个顶点
        print v
        until(u==v)
     }
   }
   
         可以发现，运行Tarjan算法的过程中，每个顶点都被访问了一次，且只进出了一次堆栈，每条边也只被访问了一次，
         所以该算法的时间复杂度为O(V+E)。
 * @author Administrator
 *
 * @param <V>
 */
public class SCCTarjan<V extends Object> {
        /** 记录访问过的节点，用于回溯 */
        private final Stack<V> stack = new Stack<>();
        /** DFN(u) 为节点u搜索的次序编号(时间戳) */
        private final Map<V, Integer> indexMap = new HashMap<>(); 
        /** Low(u) 为u或u的子树能够追溯到的最早的栈中节点的次序号。*/
        private final Map<V, Integer> lowlinkMap = new HashMap<>();
        
        /** 节点的访问编号，时间戳*/
        private int index = 0;
        /** 强连通分量 */
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
                                lowlinkMap.put(v,Math.min(lowlinkMap.get(v), lowlinkMap.get(n)));
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
