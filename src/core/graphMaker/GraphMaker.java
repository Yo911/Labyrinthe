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
			while ((line = reader.readLine()) != null) {
				if ( line.equals("\n") )
						continue;
				line = line.replace("\n", "");
				text += line;
				for (int j = 0; j < line.length(); j++) {
					c = line.charAt(j);
					if (c != WALL) {
						int cost = 0;
						char el  = 0;
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
							arrival.add(k);
						}
						if (c == DEPART) {
							cost = 1;
							el = DEPART;
							type = "depart";
							depart.add(k);
						}
						GenericNode<String, Object> n = new GenericNode<String, Object>(el + "");
						
						n.setType(type);
						nodes.put(k, n);
						graph.registerNode(n);
						if (i != 0 && c != WALL) {
							// GAUCHE
							if (line.charAt(j - 1) != BUSH) {
								new GenericEdge(n, nodes.get(k - 1), cost);
							}
							if (line.charAt(j - 1) == BUSH) {
								new GenericEdge(n, nodes.get(k - 1), 2);
							}

							// HAUT
							if (text.charAt(k - line.length()) != BUSH) {
								new GenericEdge(n, nodes.get(k - line.length()), cost);
							}
							if (text.charAt(k - line.length()) == BUSH) {
								new GenericEdge(n, nodes.get(k - line.length()), 2);
							}

							// HAUT - GAUCHE
							if (text.charAt(k - line.length() - 1) != BUSH) {
								new GenericEdge(n, nodes.get(k - line.length() - 1), cost);
							}
							if (text.charAt(k - line.length() - 1) == BUSH) {
								new GenericEdge(n, nodes.get(k - line.length() - 1), 2);
							}

							// HAUT - DROITE
							if (text.charAt(k - line.length() + 1) != BUSH) {
								new GenericEdge(n, nodes.get(k - line.length() + 1), cost);
							}
							if (text.charAt(k - line.length() + 1) == BUSH) {
								new GenericEdge(n, nodes.get(k - line.length() + 1), 2);
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
	
	public Map<Integer, GenericNode<String, Object>> getNodes() {
		return nodes;
	}
	
	public int getLength() {
		return fileLength;
	}
	
	public List<Integer> getDepart() {
		return depart;
	}

	public void setDepart(List<Integer> depart) {
		this.depart = depart;
	}

	public List<Integer> getArrival() {
		return arrival;
	}

	public void setArrival(List<Integer> arrival) {
		this.arrival = arrival;
	}

	private int fileLength;
	private IGraph<String, Object> graph;
	private List<Integer> depart  = new ArrayList<Integer>();
	private List<Integer> arrival = new ArrayList<Integer>();
	private Map<Integer, GenericNode<String, Object>> nodes = new HashMap<Integer, GenericNode<String, Object>>();
	
	public static final char FREE_SPACE = ' ';
	public static final char BUSH       = 'G';
	public static final char WALL       = '*';
	public static final char ARRIVAL    = 'A';
	public static final char DEPART     = 'D'; 

}
