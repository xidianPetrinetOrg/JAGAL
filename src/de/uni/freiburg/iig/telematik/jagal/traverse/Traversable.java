package de.uni.freiburg.iig.telematik.jagal.traverse;


import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;


public interface Traversable<G extends Object> {
	
	public Collection<G> getParents(G node) throws VertexNotFoundException, ParameterException;
	
	public Collection<G> getChildren(G node) throws VertexNotFoundException, ParameterException;

	public int nodeCount();
	
	public Collection<G> getNodes();

}
