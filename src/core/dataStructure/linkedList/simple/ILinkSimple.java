package core.dataStructure.linkedList.simple;

public interface ILinkSimple<V> {
	public ILinkSimple<V> getNext();
	public V getValue();
	public void setNext(ILinkSimple<V> next);
	public void setValue(V object);
}
