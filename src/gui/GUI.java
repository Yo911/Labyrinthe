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
import core.graphMaker.GraphMaker;

public class GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	public GUI() {
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
		        System.out.println("Fichier choisi : " + fileChooser.getSelectedFile());
				if(file != null) {
					

//					GraphMaker gm = null;
//					try {
//						gm = new GraphMaker(file);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			//
//					IGraph<String, Object> graph = gm.getGraph();
					System.out.println("graph made in yehouda :D");
					//return;
					
					jp.removeAll();
					GraphMaker gm;
					try {
						gm = new GraphMaker(file);
						if( drawField(gm) ) {
							
							getDetails(gm);
						} else {
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

	boolean drawField(GraphMaker gm) throws IOException {
		if ( !gm.isWellFormed() )
			return false;
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
				String img = "wall.png";
				GenericNode<String, Object> node = nodes.get(co.toString());
				if ( node != null ) {
					img = node.getType() + ".png";
				}
				BufferedImage myPicture = ImageIO.read(new File("images/" + img));
				JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				jp.add(picLabel, gbc);
				jp.repaint();
				revalidate();
			}
		}
		return true;
	}
	
	private void getDetails(GraphMaker gm) {
		for ( int i = 0; i < gm.getGraph().getDepart().size(); i++ ) {
			JLabel nbMousesByGateLabel = new JLabel( "Porte" + (i + 1) );
			bottomPanel.add(nbMousesByGateLabel);
			JTextField nbMousesByGate = new JTextField( "Porte" + (i + 1) );
			bottomPanel.add(nbMousesByGate);
		}
		JButton lancer = new JButton("Lachez les souris !!");
		
		lancer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		bottomPanel.add(lancer);
		bottomPanel.repaint();
		revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

	// composant graphique
	private int height;
	private int width;
	private Dimension dimension;
	private File file;
	

	JPanel jp 		 	   = new JPanel();
	JPanel bottomPanel 	   = new JPanel();
	BorderLayout bl 	   = new BorderLayout();
	BorderLayout bl2 	   = new BorderLayout();
	GridBagLayout gbl	   = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
}
