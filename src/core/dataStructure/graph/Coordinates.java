package core.dataStructure.graph;

import core.dataStructure.graph.interfaces.ICoordinates;

public class Coordinates implements ICoordinates {
	
	public Coordinates() {
		// TODO Auto-generated constructor stub
	}
	
	public Coordinates(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	
	public Coordinates(Coordinates c) {
		// TODO Auto-generated constructor stub
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public void setCoordinates(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	
	public void setCoordinates(Coordinates c) {
		// TODO Auto-generated constructor stub
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public boolean equals(Coordinates c) {
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
