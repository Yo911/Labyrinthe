package core.play;

import java.util.EventListener;

public class MoveEventListener implements EventListener {
	
	public void onEvent(MoveEventData data) {
		System.out.println("coucou");
	}
	
}
