package core.play;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.dataStructure.graph.GenericEdge;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.Graph;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.dataStructure.roundRobin.IRoundRobin;
import core.dataStructure.roundRobin.RoundRobinFIFO;
import core.dataStructure.roundRobin.exceptions.RoundRobinEmptyException;
import core.graphMaker.GraphMaker;

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
		
		graph.addDepart(paris);
		graph.addDepart(grenoble);
		graph.addArrival(marseille);
		
		int nbMouse = 2;
		
		IRoundRobin<IMouse<String,Object>> rr = new RoundRobinFIFO<>();
		
		List<INode<String, Object>> departure = graph.getDepart();
		
		for(int i = 0; i < nbMouse; i++) {
			rr.add(new Mouse<>(departure.get(i%departure.size()), graph, 0));
		}

		int j, i = 0;

		IMouse<String,Object> m = null;
		try {
			while(rr.size() != 0) {
				
//				System.out.println(rr.next().getLocation());
				j = 0;
				while(j < rr.size()) {
					j++;
					System.out.println("Mouse " + j + " location: " + rr.next().getLocation());
				}
				System.out.println("-----");
				
				i++;
				m = rr.next();
				if(m.doSomething() == true) {
					rr.remove();
				}
			}
		} catch (RoundRobinEmptyException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}

}
