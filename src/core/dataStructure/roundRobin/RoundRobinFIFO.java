package core.dataStructure.roundRobin;

import core.dataStructure.linkedList.simple.OptimizedLinkSimple;
import core.dataStructure.roundRobin.exceptions.RoundRobinEmptyException;
import core.dataStructure.roundRobin.exceptions.RoundRobinIllegalAccessException;

public class RoundRobinFIFO<T> implements IRoundRobin<T> {

	private int size;
	private OptimizedLinkSimple<T> lastIn;
	private OptimizedLinkSimple<T> previous;
	private OptimizedLinkSimple<T> morePrevious;
	private boolean hasTurned;
	private boolean hasRemoved;
	private boolean hasBegun;
	
	public void add(T value) {
		
		OptimizedLinkSimple<T> newLink = new OptimizedLinkSimple<T>();
		newLink.setValue(value);
		
		hasTurned = false;
		
		if(previous == null) {
			previous = newLink;
			previous.setNext(previous);
			previous.setIsFirst(true);
			hasRemoved = false;
			hasBegun = false;
		}
		else {
			if(morePrevious == null) {
				previous.setNext(newLink);
				newLink.setNext(previous);
				if(hasBegun == false) {
					morePrevious = previous;
					previous = newLink;
				}
				else {
					morePrevious = newLink;
				}
			}
			else {
				newLink.setNext(lastIn.getNext());
				lastIn.setNext(newLink);
				if(hasBegun == false) {
					previous = newLink;
					morePrevious = lastIn;
				}
			}
		}
		lastIn = newLink;
		size++;
	}

	public void turn(int i) throws RoundRobinEmptyException {
		
		if(previous == null) throw new RoundRobinEmptyException();
		if(i <= 0) throw new RoundRobinIllegalAccessException("Rotation " + i + " forbidden");
		
		hasBegun = true;
		
		if(morePrevious == null) {
			hasTurned = true;
		}
		else {
			if( hasRemoved == true ) {
				i--;
				previous = previous.getNext();
			}
			while( i-- > 0 ) {
				morePrevious = previous;
				previous = previous.getNext();
			}
			if(previous.getNext().isFirst()) {
				hasTurned = true;
			}
			else {
				hasTurned = false;
			}
		}
		hasRemoved = false;
	}

	public boolean hasNext() {
		return !hasTurned;
	}
	
	public T next() throws RoundRobinEmptyException {
		if(previous == null) throw new RoundRobinEmptyException();
		T value = getValue();
		turn(1);
		return value;
	}
	
	public void rewind() throws RoundRobinEmptyException {
		if(previous == null) throw new RoundRobinEmptyException();
		if(previous.getNext().isFirst() == false) {
			if( hasRemoved == true ) {
				previous = previous.getNext();
			}
			while(previous.getNext().isFirst() == false) {
				morePrevious = previous;
				previous = previous.getNext();
			}
		}
		hasTurned = false;
		hasRemoved = false;
	}

	public void remove() throws RoundRobinEmptyException {
		if(hasRemoved == true) throw new RoundRobinIllegalAccessException("Remove before next() is forbidden.");
		if(previous == null) throw new RoundRobinEmptyException();
		
		hasRemoved = true;
		
		if(previous.getNext().isFirst()) {
			hasTurned = true;
		}
		
		if(morePrevious == null) {
			previous = null;
			hasTurned = true;
		}
		else {
			
			if(previous == lastIn) {
				lastIn = morePrevious; 
			}
			
			if(previous.getNext() == morePrevious) {
				previous = morePrevious;
				previous.setNext(previous);
				previous.setIsFirst(true);
				morePrevious = null;
			}
			else {
				if(previous.isFirst()) {
					previous.getNext().setIsFirst(true);
				}
				morePrevious.setNext(previous.getNext());
				previous = morePrevious;
			}
		}
		size--;
	}

	public OptimizedLinkSimple<T> getLink() throws RoundRobinEmptyException {
		return previous.getNext();
	}
	
	public T getValue() throws RoundRobinEmptyException {
		if(previous == null) throw new RoundRobinEmptyException();
		return previous.getNext().getValue();
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean equals(OptimizedLinkSimple<T> l) throws RoundRobinEmptyException {
		if(previous == null) throw new RoundRobinEmptyException();
		return previous.getNext() == l;
	}

	public int size() {
		return size;
	}

	@Override
	public boolean isEquals(T t) throws RoundRobinEmptyException {
		return previous.getValue().equals(t);
	}
}
