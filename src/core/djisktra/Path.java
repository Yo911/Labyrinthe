package core.djisktra;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;

import core.dataStructure.stack.LinkedStack;
import core.dataStructure.stack.exceptions.StackEmptyException;
import core.dataStructure.graph.interfaces.INode;

public class Path implements Cloneable {

	public static final Path EMPTY = null;
	private int cost;
	private LinkedStack<Entry<INode<?,?>,Integer>> path;
	
	public Path(INode<?,?> node) {
		cost = 0;
		path = new LinkedStack<Entry<INode<?,?>,Integer>>();
		path.push(new AbstractMap.SimpleEntry<INode<?,?>,Integer>(node, 0));
	}
	
	private Path() {
		
	}
	
	public void add(INode<?,?> node, int cost) {
		this.cost += cost;
		path.push(new AbstractMap.SimpleEntry<INode<?,?>,Integer>(node, cost));
	}
	
	public INode<?,?> peek() throws StackEmptyException {
		return path.peek().getKey();
	}
	
	public INode<?,?> pop() throws StackEmptyException {
		Entry<INode<?,?>,Integer> node = path.pop();
		cost -= node.getValue();
		return node.getKey();
	}

	public int getCost() {
		return cost;
	}
	
	public boolean contains(INode<?,?> node) {
		Iterator<Entry<INode<?,?>,Integer>> it = path.iterator();
		while(it.hasNext()) {
			if(it.next().getKey().equals(node))
				return true;
		}
		return false;
	}
	
	public Path clone() {
		Path clone = new Path();
		clone.cost = cost;
		clone.path = path.clone();
		return clone;
	}
	
	public Iterator<Entry<INode<?,?>,Integer>> iterator() {
		return path.iterator();
	}
}
