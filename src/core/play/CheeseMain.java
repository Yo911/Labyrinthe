package core.play;


import gui.GUI;
import gui.MainListener;

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
		
		Runnable r = new Runnable(){
			public void run(){
				GUI.getGUI(mainListener);
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
					
					
						Thread.sleep(mainListener.getWaitingTime());
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
		
		graphIsValid = gm.isWellFormed() && (new GraphValidator()).forbidDeadlock(gm.getGraph()) ;
		if(graphIsValid == true) {
			CheeseSettings.setGraphMaker(gm);
			CheeseSettings.setGraph(gm.getGraph());
		}
	}

	public static boolean isGraphValid() {
		return CheeseMain.graphIsValid;
	}
}
