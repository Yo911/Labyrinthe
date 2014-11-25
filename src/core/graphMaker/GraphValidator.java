package core.graphMaker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import core.dataStructure.graph.Gate;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.play.CheeseSettings;
import core.router.IRouter;
import core.router.Path;
import core.router.djisktra.DjisktraRouter;

public class GraphValidator {
	
	private static IRouter<String,Object> router;
	
	static {
		router = new DjisktraRouter<>();
		router.setComparator(CheeseSettings.getComparator());
	}
	
	public static boolean forbidDeadlock(IGraph<String,Object> graph) {
		
		boolean isValid = false;
		
		router.setGraph(graph);
		
		Collection<Gate<String,Object>> gates = graph.getDepartures();
		Collection<INode<String,Object>> cheeses = graph.getArrival();
		
		for(Gate<String,Object> gate : gates) {
			isValid |= validateGateDepartures(gate,cheeses);
		}
		
		return isValid;
	}
	

	private static boolean validateGateDepartures(Gate<String,Object> gate, Collection<INode<String, Object>> cheeses) {
		
		Collection<INode<String,Object>> cases = gate.getCaseAround();
		Collection<INode<String,Object>> c;
		Set<INode<String,Object>> checkedCases = new HashSet<>();
		int checkedNumber = 0;
		boolean cheeseFound;
		boolean gateIsValid = false;
		int max = cases.size();
		
		for(INode<String,Object> depart : cases) {
			
			if(checkedCases.contains(depart)) {
				continue;
			}
			
			cheeseFound = false;
			
			for(INode<String,Object> cheese : cheeses) {
				if(router.findRoute(depart,cheese) != Path.EMPTY) {
					cheeseFound = true;
					gateIsValid = true;
					break;
				}
			}
			
			c = getFriendsInGate(depart,cases);
			checkedNumber += c.size();
			checkedCases.addAll(c);
			
			if(cheeseFound == false) {
				if(max == c.size()) {
					gate.unvalidateGate();
				}

				for(INode<String, Object> n : c) {
					gate.unvalidate(n);
				}
			}
			
			if(checkedNumber == max) {
				break;
			}
		}
		
		return gateIsValid;
	}

	
	private static Collection<INode<String, Object>> getFriendsInGate(INode<String, Object> caseChecked, Collection<INode<String, Object>> potentialFriends) {
		
		Set<INode<String,Object>> validateOnes = new HashSet<>();
		Set<INode<String,Object>> casesToCheck = new HashSet<>();
		Set<Entry<INode<String, Object>, Integer>> neighbours = new HashSet<>();
		Iterator<INode<String, Object>> it;
		INode<String, Object> tmp;
		
		do {
			neighbours = caseChecked.getNeighBours();
			for(Entry<INode<String, Object>, Integer> maybeFriend : neighbours) {
				tmp = maybeFriend.getKey();
				if(potentialFriends.contains(tmp) && !validateOnes.contains(tmp)) {
					casesToCheck.add(tmp);
				}
			}
			validateOnes.add(caseChecked);
			it = casesToCheck.iterator();
			try {
				caseChecked = it.next();
				it.remove();
			}
			catch(NoSuchElementException e) {
				break;
			}
		} while(true);
		
		return validateOnes;
	}
}
