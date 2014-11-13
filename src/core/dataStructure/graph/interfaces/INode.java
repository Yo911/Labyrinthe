package core.dataStructure.graph.interfaces;

import java.util.List;

public interface INode<K, V> extends IElement{
    public K getId();
    public V getValue();
    public List<IEdge> getEdges();
	public boolean isUsed();
	public void setUsed(boolean used);
}

