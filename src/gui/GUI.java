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
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.dataStructure.graph.Coordinates;
import core.dataStructure.graph.Gate;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.interfaces.INode;
import core.dataStructure.roundRobin.IRoundRobin;
import core.dataStructure.roundRobin.RoundRobinFIFO;
import core.dataStructure.roundRobin.exceptions.RoundRobinEmptyException;
import core.graphMaker.GraphMaker;
import core.play.CheeseMain;
import core.play.CheeseSettings;
import core.play.IMouse;
import core.play.MoveEventData;

public class GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private GUI() {
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
						
						boolean graphIsWellFormed = CheeseMain.makeGraph(file);
						
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
		JPanel north = new JPanel();
		north.setLayout(bl2);
		north.add( getFile, 	BorderLayout.CENTER );
		add( 	   bottomPanel, BorderLayout.SOUTH  );
		
		add(	   north, 		BorderLayout.NORTH  );
		add( 	   jp,      	BorderLayout.CENTER );
		setLocationRelativeTo(null);
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
		
//		lancer.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				CheeseMain.letsGo();
//			}
//			
//		});
		lancer.addActionListener(this);
		
		bottomPanel.add(lancer);
		bottomPanel.repaint();
		revalidate();
	}
	
	public void refresh(MoveEventData med) {
		if( med.getNodeLeaved() != null )
			refreshNode(med.getNodeLeaved());
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

	private synchronized void letsGo() {
		IRoundRobin<IMouse<String,Object>> rr = new RoundRobinFIFO<>();
		
		Set<Gate<String, Object>> departures = CheeseSettings.getGraph().getDepartures();

		
		int j = 0;
		for(Gate<String, Object> g : departures) {
			g.setMouseNumber(CheeseSettings.getMouseNumberForGate(j));
			j++;
		}

		int i = 0;
		IMouse<String,Object> m = null;
		try {
			do {
				for(Gate<String, Object> gate : departures) {
					System.out.println("in for : gate = " + gate);
					rr.add(gate.getNewMouses());
				}
				System.out.println("size = " + rr.size());

				
				if(rr.size() != 0) {
					
					i++;
					m = rr.next();
					refreshNode(m.getLocation());
					Thread.sleep(1000);
					if(m.doSomething()) {
						System.out.println("Mouse " + m.hashCode() + " location: " + m.getLocation());
						rr.remove();
					}
					else {
						System.out.println("Mouse " + m.hashCode() + " location: " + m.getLocation());
					}
					
//					Thread.sleep(1);
				}
			} while(rr.size() != 0) ;
		} catch (RoundRobinEmptyException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(i);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(lancer) ) {
			start = true;
			letsGo();
		}
	}
	
	public static GUI getGUI() {
		if (gui == null)
			gui = new GUI();
		return gui;
	}

	private File file;
	private GraphMaker gm;
	private static GUI gui;
	
	public boolean start = false;

	// composant graphique
	private int height;
	private int width;
	private Dimension dimension;
	private JPanel jp 		 	   = new JPanel();
	private JPanel bottomPanel 	   = new JPanel();
	private BorderLayout bl 	   = new BorderLayout();
	private BorderLayout bl2 	   = new BorderLayout();
	private GridBagLayout gbl	   = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private JButton lancer 		   = new JButton("Lachez les souris !!");
	private Map<String, JComponent> components = new HashMap<>();
	
}
