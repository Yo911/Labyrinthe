package core.play;

import gui.GamePanel;
import gui.GatesGroupPanel;
import gui.MainListener;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JSpinner;

import core.dataStructure.graph.Gate;
import core.dataStructure.graph.interfaces.IGraph;
import core.graphMaker.GraphMaker;
import core.router.Path;

public class CheeseSettings {
	
	private static Comparator<Path> comparator;
	private static IGraph<String,Object> graph;
	private static GraphMaker graphMaker;
	private static volatile MainListener mainListener;
	private static volatile Map<Gate<String,Object>,Integer> gateSettings = new HashMap<Gate<String,Object>,Integer>();
	private static Map<Gate,JSpinner> gateSpinners;
	private static GamePanel gamePanel;
	private static volatile long turnTime = 500;

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

	public static void setSpinners(GatesGroupPanel ggp) {
		gateSpinners = ggp.getAllSpinners();
	}
	
	public static int getMouseNumberForGate(Gate<String, Object> gate) {
		if(gateSettings.isEmpty() || gateSettings.get(gate) == null) {
			gateSettings.put(gate, 50);
			return 50;
		}
		return gateSettings.get(gate);
	}
	
	public static void setMouseNumberForGate(Gate<String, Object> gate, int n) {
		gate.setMouseNumber(n);
		gateSettings.put(gate, n);
	}

	public static void setMouseNumberForSpinner(Gate<String, Object> gate) {
		gateSpinners.get(gate).setValue(gate.getMouseNumber());
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

	public static GamePanel getGamePanel() {
		return CheeseSettings.gamePanel;
	}
	
	public static void setGamePanel(GamePanel gamePanel) {
		CheeseSettings.gamePanel = gamePanel;
	}

	public static void setTurnTime(long time) {
		CheeseSettings.turnTime = time;
	}
	
	public static long getTurnTime() {
		return CheeseSettings.turnTime ;
	}

}
