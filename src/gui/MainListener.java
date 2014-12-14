package gui;

import gui.GatesGroupPanel.GateConfiguratorPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import core.dataStructure.graph.Gate;
import core.dataStructure.queue.priority.LinkedPriorityQueue;
import core.play.CheeseMain;

public class MainListener implements EventListener {
	
	private Gui gui;
	private List<Thread> threadList = new ArrayList<Thread>();
	private SettingMouseNumberData smnd = new SettingMouseNumberData();

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
		
		public void setGate(Gate<String, Object> gate) {
			this.gate = gate;
		}
		
		public void setNumber(int mouseNumber) {
			this.mouseNumber = mouseNumber;
		}
		
		private SettingMouseNumberData() {
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
	
	public void setGui(Gui gui2) {
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
		System.out.println("gate("+gate.getLocation()+")"+ " " + number);
		smnd.setGate(gate);
		smnd.setNumber(number);
		eventQueue.add(new EventContext(event.SET_MOUSE, smnd));
	}
	
	public void setWaitingTime(long time) {
		eventQueue.add(new EventContext(event.TIME_CHANGE, new SettingWaitingTimeData(time)));
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

	private synchronized void fire(final EventContext e) {
		switch(e.getEventType()) {
			case LAUNCH:
				threadList.add(new Thread() {
					public void run() {
						CheeseMain.letsGo();
					}
				});
				threadList.get(threadList.size()-1).start();
				break;
			case NEW_GRAPH:
				Iterator<Thread> it = threadList.iterator();
				while(it.hasNext()) {
					it.next().interrupt();
					it.remove();
				}
				threadList.add(new Thread() {
					public void run() {
						CheeseMain.makeGraph(((SettingNewFileGraph)e.getData()).getFile());
						CheeseMain.connectGateConfiguratorPanels(((SettingNewFileGraph)e.getData()).getGatesGroupPanel());
						synchronized(gui) {
							gui.notify();
						}
					}
				});
				threadList.get(threadList.size()-1).start();
				break;
			case SET_MOUSE:
				threadList.add(new Thread() {
					public void run() {
					CheeseMain.setMouseNumberForGate(
							((SettingMouseNumberData)e.getData()).getGate(),
							((SettingMouseNumberData)e.getData()).getMouseNumber()
						);
					}
				});
				threadList.get(threadList.size()-1).start();
				break;
			case TIME_CHANGE:
				threadList.add(new Thread() {
					public void run() {
						CheeseMain.setTurnTime(((SettingWaitingTimeData)e.getData()).getTime());
					}
				});
				threadList.get(threadList.size()-1).start();
				break;
		}
	}
}
