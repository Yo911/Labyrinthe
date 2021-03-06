package gui;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.dataStructure.graph.Gate;
import core.play.CheeseSettings;

public class GatesGroupPanel extends JPanel {

	private static final long serialVersionUID = -507763454416265178L;
	private static final MainListener mainListener = CheeseSettings.getMainLister();
	private Map<Gate, JSpinner> allSpinners = new HashMap<Gate, JSpinner>();
	
	public class GateConfiguratorPanel extends JPanel {

		private static final long serialVersionUID = 4841609095354179480L;
		private JSpinner spinner;
		private Gate<String,Object> gate;

		public GateConfiguratorPanel(Gate<String,Object> gate) {
			
			this.gate = gate;
			this.spinner = new JSpinner();
			this.spinner.setPreferredSize(new Dimension(50, 25));
			this.spinner.setValue(CheeseSettings.getMouseNumberForGate(gate));
			
			JPanel panel_7 = new JPanel();
			JLabel label_1 = new JLabel("Gate("+this.gate.getLocation()+")");
			panel_7.add(label_1);
			panel_7.add(spinner);
			add(panel_7);
			allSpinners.put(gate, spinner);
			initSpinnerLister();
		}

		private void initSpinnerLister() {
			spinner.addChangeListener( new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					mainListener.setMouseNumberForGate(gate, (Integer)spinner.getValue());
				}
			});
			
		}

	}

	public void addGateConfiguratorPanelWith(Gate<String, Object> gate) {
		add(new GateConfiguratorPanel(gate));
	}
	
	public Map<Gate, JSpinner> getAllSpinners() {
		return allSpinners;
	}

}
