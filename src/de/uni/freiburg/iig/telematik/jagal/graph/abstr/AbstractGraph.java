package de.uni.freiburg.iig.telematik.jagal.graph.abstr;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.types.HashList;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.EdgeNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.GraphException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.traverse.Traversable;


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
	protected List<E> edgeList = new HashList<E>();
	protected Map<String, V> vertexMap = new HashMap<String, V>();
	protected Set<U> elementSet = new HashSet<U>();
	protected Map<String, EdgeContainer<E>> edgeContainers = new LinkedHashMap<String, EdgeContainer<E>>();
	
	//------- Constructors ---------------------------------------------------------------
	
	public AbstractGraph() {}
	
	public AbstractGraph(String name) {
		setName(name);
	}
	
	public AbstractGraph(Collection<String> vertexes) {
		addVertices(vertexes);
	}
	
	public AbstractGraph(String name, Collection<String> vertexes) {
		setName(name);
		addVertices(vertexes);
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
	 * @ if the given name is <code>null</code>.
	 */
	public void setName(String name) {
		Validate.notNull(name);
		this.name = name;
	}
	
	/**
	 * Checks if the graph is empty (i.e. contains no vertexes).
	 * @return <code>true</code> if the graph is empty;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isEmpty(){
		return edgeContainers.keySet().isEmpty();
	}
	
	/**
	 * Checks if the graph is trivial (i.e. contains only one vertex).
	 * @return <code>true</code> if the graph is trivial;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isTrivial(){
		return edgeContainers.keySet().size()<=1;
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
		for(String vertexName: vertexMap.keySet()){
			if(!getEdgeContainer(vertexName).hasIncomingEdges() && getEdgeContainer(vertexName).hasOutgoingEdges())
				sources.add(getVertex(vertexName));
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
		for(String vertexName: vertexMap.keySet()){
			if(!getEdgeContainer(vertexName).hasOutgoingEdges() && getEdgeContainer(vertexName).hasIncomingEdges())
				drains.add(getVertex(vertexName));
		}
		return drains;
	}
	
	/**
	 * Returns all separated vertexes of the graph.<br>
	 * Separated vertexes have no edges at all.
	 * @return A list of all separated vertexes.
	 */
	public Set<V> getSeparatedVertices(){
		Set<V> separatedVertexes = new HashSet<V>();
		for(String vertexName: vertexMap.keySet()){
			if(getEdgeContainer(vertexName).isEmpty())
				separatedVertexes.add(getVertex(vertexName));
		}
		return separatedVertexes;
	}
	
	public boolean hasSeparatedVertices(){
		for(String vertexName: vertexMap.keySet()){
			if(getEdgeContainer(vertexName).isEmpty())
				return true;
		}
		return false;
	}
	
	
	//------- VERTEX manipulation methods --------------------------------------------------------------
	
	
	/**
	 * Returns all vertexes of this graph.
	 * @return A set containing all vertexes of this graph.
	 */
	public Collection<V> getVertices(){
		return Collections.unmodifiableCollection(vertexMap.values());
	}
	
	public int getVertexCount(){
		return vertexMap.keySet().size();
	}

	/**
	 * Adds a vertex to the graph if it is not already contain an equal vertex.
	 * @param vertex Vertex to add
	 * @return 
	 */
	
	/**
	 * Adds a vertex with the given name to the graph<br>
	 * if it does not already contain a vertex with the same name.
	 * @param vertexName The name of the new vertex.
	 * @param element The element of the new vertex.
	 * @return <code>true</code> if the vertex could be inserted;
     *		<code>false</code> otherwise.	
	 * @ if the vertex name is <code>null</code>.
	 */
	public boolean addVertex(String vertexName) {
		return addVertex(vertexName, null);
	}
	
	/**
	 * Adds a vertex with the given name and element to the graph<br>
	 * if it does not already contain a vertex with the same name.
	 * @param vertexName The name of the new vertex.
	 * @param element The element of the new vertex.
	 * @return <code>true</code> if the vertex could be inserted;
     *		<code>false</code> otherwise.	
	 * @ if the vertex name is <code>null</code>.
	 */
	public boolean addVertex(String vertexName, U element) {
		Validate.notNull(vertexName);
		if(containsVertex(vertexName))
			return false;
		vertexMap.put(vertexName, createNewVertex(vertexName, element));
		edgeContainers.put(vertexName, new EdgeContainer<E>());
		return true;
	}
	
	/**
	 * Adds vertexes with the given names to the graph.<br>
	 * Vertex names have to be unique. In case the graph already contains vertexes with
	 * given names, less vertices than the given number of arguments may be added to the graph.<br>
	 * This method calls {@link #addVertex(String)} for each vertex name.
	 * @param vertexNames Names for the graph vertices.
	 * @return <code>true</code> if at least one vertex was successfully added;<br>
	 * <code>false</code> otherwise.
	 * @ If the set of vertex names is <code>null</code>
	 * or contains <code>null</code>-values.
	 * @see #addVertex(String)
	 */
	public boolean addVertices(Collection<String> vertexNames) {
		Validate.notNull(vertexNames);
		boolean updated = false;
		for(String vertexName: vertexNames){
			if(addVertex(vertexName)){
				updated = true;
			}
		}
		return updated;
	}
	
//	/**
//	 * Adds a new vertex containing the given element to the graph if it is not already contain an equal vertex.
//	 * @param element Element for which a vertex should be added.
//	 * @return <code>true</code> if a new vertex could be inserted;
//     *		<code>false</code> otherwise.	
//	 */
//	public boolean addElement(U element){
//		V newVertex = createNewVertex(element);
//		return addVertex(newVertex);
//	}
	
//	/**
//	 * Adds all specified elements by repeatedly calling the method {@link AbstractGraph#addElement(U)}.
//	 * @param elements Collection of elements to add 
//	 * @return <code>true</code> if all elements could be inserted;
//     *		<code>false</code> otherwise.
//	 * @see AbstractGraph#addElement(U)
//	 */
//	public boolean addAllElements(Collection<U> elements){
//		boolean check = true;
//		for(U element: elements){
//			check &= addElement(element);
//		}
//		return check;
//	}
	
	public boolean containsElement(U element){
		return vertexMap.containsKey(name);
	}
	
	/**
	 * Checks, if the graph contains the given vertex.
	 * @return <code>true</code> if the specified vertex is present;
     *		<code>false</code> otherwise.
	 */
	protected boolean containsVertex(V vertex){
		return containsVertex(vertex.getName());
	}
	
	/**
	 * Checks, if the graph contains a vertex with the given name.
	 * @param name
	 * @return <code>true</code> if the specified vertex is present;
     *		<code>false</code> otherwise.
	 */
	public boolean containsVertex(String name){
		return vertexMap.containsKey(name);
	}
	
	/**
	 * Checks, if the graph contains a vertex with the given name and object.
	 * @param name
	 * @return <code>true</code> if the specified vertex is present;
     *		<code>false</code> otherwise.
	 */
	public boolean containsVertex(String name, U object){
		if(!vertexMap.containsKey(name))
			return false;
		if(getVertex(name).getElement() != object)
			return false;
		return true;
	}
	
//	/**
//	 * Checks, if the graph contains all given vertexes.
//	 * It uses the method {@link AbstractGraph#contains(Vertex)}
//	 * @param Vertex Vertex to check
//	 * @return <code>true</code> if all specified vertexes are present;
//     *		<code>false</code> otherwise.
//     * @see AbstractGraph#contains(Vertex)
//	 */
//	public boolean containsAllVertices(Collection<V> vertexes){
//		for(V v: vertexes){
//			if(!contains(v))
//				return false;
//		}
//		return true;
//	}
	
//	/**
//	 * Returns the reference of a contained node, that equals to the given node.
//	 * @param vertex
//	 * @return
//	 */
//	public V getEqualVertex(V vertex) throws VertexNotFoundException{
//		for(V v: edgeContainers.keySet()){
//			if(v.equals(vertex)){
//				return v;
//			}
//		}
//		throw new VertexNotFoundException(vertex, this);
//	}
//	
//	public E getEqualEdge(E edge) {
//		for(E graphEdge: edgeSet){
//			if(graphEdge.equals(edge)){
//				return graphEdge;
//			}
//		}
//		return null;
//	}
	
	protected abstract V createNewVertex(String name, U element) ;
	
	protected abstract E createNewEdge(V sourceVertex, V targetVertex) ;
	
	public V getVertex(String name){
		return vertexMap.get(name);
	}
	
	/**
	 * Returns the vertex, that holds the given element.
	 * @param element
	 * @return The vertex containing the given element; <br>
	 * <code>null</code> otherwise.
	 */
	public V getVertex(Object element){
		for(String vertexName: vertexMap.keySet()){
			if(getVertex(vertexName).getElement() == element){
				return getVertex(vertexName);
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
	public Collection<V> vertexes(){
		return Collections.unmodifiableCollection(vertexMap.values());
	}
	
	/**
	 * Removes the vertex with the given name from the graph.
	 * Before removing the Vertex itself, all its edges are removed from the graph.
	 * @param vertexName The name of the vertex to remove
	 * @return <code>true</code> if the removal was successful;
     *		<code>false</code> otherwise.
	 * @throws VertexNotFoundException 
	 * @see AbstractGraph#removeEdge(E)
	 */
	public boolean removeVertex(String vertexName) throws VertexNotFoundException {
        validateVertex(vertexName);
        for(E vertexEdge: getEdgesFor(vertexName)){
        	removeEdge(vertexEdge);
        }
        edgeContainers.keySet().remove(vertexName);
        return true;
	}
	
	//------- VERTEX property methods -------------------------------------------------------------
	
	/**
	 * Returns the in-degree for the vertex with the given name (i.e. the number of incoming edges).
	 * @param vertexName The name of the vertex for which the in-degree is requested.
	 * @return The number of incoming edges of the vertex with the given name.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public int inDegreeOf(String vertexName) throws VertexNotFoundException{
		if(!containsVertex(vertexName))
			throw new VertexNotFoundException(vertexName, this);
		return getEdgeContainer(vertexName).getIncomingEdges().size();
	}
	
	/**
	 * Returns the out-degree for the vertex with the given name (i.e. the number of outgoing edges).
	 * @param vertexName The name of the vertex for which the out-degree is requested.
	 * @return The number of outgoing edges of vertex with the given name.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public int outDegreeOf(String vertexName) throws VertexNotFoundException{
		if(!containsVertex(vertexName))
			throw new VertexNotFoundException(vertexName, this);
		return getEdgeContainer(vertexName).getOutgoingEdges().size();
	}
	
	/**
	 * Checks if the vertex with the given name is a source vertex.<br>
	 * A vertex is considered a source if it does not have incoming edges,<br>
	 * but at least one outgoing edge.
	 * @param vertexName The name of the vertex for which the property is checked.
	 * @return <code>true</code> if the vertex with the given name is a source vertex;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public boolean isSource(String vertexName) throws VertexNotFoundException{
		if(!containsVertex(vertexName))
			throw new VertexNotFoundException(vertexName, this);
		return !getEdgeContainer(vertexName).hasIncomingEdges() && getEdgeContainer(vertexName).hasOutgoingEdges();
	}
	
	/**
	 * Checks if the vertex with the given name is a drain vertex.<br>
	 * A vertex is considered a drain if it does not have outgoing edges,<br>
	 * but at least one incoming edge.
	 * @param vertex The name of the vertex for which the property is checked.
	 * @return <code>true</code> if the vertex with the given name is a drain vertex;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public boolean isDrain(String vertexName) throws VertexNotFoundException{
		if(!containsVertex(vertexName))
			throw new VertexNotFoundException(vertexName, this);
		return !getEdgeContainer(vertexName).hasOutgoingEdges() && getEdgeContainer(vertexName).hasIncomingEdges();
	}
	
	/**
	 * Checks if the vertex with the given name is separated.<br>
	 * A vertex is considered separated if it has no edges at all.
	 * @param vertex The name of the vertex for which the property is checked.
	 * @return <code>true</code> if the vertex with the given name is separated;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public boolean isSeparated(String vertexName) throws VertexNotFoundException{
		if(!containsVertex(vertexName))
			throw new VertexNotFoundException(vertexName, this);
		return getEdgeContainer(vertexName).isEmpty();
	}
	
	//------- EDGE manipulation methods ----------------------------------------------------------
	
    public List<E> getEdges(){
        return Collections.unmodifiableList(edgeList);     
    }
    
    public int getEdgeCount(){
        return edgeList.size();  
    }
    
	/**
	 * Returns the corresponding Edge between the given source and target vertexes.
	 * @param sourceVertexName The name of the source vertex.
	 * @param targetVertexName The name of the target vertex.
	 * @return The corresponding edge between the given source and target vertexes.
     * @throws VertexNotFoundException If the given vertexes are not found.
     * @throws EdgeNotFoundException If there exists no edge between the given vertexes.
     */
	public E getEdge(String sourceVertexName, String targetVertexName) throws VertexNotFoundException, EdgeNotFoundException {
		if(!containsVertex(sourceVertexName))
			throw new VertexNotFoundException(sourceVertexName, this);
		if(!containsVertex(targetVertexName))
			throw new VertexNotFoundException(targetVertexName, this);
	
        for(Iterator<E> iter=getEdgeContainer(sourceVertexName).getOutgoingEdges().iterator(); iter.hasNext();){
            E e = iter.next();
            if (e.getTarget().getName().equals(targetVertexName)) {
                return e;
            }
        }
        throw new EdgeNotFoundException(sourceVertexName, targetVertexName, this);
    }
	
	/**
	 * Checks, if the graph contains an edge between vertexes with the given names.
	 * @param sourceVertexName The name of the source vertex.
	 * @param targetVertexName The name of the target vertex.
	 * @return <code>true</code> if the graph contains the edge;
     *		<code>false</code> otherwise.
	 */
	public boolean containsEdge(String sourceVertexName, String targetVertexName){
		try{
			getEdge(sourceVertexName, targetVertexName);
			return true;
		} catch (GraphException e) {
			return false;
		}
	}

	
	/**
	 * Adds a new edge between two vertexes with the given names to the graph.
	 * @param sourceVertexName The name of the source vertex.
	 * @param targetVertexName The name of the target vertex.
	 * @return The newly created edge or<br>
	 * <code>null</code> if the graph already contains an edge between the given source and target vertexes.
	 * @throws VertexNotFoundException If the graph does not contain vertexes 
	 * that are equal to the given source and target vertexes.
	 */
	public E addEdge(String sourceVertexName, String targetVertexName) throws VertexNotFoundException{
        if(!containsVertex(sourceVertexName))
        	throw new VertexNotFoundException(sourceVertexName, this);
        if(!containsVertex(targetVertexName))
        	throw new VertexNotFoundException(targetVertexName, this);

        if(containsEdge(sourceVertexName, targetVertexName)){
        	return null;
        }
        
		E newEdge = createNewEdge(getVertex(sourceVertexName), getVertex(targetVertexName));
		edgeList.add(newEdge);
		getEdgeContainer(sourceVertexName).addOutgoingEdge(newEdge);
		getEdgeContainer(targetVertexName).addIncomingEdge(newEdge);
		return newEdge;
	}
	
//	/**
//	 * Adds a new edge between the two given objects to this graph.<br>
//	 * The method creates new Vertexes containing the objects and searches for equal graph vertexes
//	 * (no pure reference equality, see {@link Vertex#equals(Object)}).
//	 * It then tries create a new edge between them.
//	 * @param sourceElement The source object of this edge.
//	 * @param targetElement The target object of this edge.
//	 * @return The newly created edge or<br>
//	 * <code>null</code> if the graph already contains an edge between the given source and target objects.
//	 * @throws VertexNotFoundException If the graph does not contain vertexes 
//	 * that contain the given objects.
//	 */
//	public E addEdge(U sourceElement, U targetElement) throws VertexNotFoundException{
//		//Ensure parameter validity
//		if(sourceElement.equals(targetElement))
//			return null;
//		V sourceVertex = createNewVertex(sourceElement);
//		V targetVertex = createNewVertex(targetElement);
//		return addEdge(sourceVertex, targetVertex);
//	}

	/**
	 * Returns the edge container fort the vertex with the given name.
	 * @param vertexName The name of the vertex vertex for which the edge container is requested.
	 * @return The edge container for the vertex with the given name.
	 */
    protected EdgeContainer<E> getEdgeContainer(String vertexName) {
    	return edgeContainers.get(vertexName);
    }
    
    public void printEdgeContainers(){
    	for(V vertex: getVertices()){
    		System.out.println(vertex.getName() + ": \n" + getEdgeContainer(vertex.getName()));
    	}
    }
   
    /**
     * Returns all edges (incoming + outgoing) of the given vertex.
     * @param vertexName The vertex for which edges are requested.
     * @return A set of all edged leading from or to the given vertex.
     * @throws VertexNotFoundException If vertex is not found.
     */
    protected Set<E> getEdgesFor(String vertexName) {
    	Set<E> inAndOut = new HashSet<E>();
    	inAndOut.addAll(getEdgeContainer(vertexName).getIncomingEdges());
        inAndOut.addAll(getEdgeContainer(vertexName).getOutgoingEdges()); 
        return inAndOut;
    }
    
    /**
	 * Checks if the vertex with the given name has outgoing edges.
	 * @param vertexName The name of the vertex to check.
	 * @return <code>true</code> if the vertex wit hthe given name has outgoing edges;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public boolean hasOutgoingEdges(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		return getEdgeContainer(vertexName).hasOutgoingEdges();
	}
    
	/**
	 * Returns all outgoing edges of the vertex with the given name.
	 * @param vertexName The name of the vertex for which the outgoing edges are requested.
	 * @return A list of all outgoing edges of the vertex with the given name.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public List<E> getOutgoingEdgesFor(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		return Collections.unmodifiableList(getEdgeContainer(vertexName).getOutgoingEdges());
	}
	
	/**
	 * Checks if the vertex with the given name has incoming edges.
	 * @param vertexName The name of the vertex to check.
	 * @return <code>true</code> if the vertex with the given name has incoming edges;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public boolean hasIncomingEdges(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		return getEdgeContainer(vertexName).hasIncomingEdges();
	}
	
	/**
	 * Returns all incoming edges of the vertex with the given name.
	 * @param vertexName The name of the vertex for which the incoming edges are requested.
	 * @return A list of all incoming edges of the vertex with the given name.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public List<E> getIncomingEdgesFor(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		return Collections.unmodifiableList(getEdgeContainer(vertexName).getIncomingEdges());
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
	protected boolean removeEdge(E edge) throws VertexNotFoundException{
		//Disconnect edge from source and target vertex
		getEdgeContainer(edge.getSource().getName()).removeOutgoingEdge(edge);
		getEdgeContainer(edge.getTarget().getName()).removeIncomingEdge(edge);
		//Remove edge
		return edgeList.remove(edge);
	}
	
	/**
	 * Removes an edge between two vertices.
	 * @param sourceVertexName The name of the source vertex.
	 * @param targetVertexName The name of the target vertex.
	 * @return <code>true</code> if the removal was successful;
     *		<code>false</code> if the graph does not contain an edge between the given vertexes.
	 * @throws VertexNotFoundException If the graph does not contain one of the edge vertexes.
	 * @throws EdgeNotFoundException If the graph does not contain an edge from the source to the target vertex.
	 */
	public boolean removeEdge(String sourceVertexName, String targetVertexName) throws VertexNotFoundException, EdgeNotFoundException {
		return removeEdge(getEdge(sourceVertexName, targetVertexName));
	}
	
	/**
	 * Removes all given edges.
	 * @param edges The set of edges to remove.
	 * @return <code>true</code> if the set of maintained edges was modified;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If there exists an edge vertex that is not contained in the graph.
	 */
	protected boolean removeEdges(Collection<E> edges) throws VertexNotFoundException{
        boolean modified = false;
        for (E e : edges) {
            modified |= removeEdge(e);
        }
        return modified;
    }
	
	/**
	 * Removes all outgoing edges of the vertex with the given name.
	 * @param vertexName The name of the vertex for which the outgoing edges should be removed.
	 * @return <code>true</code> if the set of maintained edges was modified;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public boolean removeOutgoingEdgesFor(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		return removeEdges(getEdgeContainer(vertexName).getOutgoingEdges());
	}
	
	/**
	 * Removes all incoming edges of the vertex with the given name.
	 * @param vertexName The name of the vertex for which the incoming edges should be removed.
	 * @return <code>true</code> if the set of maintained edges was modified;<br>
	 * <code>false</code> otherwise.
	 * @throws VertexNotFoundException If the graph does not contain the given vertex.
	 */
	public boolean removeIncomingEdgesFor(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		return removeEdges(getEdgeContainer(vertexName).getIncomingEdges());
	}
	
	//------- Reachability methods -----------------------------------------------------------
	
	
	/**
	 * Returns all parents of the vertex wit hthe given name.
	 * @param vertexName The name of the vertex whose parents are requested.
	 * @return A list containing all parents of the vertex with the given name.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public Set<V> getParents(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		Set<V> parents = new HashSet<V>();
		for(E e: getEdgeContainer(vertexName).getIncomingEdges()){
			parents.add(e.getSource());
		}
		return parents;
	}
	
	/**
	 * Returns all children of the vertex with the given name.
	 * @param vertexName The name of the vertex whose children are requested.
	 * @return A list containing all children of the vertex with the given name.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 */
	public Set<V> getChildren(String vertexName) throws VertexNotFoundException{
		validateVertex(vertexName);
		Set<V> children = new HashSet<V>();
		for(E e: getEdgeContainer(vertexName).getOutgoingEdges()){
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
	 * Returns the neighbors of the vertex with the given name (children + parents).
	 * @param vertexName The name of the vertex whose neighbors are requested.
	 * @return A list containing all neighbors of the vertex with the given name.
	 * @throws VertexNotFoundException If the graph does not contain a vertex with the given name.
	 * @see #getChildren(Vertex)
	 * @see #getParents(Vertex)
	 */
	public ArrayList<V> getNeighbors(String vertexName) throws GraphException{
		validateVertex(vertexName);
		ArrayList<V> neighbors = new ArrayList<V>();
		neighbors.addAll(getChildren(vertexName));
		neighbors.addAll(getParents(vertexName));
		return neighbors;
	}
	
	@Override
	public Collection<V> getNodes() {
		return getVertices();
	}
	
	//------- Output methods --------------------------------------------------------------------
	

	@Override
	public Collection<V> getParents(V node) throws VertexNotFoundException {
		return getParents(node.getName());
	}

	@Override
	public Collection<V> getChildren(V node) throws VertexNotFoundException {
		return getChildren(node.getName());
	}

	@Override
	public String toString(){
		return String.format(toStringFormat, name, 
											 Arrays.toString(vertexMap.keySet().toArray()), 
											 Arrays.toString(getEdges().toArray()));
	}
	
	//------- Other methods ---------------------------------------------------------------------
	
	public Set<U> getElementSet(){
		return getElementSet(getVertices());
	}
	
	public Set<U> getElementSet(Collection<V> vertexes){
		Set<U> result = new HashSet<U>(vertexes.size());
		for(V vertex: vertexes)
			result.add(vertex.getElement());
		return result;
	}
	
	public List<U> getElementList(){
		return getElementList(getVertices());
	}
	
	public List<U> getElementList(Collection<V> vertexes){
		List<U> result = new HashList<U>();
		for(V vertex: vertexes)
			result.add(vertex.getElement());
		return result;
	}

	@Override
	public int nodeCount() {
		return edgeContainers.keySet().size();
	}
	
	protected void validateVertex(String vertexName) throws VertexNotFoundException{
		if(!containsVertex(vertexName))
        	throw new VertexNotFoundException(vertexName, this);
	}
	
}
