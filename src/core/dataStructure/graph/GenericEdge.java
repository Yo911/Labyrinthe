package core.dataStructure.graph;

import java.util.HashMap;
import java.util.Map;

import core.dataStructure.graph.interfaces.IEdge;
import core.dataStructure.graph.interfaces.INode;


public class GenericEdge implements IEdge {
	
	private INode<?,?> node1 = null;
	private INode<?,?> node2 = null;
	private Map<String,Object> attributes = new HashMap<String,Object>();
	
	public GenericEdge(INode<?,?> node1, INode<?,?> node2, Object o) {
		this.node1 = node1;
		this.node2 = node2;
		
		node1.getEdges().add(this);
		node2.getEdges().add(this);
		
		attributes.put("cost", o);
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public INode<?,?> getOther(@SuppressWarnings("rawtypes") INode node) {
		if(node1 == node) return node2;
		if(node2 == node) return node1;
		return null;
	}

	public void setAttribute(String string, Object o) {
		attributes.put(string,o);
	}


}
