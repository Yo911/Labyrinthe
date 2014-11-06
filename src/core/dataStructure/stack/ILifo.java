package core.dataStructure.stack;

import java.util.Iterator;
import core.dataStructure.stack.exceptions.StackEmptyException;

public interface ILifo<V> {
	public V peek() throws StackEmptyException;
	public V pop() throws StackEmptyException;
	public void push(V obj);
	public int size();
	public boolean isEmpty();
	public Iterator<V> iterator();
}
