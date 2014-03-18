package de.uni.freiburg.iig.telematik.jagal.graph.abstr;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;

/**
 * This class maintains incoming and outgoing edges and is used in graph implementations.<br>
 * 
 * @author Thomas Stocker
 *
 * @param <E> Edge Type
 */
public class EdgeContainer<E extends Edge<?>>{
	/**
	 * Incoming edges.
	 */
	private ArrayList<E> incoming = new ArrayList<E>();
	/**
	 * Outgoing edges.
	 */
	private ArrayList<E> outgoing = new ArrayList<E>();

	/**
	 * Returns an unmodifiable version of the incoming edges.
	 * @return All incoming edges.
	 */
	public List<E> getIncomingEdges(){
		return Collections.unmodifiableList(incoming);
	}

	/**
	 * Returns an unmodifiable version of the outgoing edges.
	 * @return All outgoing edges.
	 */
	public List<E> getOutgoingEdges(){
		return new ArrayList<E>(outgoing);
	}

	/**
	 * Adds an incoming edge.
	 * @param e Incoming edge.
	 */
	public void addIncomingEdge(E e){
		incoming.add(e);
	}

	/**
	 * Adds an outgoing edge.
	 * @param e Outgoing edge.
	 */
	public void addOutgoingEdge(E e){
		outgoing.add(e);
	}

	/**
	 * Removes an incoming edge.
	 * @param e Incoming edge to be removed.
	 */
	public void removeIncomingEdge(E e){
		incoming.remove(e);
	}

	/**
	 * Removes an outgoing edge.
	 * @param e Outgoing edge to be removed.
	 */
	public void removeOutgoingEdge(E e){
		outgoing.remove(e);
	}
	
	/**
	 * checks if there are incoming edges.
	 * @return <code>true</code> if there are incoming edges;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean hasIncomingEdges(){
		return !incoming.isEmpty();
	}
	
	/**
	 * checks if there are outgoing edges.
	 * @return <code>true</code> if there are outgoing edges;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean hasOutgoingEdges(){
		return !outgoing.isEmpty();
	}
	
	/**
	 * Checks if there are edges at all.
	 * @return <code>true</code> if there are edges;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isEmpty(){
		return !hasIncomingEdges() && !hasOutgoingEdges();
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Incoming:");
		builder.append('\n');
		for(E incomingEdge: incoming){
			builder.append(incomingEdge);
			builder.append('\n');
		}
		builder.append("Outgoing:");
		builder.append('\n');
		for(E outgoingEdge: outgoing){
			builder.append(outgoingEdge);
			builder.append('\n');
		}
		return builder.toString();
	}
}
