package core.dataStructure.graph.interfaces;

import java.util.Collection;
import java.util.List;

import core.dataStructure.graph.Gate;

public interface IGraph<K,V> {
	public INode<K, V> getNode(K key);
	public Collection<INode<K, V>> getNodes();
	public void registerNode(INode<K, V> node);
	public void unregisterNode(K key);
	public boolean contains(INode<K, V> node);
	public void addArrival(INode<K, V> arrival);
	public void addDepart(Gate<K, V> depart);
	public List<Gate<K, V>> getDepart();
	public List<INode<K, V>> getArrival();
}