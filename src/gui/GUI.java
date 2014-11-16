package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
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

import core.dataStructure.graph.Coordonne;
import core.dataStructure.graph.GenericNode;
import core.graphMaker.GraphMaker;

public class GUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GUI() {
		// TODO Auto-generated constructor stub
		Listen l = new Listen();
		addWindowListener(l);
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int) dimension.getHeight() / 2;
		width = (int) dimension.getWidth() / 2;
		height = 700;
		width = 1500;
		setPreferredSize(new Dimension(width, height));// changed it to preferredSize, Thanks!
		setLocation(1, 1);
		setTitle("PITITE SOURIS !! ");

		BorderLayout bl = new BorderLayout();
		BorderLayout bl2 = new BorderLayout();
		GridBagLayout gbl = new GridBagLayout();
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		

		final JPanel jp = new JPanel();

		setLayout(bl);
		jp.setLayout(gbl);
		JButton getFile = new JButton("choose your file");
		
		getFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				// création de la boîte de dialogue
		        JFileChooser fileChooser = new JFileChooser();
		        
		        // affichage
		        fileChooser.showOpenDialog(null);
		         
		        // récupération du fichier sélectionné
		        file = fileChooser.getSelectedFile();
		        System.out.println("Fichier choisi : " + fileChooser.getSelectedFile());
				if(file != null) {
					jp.removeAll();
					GraphMaker gm;
					try {
						gm = new GraphMaker(file);
						if( gm.isWellFormed() ) {
							Map<String, GenericNode<String, Object>> nodes = gm.getNodes();
							int lengthMax = gm.getLength();
							int iMax = gm.getLineLength();
							for(int j = 0 ; j < lengthMax / iMax ; j++) {
								for(int i = 0; i < iMax; i++) {
									Coordonne co = new Coordonne(i, j);
									gbc.gridy = j;
									gbc.gridx = i;
									gbc.gridheight = 1;
									gbc.gridwidth = 1;
	//								jp.add(new JLabel(c + ""), gbc);
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
						} else {
							jp.add(new Label("The file isn't good ! "));
							jp.repaint();
							revalidate();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		JPanel north = new JPanel();
		north.setLayout(bl2);
		north.add(getFile, BorderLayout.CENTER);
		add( north, BorderLayout.NORTH  );
		add( jp,      BorderLayout.CENTER );
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	// composant graphique
	private int height;
	private int width;
	private Dimension dimension;
	private File file;
	
}
