package gui;

import javax.swing.SwingUtilities;

public class MainGUI {

	public static void main(String[] args) {
		//new MyUI();
		Runnable r = new Runnable(){
			public void run(){
				new GUI();
			}
		};
		SwingUtilities.invokeLater(r);
	}
}
