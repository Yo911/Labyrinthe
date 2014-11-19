package core.play;


import java.util.List;
import javax.swing.SwingUtilities;
import core.dataStructure.graph.Gate;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.roundRobin.IRoundRobin;
import core.dataStructure.roundRobin.RoundRobinFIFO;
import core.dataStructure.roundRobin.exceptions.RoundRobinEmptyException;
import gui.GUI;

public class CheeseMain {
	
	public static void main(String[] args) {
		
		Runnable r = new Runnable(){
			public void run(){
				GUI.getGUI();
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	public static void letsGo(IGraph<String, Object> graph) {
		IRoundRobin<IMouse<String,Object>> rr = new RoundRobinFIFO<>();
		
		List<Gate<String, Object>> departures = graph.getDepartures();

		System.out.println("dep size = " + departures.size());
		for(int j = 0; j < departures.size(); j++) {
			departures.get(j).setMouseNumber(CheeseSettings.getMouseNumberForGate(j));
		}

		int i = 0;
						IMouse<String,Object> m = null;
		try {
			do {
				System.out.println("do");
				for(Gate<String, Object> gate : departures) {
					System.out.println("in for : gate = " + gate);
					rr.add(gate.getNewMouses());
				}
				
				if(rr.size() != 0) {
					
									System.out.println("size = " + rr.size());
					
//									if(i%rr.size() == 0)
//										System.out.println("-----");
					
					i++;
									m = rr.next();
					if(rr.next().doSomething() == true) {
										System.out.println("Mouse " + m.hashCode() + " location: " + m.getLocation());
						rr.remove();
					}
									else {
										System.out.println("Mouse " + m.hashCode() + " location: " + m.getLocation());
									}
				}
			} while(rr.size() != 0) ;
		} catch (RoundRobinEmptyException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}
	
	

}
