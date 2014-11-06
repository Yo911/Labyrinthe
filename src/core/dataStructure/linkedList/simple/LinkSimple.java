package core.dataStructure.linkedList.simple;

public class LinkSimple<V> implements ILinkSimple<V>{

	private V object = null;
	private ILinkSimple<V> next = null;
	
	public ILinkSimple<V> getNext() {
		return next;
	}

	public V getValue() {
		return object;
	}

	public void setNext(ILinkSimple<V> next) {
		this.next = next; 
	}

	public void setValue(V object) {
		this.object = object ;
	}
}
