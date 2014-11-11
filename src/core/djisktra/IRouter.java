package core.djisktra;

import java.util.Comparator;

import core.dataStructure.graph.Graph;
import core.dataStructure.graph.interfaces.INode;

public interface IRouter<K,V> {
	public Path findRoute(INode<K,V> start,INode<K,V> end);
	public void setGraph(Graph<K,V> graph);
	public void setComparator(Comparator<Path> comparator);
}
