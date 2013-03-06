package graph.abstr;

import graph.Edge;
import graph.Vertex;
import graph.exception.EdgeNotFoundException;
import graph.exception.GraphException;
import graph.exception.VertexNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import traverse.Traversable;
import types.HashList;

/**
 * Directed Graph!
 * Multiple edges are not allowed
 * Self-loops are not allowed
 * 
 * @author Thomas Stocker
 *
 * @param <V> Vertex Type
 * @param <E> Edge Type
 */
public abstract class AbstractGraph<V extends Vertex<U>, E extends Edge<V>, U> implements Traversable<V>{
	
	protected final String toStringFormat = "%s: V=%s \n E=%s \n";
	
	protected String name = getDefaultName();
	protected List<E> edgeSet = new HashList<E>();
	protected Map<V, EdgeContainer<E>> vertexMap = new LinkedHashMap<V, EdgeContainer<E>>();
	
	//------- Constructors ---------------------------------------------------------------
	
	public AbstractGraph() {}
	
	public AbstractGraph(String name){
		if(name == null)
			throw new NullPointerException();
		this.name = name;
	}
	
	public AbstractGraph(Collection<V> vertexes){
		if(vertexes == null)
			throw new NullPointerException();
		if(!vertexes.isEmpty())
			addAllVertexes(vertexes);
	}
	
	public AbstractGraph(String name, Collection<V> vertexes){
		if((name == null) || (vertexes == null))
			throw new NullPointerException();
		this.name = name;
		if(!vertexes.isEmpty())
			addAllVertexes(vertexes);
	}
	
	//------- Graph properties --------------------------------------------------------------
	
	/**
	 * Returns the name of the graph.
	 * @return The graph's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the default graph name.<br>
	 * Instead of using a constant field the default name is given by a method
	 * to give subclasses overriding possibilities.
	 * @return The default name for graphs.
	 */
	protected String getDefaultName(){
		return "Graph";
	}
	
	/**
	 * Sets the name of the graph.
	 * @param name The desired graph name.
	 */
	public void setName(String name){
		if(name!=null){
			this.name = name;
		}
	}
	
	/**
	 * Checks if the graph is empty (i.e. contains no vertexes).
	 * @return <code>true</code> if the graph is empty;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isEmpty(){
		return vertexMap.keySet().isEmpty();
	}
	
	/**
	 * Checks if the graph is trivial (i.e. contains only one vertex).
	 * @return <code>true</code> if the graph is trivial;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isTrivial(){
		return vertexMap.keySet().size()<=1;
	}
	
//	/**
//	 * Checks if the graph is connected.<br>
//	 * A graph is called connected if there is a set of undirected edges connecting them.<br>
//	 * Connected graphs especially contain no separated vertexes.
//	 * @return <code>true</code> if the graph is connected;<br>
//	 * <code>false</code> otherwise.
//	 * @see #getSeparatedVertexes()
//	 */
//	public boolean isConnected(){
//		return getSeparatedVertexes().isEmpty();
//	}
	
	/**
	 * Returns all source vertexes of the graph.<br>
	 * Source vertexes have no incoming, but at least one outgoing edge.
	 * @return A list of all source vertexes.
	 */
	public List<V> getSources(){
		List<V> sources = new ArrayList<V>();
		for(V vertex: vertexMap.keySet()){
			if(!vertexMap.get(vertex).hasIncomingEdges() && vertexMap.get(vertex).hasOutgoingEdges())
				sources.add(vertex);
		}
		return sources;
	}
	
	/**
	 * Returns all drain vertexes of the graph.<br>
	 * Drain vertexes have no outgoing, but at least one incoming edge.
	 * @return A list of all drain vertexes.
	 */
	public List<V> getDrains(){
		List<V> drains = new ArrayList<V>();
		for(V vertex: vertexMap.keySet()){
			if(!vertexMap.get(vertex).hasOutgoingEdges() && vertexMap.get(vertex).hasIncomingEdges())
				drains.add(vertex);
		}
		return drains;
	}
	
	/**
	 * Returns all separated vertexes of the graph.<br>
	 * Separated vertexes have no edges at all.
	 * @return A list of all separated vertexes.
	 */
	public Set<V> getSeparatedVertexes(){
		Set<V> separatedVertexes = new HashSet<V>();
		for(V vertex: vertexMap.keySet()){
			if(vertexMap.get(vertex).isEmpty())
				separatedVertexes.add(vertex);
		}
		return separatedVertexes;
	}
	
