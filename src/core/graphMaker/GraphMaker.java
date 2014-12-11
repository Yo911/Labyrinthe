package core.graphMaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.dataStructure.graph.Coordinates;
import core.dataStructure.graph.Gate;
import core.dataStructure.graph.GenericEdge;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.Graph;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;

public class GraphMaker {
	
	public GraphMaker(File f) throws IOException {
		
		try {
			InputStream ips = new FileInputStream(f);
			InputStreamReader ipsr=new InputStreamReader(ips);
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(ipsr);
			
			graph = new Graph<String, Object>();
			String line;
			String text = "";
			char c = 0;
			int i = 0, k = 0;
			Coordinates coordinates = new Coordinates();
			Coordinates ref         = new Coordinates();
			while ((line = reader.readLine()) != null) {
				if ( line.equals("\n") )
						continue;
				if( i == 0)
					lineLength = line.length();
				wellFormed = isGoodLine(line, i);
				if( !wellFormed )
					return;
				line = line.replace("\n", "");
				text += line;
				for (int j = 0; j < line.length(); j++) {
					c = line.charAt(j);
					wellFormed = goodElement(c);
					if( !wellFormed )
						return;
					coordinates.setCoordinates(j, i);
					ref.setCoordinates(j, i);
					if( c != CHAR_WALL ) {
						int cost = 1;
						String type = "";
						switch (c) {
								case CHAR_BUSH :
									cost = 2;
									type = TYPE_BUSH;
									break;
								case CHAR_FREE_SPACE :
									cost = 1;
									type = TYPE_FREE;
									break;
								case CHAR_ARRIVAL :
									cost = 1;
									type = TYPE_ARRIVAL;
									break;
								case CHAR_DEPART :
									cost = 1;
									type = TYPE_DEPART;
									gates.add(new Coordinates(j, i));
									break;
								default:
									System.out.println("default case");
									return;
						}

						if (i != 0 && c != CHAR_WALL && c != CHAR_DEPART) {
							GenericNode<String, Object> n = new GenericNode<String, Object>(coordinates.toString());
	
							if (c == CHAR_ARRIVAL) {
								graph.addArrival(n);
							}
							
							n.setType(type);
							n.getCoordinates().setCoordinates(coordinates);
							nodes.put(ref.toString(), n);
							graph.registerNode(n);
							// GAUCHE
							if (line.charAt(j - 1) != CHAR_WALL && line.charAt(j - 1) != CHAR_DEPART) {
								ref.setCoordinates(coordinates);
								ref.setX(coordinates.getX() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (line.charAt(j - 1) != CHAR_BUSH) ? cost : 2);
							}
	
							// HAUT
							if (text.charAt(k - line.length()) != CHAR_WALL && text.charAt(k - line.length()) != CHAR_DEPART) {
								ref.setCoordinates(coordinates);
								ref.setY(coordinates.getY() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (text.charAt(k - line.length()) != CHAR_BUSH) ? cost : 2);
							}

							// HAUT - GAUCHE
							if (text.charAt(k - line.length()) != CHAR_WALL && line.charAt(j - 1) != CHAR_WALL && text.charAt(k - line.length() - 1) != CHAR_WALL && text.charAt(k - line.length() - 1) != CHAR_DEPART) {
								ref.setCoordinates(coordinates);
								ref.setX(coordinates.getX() - 1);
								ref.setY(coordinates.getY() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (text.charAt(k - line.length() - 1) != CHAR_BUSH) ? cost : 2);
							}

							// HAUT - DROITE
							if (text.charAt(k - line.length()) != CHAR_WALL && line.charAt(j + 1) != CHAR_WALL && text.charAt(k - line.length() + 1) != CHAR_WALL && text.charAt(k - line.length() + 1) != CHAR_DEPART) {
								ref.setCoordinates(coordinates);
								ref.setX(coordinates.getX() + 1);
								ref.setY(coordinates.getY() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (text.charAt(k - line.length() + 1) != CHAR_BUSH) ? cost : 2);
							}
						}
					}
					k++;
				}
				i++;
			}
			fileLength = text.length();
			reader.close();
			ipsr.close();
			ips.close();
			if (graph != null && nodes != null) {
				initGates();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void initGates() {
		for ( int i = 0; i < gates.size(); i++ ) {
			Set<INode<String,Object>> allAround = getNodeAround(gates.get(i));
			System.out.println("list nodes allAround");
			for(INode<String,Object> n : allAround) {
				System.out.println(n);
			}
			System.out.println();
			graph.addDepart(new Gate<String, Object>(allAround, graph));
		}
	}
	
	private Set<INode<String,Object>> getNodeAround(Coordinates c) {
		Set<INode<String,Object>> around = new HashSet<>();
		Coordinates co = new Coordinates(c);
		
		// HAUT, BAS, GAUCHE, DROITE
		co.setCoordinates(c);
		co.setX(c.getX() - 1);
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));
		
		co.setCoordinates(c);
		co.setX(c.getX() + 1);
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));
		
