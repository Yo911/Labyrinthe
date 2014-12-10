package gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import core.dataStructure.graph.Coordinates;
import core.graphMaker.GraphMaker;

public class GatesGroupPanel extends JPanel {

	private static final long serialVersionUID = -507763454416265178L;
	private Set<GateConfiguratorPanel> gateConfiguratorPanelSet = new HashSet<GateConfiguratorPanel>();
	
	public void addGateConfiguratorPanels(GraphMaker graph) {
		List<Coordinates> gateCoordinates = graph.getGates();
		GateConfiguratorPanel gateConfiguratorPanel;
		for(Coordinates c : gateCoordinates) {
			gateConfiguratorPanel = new GateConfiguratorPanel(c);
			add(gateConfiguratorPanel);
			gateConfiguratorPanelSet.add(gateConfiguratorPanel);
		}
	}

	public class GateConfiguratorPanel extends JPanel {
		
		private static final long serialVersionUID = 4841609095354179480L;

		public GateConfiguratorPanel(Coordinates c) {
			JPanel panel_7 = new JPanel();
		
			JLabel label_1 = new JLabel("Gate("+c+")");
			panel_7.add(label_1);
			
			JSpinner spinner_1 = new JSpinner();
			panel_7.add(spinner_1);
			
			add(panel_7);
		}

	}

}