	public boolean hasSeparatedVertexes(){
		for(V vertex: vertexMap.keySet()){
			if(vertexMap.get(vertex).isEmpty())
				return true;
		}
		return false;
	}
	
	
	//------- VERTEX manipulation methods --------------------------------------------------------------
	
	
	/**
	 * Returns all vertexes of this graph.
	 * @return A set containing all vertexes of this graph.
	 */
	public Set<V> getVertexes(){
		return Collections.unmodifiableSet(vertexMap.keySet());
	}

	/**
	 * Adds a vertex to the graph if it is not already contain an equal vertex.
	 * @param vertex Vertex to add
	 * @return <code>true</code> if the vertex could be inserted;
     *		<code>false</code> otherwise.	
	 */
	public boolean addVertex(V vertex){
		if(!contains(vertex)){
			vertexMap.put(vertex, new EdgeContainer<E>());
			return true;
		}
		return false;
	}
	
	/**
	 * Adds all specified vertexes by repeatedly calling the method {@link AbstractGraph#addVertex(V)}.
	 * @param vertexes Collection of vertexes to add 
	 * @return <code>true</code> if all vertexes could be inserted;
     *		<code>false</code> otherwise.
	 * @see AbstractGraph#addVertex(V)
	 */
	public boolean addAllVertexes(Collection<V> vertexes){
		boolean check = true;
		for(V vertex: vertexes){
			check &= addVertex(vertex);
		}
		return check;
	}
	
	/**
	 * Adds a new vertex containing the given element to the graph if it is not already contain an equal vertex.
	 * @param element Element for which a vertex should be added.
	 * @return <code>true</code> if a new vertex could be inserted;
     *		<code>false</code> otherwise.	
	 */
	public boolean addElement(U element){
		V newVertex = createNewVertex(element);
		return addVertex(newVertex);
	}
	
	/**
	 * Adds all specified elements by repeatedly calling the method {@link AbstractGraph#addElement(U)}.
	 * @param elements Collection of elements to add 
	 * @return <code>true</code> if all elements could be inserted;
     *		<code>false</code> otherwise.
	 * @see AbstractGraph#addElement(U)
	 */
	public boolean addAllElements(Collection<U> elements){
		boolean check = true;
		for(U element: elements){
			check &= addElement(element);
		}
		return check;
	}
	
	/**
	 * Checks, if the graph contains a vertex equal to the given vertex.
	 * -> This is not a pure reference equality (see {@link Vertex#equals(Object)}).
	 * @param Vertex Vertex to check
	 * @return <code>true</code> if the specified vertex is present;
     *		<code>false</code> otherwise.
	 */
	public boolean contains(V vertex){
		try{
			getEqualVertex(vertex);
			return true;
		}catch(VertexNotFoundException e){
			return false;
		}
	}
	
	/**
	 * Checks, if the graph contains all given vertexes.
	 * It uses the method {@link AbstractGraph#contains(Vertex)}
	 * @param Vertex Vertex to check
	 * @return <code>true</code> if all specified vertexes are present;
     *		<code>false</code> otherwise.
     * @see AbstractGraph#contains(Vertex)
	 */
	public boolean containsAll(Collection<V> vertexes){
		for(V v: vertexes){
			if(!contains(v))
				return false;
		}
		return true;
	}
	
	/**
	 * Returns the reference of a contained node, that equals to the given node.
	 * @param vertex
	 * @return
	 */
	public V getEqualVertex(V vertex) throws VertexNotFoundException{
		for(V v: vertexMap.keySet()){
			if(v.equals(vertex)){
				return v;
			}
		}
		throw new VertexNotFoundException(vertex, this);
	}
	
	public E getEqualEdge(E edge) {
		for(E graphEdge: edgeSet){
			if(graphEdge.equals(edge)){
				return graphEdge;
			}
		}
		return null;
	}
	
	protected abstract V createNewVertex(U element);
	
	protected abstract E createNewEdge(V sourceVertex, V targetVertex);
	
