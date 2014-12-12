package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import core.play.CheeseMain;
import core.play.CheeseSettings;


public class Gui extends JFrame {
	
	private JButton btnGetFile;
	private JButton btnLaunch;
	private GamePanel gamePanel;
	private JTextField turnTimeTextField;
	private GatesGroupPanel gatesGroupPanel;
	private MainListener mainListener;
	private JLabel lblNbMovements;
	private JLabel lblNbMovingMouses;
	private JLabel lblNbMousesArrivied;
	private JLabel lblNbTurn;
	private static final long serialVersionUID = 5879604636994443413L;
	
	public Gui() {
		this.mainListener = CheeseSettings.getMainLister();
	}
	
	public void initGui() {
		
		int width = 800;
		int height = 500;
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JPanel panel_2 = new JPanel();
		panel_2.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		panel_2.setMaximumSize(new Dimension((int)(width*0.25),(int)(height*0.6)));
		panel.add(panel_2);
		
		btnGetFile = new JButton("Choisir un fichier");
		
		panel_2.add(btnGetFile);
		btnGetFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3);
		FlowLayout fl_panel_3 = new FlowLayout(FlowLayout.CENTER, 5, 5);
		panel_3.setLayout(fl_panel_3);
		
		JLabel lblTempsDeTour = new JLabel("Temps de tour: ");
		panel_3.add(lblTempsDeTour);	
		
		turnTimeTextField = new JTextField();
		turnTimeTextField.setHorizontalAlignment(SwingConstants.LEFT);
		turnTimeTextField.setText("100");
		panel_3.add(turnTimeTextField);
		turnTimeTextField.setColumns(10);
		
		gatesGroupPanel = new GatesGroupPanel();
		gatesGroupPanel.setBorder(null);
		gatesGroupPanel.setLayout(new BoxLayout(gatesGroupPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scroll = new JScrollPane(gatesGroupPanel);
		
		panel_2.add(scroll);
		
		scroll.setViewportBorder(null);
		
		
		btnLaunch = new JButton("Lacher les souris");
		panel_2.add(btnLaunch);
		btnLaunch.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		panel.add(panel_1);
		
		gamePanel = new GamePanel();
		JPanel panel_6 = new JPanel();
		gamePanel.setPreferredSize(new Dimension((int)(0.7*width),(int)(0.9*height)));
		panel_1.add(gamePanel);
		

		panel_1.add(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.X_AXIS));
		
		JPanel panel_11 = new JPanel();
		panel_6.add(panel_11);
		
		JLabel lblTurn = new JLabel("Nombre de tour:");
		panel_11.add(lblTurn);
		
		lblNbTurn = new JLabel("12");
		panel_11.add(lblNbTurn);
		
		JPanel panel_10 = new JPanel();
		panel_6.add(panel_10);
		
		JLabel lblMovements = new JLabel("Déplacements");
		panel_10.add(lblMovements);
		
		lblNbMovements = new JLabel("0");
		panel_10.add(lblNbMovements);
		
		JPanel panel_9 = new JPanel();
		panel_6.add(panel_9);
		
		JLabel lblMovingMouses = new JLabel("Souris en déplacement:");
		panel_9.add(lblMovingMouses);
		
		lblNbMovingMouses = new JLabel("0");
		panel_9.add(lblNbMovingMouses);
		
		JPanel panel_12 = new JPanel();
		panel_6.add(panel_12);
		
		JLabel lblMousesArrived = new JLabel("Nombre de souris arrivées:");
		panel_12.add(lblMousesArrived);
		
		lblNbMousesArrivied = new JLabel("0");
		panel_12.add(lblNbMousesArrivied);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
		initListener();
	}
	
	private void initListener() {
		btnGetFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
		        JFileChooser fileChooser = new JFileChooser();
		        fileChooser.showOpenDialog(null);
		        File file = fileChooser.getSelectedFile();
				if(file != null) {
					gamePanel.removeAll();
					try {
						
						boolean graphIsWellFormed = fireNewGraph(file);
						
						if(graphIsWellFormed) {
							gamePanel.drawMap(CheeseSettings.getGraphMaker());
						}
						else {
							gamePanel.add(new JLabel("The file isn't good ! "));
							gamePanel.repaint();
						}
						revalidate();
					
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		btnLaunch.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireLlaunch();
			}
		});
		
		turnTimeTextField.getDocument().addDocumentListener( new DocumentListener() {
			
			private long previousTime;
			
			public void updateData(long newTime) {
				this.previousTime = newTime;
				fireTimeSetting(newTime);
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println("je passe");
				try {
					long time = Long.parseLong(turnTimeTextField.getText());
					updateData(time);
				}
				catch(NumberFormatException ex) {
					setPreviousText();
				}
			}
			
			private void setPreviousText() {
				turnTimeTextField.setText(""+previousTime);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				removeUpdate(e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		
		CheeseSettings.setGamePanel(gamePanel);
	}
	
	private void fireTimeSetting(long time) {
		mainListener.setWaitingTime(time);
	}
	
	private boolean fireNewGraph(File file) {
		mainListener.newGraph(file,gatesGroupPanel);
		synchronized(this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return CheeseMain.isGraphValid();
	}
	
	private void fireLlaunch() {
		mainListener.launchMouses();
	}
	
	public void refreshData(final int nbMovements, final int nbMovingMouses, final int nbMousesArrivied, final int nbTurn) {
		lblNbMovements.setText(""+nbMovements);
		lblNbMovingMouses.setText(""+nbMovingMouses);
		lblNbMousesArrivied.setText(""+nbMousesArrivied);
		lblNbTurn.setText(""+nbTurn);
	}
}
