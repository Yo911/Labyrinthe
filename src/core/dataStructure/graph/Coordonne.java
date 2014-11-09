package core.dataStructure.graph;

public class Coordonne {
	
	public Coordonne() {
		// TODO Auto-generated constructor stub
	}
	
	public Coordonne(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	
	public void setCoordonne(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Coordonne c) {
		return (c.x == x && c.y == y);
	}
	
	public String toString() {
		return x + ", " + y;
	}

	public int x;	// x = colonne
	public int y;	// y = line
}