	/**
	 * Returns the vertex, that holds the given element.
	 * @param element
	 * @return The vertex containing the given element; <br>
	 * <code>null</code> otherwise.
	 */
	public V getVertex(Object element){
		for(V v: vertexMap.keySet()){
			if(v.getElement() == element){
				return v;
			}
		}
		return null;
	}
	
	/**
	 * Checks, if the graph contains a vertex, that holds the given element.
	 * @param element query element
	 * @return <code>true</code> if a vertex was found;
     *		<code>false</code> otherwise.
	 */
	public boolean containsObject(Object element){
		return getVertex(element) != null;
	}
	
	/**
	 * Checks for every element within the given set, 
	 * if the graph contains a vertex, that holds the element.
	 * It returns true, if this is true for every element.
	 * @param elements set of query elements
	 * @return <code>true</code> if a vertex was found for every element;
     *		<code>false</code> otherwise.
	 */
	public boolean containsAllObjects(Collection<? extends Object> elements){
		for(Object o: elements)
			if(!containsObject(o))
				return false;
		return true;
	}
	
	/**
	 * Returns all Vertexes contained in the graph.
	 * @return All Vertexes within the graph
	 */
	public Set<V> vertexes(){
		return Collections.unmodifiableSet(vertexMap.keySet());
	}
	
	/**
	 * Removes the given Vertex from the graph.
	 * Before removing the Vertex itself, all its edges are removed from the graph.
	 * @param Vertex Vertex to remove
	 * @return <code>true</code> if the removal was successful;
     *		<code>false</code> otherwise.
	 * @see AbstractGraph#removeEdge(E)
	 */
	public boolean removeVertex(V vertex) throws GraphException{
        if(!contains(vertex))
        	throw new VertexNotFoundException(vertex, this);
		removeAllEdges(new ArrayList<E>(getEdgesFor(vertex)));
        vertexMap.keySet().remove(vertex);
        return true;
	}
	
	//------- VERTEX property methods -------------------------------------------------------------
	
	/**
	 * Returns the in-degree for the given vertex (i.e. the number of incoming edges).
	 * @param vertex The vertex for which the in-degree is desired.
	 * @return The number of incoming edges of the given vertex.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public int inDegreeOf(V vertex) throws VertexNotFoundException{
		return getEdgeContainer(vertex).getIncomingEdges().size();
	}
	
	/**
	 * Returns the out-degree for the given vertex (i.e. the number of outgoing edges).
	 * @param vertex The vertex for which the out-degree is desired.
	 * @return The number of outgoing edges of the given vertex.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public int outDegreeOf(V vertex) throws VertexNotFoundException{
		return getEdgeContainer(vertex).getOutgoingEdges().size();
	}
	
	/**
	 * Checks if the given vertex is a source.<br>
	 * A vertex is considered to be a source if it does not have incoming edges,<br>
	 * but at least one outgoing edge.
	 * @param vertex The vertex for which the property is checked.
	 * @return <code>true</code> if the given vertex is a source;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean isSource(V vertex) throws VertexNotFoundException{
		return !getEdgeContainer(vertex).hasIncomingEdges() && getEdgeContainer(vertex).hasOutgoingEdges();
	}
	
	/**
	 * Checks if the given vertex is a drain.<br>
	 * A vertex is considered to be a drain if it does not have outcoming edges,<br>
	 * but at least one incoming edge.
	 * @param vertex The vertex for which the property is checked.
	 * @return <code>true</code> if the given vertex is a drain;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean isDrain(V vertex) throws VertexNotFoundException{
		return !getEdgeContainer(vertex).hasOutgoingEdges() && getEdgeContainer(vertex).hasIncomingEdges();
	}
	
	/**
	 * Checks if the given vertex is separated.<br>
	 * A vertex is considered to be separated if it has no edges at all.
	 * @param vertex The vertex for which the property is checked.
	 * @return <code>true</code> if the given vertex is separated;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean isSeparated(V vertex) throws VertexNotFoundException{
		return getEdgeContainer(vertex).isEmpty();
	}
	
	//------- EDGE manipulation methods ----------------------------------------------------------
	
    public List<E> getEdges(){
        return Collections.unmodifiableList(edgeSet);     
    }
    
	/**
	 * Returns the corresponding Edge between the given source and target vertexes.
	 * @param sourceVertex The source vertex of the desired edge.
	 * @param targetVertex The target vertex of the desired edge.
	 * @return The corresponding edge between the given source and target vertexes.
     * @throws VertexNotFoundException If the given vertexes are not found.
     * @throws EdgeNotFoundException If there exists no edge between the given vertexes.
     */
	public E getEdge(V sourceVertex, V targetVertex) throws GraphException{
		if(!contains(sourceVertex))
			throw new VertexNotFoundException(sourceVertex, this);
		if(!contains(targetVertex))
			throw new VertexNotFoundException(targetVertex, this);
		
        for(Iterator<E> iter=vertexMap.get(sourceVertex).getOutgoingEdges().iterator(); iter.hasNext();){
            E e = iter.next();
            if (e.getTarget().equals(targetVertex)) {
                return e;
            }
        }
        throw new EdgeNotFoundException(sourceVertex, targetVertex, this);
    }
	
