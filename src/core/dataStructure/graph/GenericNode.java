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
		coordinates = new Coordinates();
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
	
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	
	public Coordinates getCoordinates() {
		return this.coordinates;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Entry<INode<K,V>,Integer>> getNeighBours() {
		Set<Entry<INode<K,V>,Integer>> Neighbours = new HashSet<>();
		List<IEdge> edges = getEdges();
		for(IEdge edj : edges) {
			Neighbours.add(new AbstractMap.SimpleEntry<INode<K,V>,Integer>((INode<K,V>)edj.getOther(this),  (Integer) edj.getAttribute("cost")));
		}
		return Neighbours;
	}

	private Coordinates coordinates;

	private K key = null;
	private V value = null;
	private boolean used = false;
	private String type;
	
	private List<IEdge> listEdge = null; 
}