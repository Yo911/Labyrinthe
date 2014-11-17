package core.play;

import java.awt.Event;

public class MoveEvent extends Event {

	private static final long serialVersionUID = 3827353467134414001L;
	
	private MoveEventData data;
	
	public MoveEvent(Object target, int id, MoveEventData data) {
		super(target, id, data);
		this.data = data;
	}

	public MoveEventData getData() {
		return this.data;
	}
	
}