	/**
	 * Checks, if the graph contains the given edge.
	 * @param edge Edge to check
	 * @return <code>true</code> if the graph contains the given edge;
     *		<code>false</code> otherwise.
	 */
	public boolean containsEdge(E edge){
		return edgeSet.contains(edge);
	}
	
	/**
	 * Checks, if the graph contains an edge between the given vertexes.
	 * @param sourceVertex Source vertex of the edge.
	 * @param targetVertex Target vertex of the edge.
	 * @return <code>true</code> if the graph contains the edge;
     *		<code>false</code> otherwise.
	 */
	public boolean containsEdge(V sourceVertex, V targetVertex){
		try{
			getEdge(sourceVertex, targetVertex);
			return true;
		} catch (GraphException e) {
			return false;
		}
	}

	
	/**
	 * Adds a new edge between the two given vertexes to this graph.<br>
	 * The method searches for graph vertexes that are equal to the given vertexes 
	 * (no pure reference equality, see {@link Vertex#equals(Object)}) and tries to create a new edge between them.
	 * @param sourceVertex The source vertex of this edge.
	 * @param targetVertex The target vertex of this edge.
	 * @return The newly created edge or<br>
	 * <code>null</code> if the graph already contains an edge between the given source and target vertexes.
	 * @throws VertexNotFoundException If the graph does not contain vertexes 
	 * that are equal to the given source and target vertexes.
	 */
	public E addEdge(V sourceVertex, V targetVertex) throws VertexNotFoundException{
		//Ensure parameter validity
		if(sourceVertex==null || targetVertex==null)
			throw new NullPointerException();
//		if(sourceVertex.equals(targetVertex))
//			return null;
        if(!contains(sourceVertex))
        	throw new VertexNotFoundException(sourceVertex, this);
        if(!contains(targetVertex))
        	throw new VertexNotFoundException(targetVertex, this);

        //Create a new edge and set source and target
        //The vertex references have to point to vertexes actually contained in the graph
        //-> getEqualVertex(...)
        E newEdge = createNewEdge(sourceVertex, targetVertex);
		if (containsEdge(newEdge)) {
			//Graph already contains an equal edge
            return null;
        } else {
            edgeSet.add(newEdge);
            vertexMap.get(sourceVertex).addOutgoingEdge(newEdge);
            vertexMap.get(targetVertex).addIncomingEdge(newEdge);
            return newEdge;
        }
	}
	
	/**
	 * Adds a new edge between the two given objects to this graph.<br>
	 * The method creates new Vertexes containing the objects and searches for equal graph vertexes
	 * (no pure reference equality, see {@link Vertex#equals(Object)}).
	 * It then tries create a new edge between them.
	 * @param sourceElement The source object of this edge.
	 * @param targetElement The target object of this edge.
	 * @return The newly created edge or<br>
	 * <code>null</code> if the graph already contains an edge between the given source and target objects.
	 * @throws VertexNotFoundException If the graph does not contain vertexes 
	 * that contain the given objects.
	 */
	public E addEdge(U sourceElement, U targetElement) throws VertexNotFoundException{
		//Ensure parameter validity
		if(sourceElement.equals(targetElement))
			return null;
		V sourceVertex = createNewVertex(sourceElement);
		V targetVertex = createNewVertex(targetElement);
		return addEdge(sourceVertex, targetVertex);
	}

