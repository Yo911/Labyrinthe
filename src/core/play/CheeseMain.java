package core.play;

//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import core.dataStructure.graph.Gate;
import core.dataStructure.graph.GenericEdge;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.Graph;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.dataStructure.roundRobin.IRoundRobin;
import core.dataStructure.roundRobin.RoundRobinFIFO;
import core.dataStructure.roundRobin.exceptions.RoundRobinEmptyException;
//import core.graphMaker.GraphMaker;

public class CheeseMain {

	public static void main(String[] args) {
//		GraphMaker gm = null;
//		try {
//			gm = new GraphMaker(new File("/Users/mickx/Desktop/test.txt"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		IGraph<String, Object> graph = gm.getGraph();
		
		IGraph<String, Object> graph = new Graph<String, Object>();
		
		GenericNode<String, Object> paris = new GenericNode<String, Object>("Paris");
		GenericNode<String, Object> lyon = new GenericNode<String, Object>("Lyon");
		GenericNode<String, Object> grenoble = new GenericNode<String, Object>("Grenoble");
		GenericNode<String, Object> valence = new GenericNode<String, Object>("Valence");
		GenericNode<String, Object> gap = new GenericNode<String, Object>("Gap");
		GenericNode<String, Object> marseille = new GenericNode<String, Object>("Marseille");

		graph.registerNode(paris);
		graph.registerNode(lyon);
		graph.registerNode(grenoble);
		graph.registerNode(valence);
		graph.registerNode(gap);
		graph.registerNode(marseille);
		
		// Build edges
		new GenericEdge(paris, lyon, 1);
		new GenericEdge(paris, grenoble, 1);
		new GenericEdge(lyon, grenoble, 1);
		new GenericEdge(lyon, valence, 1);
		new GenericEdge(lyon, gap, 1);
		new GenericEdge(grenoble, valence, 1);
		new GenericEdge(grenoble, gap, 1);
		new GenericEdge(gap, marseille, 1);
		new GenericEdge(valence, marseille, 1);
		
		Set<INode<String,Object>> s1 = new HashSet<>();
		Set<INode<String,Object>> s2 = new HashSet<>();
		
		s1.add(paris);
		s2.add(grenoble);
		
		Gate<String,Object> g1 = new Gate<>(s1,graph);
		Gate<String,Object> g2 = new Gate<>(s2,graph);
		
		graph.addDepart(g1);
		graph.addDepart(g2);
		graph.addArrival(marseille);

		IRoundRobin<IMouse<String,Object>> rr = new RoundRobinFIFO<>();
		
		List<Gate<String, Object>> departures = graph.getDepart();
		
		for(int j = 0; j < departures.size(); j++) {
			departures.get(j).setMouseNumber(CheeseSettings.getMouseNumberForGate(j));
		}

		int i = 0;
		IMouse<String,Object> m = null;
		try {
			do {
				
				for(Gate<String, Object> gate : departures) {
					rr.add(gate.getNewMouses());
				}
				
				if(rr.size() != 0) {
					
//					System.out.println("size = " + rr.size());
					
//					if(i%rr.size() == 0)
//						System.out.println("-----");
					
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
