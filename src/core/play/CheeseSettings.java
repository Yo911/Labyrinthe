package core.play;

import gui.MainListener;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import core.dataStructure.graph.Gate;
import core.dataStructure.graph.interfaces.IGraph;
import core.graphMaker.GraphMaker;
import core.router.Path;

public class CheeseSettings {
	
	private static Comparator<Path> comparator;
	private static IGraph<String,Object> graph;
	private static GraphMaker graphMaker;
	private static volatile MainListener mainListener;
	private static Map<Gate<String,Object>,Integer> gateSettings = new HashMap<Gate<String,Object>,Integer>();

	public static Comparator<Path> getComparator() {
		if(comparator == null) {
			comparator = new Comparator<Path>() {

				@Override
				public int compare(Path o1, Path o2) {
					return o2.getCost() - o1.getCost();
				}
				
			};
		}
		return comparator;
	}

	public static int getMouseNumberForGate(Gate<String, Object> gate) {
		if(gateSettings.isEmpty()) 
			return 50;
		return gateSettings.get(gate);
	}
	
	public static void setMouseNumberForGate(Gate<String,Object> gate, int n) {
		gateSettings.put(gate, n);
		gate.setMouseNumber(n);
	}

	public static IGraph<String, Object> getGraph() {
		return CheeseSettings.graph;
	}

	public static void setGraph(IGraph<String, Object> graph) {
		CheeseSettings.graph = graph;
	}

	public static GraphMaker getGraphMaker() {
		return CheeseSettings.graphMaker;
	}
	
	public static void setGraphMaker(GraphMaker graphMaker) {
		CheeseSettings.graphMaker = graphMaker;
	}

	public static MainListener getMainLister() {
		return CheeseSettings.mainListener;
	}
	
	public static void setMainListener(MainListener mainListener) {
		CheeseSettings.mainListener = mainListener;
	}

}
