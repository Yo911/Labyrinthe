package core.dataStructure.graph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import core.dataStructure.graph.interfaces.IEdge;
import core.dataStructure.graph.interfaces.INode;


public class GenericNode<K,V> implements INode<K,V> {
	
	public GenericNode(K key) {
		this.key = key;
		listEdge = new ArrayList<IEdge>();
		coordonne = new Coordonne();
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
	
	@SuppressWarnings("unchecked")
	public Set<Entry<INode<?,?>,Integer>> getNeighBours() {
		Set<Entry<INode<?,?>,Integer>> Neighbours = new HashSet<>();
		List<IEdge> edges = getEdges();
		for(IEdge edj : edges) {
			Neighbours.add(new AbstractMap.SimpleEntry<INode<?,?>,Integer>((INode<K,V>)edj.getOther(this),  (Integer) edj.getAttribute("cost")));
		}
		return Neighbours;
	}

	public Coordonne coordonne;

	private K key = null;
	private V value = null;
	private boolean used = false;
	private String type;
	
	private List<IEdge> listEdge = null; 
}