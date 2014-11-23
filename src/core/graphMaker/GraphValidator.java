package core.graphMaker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import core.dataStructure.graph.Gate;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.play.CheeseSettings;
import core.router.IRouter;
import core.router.Path;
import core.router.djisktra.DjisktraRouter;

public class GraphValidator {
	
	private IRouter<String,Object> router;
//	
//	static {
//		router = new DjisktraRouter<>();
//		router.setComparator(CheeseSettings.getComparator());
//	}
	
	public GraphValidator() {
		router = new DjisktraRouter<>();
		router.setComparator(CheeseSettings.getComparator());
	}
	
	public boolean forbidDeadlock(IGraph<String,Object> graph) {
		
		boolean isValid = false;
		
		router.setGraph(graph);
		
		Collection<Gate<String,Object>> gates = graph.getDepartures();
		Collection<INode<String,Object>> cheeses = graph.getArrival();
		
		for(Gate<String,Object> gate : gates) {
			isValid |= validateGateDepartures(gate,cheeses);
		}
		
		return isValid;
	}

	private boolean validateGateDepartures(Gate<String,Object> gate, Collection<INode<String, Object>> cheeses) {
		
		Collection<INode<String,Object>> cases = gate.getCaseAround();
		Collection<INode<String,Object>> potentialFriends;
		Set<INode<String,Object>> checkedCases = new HashSet<>();
		int checkedNumber = 0;
		boolean cheeseFound;
		boolean gateIsValid = false;
		
		for(INode<String,Object> depart : cases) {
			cheeseFound = false;
			
			for(INode<String,Object> cheese : cheeses) {
				if(router.findRoute(depart,cheese) != Path.EMPTY) {
					cheeseFound = true;
					gateIsValid = true;
					break;
				}
			}
			
			checkedCases.add(depart);
			checkedNumber++;
			
			if(cheeseFound == true) {
				potentialFriends = new HashSet<>();
				potentialFriends.addAll(gate.getCaseAround());
				potentialFriends.removeAll(checkedCases);
				if(!potentialFriends.isEmpty()) {
					checkedNumber += getFriendInGate(depart, potentialFriends, cases.size() - checkedNumber);
				}
			}
			else {
				gate.unvalidate(depart);
			}
			
			if(checkedNumber == cases.size()) {
				break;
			}
		}
		return gateIsValid;
	}

	private int getFriendInGate(INode<String, Object> depart, Collection<INode<String, Object>> potentialFriends, int max) {
		
		Set<Entry<INode<String, Object>, Integer>> neighbours = depart.getNeighBours();
		Collection<INode<String, Object>> maybeFriends;
		int checkedNumber = max;
		
		for(Entry<INode<String, Object>, Integer> neighbour : neighbours) {
			if(potentialFriends.contains(neighbour.getKey())) {
				
				checkedNumber--;
				
				if(checkedNumber == 0) {
					break;
				}
				
				maybeFriends = new HashSet<>();
				maybeFriends.addAll(potentialFriends);
				maybeFriends.remove(neighbour.getKey());
				
				if(!maybeFriends.isEmpty())
					checkedNumber += getFriendInGate(neighbour.getKey(), maybeFriends, checkedNumber);

				if(checkedNumber == 0) {
					break;
				}
			}
		}
		
		return checkedNumber;
	}
}
