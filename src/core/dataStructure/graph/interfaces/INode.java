package core.dataStructure.graph.interfaces;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public interface INode<K, V> extends IElement{
    public K getId();
    public V getValue();
    public List<IEdge> getEdges();
	public boolean isUsed();
	public void setUsed(boolean used);
	public Set<Entry<INode<K,V>, Integer>> getNeighBours();
}

