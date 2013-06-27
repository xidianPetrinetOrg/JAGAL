package de.uni.freiburg.iig.telematik.jagal.graph.algorithm.coloring;


import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Graph;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;


public class ColoringUtils {
	
	public static <V extends Vertex<U>, E extends Edge<V>, U> Coloring<V> naiveColoring(AbstractGraph<V, E, U> graph) throws ParameterException{
		Validate.notNull(graph);
		Coloring<V> coloring = new Coloring<V>();
		Set<Integer> neighborColors = new HashSet<Integer>();
		for(V vertex: graph.getVertexes()){
			neighborColors.clear();
			try {
				for(V neighbor: graph.getNeighbors(vertex)){
					if(coloring.isColored(neighbor)){
						neighborColors.add(coloring.getColor(neighbor));
					}
				}
			} catch (GraphException e) {
				// Cannot hapen, since we only use graph vertexes.
				e.printStackTrace();
			} catch (ParameterException e){
				// Cannot happen, since graph vertexes are never null
			}
			Integer vertexColor = 1;
			while(neighborColors.contains(vertexColor)){
				vertexColor++;
			}
			coloring.setColor(vertex, vertexColor);
		}
		return coloring;
	}
	
	public static <V extends Vertex<U>, E extends Edge<V>, U> Coloring<U> getElementColoring(AbstractGraph<V, E, U> graph, Coloring<V> vertexColoring) throws ParameterException{
		Validate.notNull(graph);
		Coloring<U> elementColoring = new Coloring<U>();
		for(V vertex: graph.getVertexes()){
			elementColoring.setColor(vertex.getElement(), vertexColoring.getColor(vertex));
		}
		return elementColoring;
	}
	
	/**
	 * Determines the maximum clique of the given graph.<br>
	 * WARNING: Extreme bad runtime, use only for small graphs.
	 * @param graph
	 * @return
	 */
	public static <V extends Vertex<U>, E extends Edge<V>, U> Set<V> maxClique(AbstractGraph<V, E, U> graph){
		Set<V> vertexes = new HashSet<V>();
		Set<V> biggestCliqueSoFar = new HashSet<V>();
		for(V testVertex: graph.getVertexes()){
			// Build the biggest clique containing the current vertex.
			vertexes.addAll(graph.getVertexes());
			vertexes.remove(testVertex);
			
			Set<V> maxClique = new HashSet<V>();
			for(V vertex: vertexes){
				boolean isCliqueVertex = true;
				// Check if the given vertex is connected to all vertexes in the clique
				for(V cliqueVertex: maxClique){
					try {
						if(!graph.getChildren(cliqueVertex).contains(vertex) && !graph.getParents(cliqueVertex).contains(vertex)){
							// Vertex cannot belong to the clique
							isCliqueVertex = false;
							break;
						}
					} catch (GraphException e) {
						// Cannot happen, since we only use graph vertexes
						e.printStackTrace();
					}
				}
				if(isCliqueVertex){
					maxClique.add(vertex);
				}
			}
			if(maxClique.size() > biggestCliqueSoFar.size()){
				biggestCliqueSoFar = maxClique;
			}
		}
		return biggestCliqueSoFar;
	}
	
	public static void main(String[] args) throws Exception {
		Graph<String> g = new Graph<String>();
		g.addElement("1");
		g.addElement("2");
		g.addElement("3");
		g.addElement("4");
		g.addElement("5");
		g.addElement("6");
		g.addEdge("1", "2");
		g.addEdge("1", "5");
		g.addEdge("2", "5");
		g.addEdge("2", "3");
		g.addEdge("5", "4");
		g.addEdge("3", "4");
		g.addEdge("4", "6");
		System.out.println(maxClique(g));
	}

}
