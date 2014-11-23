package core.play;

import java.util.Comparator;

import core.dataStructure.graph.interfaces.IGraph;
import core.graphMaker.GraphMaker;
import core.router.Path;

public class CheeseSettings {
	
	private static Comparator<Path> comparator;
	private static IGraph<String,Object> graph;
	private static GraphMaker graphMaker;

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

	public static int getMouseNumberForGate(int j) {
		return 3;
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

}
