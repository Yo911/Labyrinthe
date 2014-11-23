package core.router.djisktra;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.dataStructure.queue.priority.LinkedPriorityQueue;
import core.dataStructure.stack.exceptions.StackEmptyException;
import core.router.IRouter;
import core.router.Path;

public class DjisktraRouter<K,V> implements IRouter<K,V>{

	private IGraph<K,V> graph;
	private INode<K,V> start;
	private INode<K,V> end;
	private Comparator<Path> comparator;
	
	public DjisktraRouter(IGraph<K,V> graph, Comparator<Path> comparator) {
		this.graph = graph;
		this.comparator = comparator;
	}
	
	public DjisktraRouter() {
		this(null, null);
	}
	
	public void setGraph(IGraph<K,V> graph) {
		this.graph = graph;
	}
	
	private void setRoute(INode<K,V> start,INode<K,V> end) {
		this.start = start;
		this.end = end;
	}
	
	public void setComparator(Comparator<Path> comparator) {
		this.comparator = comparator;
	}
	
	public Path findRoute(INode<K,V> start,INode<K,V> end, Set<INode<K,V>> forbiddenSteps) {
		setRoute(start,end);
		return findRoute(forbiddenSteps);
	}
	
	@Override
	public Path findRoute(INode<K, V> start, INode<K, V> end) {
		return findRoute(start,end,null);
	}
	
	private Path findRoute(Set<INode<K, V>> forbiddenSteps) {
		
		if(this.comparator == null || this.graph == null || this.start == null || this.end == null) {
			throw new RuntimeException();
		}
		
		if(!graph.contains(start) || !graph.contains(end)) {
			return Path.EMPTY;
		}
		
		System.out.println();
		
		Path path = new Path(start);
		NextStepsPriorityQueue queue = new NextStepsPriorityQueue();
		queue.addForbiddenNextSteps(forbiddenSteps);
		queue.add(path);
		try {
			while(!queue.peek().peek().getKey().equals(end)) {
				queue.getNextStep();
				if(queue.peek() == null) {
					return Path.EMPTY;
				}
			}
		} catch (StackEmptyException e) {
			throw new RuntimeException("Erreur de programmation makeDjisktra");
		}
		
		return queue.peek();
	}

	private class NextStepsPriorityQueue extends LinkedPriorityQueue<Path> {

		private Set<INode<K,V>> unmarkedNodes = new HashSet<INode<K,V>>();
		private Set<INode<K,V>> forbiddenNextSteps;
		
		public NextStepsPriorityQueue() {
			super(comparator);
			unmarkedNodes.addAll(graph.getNodes());
		}

		public void addForbiddenNextSteps(Set<INode<K,V>> forbiddenNextSteps) {
			this.forbiddenNextSteps = forbiddenNextSteps;
		}

		@SuppressWarnings("unchecked")
		public void getNextStep() {
			Path path = remove();
			try {
				removeMarkedNode((INode<K,V>)path.peek().getKey());
			} catch (StackEmptyException e) {
				throw new RuntimeException("Erreur de programmation getNextStep");
			}
		
			addValidePathFrom(path);
		}
		
		private void removeMarkedNode(INode<K,V> node) {
			unmarkedNodes.remove(node);
			Iterator<Path> it = iterator();
			while(it.hasNext()) {
				try { 
					Path p = it.next();
					if(p.peek().getKey().equals(node)) {
						it.remove();
					}
				} catch (StackEmptyException e) {
					throw new RuntimeException("Erreur de programmation removeMarkedNode");
				}
			}
		}

		@SuppressWarnings("unchecked")
		public void addValidePathFrom(Path path) {
			INode<K,V> node = null;
			try {
				node = (INode<K,V>) path.peek().getKey();
			} catch (StackEmptyException e) {
				throw new RuntimeException("Erreur de programmation addValidePathFrom");
			}
			
			Path newPath;
			Set<Entry<INode<K,V>,Integer>> Neighbours = node.getNeighBours();
			
			for(Entry<INode<K,V>,Integer> neighboor : Neighbours) {
				if(!path.contains(neighboor.getKey()) && unmarkedNodes.contains(neighboor.getKey()) 
						&& (forbiddenNextSteps == null || !forbiddenNextSteps.contains(neighboor.getKey()))) {
						newPath = path.clone();
						newPath.add(neighboor.getKey(), neighboor.getValue());
						add(newPath);
				}
			}
			forbiddenNextSteps = null;
		}
	}
}
