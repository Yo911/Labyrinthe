package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		setSize(width, height);
		setTitle("PITITE SOURIS !! ");

		BorderLayout bl = new BorderLayout();
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

					try {
						String line;
						InputStream ips = new FileInputStream(file);
						InputStreamReader ipsr=new InputStreamReader(ips);
						BufferedReader reader = new BufferedReader(ipsr);
						int i = 0;
						while ((line = reader.readLine()) != null) {
							System.out.println(line);
							gbc.gridx = 0;
							gbc.gridy = i;
							gbc.gridheight = 1;
							gbc.gridwidth = 2;
							jp.add(new JLabel(line), gbc);
							i++;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		add( getFile, BorderLayout.NORTH );
		add( jp );
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
	private int i;
	
}
