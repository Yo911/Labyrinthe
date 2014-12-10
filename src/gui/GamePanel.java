package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.dataStructure.graph.Coordinates;
import core.dataStructure.graph.GenericNode;
import core.dataStructure.graph.interfaces.INode;
import core.graphMaker.GraphMaker;
import core.play.MoveEventData;

public class GamePanel extends JPanel{

	private static final long serialVersionUID = -8860204251354754377L;
	private Map<String, JComponent> components = new HashMap<>();
	private GridBagConstraints gbc = new GridBagConstraints();
	GridBagLayout gbl = new GridBagLayout();
	private GraphMaker graphMaker;

	public void drawMap(GraphMaker graphMaker) throws IOException {
		Map<String, GenericNode<String, Object>> nodes = graphMaker.getNodes();
		int lengthMax = graphMaker.getLength();
		int iMax = graphMaker.getLineLength();
		
		for(int j = 0 ; j < lengthMax / iMax ; j++) {
			for(int i = 0; i < iMax; i++) {
				Coordinates co = new Coordinates(i, j);
				gbc.gridy = j;
				gbc.gridx = i;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				String img = "wall";
				for(int k = 0; k < graphMaker.getGates().size(); k++) {
					Coordinates c = graphMaker.getGates().get(k);
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
				add(picLabel, gbc);
				components.put(co.toString(), picLabel);
			}
		}
		repaint();
		setGraphMaker(graphMaker);
	}
	
	private void setGraphMaker(GraphMaker graphMaker) {
		this.graphMaker = graphMaker;
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
			remove(components.get(coo.toString()));
			components.remove(coo.toString());
			add(picLabel, gbc);
			components.put(coo.toString(), picLabel);
			repaint(gbc.gridx, gbc.gridy, gbc.gridx + gbc.gridwidth, gbc.gridy + gbc.gridheight);
			revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshAllNode() {
		removeAll();
		setLayout(gbl);
		for (Map.Entry< String, GenericNode<String, Object> > node : graphMaker.getNodes().entrySet()) {
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
				remove(components.get(coo.toString()));
				components.remove(coo.toString());
				add(picLabel, gbc);
				components.put(coo.toString(), picLabel);
				repaint(gbc.gridx, gbc.gridy, gbc.gridx + gbc.gridwidth, gbc.gridy + gbc.gridheight);
				revalidate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}