package core.play;

import core.dataStructure.graph.interfaces.INode;

public interface IMouse<K,V> {
	public boolean doSomething();
	public INode<K,V> getLocation();
}
