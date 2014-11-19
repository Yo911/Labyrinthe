package core.dataStructure.graph.interfaces;

import java.util.Collection;
import java.util.Set;

import core.dataStructure.graph.Gate;

public interface IGraph<K,V> {
	public INode<K, V> getNode(K key);
	public Collection<INode<K, V>> getNodes();
	public void registerNode(INode<K, V> node);
	public void unregisterNode(K key);
	public boolean contains(INode<K, V> node);
	public void addArrival(INode<K, V> arrival);
	public void addDepart(Gate<K, V> depart);
	public Set<Gate<K, V>> getDepartures();
	public Set<INode<K, V>> getArrival();
}