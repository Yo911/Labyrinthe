package core.play;

import java.util.HashSet;
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
	private INode<K,V> location = null;
	private int counter;
	private IGraph<K,V> map;
	private IRouter<K,V> router = new DjisktraRouter<>();
	private Path route;
	private boolean canMove;
	
	private final EventListenerList listeners = new EventListenerList();
	private boolean hasMoved;
	
	public Mouse(INode<K,V> location, IGraph<K,V> map, int counter) {
		setLocation(location,counter);
		this.map = map;
		router.setComparator(CheeseSettings.getComparator());
		router.setGraph(map);
		chooseCloserCheese();
		listeners.add(MoveEventListener.class, MoveEventListener.getListener());
		notifyMove(new MoveEventData(null,this.location));
	}

	@SuppressWarnings("unchecked")
	private void chooseCloserCheese() {
		Set<INode<K,V>> cheeses = map.getArrival();
		LinkedPriorityQueue<Path> queue = new LinkedPriorityQueue<>(CheeseSettings.getComparator());
		for(INode<K,V> c : cheeses) {
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
	
	public boolean setLocation(INode<K,V> location, int cost) {
		if( location.isUsed() )
			return false;
		leaveLocation();
		this.location = location;
		this.counter = cost;
		settleOnLocation();
		return true;
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
		if( path != null)
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
		
		hasMoved = true;
		
		Set<INode<K,V>> forbiddenNextSteps = getForbiddenNextSteps();
		
		if( !canMove || counter > 1 ) {
			stay();
			return false;
		}
			
		return move(forbiddenNextSteps);
	}
	
	private void stay() {
		counter--;
		hasMoved = false;
	}
	
	@Override
	public boolean hasMoved() {
		return this.hasMoved;
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
		INode<?,?> oldLocation = this.location;
		try {
			Entry<INode<?,?>, Integer> newLocation = route.peek();
			if(setLocation((INode<K, V>) newLocation.getKey(),newLocation.getValue())) {
				route.pop();
				route.peek(); // Si on est arrivé au fromage une exception est levée;
				notifyMove(new MoveEventData(oldLocation, newLocation.getKey()));
			} else {
				stay();
			}
			
		} catch (StackEmptyException e) {
			leaveLocation();
			notifyMove(new MoveEventData(oldLocation,null));
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
