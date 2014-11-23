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
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.dataStructure.graph.Coordinates;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.interfaces.IGraph;
import core.dataStructure.graph.interfaces.INode;
import core.graphMaker.GraphMaker;
import core.play.CheeseMain;
import core.play.CheeseSettings;
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
				
				// création de la boîte de dialogue
		        JFileChooser fileChooser = new JFileChooser();
		        
		        // affichage
		        fileChooser.showOpenDialog(null);
				
		        // récupération du fichier sélectionné
		        file = fileChooser.getSelectedFile();
				if(file != null) {
					jp.removeAll();
					try {
						
						boolean graphIsWellFormed = CheeseMain.makeGraph(file);
						
						gm = CheeseSettings.getGraphMaker();
						graph = CheeseSettings.getGraph();
						
						if(graphIsWellFormed) {
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

	boolean drawField() throws IOException {

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
					System.out.println(c + "  |||  " + co);
					if(c.toString().equals(co.toString())) {
						System.out.println("here");
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
				jp.repaint();
				revalidate();
			}
		}
		return true;
	}

	private void getDetails() {
		for ( int i = 0; i < gm.getGraph().getDepartures().size(); i++ ) {
			JLabel nbMousesByGateLabel = new JLabel( "Porte" + (i + 1) );
			bottomPanel.add(nbMousesByGateLabel);
			JTextField nbMousesByGate = new JTextField( "Porte" + (i + 1) );
			bottomPanel.add(nbMousesByGate);
		}
		JButton lancer = new JButton("Lachez les souris !!");
		
		lancer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				CheeseMain.letsGo();
			}
			
		});
		
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
		if( node.isUsed() ) {
			img = "mouse";
		} else {
			if ( node != null ) {
				img = node.getType();
			}
		}
		if (graph.getDepartures().contains(coo))  
			img = "depart";
		try {
			BufferedImage myPicture = ImageIO.read(new File("images/" + img + ".png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			jp.add(picLabel, gbc);
			jp.repaint();
			revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	
	public static GUI getGUI() {
		if (gui == null)
			gui = new GUI();
		return gui;
	}

	// composant graphique
	private int height;
	private int width;
	private Dimension dimension;
	private File file;
	private IGraph<String, Object> graph;
	private GraphMaker gm;
	private static GUI gui;
	

	JPanel jp 		 	   = new JPanel();
	JPanel bottomPanel 	   = new JPanel();
	BorderLayout bl 	   = new BorderLayout();
	BorderLayout bl2 	   = new BorderLayout();
	GridBagLayout gbl	   = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
}
