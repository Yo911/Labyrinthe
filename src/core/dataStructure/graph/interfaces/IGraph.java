package core.dataStructure.graph.interfaces;

import java.util.Collection;
import java.util.List;

public interface IGraph<K,V> {
	public INode<K, V> getNode(K key);
	public Collection<INode<K, V>> getNodes();
	public void registerNode(INode<K, V> node);
	public void unregisterNode(K key);
	public boolean contains(INode<K, V> node);
	public void addArrival(INode<K, V> arrival);
	public void addDepart(INode<K, V> depart);
	public List<INode<K, V>> getDepart();
	public List<INode<K, V>> getArrival();
}