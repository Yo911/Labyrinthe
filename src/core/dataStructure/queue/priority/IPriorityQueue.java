package core.dataStructure.queue.priority;

import java.util.Iterator;

public interface IPriorityQueue<V> {
	public Iterator<V> iterator();
	public void add(V value);
	public V peek();
	public V remove();
}
