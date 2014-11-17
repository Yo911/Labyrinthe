package core.dataStructure.graph.interfaces;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import core.dataStructure.graph.Coordinates;

public interface INode<K, V> extends IElement{
    public K getId();
    public V getValue();
    public List<IEdge> getEdges();
	public boolean isUsed();
	public void setUsed(boolean used);
	public void setCoordinates(Coordinates coordinates);
	public Coordinates getCoordinates();
	public Set<Entry<INode<K,V>, Integer>> getNeighBours();
}

