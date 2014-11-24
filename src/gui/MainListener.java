package gui;

import java.io.File;
import java.util.Comparator;
import java.util.EventListener;

import core.dataStructure.queue.priority.LinkedPriorityQueue;
import core.play.CheeseMain;

public class MainListener implements EventListener {

	private long waitingTime = 50;
	private GUI gui;

	private volatile LinkedPriorityQueue<EventContext> eventQueue = new LinkedPriorityQueue<>( new Comparator<EventContext>() {
		@Override
		public int compare(EventContext o1, EventContext o2) {
			return -1;
		}
	});
	
	class EventContext {
		private event eventType;
		private EventData data;
		
		private EventContext(event eventType, EventData data) {
			this.eventType = eventType;
			this.data = data;
		}
		
		private EventContext(event eventType) {
			this.eventType = eventType;
		}
		
		public EventData getData() {
			return this.data;
		}
		
		public event getEventType() {
			return this.eventType;
		}
	}
	
	class SettingMouseNumberData implements EventData {
		
		private int gateId;
		private int mouseNumber;
		
		private SettingMouseNumberData(int gateId, int mouseNumber) {
			this.gateId = gateId;
			this.mouseNumber = mouseNumber;
		}
		
		public int getGateId() {
			return this.gateId;
		}
		
		public int getMouseNumber() {
			return mouseNumber;
		}
		
	}
	
	class SettingWaitingTimeData implements EventData {
		private long time;
		
		private SettingWaitingTimeData(long time) {
			this.time = time;
		}
		
		public long getTime() {
			return this.time;
		}
	}
	
	class SettingNewFileGraph implements EventData {
		private File file;
		
		private SettingNewFileGraph(File file) {
			this.file = file;
		}
		
		public File getFile() {
			return this.file;
		}
	}
	
	public void setGui(GUI gui) {
		this.gui = gui;
	}
	
	private static enum event {
	    LAUNCH, NEW_GRAPH, SET_MOUSE, TIME_CHANGE
	}

	public void launchMouses() {
		eventQueue.add(new EventContext(event.LAUNCH));
	}
	
	public void newGraph(File file) {
		eventQueue.add(new EventContext(event.NEW_GRAPH, new SettingNewFileGraph(file)));
	}
	
	public void setMouseNumberForGate(int i, int number) {
		eventQueue.add(new EventContext(event.SET_MOUSE, new SettingMouseNumberData(i,number)));
	}
	
	public void setWaitingTime(long time) {
		eventQueue.add(new EventContext(event.TIME_CHANGE, new SettingWaitingTimeData(time)));
	}

	public long getWaitingTime() {
		return this.waitingTime ;
	}

	public void fireEvents() {
		EventContext e;
		while(true) {
			e = eventQueue.remove();
			if(e == null)
				break;
			fire(e);
		}
	}

	private void fire(EventContext e) {
		switch(e.getEventType()) {
			case LAUNCH:
					CheeseMain.letsGo();
				break;
			case NEW_GRAPH:
					CheeseMain.makeGraph(((SettingNewFileGraph)e.getData()).getFile());
					synchronized(gui) {
						gui.notify();
					}
				break;
			case SET_MOUSE:	
				break;
			case TIME_CHANGE:
				break;
			default:
				break;
		}
	}
}
