package core.dataStructure.queue.priority;

import java.util.Comparator;

import core.dataStructure.linkedList.simple.ILinkSimple;
import core.dataStructure.linkedList.simple.LinkSimple;

public class LinkedPriorityQueue<V> implements IPriorityQueue<V> {

	private Comparator<V> comparator = null;
	private ILinkSimple<V> head = null;
	
	public LinkedPriorityQueue(Comparator<V> comparator) {
		this.comparator = comparator;
	}
	
	public void add(V value) {
		
		ILinkSimple<V> tmp = head;
		ILinkSimple<V> newLink = new LinkSimple<V>();
		newLink.setValue(value);
		newLink.setNext(null);
		
		if(tmp == null) {
			head = newLink;
		}
		else {
			if( comparator.compare( tmp.getValue() , value ) < 0 ) {
				head = newLink;
				newLink.setNext(tmp);
			}
			else {
				while( tmp.getNext() != null && comparator.compare( tmp.getNext().getValue() , value ) >= 0 ) {
					tmp = tmp.getNext();
				}
				newLink.setNext(tmp.getNext());
				tmp.setNext(newLink);
			}
		}
	}

	public V peek() {
		if(head == null) return null;
		return head.getValue();
	}

	public V remove() {
		if(head == null) return null;
		ILinkSimple<V> tmp = head;
		head = head.getNext();
		return tmp.getValue();
	}
}
