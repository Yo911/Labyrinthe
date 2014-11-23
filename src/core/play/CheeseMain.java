package core.play;


import gui.GUI;

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
	
	public static void main(String[] args) {
		
		Runnable r = new Runnable(){
			public void run(){
				GUI.getGUI();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	public static void letsGo() {
		IRoundRobin<IMouse<String,Object>> rr = new RoundRobinFIFO<>();
		
		Set<Gate<String, Object>> departures = CheeseSettings.getGraph().getDepartures();

		
		int j = 0;
		for(Gate<String, Object> g : departures) {
			g.setMouseNumber(CheeseSettings.getMouseNumberForGate(j));
			j++;
		}

		int i = 0;
		IMouse<String,Object> m = null;
		try {
			do {
				for(Gate<String, Object> gate : departures) {
					System.out.println("in for : gate = " + gate);
					rr.add(gate.getNewMouses());
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
					
//					Thread.sleep(1);
				}
			} while(rr.size() != 0) ;
		} catch (RoundRobinEmptyException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}

	public static boolean makeGraph(File file) {
		
		GraphMaker gm = null;
		
		try {
			gm = new GraphMaker(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean result = gm.isWellFormed() && (new GraphValidator()).forbidDeadlock(gm.getGraph()) ;
		if(result == true) {
			CheeseSettings.setGraphMaker(gm);
			CheeseSettings.setGraph(gm.getGraph());
		}
		
		return result;
	}
	
	

}
