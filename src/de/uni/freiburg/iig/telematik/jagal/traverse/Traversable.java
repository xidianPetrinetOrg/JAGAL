package de.uni.freiburg.iig.telematik.jagal.traverse;


import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;

/**
 * 可遍历接口，实现此接口的对象，即成为可以遍历的对象
 * AbstractGraph类，实现了该接口
 * @param <G> 图节点（顶点）类型
 */
public interface Traversable<G extends Object> {
	/** 获取参数节点的父节点集合 */
	public Collection<G> getParents(G node) throws VertexNotFoundException, ParameterException;
	/** 获取参数节点的子节点集合 */
	public Collection<G> getChildren(G node) throws VertexNotFoundException, ParameterException;
    /** 节点个数  */
	public int nodeCount();
	/** 获取所有节点集合 */
	public Collection<G> getNodes();

}
