package core.play;

import core.dataStructure.graph.Coordinates;
import core.dataStructure.graph.interfaces.INode;

public class MoveEventData {

	private INode<?,?> nodeLeaved;
	private INode<?,?> newNode;
	
	public MoveEventData(INode<?,?> nodeLeaved, INode<?,?> newNode) {
		this.newNode = newNode;
		this.nodeLeaved = nodeLeaved;
	}
	
	public INode<?,?> getNodeLeaved() {
		return this.nodeLeaved;
	}
	
	public INode<?,?> getNewNode() {
		return this.newNode;
	}
	
	public Coordinates getNodeLeavedCoordinates() {
		return this.nodeLeaved.getCoordinates();
	}
	
	public Coordinates getNewNodeCoordinates() {
		return this.newNode.getCoordinates();
	}
}
