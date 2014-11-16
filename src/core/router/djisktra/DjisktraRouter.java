package core.router.djisktra;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import core.dataStructure.graph.Graph;
import core.dataStructure.graph.interfaces.IEdge;
import core.dataStructure.graph.interfaces.INode;
import core.dataStructure.queue.priority.LinkedPriorityQueue;
import core.dataStructure.stack.exceptions.StackEmptyException;
import core.router.IRouter;
import core.router.Path;

public class DjisktraRouter<K,V> implements IRouter<K,V>{

	private Graph<K,V> graph;
	private INode<K,V> start;
	private INode<K,V> end;
	private Comparator<Path> comparator;
	
	public DjisktraRouter(Graph<K,V> graph, Comparator<Path> comparator) {
		this.graph = graph;
		this.comparator = comparator;
	}
	
	public DjisktraRouter() {
		this(null, null);
	}
	
	public void setGraph(Graph<K,V> graph) {
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
		
		Path path = new Path(start);
		NextStepsPriorityQueue<K,V> queue = new NextStepsPriorityQueue<>();
		queue.addForbiddenNextSteps(forbiddenSteps);
		queue.add(path);
		
		try {
			while(!queue.peek().peek().getKey().equals(end)) {
				queue.getNextStep();
			}
		} catch (StackEmptyException e) {
			throw new RuntimeException("Erreur de programmation makeDjisktra");
		}
		
		return queue.peek();
	}

	private class NextStepsPriorityQueue<A,E> extends LinkedPriorityQueue<Path> {

		private Set<INode<?,?>> markedNodes = new HashSet<INode<?,?>>();
		private Set<INode<A,E>> forbiddenNextSteps;
		
		public NextStepsPriorityQueue() {
			super(comparator);
		}

		public void addForbiddenNextSteps(Set<INode<A,E>> forbiddenNextSteps) {
			this.forbiddenNextSteps = forbiddenNextSteps;
		}

		public void getNextStep() {
			Path path = remove();
			try {
				removeMarkedNode(path.peek().getKey());
			} catch (StackEmptyException e) {
				throw new RuntimeException("Erreur de programmation getNextStep");
			}
			addValidePathFrom(path);
		}
		
		private void removeMarkedNode(INode<?,?> node) {
			markedNodes.add(node);
			Iterator<Path> it = iterator();
			while(it.hasNext()) {
				try {
					if(it.next().peek().equals(node)) {
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
				node = (INode<K, V>) path.peek().getKey();
			} catch (StackEmptyException e) {
				throw new RuntimeException("Erreur de programmation addValidePathFrom");
			}
			
			Path newPath;
			Set<Entry<INode<K,V>,Integer>> Neighbours = getNeighBours(node);
			
			for(Entry<INode<K,V>,Integer> neighboor : Neighbours) {
				if(!path.contains(neighboor.getKey()) && !markedNodes.contains(neighboor.getKey()) 
						&& (forbiddenNextSteps == null || !forbiddenNextSteps.contains(neighboor.getKey()))) {
						newPath = path.clone();
						newPath.add(neighboor.getKey(), neighboor.getValue());
						add(newPath);
				}
			}
			forbiddenNextSteps = null;
		}

		@SuppressWarnings("unchecked")
		private Set<Entry<INode<K,V>,Integer>> getNeighBours(INode<K, V> node) {
			Set<Entry<INode<K,V>,Integer>> Neighbours = new HashSet<Entry<INode<K,V>,Integer>>();
			List<IEdge> edges = node.getEdges();
			for(IEdge edj : edges) {
				Neighbours.add(new AbstractMap.SimpleEntry<INode<K,V>,Integer>((INode<K,V>)edj.getOther(node),  (Integer) edj.getAttribute("cost")));
			}
			return Neighbours;
		}
	}
}
