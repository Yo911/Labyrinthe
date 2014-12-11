package core.router;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;

import core.dataStructure.graph.interfaces.INode;
import core.dataStructure.stack.LinkedStack;
import core.dataStructure.stack.exceptions.StackEmptyException;
import core.graphMaker.GraphMaker;

public class Path implements Cloneable {

	public static final Path EMPTY = null;
	private int cost;
	private LinkedStack<Entry<INode<?, ?>, Integer>> path;

	public Path(INode<?, ?> node) {
		this();
		path.push(new AbstractMap.SimpleEntry<INode<?, ?>, Integer>(node, 0));
	}

	private Path() {
		cost = 0;
		path = new LinkedStack<Entry<INode<?, ?>, Integer>>();
	}

	private Path(LinkedStack<Entry<INode<?, ?>, Integer>> clone, int cost) {
		this.cost = cost;
		this.path = clone;
	}

	public void add(INode<?, ?> node, int cost) {
		if (!path.isEmpty()) {
			try {
				if (path.peek().getKey().getType().equals(GraphMaker.TYPE_BUSH)
						&& node.getType().equals(GraphMaker.TYPE_FREE)) {
					cost = 1;
				} else if (path.peek().getKey().getType()
						.equals(GraphMaker.TYPE_FREE)
						&& node.getType().equals(GraphMaker.TYPE_BUSH)) {
					cost = 2;
				}
			} catch (StackEmptyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			if (node.getType().equals(GraphMaker.TYPE_FREE)) {
				cost = 1;
			} else if (node.getType().equals(GraphMaker.TYPE_BUSH)) {
				cost = 2;
			}

		}
		this.cost += cost;
		path.push(new AbstractMap.SimpleEntry<INode<?, ?>, Integer>(node, cost));
	}

	public Entry<INode<?, ?>, Integer> peek() throws StackEmptyException {
		return path.peek();
	}

	public Entry<INode<?, ?>, Integer> pop() throws StackEmptyException {
		Entry<INode<?, ?>, Integer> node = path.pop();
		cost -= node.getValue();
		return node;
	}

	public int getCost() {
		return cost;
	}

	public boolean contains(INode<?, ?> node) {
		Iterator<Entry<INode<?, ?>, Integer>> it = path.iterator();
		while (it.hasNext()) {
			if (it.next().getKey().equals(node))
				return true;
		}
		return false;
	}

	public Path clone() {
		return new Path(path.clone(), cost);
	}

	public Path revert() {
		Path tmp = clone();
		Path revert = new Path();
		Entry<INode<?, ?>, Integer> node;
		while (true) {
			try {
				node = tmp.pop();
			} catch (StackEmptyException e) {
				break;
			}
			revert.add(node.getKey(), node.getValue());
		}
		return revert;
	}
}
