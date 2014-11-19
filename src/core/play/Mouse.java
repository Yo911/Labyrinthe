package core.play;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.EventListenerList;

import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.dataStructure.queue.priority.LinkedPriorityQueue;
import core.dataStructure.stack.exceptions.StackEmptyException;
import core.router.IRouter;
import core.router.Path;
import core.router.djisktra.DjisktraRouter;

public class Mouse<K,V> implements IMouse<K,V> {
	
	private INode<K,V> cheese;
	private INode<K,V> location;
	private int counter;
	private IGraph<K,V> map;
	private IRouter<K,V> router = new DjisktraRouter<>();
	private Path route;
	private boolean canMove;
	
	private final EventListenerList listeners = new EventListenerList();
	
	public Mouse(INode<K,V> location, IGraph<K,V> map, int counter) {
		setLocation(location,counter);
		this.map = map;
		router.setComparator(CheeseSettings.getComparator());
		router.setGraph(map);
		chooseCloserCheese();
		listeners.add(MoveEventListener.class, MoveEventListener.getListener());
		notifyMove(new MoveEventData(null,this.location));
		System.out.println("et ici");
	}

	@SuppressWarnings("unchecked")
	private void chooseCloserCheese() {
		List<INode<K,V>> cheeses = map.getArrival();
		System.out.println(cheeses.size());
		LinkedPriorityQueue<Path> queue = new LinkedPriorityQueue<>(CheeseSettings.getComparator());
		for(INode<K,V> c : cheeses) {
			System.out.println("location isContained " + map.contains(location));
			System.out.println("cheese isContained " + map.contains(c));
			queue.add(router.findRoute((INode<K,V>)location,c));
		}
		try {
			this.cheese = (INode<K, V>) queue.peek().peek().getKey();
			this.route = orderRoute(queue.peek());
		} catch (StackEmptyException e) {
			throw new RuntimeException("No cheese found!");
		}
	}

	private Path orderRoute(Path route) {
		Path path = route.revert();
		try {
			path.pop();
		} catch (StackEmptyException e) {
			throw new RuntimeException("StackEmptyException Mouse.orderRoute");
		}
		return path;
	}

	public INode<K,V> getLocation() {
		return location;
	}
	
	public void setLocation(INode<K,V> location, int cost) {
		leaveLocation();
		this.location = location;
		this.counter = cost;
		settleOnLocation();
	}

	private void leaveLocation() {
		if(this.location == null) return;
		this.location.setUsed(false);
	}
	
	private void settleOnLocation() {
		if(this.location == null) return;
		this.location.setUsed(true);
	}
	
	private void getNewRoute(Set<INode<K,V>> forbiddenNextSteps) {
		Path path = router.findRoute((INode<K,V>)location, (INode<K,V>)cheese, forbiddenNextSteps);
		this.route = orderRoute(path);
	}
	
	public Set<INode<K,V>> getForbiddenNextSteps() {
		boolean all = true;
		Set<INode<K,V>> forbiddenNextSteps = new HashSet<>();
		Set<Entry<INode<K,V>,Integer>> set = this.location.getNeighBours();
		for(Entry<INode<K,V>,Integer> node : set) {
			if(node.getKey().isUsed() == true) {
				forbiddenNextSteps.add(node.getKey());
			}
			else {
				all = false;
			}
		}
		
		if(all) {
			canMove = false;
		}
		else {
			canMove = true;
		}
		
		return forbiddenNextSteps;
	}

	public boolean doSomething() {
		
		Set<INode<K,V>> forbiddenNextSteps = getForbiddenNextSteps();
		
		if(counter > 1 || canMove == false) {
			stay();
			return false;
		}
			
		return move(forbiddenNextSteps);
	}
	
	private void stay() {
		counter--;
	}

	private boolean move(Set<INode<K,V>> forbiddenNextSteps) {
		
		try {
			if(forbiddenNextSteps.contains(route.peek().getKey())) {
				getNewRoute(forbiddenNextSteps);
			}
		} catch (StackEmptyException e1) {
			throw new RuntimeException("StackEmptyException Mouse.move");
		}
		
		return goForward();
	}

	@SuppressWarnings("unchecked")
	private boolean goForward() {
		try {
			
			INode<?,?> oldLocation = this.location;
			Entry<INode<?,?>, Integer> newLocation = route.pop();
			setLocation((INode<K, V>) newLocation.getKey(),newLocation.getValue());
			route.peek(); // Si on est arrivé au fromage une exception est levée;
			notifyMove(new MoveEventData(oldLocation, newLocation.getKey()));
			
		} catch (StackEmptyException e) {
			leaveLocation();
			notifyMove(new MoveEventData(getLocation(),null));
			return true;
		}
		return false;
	}
	
	public void notifyMove(MoveEventData event) {
		for(MoveEventListener listener: listeners.getListeners(MoveEventListener.class)) {
			listener.onEvent(event);
		}
	}
}
