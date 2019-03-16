package ssii.rna;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

/**
 * A basic GUI for painting. It allows to clear the canvas, it has a rubber to 
 * delete pixels,  it can save the image to as a png, and it can be configured
 * to give the image as an array of 1s and 0s for pattern recognition.
 *  
 * Distributed under GPLv3
 * @author Jorge J. Gómez Sanz
 *
 */
public class FreeHandCanvas {

	JPanel canvas=new JPanel();
	JButton recognise=new JButton("Recognise");
	JLabel output=new JLabel("Result");
	private JFrame window;
	final int RADIUS=5; // radius of the oval used to paint

	Vector<Point> points=new Vector<Point>();
	protected int moderubber;

	public FreeHandCanvas() {
		window=new JFrame("Drawing") ;
		window.getContentPane().setLayout(new BoxLayout(window.getContentPane(),BoxLayout.Y_AXIS));
		
		JPanel southPanel=new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel,BoxLayout.Y_AXIS));
		
		canvas=new JPanel(){
			public void paint(Graphics g) {
				super.paint(g);
				paintElements(g);
			}

		};
		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if (moderubber==0)
					points.add(e.getPoint());
				if (moderubber==1) {				

					Vector<Point> toRemove=new Vector<Point>();
					for (Point p:points)
						if (p.distance(e.getPoint())<RADIUS){
							toRemove.add(p);
						}					
					points.removeAll(toRemove);
				}

				window.repaint();
			}
		});

		canvas.setBackground(Color.WHITE);		
		
		
		
		
		// panel to save images
		JPanel savePanel=new JPanel();
		GridBagLayout gbl=new GridBagLayout();
		savePanel.setLayout(gbl);
		JButton save=new JButton("Save");
		final JTextField filenameField=new JTextField("images/",20);
		final JLabel saveresult=new JLabel("Result of saving");
		GridBagConstraints gbc=new GridBagConstraints();
		
		//gbc.weightx=1;
		//gbc.weighty=1;
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 2, 2, 2);

		gbc.gridx=0;
		gbc.gridy=0;
		
		gbl.setConstraints(filenameField, gbc);
		savePanel.add(filenameField);
		
		gbc.gridx=1;
		gbc.gridy=0;
		
		gbl.setConstraints(save, gbc);
		savePanel.add(save);
		gbc.gridwidth=2;
		gbc.gridheight=1;		
		gbc.gridx=0;
		gbc.gridy=1;		
		
		gbl.setConstraints(saveresult, gbc);
		savePanel.add(saveresult);
		
		
		save.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveImageToFile(filenameField, saveresult);				
			}
		});
		
		
		
		// panel to perform recognition of shapes
		JPanel recognisePanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		recognisePanel.add(recognise);
		recognisePanel.add(output);
		
		
		// panel with draing tools
		JPanel paintTools=new JPanel();
		JButton clear=new JButton("clear");
		
		clear.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				points.clear();
				window.repaint();
			}
		});

		final JButton rubber=new JButton("Rubber off");
		rubber.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				moderubber=(moderubber+1)%2;
				if (moderubber==1)
					rubber.setText("Rubber on");
				if (moderubber==0)
					rubber.setText("Rubber off");

			}
		});
		paintTools.add(clear);
		paintTools.add(rubber);

		
		// connecting all panels
		southPanel.add(paintTools);
		southPanel.add(recognisePanel);		
		southPanel.add(savePanel);
		
		// to add a border without affecting the painting area
		JPanel externalCanvas=new JPanel(new FlowLayout());
		externalCanvas.add(canvas);
		externalCanvas.setBorder(new LineBorder(Color.BLACK,3));
		
		window.getContentPane().add(externalCanvas);
		window.getContentPane().add(southPanel);


		canvas.setMinimumSize(new Dimension(150,150));
		canvas.setMaximumSize(new Dimension(150,150));
		canvas.setPreferredSize(new Dimension(150,150));

		window.pack();
		savePanel.invalidate();
		window.setResizable(false);
	}

	public JButton getButton() {
		return recognise;
	}

	public void setResult(String result) {
		this.output.setText(result);
		window.repaint();
	}

	protected void paintElements(Graphics g) {
		g.setColor(Color.BLACK);
		for (Point p:points) {			
			g.fillOval(p.x-1, p.y-1, 2, 2);
		}		
	}

	/**
	 * Convert the picture to a linear array of 1 and 0 representing image pixels
	 * 1 means the pixel was activated
	 * 0 means the pixel was not activated
	 * @return
	 */
	public double[] getImage(){

		// try and catch block to handle exceptions 
		double[] data=new double[150*150];

		// create an object of robot class 
		BufferedImage bi=new BufferedImage(150,150,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d=bi.createGraphics();
		canvas.paint(g2d);
		g2d.dispose();

		// get the pixel color
		for (int x=0;x<150;x++) {
			for (int y=0;y<150;y++) {
				int c = bi.getRGB(x, y);
				if (c!=-16777216) // RGB code for white
					data[y*150+x]=0.0;
				else
					data[y*150+x]=1.0;
			}
		}

		return data;
	}

	/**
	 * Makes the GUI visible
	 */
	public void show() {
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	/**
	 * It stores the current canvas as a png file
	 * @param filenameField
	 * @param saveresult
	 */
	private void saveImageToFile(final JTextField filenameField, final JLabel saveresult) {
		BufferedImage bi=new BufferedImage(150,150,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d=bi.createGraphics();
		canvas.paint(g2d);
		g2d.dispose();
		saveresult.setText("saving");
		try {
			String filename=filenameField.getText();

			if (!filename.toLowerCase().endsWith(".png"))
				filename=filename+".png";
			File f=new File(filename);
			if (new File(f.getParent()).exists()) {
				ImageIO.write(bi, "png",f);
				saveresult.setText("done");
			}else						
				saveresult.setText("path does not exist");
		} catch (IOException e1) {
			e1.printStackTrace();
			saveresult.setText("failure, see console");
		}
	}

	/**
	 * Sample main to use the GUI. It can be used to generate the 
	 * images for training 
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		final FreeHandCanvas fhc=new FreeHandCanvas();
		fhc.getButton().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(Utils.getStringImageFromArray(fhc.getImage(),150));
			}
		});		
		
	 fhc.show();
	}

}
