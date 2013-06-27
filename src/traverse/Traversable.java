package traverse;

import graph.exception.VertexNotFoundException;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;


public interface Traversable<G extends Object> {
	
	public Set<G> getParents(G node) throws VertexNotFoundException, ParameterException;
	
	public Set<G> getChildren(G node) throws VertexNotFoundException, ParameterException;
	
	public int nodeCount();
	
	public Set<G> getNodes();

}
