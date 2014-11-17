package core.dataStructure.graph;

import java.util.Collection;
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
	
	public Gate(Collection<INode<K,V>> departures, IGraph<K,V> graph) {
		this.departures = new HashSet<>();
		this.departures.addAll(departures);
		this.mouseNumber = 0;
		this.graph = graph;
	}
	
	public void setMouseNumber(int mouseNumber) {
		this.mouseNumber = mouseNumber;
	}
	
	public Set<IMouse<K,V>> getNewMouses() {
		if(this.mouseNumber < 0) return null; //On ne peut plus créer de soucis par cette porte
		Set<IMouse<K,V>> newMouses = null;
		for(INode<K,V> departure : departures) {
			if(!departure.isUsed()) {
				this.mouseNumber--;
				if(newMouses == null) {
					newMouses = new HashSet<>();
				}
				newMouses.add(new Mouse<K,V>(departure,graph,1));
			}
		}
		return newMouses;
	}
}
