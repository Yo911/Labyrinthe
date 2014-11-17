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
import java.util.List;
import java.util.Map;

import core.dataStructure.graph.Coordinates;
import core.dataStructure.graph.Gate;
import core.dataStructure.graph.GenericEdge;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.Graph;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;

public class GraphMaker {
	
	public GraphMaker(File f) throws IOException {
		// TODO Auto-generated constructor stub
		
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
			Coordinates ref       = new Coordinates();
			while ((line = reader.readLine()) != null) {
				if ( line.equals("\n") )
						continue;
				lineLength = line.length();
				line = line.replace("\n", "");
				text += line;
				for (int j = 0; j < line.length(); j++) {
					c = line.charAt(j);
					wellFormed = goodElement(c);
					if( !wellFormed )
						return;
					coordinates.setCoordinates(j, i);
					ref.setCoordinates(j, i);
					if( c != WALL ) {
						int cost = 0;
						char el = 0;
						String type = "";
						switch (c) {
								case BUSH :
									cost = 2;
									el = BUSH;
									type = "bush";
									break;
								case FREE_SPACE :
									cost = 1;
									el = FREE_SPACE;
									type = "free";
									break;
								case ARRIVAL :
									cost = 1;
									el = ARRIVAL;
									type = "arrival";
									break;
								case DEPART :
									cost = 1;
									el = DEPART;
									type = "depart";
									doors.add(coordinates);
									break;
								default:
									System.out.println("default case");
									return;
						}
						GenericNode<String, Object> n = new GenericNode<String, Object>(coordinates.toString());

						if (c == ARRIVAL) {
							graph.addArrival(n);
						}
						
						n.setType(type);
						n.getCoordinates().setCoordinates(coordinates);
						nodes.put(ref.toString(), n);
						graph.registerNode(n);
						if (i != 0 && c != WALL) {
							// GAUCHE
							if (line.charAt(j - 1) != WALL && line.charAt(j - 1) != DEPART) {
								ref.setCoordinates(coordinates);
								ref.setX(coordinates.getX() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (line.charAt(j - 1) != BUSH) ? cost : 2);
							}
	
							// HAUT
							if (text.charAt(k - line.length()) != WALL && text.charAt(k - line.length()) != DEPART) {
								ref.setCoordinates(coordinates);
								ref.setY(coordinates.getY() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (text.charAt(k - line.length()) != BUSH) ? cost : 2);
							}

							// HAUT - GAUCHE
							if (text.charAt(k - line.length()) != WALL && line.charAt(j - 1) != WALL && text.charAt(k - line.length() - 1) != WALL && text.charAt(k - line.length() - 1) != DEPART) {
								ref.setCoordinates(coordinates);
								ref.setX(coordinates.getX() - 1);
								ref.setY(coordinates.getY() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (text.charAt(k - line.length() - 1) != BUSH) ? cost : 2);
							}

							// HAUT - DROITE
							if (text.charAt(k - line.length()) != WALL && line.charAt(j + 1) != WALL && text.charAt(k - line.length() + 1) != WALL && text.charAt(k - line.length() + 1) != DEPART) {
								ref.setCoordinates(coordinates);
								ref.setX(coordinates.getX() + 1);
								ref.setY(coordinates.getY() - 1);
								new GenericEdge(n, nodes.get(ref.toString()), (text.charAt(k - line.length() + 1) != BUSH) ? cost : 2);
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
			initGates(getGraph(), this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initGates(IGraph<String, Object> graph, GraphMaker gm) {
		for ( int i = 0; i < doors.size(); i++ ) {
			List<INode<String,Object>> allAround = getNodeAround(doors.get(i));
			gm.getGraph().addDepart(new Gate<String, Object>(allAround, graph));
		}
	}
	
	private List<INode<String,Object>> getNodeAround(Coordinates c) {
		List<INode<String,Object>> around = new ArrayList<>();
		Coordinates co = new Coordinates(c);
		
		// HAUT, BAS, GAUCHE, DROITE
		co.setX(c.getX() - 1);
		if(canAddInNodes(co))
			around.add(nodes.get(co.toString()));
		co.setX(c.getX() + 1);
		if(canAddInNodes(co))
			around.add(nodes.get(co.toString()));
		co.setX(c.getX());
		co.setY(c.getY() - 1);
		if(canAddInNodes(co))
			around.add(nodes.get(co.toString()));
		co.setY(c.getY() + 1);
		if(canAddInNodes(co))
			around.add(nodes.get(co.toString()));

		// HAUT - GAUCHE
		co.setX( c.getX() - 1 );
		co.setY( c.getY() - 1 );
		if(canAddInNodes(co))
			around.add(nodes.get(co.toString()));
		
		// HAUT - DROITE
		co.setX( c.getX() + 1 );
		co.setY( c.getY() - 1 );
		if(canAddInNodes(co))
			around.add(nodes.get(co.toString()));
		
		return around;
	}
	
	private boolean canAddInNodes(Coordinates c) {
		if( nodes.get(c.toString()).getValue() != WALL + "" && nodes.get(c.toString()).getValue() != DEPART + "" )
			return true;
		return false;
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
	
	private int fileLength;
	private int lineLength;
	private Graph<String, Object> graph;
	private Map<String, GenericNode<String, Object>> nodes = new HashMap<String, GenericNode<String, Object>>();
	private boolean wellFormed = true;
	private List<Coordinates> doors = new ArrayList<>();
	
	public static final char FREE_SPACE = ' ';
	public static final char BUSH       = 'G';
	public static final char WALL       = '*';
	public static final char ARRIVAL    = 'A';
	public static final char DEPART     = 'D'; 
	
	private List<String> elements = new ArrayList<String>();
	{
		elements.add(FREE_SPACE + "");
		elements.add(BUSH 		+ "");
		elements.add(WALL 		+ "");
		elements.add(ARRIVAL	+ "");
		elements.add(DEPART 	+ "");
	}

}
