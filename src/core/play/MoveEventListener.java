package core.play;

import gui.GUI;

import java.util.EventListener;

public class MoveEventListener implements EventListener {
	
	private GUI gui;
	private static MoveEventListener listener;
	
	private MoveEventListener(GUI gui) {
		this.gui = gui;
	}
	
	public static MoveEventListener getListener() {
		if( listener == null ) {
			listener = new MoveEventListener(GUI.getGUI());
		}
		return listener;
	}
	
	public void onEvent(MoveEventData data) {
		gui.refresh(data);
	}
	
}
