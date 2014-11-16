package core.router;

import java.util.Comparator;
import java.util.Set;

import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;

public interface IRouter<K,V> {
	public Path findRoute(INode<K,V> start,INode<K,V> end);
	public Path findRoute(INode<K,V> location,INode<K,V> cheese, Set<INode<K,V>> forbiddenSteps);
	public void setGraph(IGraph<K,V> graph);
	public void setComparator(Comparator<Path> comparator);
}
