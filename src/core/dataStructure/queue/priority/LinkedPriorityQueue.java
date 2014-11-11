package core.dataStructure.queue.priority;

import java.util.Comparator;
import java.util.Iterator;

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

	@Override
	public Iterator<V> iterator() {
		return new Iterator<V>() {

			private ILinkSimple<V> morePrevious = null;
			private ILinkSimple<V> previous = head;
			private boolean init = false;
			private boolean hasRemoved = false;
			
			private void init() {
				if(init == false) {
					previous = head;
				}
				init = true;
			}
			@Override
			public boolean hasNext() {
				if(morePrevious == null) {
					return previous != null;
				}
				else {
					return previous.getNext() != null;
				}
			}

			@Override
			public V next() {
				init();
				V v;
				if( previous == head ) {
					if( morePrevious != null ) {
						previous = previous.getNext();
					}
					else {
						morePrevious = previous;
					}
					v = previous.getValue();
				}
				else {
					if( hasRemoved == false ) {
						morePrevious = previous;
					}
					previous = previous.getNext();
					v = previous.getValue();
				}
				hasRemoved = false;
				return v ;
			}

			@Override
			public void remove() {
				if(init == false || hasRemoved == true) throw new IllegalStateException();
				if( previous == head ) {
					previous = head = head.getNext();
					morePrevious = null;
				}
				else {
					morePrevious.setNext(previous.getNext());
					previous = morePrevious;
				}
				hasRemoved = true;
			}
			
		};
	}
}