	/**
	 * Returns the edge container fort the given vertex.
	 * @param vertex The vertex for which the edge container is requested.
	 * @return The edge container for the given vertex.
	 * @throws VertexNotFoundException If the given vertex is not found.
	 */
    protected EdgeContainer<E> getEdgeContainer(V vertex) throws VertexNotFoundException{
    	if(!this.contains(vertex))
    		throw new VertexNotFoundException(vertex, this);
    	return vertexMap.get(vertex);
    }
    	
   
    /**
     * Returns all edges (incoming + outgoing) of the given vertex.
     * @param vertex The vertex for which edges are requested.
     * @return A set of all edged leading from or to the given vertex.
     * @throws VertexNotFoundException If vertex is not found.
     */
    public Set<E> getEdgesFor(V vertex) throws VertexNotFoundException{
    	if(!contains(vertex))
    		throw new VertexNotFoundException(vertex, this);
    	Set<E> inAndOut = new HashSet<E>();
    	inAndOut.addAll(vertexMap.get(vertex).getIncomingEdges());
        inAndOut.addAll(vertexMap.get(vertex).getOutgoingEdges()); 
        return inAndOut;
    }
    
    /**
	 * Checks if the given vertex has outgoing edges.
	 * @param vertex The vertex to check.
	 * @return <code>true</code> if the given vertex has outgoing edges;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean hasOutgoingEdges(V vertex) throws VertexNotFoundException{
		return getEdgeContainer(vertex).hasOutgoingEdges();
	}
    
	/**
	 * Returns all outgoing edges of the given vertex.
	 * @param vertex The vertex for which the outgoing edges are desired.
	 * @return A list of all outgoing edges of the given vertex.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public List<E> getOutgoingEdgesFor(V vertex) throws VertexNotFoundException{
		return Collections.unmodifiableList(getEdgeContainer(vertex).getOutgoingEdges());
	}
	
	/**
	 * Checks if the given vertex has incoming edges.
	 * @param vertex The vertex to check.
	 * @return <code>true</code> if the given vertex has incoming edges;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean hasIncomingEdges(V vertex) throws VertexNotFoundException{
		return getEdgeContainer(vertex).hasIncomingEdges();
	}
	
	/**
	 * Returns all incoming edges of the given vertex.
	 * @param vertex The vertex for which the incoming edges are desired.
	 * @return A list of all incoming edges of the given vertex.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public List<E> getIncomingEdgesFor(V vertex) throws VertexNotFoundException{
		return Collections.unmodifiableList(getEdgeContainer(vertex).getIncomingEdges());
	}
	
	/**
	 * Removes an edge from the graph.
	 * Before removing the edge from the graph, 
	 * it is removed from the corresponding source and target vertexes.
	 * @param edge Edge to remove
	 * @return <code>true</code> if the removal was successful;
     *		<code>false</code> if the graph does not contain the given edge.
     * @throws VertexNotFoundException If the graph does not contain one of the edge vertexes.
     */
	public boolean removeEdge(E edge) throws VertexNotFoundException{
		E equalEdge = getEqualEdge(edge);
		if(equalEdge == null)
			return false;
		//Disconnect edge from source and target vertex
		getEdgeContainer(equalEdge.getSource()).removeOutgoingEdge(equalEdge);
		getEdgeContainer(equalEdge.getTarget()).removeIncomingEdge(equalEdge);
		//Remove edge
		return edgeSet.remove(equalEdge);
	}
	
	/**
	 * Removes an edge between the given source and target vertexes.
	 * @param sourceVertex The source vertex of the edge to remove.
	 * @param targetVertex The target vertex of the edge to remove.
	 * @return <code>true</code> if the removal was successful;
     *		<code>false</code> if the graph does not contain an edge between the given vertexes.
	 * @throws VertexNotFoundException If the graph does not contain one of the edge vertexes.
	 * @throws EdgeNotFoundException If the graph does not contain an edge from the source to the target vertex.
	 */
	public boolean removeEdge(V sourceVertex, V targetVertex) throws GraphException{
		return removeEdge(getEdge(sourceVertex, targetVertex));
	}
	
