package core.dataStructure.queue.priority;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import core.dataStructure.linkedList.simple.ILinkSimple;
import core.dataStructure.linkedList.simple.LinkSimple;

public class LinkedPriorityQueue<V> implements IPriorityQueue<V> {

	private Comparator<V> comparator = null;
	private ILinkSimple<V> head = null;
	Map<StackIterator,ILinkSimple<V>> iteratorHeads = new HashMap<>();
	
	public LinkedPriorityQueue(Comparator<V> comparator) {
		this.comparator = comparator;
	}
	
	public void add(V value) {
		
		if(value == null) return;
		
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
		return new StackIterator();
	}
	
	private class StackIterator implements Iterator<V> {

		private ILinkSimple<V> morePrevious = null;
		private ILinkSimple<V> previous = head;
		private ILinkSimple<V> localHead = head;
		private boolean init = false;
		private boolean hasRemoved = false;
		
		private void init() {
			if(init == false) {
				setHead(head);
				previous = localHead;
				iteratorHeads.put(this,head);
			}
			init = true;
		}
		
		private void setHead(ILinkSimple<V> head) {
			this.localHead = head;
		}
		
		private ILinkSimple<V> getIteratorHead() {
			return localHead;
		}
		
		@Override
		public boolean hasNext() {
			if(morePrevious == null) {
				if(previous != null) {
					return true;
				}
				if(init == true) {
					iteratorHeads.remove(this);
				}
				return false;
			}
			else {
				if(previous.getNext() != null) {
					return true;
				}
				if(init == true) {
					iteratorHeads.remove(this);
				}
				return false;
			}
		}

		@Override
		public V next() {
			init();
			V v;
			if( previous == localHead ) {
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
			if( previous == localHead ) {
				previous = localHead.getNext();
				morePrevious = null;
				removeIteratorHead(localHead);
			}
			else {
				morePrevious.setNext(previous.getNext());
				previous = morePrevious;
			}
			hasRemoved = true;
		}
	}

	public void removeIteratorHead(ILinkSimple<V> localHead) {
		ILinkSimple<V> newHead = localHead.getNext();
		if(localHead == head) {
			head = newHead;
		}
		for(StackIterator iterator : iteratorHeads.keySet()) {
			if(iterator.getIteratorHead() == localHead) {
				iterator.setHead(newHead);
				iteratorHeads.put(iterator,newHead);
			}
		}
	}
}
