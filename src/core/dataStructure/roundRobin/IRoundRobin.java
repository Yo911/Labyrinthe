package core.dataStructure.roundRobin;

import java.util.Set;

import core.dataStructure.linkedList.simple.OptimizedLinkSimple;
import core.dataStructure.roundRobin.exceptions.RoundRobinEmptyException;


public interface IRoundRobin<T> {
	public void add(T value);
	public void turn(int i) throws RoundRobinEmptyException;
	public T next() throws RoundRobinEmptyException;
	public boolean hasNext();
	public void rewind() throws RoundRobinEmptyException;
	public void remove() throws RoundRobinEmptyException;
	public OptimizedLinkSimple<T> getLink() throws RoundRobinEmptyException;
	public T getValue() throws RoundRobinEmptyException;
	public int size();
	public boolean isEmpty();
	public boolean equals(OptimizedLinkSimple<T> l) throws RoundRobinEmptyException;
	public boolean isEquals(T t) throws RoundRobinEmptyException;
	public void add(Set<T> newObject);
}
