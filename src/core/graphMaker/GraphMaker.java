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

import core.dataStructure.graph.Coordonne;
import core.dataStructure.graph.GenericEdge;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.Graph;
import core.dataStructure.graph.interfaces.IGraph;

public class GraphMaker {
	
	public GraphMaker(File f) throws IOException {
		// TODO Auto-generated constructor stub
		
		try {
			InputStream ips = new FileInputStream(f);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader reader = new BufferedReader(ipsr);
			
			graph = new Graph<String, Object>();
			String line;
			String text = "";
			char c = 0;
			int i = 0, k = 0;
			Coordonne coordonne = new Coordonne();
			Coordonne ref       = new Coordonne();
			while ((line = reader.readLine()) != null) {
				if ( line.equals("\n") )
						continue;
				lineLength = line.length();
				line = line.replace("\n", "");
				text += line;
				for (int j = 0; j < line.length(); j++) {
					c = line.charAt(j);
					coordonne.setCoordonne(j, i);
					ref.setCoordonne(j, i);
					if( c != WALL ) {
						int cost = 0;
						char el = 0;
						String type = "";
						if (c == FREE_SPACE) {
							cost = 1;
							el = FREE_SPACE;
							type = "free";
						}
						if (c == BUSH) {
							cost = 2;
							el = BUSH;
							type = "bush";
						}
						if (c == ARRIVAL) {
							cost = 1;
							el = ARRIVAL;
							type = "arrival";
							graph.addArrival(coordonne);
						}
						if (c == DEPART) {
							cost = 1;
							el = DEPART;
							type = "depart";
							graph.addDepart(coordonne);
						}
						GenericNode<String, Object> n = new GenericNode<String, Object>(el + "");
	
						n.setType(type);
						n.coordonne.setCoordonne(coordonne);
						nodes.put(coordonne, n);
						graph.registerNode(n);
						if (i != 0 && c != WALL) {
							// GAUCHE
							if (line.charAt(j - 1) != WALL) {
								ref.setCoordonne(coordonne);
								ref.setX(coordonne.getX() - 1);
								new GenericEdge(n, nodes.get(ref), (line.charAt(j - 1) != BUSH) ? cost : 2);
							}
	
							// HAUT
							if (text.charAt(k - line.length()) != WALL) {
								ref.setCoordonne(coordonne);
								ref.setX(coordonne.getY() - 1);
								new GenericEdge(n, nodes.get(ref), (text.charAt(k - line.length()) != BUSH) ? cost : 2);
							}

							// HAUT - GAUCHE
							if (text.charAt(k - line.length()) != WALL && line.charAt(j - 1) != WALL && text.charAt(k - line.length() - 1) != WALL) {
								ref.setCoordonne(coordonne);
								ref.setX(coordonne.getX() - 1);
								ref.setX(coordonne.getY() - 1);
								new GenericEdge(n, nodes.get(ref), (text.charAt(k - line.length() - 1) != BUSH) ? cost : 2);
							}

							// HAUT - DROITE
							if (text.charAt(k - line.length()) != WALL && line.charAt(j + 1) != WALL && text.charAt(k - line.length() + 1) != WALL) {
								ref.setCoordonne(coordonne);
								ref.setX(coordonne.getX() - 1);
								ref.setX(coordonne.getY() + 1);
								new GenericEdge(n, nodes.get(ref), (text.charAt(k - line.length() + 1) != BUSH) ? cost : 2);
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IGraph<String, Object> getGraph() {
		return graph;
	}
	
	public Map<Coordonne, GenericNode<String, Object>> getNodes() {
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
	private IGraph<String, Object> graph;
	private Map<Coordonne, GenericNode<String, Object>> nodes = new HashMap<Coordonne, GenericNode<String, Object>>();
	
	public static final char FREE_SPACE = ' ';
	public static final char BUSH       = 'G';
	public static final char WALL       = '*';
	public static final char ARRIVAL    = 'A';
	public static final char DEPART     = 'D'; 

}
