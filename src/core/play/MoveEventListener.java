package core.play;

import gui.GamePanel;

import java.util.EventListener;

public class MoveEventListener implements EventListener {
	
	private static GamePanel gamePanel;
	private static MoveEventListener listener;
	
	private MoveEventListener() {
		if(MoveEventListener.gamePanel == null) {
			MoveEventListener.gamePanel = CheeseSettings.getGamePanel();
		}
	}
	
	public static MoveEventListener getListener() {
		if( listener == null ) {
			listener = new MoveEventListener();
		}
		return listener;
	}
	
	public void onEvent(MoveEventData data) {
		gamePanel.refresh(data);
	}
	
}
