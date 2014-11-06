package core.dataStructure.graph;

import java.util.ArrayList;
import java.util.List;

import algo.graph.interfaces.IEdge;
import algo.graph.interfaces.INode;


public class GenericNode<K,V> implements INode<K,V> {

	private K key = null;
	private V value = null;
	private boolean used = false;
	private String type;
	
	private List<IEdge> listEdge = null; 
	
	public GenericNode(K key) {
		this.key = key;
		listEdge = new ArrayList<IEdge>();
	}

	public List<IEdge> getEdges() {
		return listEdge;
	}

	public K getId() {
		return key;
	}

	public V getValue() {
		return value;
	}
	
	public String toString() {
		return key.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}