	/**
	 * Removes all given edges.
	 * @param edges The set of edges to remove.
	 * @return <code>true</code> if the set of maintained edges was modified;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If there exists an edge vertex that is not contianed in the graph.
	 */
	public boolean removeAllEdges(Collection<E> edges) throws VertexNotFoundException{
        boolean modified = false;
        for (E e : edges) {
            modified |= removeEdge(e);
        }
        return modified;
    }
	
	/**
	 * Removes all outgoing edges of the given vertex.
	 * @param vertex The vertex for which the outgoing edges should be removed.
	 * @return <code>true</code> if the set of maintained edges was modified;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean removeOutgoingEdgesFor(V vertex) throws VertexNotFoundException{
		return removeAllEdges(getEdgeContainer(vertex).getOutgoingEdges());
	}
	
	/**
	 * Removes all incoming edges of the given vertex.
	 * @param vertex The vertex for which the incoming edges should be removed.
	 * @return <code>true</code> if the set of maintained edges was modified;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean removeIncomingEdgesFor(V vertex) throws VertexNotFoundException{
		return removeAllEdges(getEdgeContainer(vertex).getIncomingEdges());
	}
	
	//------- Reachability methods -----------------------------------------------------------
	
	
	/**
	 * Returns all parents of the given vertex.
	 * @param vertex The vertex whose parents are desired.
	 * @return A list containing all parents of the given vertex.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public Set<V> getParents(V vertex) throws VertexNotFoundException{
		Set<V> parents = new HashSet<V>();
		for(E e: getEdgeContainer(vertex).getIncomingEdges()){
			parents.add(e.getSource());
		}
		return parents;
	}
	
	/**
	 * Returns all children of the given vertex.
	 * @param vertex The vertex whose children are desired.
	 * @return A list containing all children of the given vertex.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public Set<V> getChildren(V vertex) throws VertexNotFoundException{
		Set<V> children = new HashSet<V>();
		for(E e: getEdgeContainer(vertex).getOutgoingEdges()){
			children.add(e.getTarget());
		}
		return children;
	}
	
//	@Override
//	public Set<U> getParents(U node) throws VertexNotFoundException,ParameterException {
//		Set<U> parents = new HashSet<U>();
//		for(E e: getEdgeContainer(getVertex(node)).getIncomingEdges()){
//			parents.add(e.getSource().getElement());
//		}
//		return parents;
//	}
//	
//	@Override
//	public Set<U> getChildren(U node) throws VertexNotFoundException,ParameterException {
//		Set<U> children = new HashSet<U>();
//		for(E e: getEdgeContainer(getVertex(node)).getOutgoingEdges()){
//			children.add(e.getTarget().getElement());
//		}
//		return children;
//	}
	
	/**
	 * Returns the neighbors of the given vertex (children + parents).
	 * @param vertex The vertex whose neighbors are desired.
	 * @return A list containing all neighbors of the given vertex.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 * @see #getChildren(Vertex)
	 * @see #getParents(Vertex)
	 */
	public ArrayList<V> getNeighbors(V vertex) throws GraphException{
		ArrayList<V> neighbors = new ArrayList<V>();
		neighbors.addAll(getChildren(vertex));
		neighbors.addAll(getParents(vertex));
		return neighbors;
	}
	
	@Override
	public Set<V> getNodes() {
		return getVertexes();
	}
	
	//------- Output methods --------------------------------------------------------------------
	

	@Override
	public String toString(){
		return String.format(toStringFormat, name, 
											 Arrays.toString(vertexMap.keySet().toArray()), 
											 Arrays.toString(getEdges().toArray()));
	}
	
	//------- Other methods ---------------------------------------------------------------------
	
	public Set<U> getElementSet(){
		return getElementSet(getVertexes());
	}
	
	public Set<U> getElementSet(Collection<V> vertexes){
		Set<U> result = new HashSet<U>(vertexes.size());
		for(V vertex: vertexes)
			result.add(vertex.getElement());
		return result;
	}
	
	public List<U> getElementList(){
		return getElementList(getVertexes());
	}
	
	public List<U> getElementList(Collection<V> vertexes){
		List<U> result = new HashList<U>();
		for(V vertex: vertexes)
			result.add(vertex.getElement());
		return result;
	}

	@Override
	public int nodeCount() {
		return vertexMap.keySet().size();
	}
	
}
