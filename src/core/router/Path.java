package core.router;

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
		this();
		path.push(new AbstractMap.SimpleEntry<INode<?,?>,Integer>(node, 0));
	}
	
	private Path() {
		cost = 0;
		path = new LinkedStack<Entry<INode<?,?>,Integer>>();
	}
	
	private Path(LinkedStack<Entry<INode<?, ?>, Integer>> clone, int cost) {
		this.cost = cost;
		this.path = clone;
	}

	public void add(INode<?,?> node, int cost) {
		this.cost += cost;
		path.push(new AbstractMap.SimpleEntry<INode<?,?>,Integer>(node, cost));
	}
	
	public Entry<INode<?, ?>, Integer> peek() throws StackEmptyException {
		return path.peek();
	}
	
	public Entry<INode<?, ?>, Integer> pop() throws StackEmptyException {
		Entry<INode<?,?>,Integer> node = path.pop();
		cost -= node.getValue();
		return node;
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
		return new Path(path.clone(),cost);
	}
	
	public Iterator<Entry<INode<?,?>,Integer>> iterator() {
		return path.iterator();
	}

	public Path revert() {
		Path tmp = clone();
		Path revert = new Path();
		Entry<INode<?, ?>, Integer> node;
		while(true) {
			try {
				node = tmp.pop();
			} catch (StackEmptyException e) {
				break;
			}
			revert.add(node.getKey(),node.getValue());
		}
		return revert;
	}
}
