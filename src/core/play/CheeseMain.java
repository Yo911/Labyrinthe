package core.play;


import gui.GatesGroupPanel;
import gui.MainListener;
import gui.Gui;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.SwingUtilities;

import core.dataStructure.graph.Gate;
import core.dataStructure.roundRobin.IRoundRobin;
import core.dataStructure.roundRobin.RoundRobinFIFO;
import core.dataStructure.roundRobin.exceptions.RoundRobinEmptyException;
import core.graphMaker.GraphMaker;
import core.graphMaker.GraphValidator;

public class CheeseMain {
	
	private static final MainListener mainListener = new MainListener();
	private static boolean graphIsValid;
	private static Gui gui;
	
	public static void main(String[] args) {
		
		CheeseSettings.setMainListener(mainListener);
		
		Runnable r = new Runnable(){
			public void run(){
				gui = new Gui();
				mainListener.setGui(gui);
				gui.initGui();
			}
		};
		SwingUtilities.invokeLater(r);
		
		while(true) {
			
			mainListener.fireEvents();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public static void letsGo() {
		IRoundRobin<IMouse<String,Object>> rr = new RoundRobinFIFO<>();
		
		Set<Gate<String, Object>> departures = CheeseSettings.getGraph().getDepartures();
		
		for(Gate<String, Object> g : departures) {
			g.setMouseNumber(CheeseSettings.getMouseNumberForGate(g));
		}

		int nbMovements = 0, nbMovingMouses = 0, nbMousesArrivied = 0, nbTurn = 0;
		long turnTime;
		IMouse<String, Object> m;
		try {
			do {
				
				turnTime = CheeseSettings.getTurnTime();
				
				final long startTime = System.currentTimeMillis();
				
				do {
					
					for(Gate<String, Object> gate : departures) {
						Set<IMouse<String, Object>> n = gate.getNewMouses();
						rr.add(n);
						CheeseSettings.setMouseNumberForSpinner(gate);
					}
					
					nbMovingMouses = rr.size();
					
					m = rr.next();
						
					if(m.doSomething() == true) {
						rr.remove();
						nbMousesArrivied++;
					}
					
					if(m.hasMoved()) {
						nbMovements++;
					}
					
					gui.refreshData(nbMovements, nbMovingMouses, nbMousesArrivied, nbTurn);
					
				}while(rr.size() != 0 && rr.hasNext());
				
				nbTurn++;
				
				gui.refreshData(nbMovements, nbMovingMouses, nbMousesArrivied, nbTurn);
				
				long endTime = System.currentTimeMillis();
				
				if(turnTime - (endTime - startTime) > 0)
					Thread.sleep(turnTime - (endTime - startTime));
			
			} while(rr.size() != 0) ;
			
			nbMovingMouses = 0;
			
			gui.refreshData(nbMovements, nbMovingMouses, nbMousesArrivied, nbTurn);
			
		} catch (RoundRobinEmptyException | InterruptedException e) {}
	}

	public static void makeGraph(File file) {
		
		GraphMaker gm = null;
		
		try {
			gm = new GraphMaker(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		graphIsValid = gm.isWellFormed() && GraphValidator.forbidDeadlock(gm.getGraph()) ;
		if(graphIsValid == true) {
			CheeseSettings.setGraphMaker(gm);
			CheeseSettings.setGraph(gm.getGraph());
		}
	}

	public static boolean isGraphValid() {
		return CheeseMain.graphIsValid;
	}

	public static void connectGateConfiguratorPanels(GatesGroupPanel gatesGroupPanel) {
		gatesGroupPanel.removeAll();
		if(graphIsValid) {
			Set<Gate<String, Object>> gates = CheeseSettings.getGraph().getDepartures();
			for(Gate<String, Object> gate : gates) {
				gatesGroupPanel.addGateConfiguratorPanelWith(gate);
			}
		}
	}

	public static void setMouseNumberForGate(Gate<String, Object> gate, int mouseNumber) {
		CheeseSettings.setMouseNumberForGate(gate, mouseNumber);
	}

	public static void setTurnTime(long time) {
		CheeseSettings.setTurnTime(time);
	}
}
