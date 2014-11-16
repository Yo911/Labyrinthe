package core.dataStructure.graph;

import core.dataStructure.graph.interfaces.ICoordonne;

public class Coordonne implements ICoordonne {
	
	public Coordonne() {
		// TODO Auto-generated constructor stub
	}
	
	public Coordonne(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	
	public Coordonne(Coordonne c) {
		// TODO Auto-generated constructor stub
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public void setCoordonne(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	
	public void setCoordonne(Coordonne c) {
		// TODO Auto-generated constructor stub
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public boolean equals(Coordonne c) {
		return (c.getX() == x && c.getY() == y);
	}
	
	public String toString() {
		return x + ", " + y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private int x;	// x = colonne
	private int y;	// y = line
}
