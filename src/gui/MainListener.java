package gui;

import gui.GatesGroupPanel.GateConfiguratorPanel;

import java.io.File;
import java.util.Comparator;
import java.util.EventListener;
import java.util.Set;

import core.dataStructure.graph.Gate;
import core.dataStructure.queue.priority.LinkedPriorityQueue;
import core.play.CheeseMain;

public class MainListener implements EventListener {

	private long waitingTime = 50;
	private TestGui gui;

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
		
		private Gate<String, Object> gate;
		private int mouseNumber;
		
		private SettingMouseNumberData(Gate<String, Object> gate, int mouseNumber) {
			this.gate = gate;
			this.mouseNumber = mouseNumber;
		}
		
		public Gate<String, Object> getGate() {
			return this.gate;
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
		private GatesGroupPanel gatesGroupPanel;
		
		private SettingNewFileGraph(File file, GatesGroupPanel gatesGroupPanel) {
			this.file = file;
			this.gatesGroupPanel = gatesGroupPanel;
		}
		
		public File getFile() {
			return this.file;
		}
		
		public GatesGroupPanel getGatesGroupPanel() {
			return this.gatesGroupPanel;
		}
	}
	
	class ConfiguratorsConnectionData implements EventData {
		
		private Set<GateConfiguratorPanel> gateConfiguratorPanels;
		
		private ConfiguratorsConnectionData(Set<GateConfiguratorPanel> data) {
			this.gateConfiguratorPanels = data;
		}
		
		public Set<GateConfiguratorPanel> getGateConfiguratorPanels() {
			return this.gateConfiguratorPanels;
		}
	}
	
	public void setGui(TestGui gui2) {
		this.gui = gui2;
	}
	
	private static enum event {
	    LAUNCH, NEW_GRAPH, SET_MOUSE, TIME_CHANGE
	}

	public void launchMouses() {
		eventQueue.add(new EventContext(event.LAUNCH));
	}
	
	public void newGraph(File file, GatesGroupPanel gatesGroupPanel) {
		eventQueue.add(new EventContext(event.NEW_GRAPH, new SettingNewFileGraph(file,gatesGroupPanel)));
	}
	
	public void newGraph(File file) {
		eventQueue.add(new EventContext(event.NEW_GRAPH, new SettingNewFileGraph(file,null)));
	}
	
	public void setMouseNumberForGate(Gate<String, Object> gate, int number) {
		eventQueue.add(new EventContext(event.SET_MOUSE, new SettingMouseNumberData(gate,number)));
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
					CheeseMain.connectGateConfiguratorPanels(((SettingNewFileGraph)e.getData()).getGatesGroupPanel());
					synchronized(gui) {
						gui.notify();
					}
				break;
			case SET_MOUSE:
					CheeseMain.setMouseNumberForGate(
							((SettingMouseNumberData)e.getData()).getGate(),
							((SettingMouseNumberData)e.getData()).getMouseNumber()
						);
				break;
			case TIME_CHANGE:
				break;
		}
	}
}
