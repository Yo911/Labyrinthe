package core.dataStructure.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import core.dataStructure.graph.interfaces.IEdge;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;

public class Graph<K,V> implements IGraph<K,V> {

	private Map<K,INode<K,V>> nodes = new HashMap<K,INode<K,V>>();
	private List<Coordonne> depart  = new ArrayList<Coordonne>();
	private List<Coordonne> arrival = new ArrayList<Coordonne>();
	
	public INode<K, V> getNode(K key) {
		return nodes.get(key);
	}

	public void registerNode(INode<K, V> node) {
		nodes.put(node.getId(), node);	
	}

	public void unregisterNode(K key) {
		
		Iterator<Entry<K,INode<K,V>>> it = nodes.entrySet().iterator();
		
		Iterator<IEdge> it2;
		
		INode<K,V> tmp;

		while( it.hasNext() ) {
			
			tmp = it.next().getValue();
			
			it2 = tmp.getEdges().iterator();
			
			while(it2.hasNext()) {
				if(it2.next().getOther(tmp) == getNode(key)) {
					it2.remove();
				}
			}
		}
		nodes.remove(key);
	}

	@SuppressWarnings("unchecked")
	public Collection<INode<K, V>> getNodes() {
		return (Collection<INode<K, V>>) nodes;
	}
	
	public boolean contains(INode<K, V> node) {
		for(K key : nodes.keySet()) {
			if(nodes.get(key).equals(node))
				return true;
		}
		return false;
	}

	public List<Coordonne> getDepart() {
		return depart;
	}

	public void addDepart(Coordonne depart) {
		this.depart.add(depart);
	}

	public List<Coordonne> getArrival() {
		return arrival;
	}

	public void addArrival(Coordonne arrival) {
		this.arrival.add(arrival);
	}
}
