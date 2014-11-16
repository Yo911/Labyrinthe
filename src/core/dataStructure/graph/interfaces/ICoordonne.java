package core.dataStructure.graph.interfaces;

import core.dataStructure.graph.Coordonne;

public interface ICoordonne {
	public void setCoordonne(int x, int y);
	
	public void setCoordonne(Coordonne c);
	
	public boolean equals(Coordonne c);
	
	public String toString();

	public int getX();

	public void setX(int x);

	public int getY();

	public void setY(int y);
}
