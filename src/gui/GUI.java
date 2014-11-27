package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import core.dataStructure.graph.Coordinates;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.interfaces.INode;
import core.graphMaker.GraphMaker;
import core.play.CheeseMain;
import core.play.CheeseSettings;
import core.play.MoveEventData;

public class GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private GUI(MainListener listner) {
		listeners.add(MainListener.class, listner);
		Listen l = new Listen();
		addWindowListener(l);
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int) dimension.getHeight() / 2;
		width = (int) dimension.getWidth() / 2;
		height = 700;
		width = 1500;
		setPreferredSize(new Dimension(width, height));
		setLocation(1, 1);
		setTitle("PITITE SOURIS !! ");

		gbc.fill = GridBagConstraints.BOTH;
		

		setLayout(bl);
		jp.setLayout(gbl);
		JButton getFile = new JButton("choose your file");
		
		getFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
		        JFileChooser fileChooser = new JFileChooser();
		        fileChooser.showOpenDialog(null);
		        file = fileChooser.getSelectedFile();
				if(file != null) {
					jp.removeAll();
					try {
						
						boolean graphIsWellFormed = fireNewGraph(file);
						
						if(graphIsWellFormed) {
							gm = CheeseSettings.getGraphMaker();
							drawField();
							getDetails();
						}
						else {
							jp.add(new JLabel("The file isn't good ! "));
							jp.repaint();
							revalidate();
						}
					
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		add( bottomPanel, BorderLayout.SOUTH  );
		add( getFile	, BorderLayout.NORTH  );
		add( jp			, BorderLayout.CENTER );
		setLocationRelativeTo(null);
		setLocation(10, 10);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}

	void drawField() throws IOException {

		Map<String, GenericNode<String, Object>> nodes = gm.getNodes();
		int lengthMax = gm.getLength();
		int iMax = gm.getLineLength();
		
		for(int j = 0 ; j < lengthMax / iMax ; j++) {
			for(int i = 0; i < iMax; i++) {
				Coordinates co = new Coordinates(i, j);
				gbc.gridy = j;
				gbc.gridx = i;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				String img = "wall";
				for(int k = 0; k < gm.getGates().size(); k++) {
					Coordinates c = gm.getGates().get(k);
					if(c.toString().equals(co.toString())) {
						img = "depart";
					}
				}
				GenericNode<String, Object> node = nodes.get(co.toString());
				if ( node != null ) {
					img = node.getType();
				}
				BufferedImage myPicture = ImageIO.read(new File("images/" + img + ".png"));
				JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				jp.add(picLabel, gbc);
				components.put(co.toString(), picLabel);
			}
		}
		jp.repaint();
		revalidate();
	}

	private void getDetails() {
		for ( int i = 0; i < gm.getGraph().getDepartures().size(); i++ ) {
			JLabel nbMousesByGateLabel = new JLabel( "Porte" + (i + 1) );
			bottomPanel.add(nbMousesByGateLabel);
			JTextField nbMousesByGate = new JTextField( "Porte" + (i + 1) );
			bottomPanel.add(nbMousesByGate);
		}
		
		lancer.addActionListener(this);
		
		bottomPanel.add(lancer);
		bottomPanel.repaint();
		revalidate();
	}
	
	public void refresh(MoveEventData med) {
		if( med.getNodeLeaved() != null ) {
			if(med.getNewNode() == null) {
				System.out.println("la");
			}
			refreshNode(med.getNodeLeaved());
		}
		if( med.getNewNode() != null )
			refreshNode(med.getNewNode());
	}
	
	public void refreshNode(INode<?, ?> node) {
		Coordinates coo = node.getCoordinates();
		gbc.gridx = coo.getX();
		gbc.gridy = coo.getY();
		gbc.gridheight = 1;
		gbc.gridwidth = 1;

		String img = "wall";
		if ( node != null ) {
			if( node.isUsed() ) {
				img = "mouse";
			} else {
				img = node.getType();
			}
		}
		
		try {
			BufferedImage myPicture = ImageIO.read(new File("images/" + img + ".png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			jp.remove(components.get(coo.toString()));
			components.remove(coo.toString());
			jp.add(picLabel, gbc);
			components.put(coo.toString(), picLabel);
			jp.repaint(gbc.gridx, gbc.gridy, gbc.gridx + gbc.gridwidth, gbc.gridy + gbc.gridheight);
			jp.revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshAllNode() {
		jp.removeAll();
		jp.setLayout(gbl);
		for (Map.Entry< String, GenericNode<String, Object> > node : gm.getNodes().entrySet()) {
			Coordinates coo = node.getValue().getCoordinates();
			gbc.gridx = coo.getX();
			gbc.gridy = coo.getY();
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			
			String img = "wall";
			if ( node != null ) {
				if( node.getValue().isUsed() ) {
					img = "mouse";
				} else {
					img = node.getValue().getType();
				}
			}
			
			try {
				BufferedImage myPicture = ImageIO.read(new File("images/" + img + ".png"));
				JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				jp.remove(components.get(coo.toString()));
				components.remove(coo.toString());
				jp.add(picLabel, gbc);
				components.put(coo.toString(), picLabel);
				jp.repaint(gbc.gridx, gbc.gridy, gbc.gridx + gbc.gridwidth, gbc.gridy + gbc.gridheight);
				jp.revalidate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static GUI getGUI(MainListener listener) {
		if(gui == null) {
			gui = new GUI(listener);
			listener.setGui(gui);
		}
		return gui;
	}
	
	public static GUI getGUI() {
		return gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(lancer) ) {
			fireLlaunch();
		}
	}
	
	private void fireLlaunch() {
		MainListener[] listener = this.listeners.getListeners(MainListener.class);
		listener[0].launchMouses();
	}

	private boolean fireNewGraph(File file) {
		MainListener[] listener = this.listeners.getListeners(MainListener.class);
		listener[0].newGraph(file);
		synchronized(gui) {
			try {
				gui.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return CheeseMain.isGraphValid();
	}

	// composant graphique
	private int height;
	private int width;
	private Dimension dimension;
	private File file;
	private GraphMaker gm;
	private volatile static GUI gui;
	private EventListenerList listeners = new EventListenerList();
	

	private JPanel jp 		 	   = new JPanel();
	private JPanel bottomPanel 	   = new JPanel();
	private BorderLayout bl 	   = new BorderLayout();
	private GridBagLayout gbl	   = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private JButton lancer = new JButton("Lachez les souris !!");
	private Map<String, JComponent> components = new HashMap<>();
	
}
