package core.dataStructure.stack;


import java.util.Iterator;
import core.dataStructure.linkedList.simple.ILinkSimple;
import core.dataStructure.linkedList.simple.LinkSimple;
import core.dataStructure.stack.exceptions.StackEmptyException;


public class LinkedStack<V> implements ILifo<V>, Cloneable {

	private ILinkSimple<V> pileTop = null;
	
	public Iterator<V> iterator() {
		
		return new Iterator<V>() {
			
			private int init = 0;
			
			ILinkSimple<V> lastTop = null;
			
			ILinkSimple<V> newPileTop = pileTop;
			
			public boolean hasNext() {
				return newPileTop != null;
			}

			public V next() {
				init();
				lastTop = newPileTop;
				newPileTop = newPileTop.getNext();
				return lastTop.getValue();
			}
			
			private void init() {
				if(init == 0) {
					init = 1;
					newPileTop = pileTop;
				}	
			}

			public void remove() {
				
				if(init == 0) {
					throw new IllegalStateException();
				}
				
				LinkedStack<V> tmpStack = new LinkedStack<V>();
				V tmp = null;
					
				try {
					while( (tmp = pop()) != lastTop.getValue() ) {
						tmpStack.push(tmp);
					}
				} catch (StackEmptyException e) {
					e.printStackTrace();
				}
				
				Iterator<V> it = tmpStack.iterator();
				
				while(it.hasNext()) {
					push(it.next());
				}
			}
		};
	}
	


	public boolean isEmpty() {
		return size() == 0 ;
	}

	public V peek() throws StackEmptyException {
		if(isEmpty()) throw new StackEmptyException();
		return pileTop.getValue();
	}

	public V pop() throws StackEmptyException {	
		if(isEmpty()) {
			throw new StackEmptyException();
		}
		V tmp = pileTop.getValue();		
		pileTop = pileTop.getNext();	
		return tmp;
	}

	public void push(V obj) {	
		ILinkSimple<V> newTop = new LinkSimple<V>();
		newTop.setValue(obj);
		newTop.setNext(pileTop);
		pileTop = newTop;
	}

	public int size() {
		
		if(pileTop == null) return 0;
		
		int i = 0;
		Iterator<V> it = iterator();
		
		while( it.hasNext() ) {
			it.next();
			i++;
		}

		return i;
	}

	public LinkedStack<V> clone() {
		LinkedStack<V> clone = new LinkedStack<V>();
		LinkedStack<V> tmp = new LinkedStack<V>();
		Iterator<V> it = iterator();
		while(it.hasNext()) {
			tmp.push(it.next());
		}
		while(!tmp.isEmpty()) {
			try {
				clone.push(tmp.pop());
			} catch (StackEmptyException e) {
				throw new RuntimeException("Erreur de programmation LinkedStack.clone");
			}
		}
		return clone;
	}
}
