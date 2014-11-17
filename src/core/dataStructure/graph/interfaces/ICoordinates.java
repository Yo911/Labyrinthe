package core.dataStructure.graph.interfaces;

import core.dataStructure.graph.Coordinates;

public interface ICoordinates {
	public void setCoordinates(int x, int y);
	
	public void setCoordinates(Coordinates c);
	
	public boolean equals(Coordinates c);
	
	public String toString();

	public int getX();

	public void setX(int x);

	public int getY();

	public void setY(int y);
}
