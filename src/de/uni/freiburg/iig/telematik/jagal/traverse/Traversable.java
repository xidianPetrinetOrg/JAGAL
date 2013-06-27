package de.uni.freiburg.iig.telematik.jagal.traverse;


import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;


public interface Traversable<G extends Object> {
	
	public Set<G> getParents(G node) throws VertexNotFoundException, ParameterException;
	
	public Set<G> getChildren(G node) throws VertexNotFoundException, ParameterException;
	
	public int nodeCount();
	
	public Set<G> getNodes();

}
