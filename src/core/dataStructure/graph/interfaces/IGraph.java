package core.dataStructure.graph.interfaces;

import java.util.Collection;
import java.util.List;

import core.dataStructure.graph.Coordonne;

public interface IGraph<K,V> {
	public INode<K, V> getNode(K key);
	public Collection<INode<K, V>> getNodes();
	public void registerNode(INode<K, V> node);
	public void unregisterNode(K key);
	public boolean contains(INode<K, V> node);
	public void addArrival(Coordonne arrival);
	public void addDepart(Coordonne depart);
}