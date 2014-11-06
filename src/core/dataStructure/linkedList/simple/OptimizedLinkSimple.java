package core.dataStructure.linkedList.simple;

public class OptimizedLinkSimple<V> implements ILinkSimple<V> {

	private V object = null;
	private OptimizedLinkSimple<V> next = null;
	private boolean isFirst = false;
	
	public OptimizedLinkSimple<V> getNext() {
		return next;
	}

	public V getValue() {
		return object;
	}
	
	public void setNext(OptimizedLinkSimple<V> next) {
		this.next = next; 
	}

	public void setValue(V object) {
		this.object = object ;
	}

	public void setIsFirst(boolean b) {
		isFirst = b;
	}
	
	public boolean isFirst() {
		return isFirst;
	}

	public void setNext(ILinkSimple<V> next) {
		this.next = (OptimizedLinkSimple<V>) next;
	}
	
}
