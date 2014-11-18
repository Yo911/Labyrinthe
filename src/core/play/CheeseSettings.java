package core.play;

import java.util.Comparator;

import core.router.Path;

public class CheeseSettings {
	
	private static Comparator<Path> comparator;
	private static MoveEventListener listener;

	public static Comparator<Path> getComparator() {
		if(comparator == null) {
			comparator = new Comparator<Path>() {

				@Override
				public int compare(Path o1, Path o2) {
					return o2.getCost() - o1.getCost();
				}
				
			};
		}
		return comparator;
	}

	public static int getMouseNumberForGate(int j) {
		return 3;
	}

	public static MoveEventListener getNotifier() {
		if( listener == null ) {
			listener = new MoveEventListener();
		}
		return listener;
	}

}