		co.setCoordinates(c);
		co.setY(c.getY() - 1);
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));
		
		co.setCoordinates(c);
		co.setY(c.getY() + 1);
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));

		// HAUT - GAUCHE
		co.setCoordinates(c);
		co.setX( c.getX() - 1 );
		co.setY( c.getY() - 1 );
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));
		
		// HAUT - DROITE
		co.setCoordinates(c);
		co.setX( c.getX() + 1 );
		co.setY( c.getY() - 1 );
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));

		// BAS - GAUCHE
		co.setCoordinates(c);
		co.setX( c.getX() - 1 );
		co.setY( c.getY() + 1 );
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));
		
		// BAS - DROITE
		co.setCoordinates(c);
		co.setX( c.getX() + 1 );
		co.setY( c.getY() + 1 );
		if(canAddInNodes(co.toString()))
			around.add(nodes.get(co.toString()));
		
		return around;
	}
	
	private boolean isGoodLine(String line, int idx) {
		if(lineLength != line.length())
			return false;
		if(idx == 0) {
			String ref = "";
			for (int i = 0; i < line.length(); i++)
				ref += "*";
			if ( ref.equals(line) )
				return true;
		} else {
			if(line.charAt(0) == '*' && line.endsWith("*"))
				return true;
		}
		return false;
	}
	
	private boolean canAddInNodes(String c) {
		if(nodes.get(c) == null)
			return false;
		return true;
	}
	
	public boolean isWellFormed() {
		return wellFormed;
	}
	
	private boolean goodElement(char c) {
		return elements.contains( c + "" );
	}
	
	public IGraph<String, Object> getGraph() {
		return graph;
	}
	
	public Map<String, GenericNode<String, Object>> getNodes() {
		return nodes;
	}
	
	public int getLength() {
		return fileLength;
	}
	
	public int getLineLength() {
		return lineLength;
	}
	
	public List<Coordinates> getGates() {
		return gates;
	}
	
	private int fileLength;
	private int lineLength;
	private Graph<String, Object> graph;
	private Map<String, GenericNode<String, Object>> nodes = new HashMap<String, GenericNode<String, Object>>();
	private boolean wellFormed = true;
	private List<Coordinates> gates = new ArrayList<>();
	
	public static final char CHAR_FREE_SPACE = ' ';
	public static final char CHAR_BUSH       = 'G';
	public static final char CHAR_WALL       = '*';
	public static final char CHAR_ARRIVAL    = 'A';
	public static final char CHAR_DEPART     = 'D'; 

	public static final String TYPE_BUSH 	= "bush";
	public static final String TYPE_FREE    = "free";
	public static final String TYPE_ARRIVAL = "arrival";
	public static final String TYPE_DEPART  = "depart";

	private List<String> elements = new ArrayList<String>();
	{
		elements.add(CHAR_FREE_SPACE + "");
		elements.add(CHAR_BUSH 		+ "");
		elements.add(CHAR_WALL 		+ "");
		elements.add(CHAR_ARRIVAL	+ "");
		elements.add(CHAR_DEPART 	+ "");
	}

}
