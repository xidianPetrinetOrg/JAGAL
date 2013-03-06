package graph.algorithm.coloring;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import graph.abstr.AbstractGraph;
import graph.exception.GraphException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import validate.ParameterException;
import validate.Validate;

/**
 * Algorithm for calculating a graph coloring.<br>
 * For details see diploma thesis of Tobias Baumann.
 * 
 * @author ts552
 *
 */
public class ExactGreedyRecursive implements GraphColoring {

	@Override
	public <V extends Vertex<U>, E extends Edge<V>, U> Coloring<V> determineColoring(AbstractGraph<V, E, U> graph) throws ParameterException {
		Validate.notNull(graph);
		Coloring<V> initialColoring = new Coloring<V>();
		Set<V> maxClique = ColoringUtils.maxClique(graph);
		int color = 1;
		for(V vertex: maxClique)
			initialColoring.setColor(vertex, color++);
		RecursiveResult<V> recursiveResult = determineColoringRec(graph, 
											 initialColoring, 
											 initialColoring.chromaticNumber(), 
											 graph.getVertexes().size() + 1, 
											 initialColoring.chromaticNumber(),
											 new ArrayList<Coloring<V>>());
		return recursiveResult.storedColorings.get(0);
	}
	
	private <V extends Vertex<U>, E extends Edge<V>, U> RecursiveResult<V> determineColoringRec(AbstractGraph<V, E, U> graph,
																				 				Coloring<V> coloring,
																				 				int k,
																				 				int best,
																				 				int bound,
																				 				List<Coloring<V>> storedColorings) throws ParameterException{

		Validate.notNull(graph);
		Validate.notNull(coloring);
		Validate.notNull(storedColorings);
		if(coloring.isComplete(graph.getVertexes())){
			// TODO: Store coloring
			storedColorings.add(coloring);
			return new RecursiveResult<V>(coloring.chromaticNumber(), storedColorings);
		}
		
		V uncoloredVertex = coloring.getUncoloredKeys(graph.getVertexes()).iterator().next();
		for(int c=1; c <= Math.min(k + 1, best - 1); c++){
			if(isColorUsedByNeighbors(graph, uncoloredVertex, coloring, c)){
				continue;
			} else {
				coloring.setColor(uncoloredVertex, c);
				RecursiveResult<V> recursiveResult = determineColoringRec(graph, coloring.clone(), Math.max(c, k), best, bound, storedColorings);
				coloring.uncolor(uncoloredVertex);
				if(bound == recursiveResult.best){
					return recursiveResult;
				}
			}
		}
		return new RecursiveResult<V>(best, storedColorings);
	}
	
	private <V extends Vertex<U>, E extends Edge<V>, U> boolean isColorUsedByNeighbors(AbstractGraph<V, E, U> graph, 
																					   V uncoloredVertex, 
																					   Coloring<V> coloring,
																					   int color) throws ParameterException{
		Validate.notNull(graph);
		Validate.notNull(uncoloredVertex);
		Validate.notNull(coloring);
		Validate.notNegative(color);
		boolean colorAlreadyUsed = false;
		try {
			for(V neighbor: graph.getNeighbors(uncoloredVertex)){
				if(coloring.isColored(neighbor) && coloring.getColor(neighbor) == color){
					colorAlreadyUsed = true;
					break;
				}
			}
		} catch (GraphException e) {
			// Cannot happen, since we only use graph vertexes
			e.printStackTrace();
		}
		return colorAlreadyUsed;
	}
	
	private class RecursiveResult<V>{
		public int best;
		public List<Coloring<V>> storedColorings;
		
		public RecursiveResult(int best, List<Coloring<V>> storedColorings){
			this.best = best;
			this.storedColorings = storedColorings;
		}
	}
	
	public static void main(String[] args) throws Exception{
		Graph<String> g = new Graph<String>();
		g.addElement("1");
		g.addElement("2");
		g.addElement("3");
		g.addElement("4");
		g.addEdge("1", "2");
		g.addEdge("2", "3");
		g.addEdge("3", "4");
		g.addEdge("4", "1");
		ExactGreedyRecursive coloring = new ExactGreedyRecursive();
		coloring.determineColoring(g);
	}

}
