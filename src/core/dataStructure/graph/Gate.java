package core.dataStructure.graph;

import java.util.Set;
import java.util.HashSet;

import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.play.IMouse;
import core.play.Mouse;

public class Gate<K,V> {
	
	private Set<INode<K,V>> departures;
	private int mouseNumber;
	private IGraph<K,V> graph;
	private boolean valid = true;
	private Coordinates coordinates;
	
	public Gate(Set<INode<K,V>> departures, IGraph<K,V> graph, Coordinates coordinates) {
		this.departures = departures;
		this.mouseNumber = 0;
		this.graph = graph;
		this.coordinates = coordinates;
	}
	
	public void setMouseNumber(int mouseNumber) {
		this.mouseNumber = mouseNumber;
	}
	
	public Set<INode<K,V>> getCaseAround() {
		return departures;
	}
	
	public Set<IMouse<K,V>> getNewMouses() {
		if(valid == false || this.mouseNumber <= 0) return null; //On ne peut plus créer de soucis par cette porte
		System.out.println("in get new mouse : " + departures.size());
		Set<IMouse<K,V>> newMouses = null;
		for(INode<K,V> departure : departures) {
			if(!departure.isUsed()) {
				this.mouseNumber--;
				if(newMouses == null) {
					newMouses = new HashSet<>();
				}
				newMouses.add(new Mouse<K,V>(departure,graph,1));
				if(this.mouseNumber <= 0)
					break;
			}
		}
		return newMouses;
	}

	public void unvalidate(INode<K,V> depart) {
		depart.setUsed(true);
	}

	public void unvalidateGate() {
		this.valid = false;
	}

	public Coordinates getLocation() {
		return this.coordinates;
	}
}
