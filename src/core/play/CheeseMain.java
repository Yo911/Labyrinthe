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
	
	public static void main(String[] args) {
		
		CheeseSettings.setMainListener(mainListener);
		
		Runnable r = new Runnable(){
			public void run(){
				Gui gui = new Gui();
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

		int i = 0;
		IMouse<String,Object> m = null;
		try {
			do {
				for(Gate<String, Object> gate : departures) {
					System.out.println("in for : gate = " + gate);
;					Set<IMouse<String, Object>> n = gate.getNewMouses();
					rr.add(n);
				}
				System.out.println("size = " + rr.size());

				
				if(rr.size() != 0) {
					
					i++;
					m = rr.next();
					
					if(m.doSomething() == true) {
						System.out.println("Mouse " + m.hashCode() + " location: " + m.getLocation());
						rr.remove();
					}
					else {
						System.out.println("Mouse " + m.hashCode() + " location: " + m.getLocation());
					}
					
					
					Thread.sleep(CheeseSettings.getTurnTime());
				}
			} while(rr.size() != 0) ;
		} catch (RoundRobinEmptyException | InterruptedException e) {}
		System.out.println(i);
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
		Set<Gate<String, Object>> gates = CheeseSettings.getGraph().getDepartures();
		for(Gate<String, Object> gate : gates) {
			gatesGroupPanel.addGateConfiguratorPanelWith(gate);
		}
	}

	public static void setMouseNumberForGate(Gate<String, Object> gate, int mouseNumber) {
		CheeseSettings.setMouseNumberForGate(gate, mouseNumber);
	}

	public static void setTurnTime(long time) {
		CheeseSettings.setTurnTime(time);
	}
}
