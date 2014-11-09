package core.dataStructure.graph.interfaces;


public interface IEdge {
	public INode<?, ?> getOther(INode<?, ?> me);
	public void setAttribute(String key, Object value);
	public Object getAttribute(String value);
}
