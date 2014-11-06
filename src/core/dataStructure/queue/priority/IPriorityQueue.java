package core.dataStructure.queue.priority;

public interface IPriorityQueue<V> {
	public void add(V value);
	public V peek();
	public V remove();
}
