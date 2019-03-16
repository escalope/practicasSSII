package es.ucm.sketchEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;


import javax.swing.*;

import ingenias.jade.components.SketchPainterAppImp;

public class SketchEditor extends JFrame 
{
	private static final long serialVersionUID = 8104671336069323765L;

	private CanvasPanel 		canvasPanel;
	private ToolButtonPanel   	toolButtonPanel;
	private ColorButtonPanel	colorButtonPanel;
	public	JLabel             	statusBar;
	
	private Container 			mainContainer;
	
	JMenuBar mainBar;
	JMenu fileMenu, editMenu, setColorMenuItem,  aboutMenu;
	JMenuItem newMenuItem, openMenuItem, closeMenuItem, saveMenuItem, 
saveAsMenuItem, exitMenuItem, undoMenuItem, redoMenuItem, 
foreGroundMenuItem, backGroundMenuItem, helpMenuItem;
	
	private NotifyResultSketch notifyTo;
	
	public SketchEditor(String header, NotifyResultSketch notifyTo)
	{
		super(header);
		
		this.notifyTo=notifyTo;
		
		mainBar = new JMenuBar();
		setJMenuBar(mainBar);	
		fileMenu  		= new JMenu("File");
		fileMenu.setMnemonic('F');
		
		newMenuItem		= new JMenuItem("New");
		openMenuItem 	= new JMenuItem("Open");
		closeMenuItem 	= new JMenuItem("Close"); 
		saveMenuItem 	= new JMenuItem("Save");
		saveAsMenuItem 	= new JMenuItem("Save As");
		exitMenuItem	= new JMenuItem("Exit");
		 
		
		newMenuItem.addActionListener(new MenuButtonListener());
		openMenuItem.addActionListener(new MenuButtonListener());
		saveMenuItem.addActionListener(new MenuButtonListener());
		saveAsMenuItem.addActionListener(new MenuButtonListener());
		closeMenuItem.addActionListener(new MenuButtonListener());
		exitMenuItem.addActionListener(new MenuButtonListener());
		
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(closeMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);		
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic('E');
		
		undoMenuItem	   = new JMenuItem("Undo");
		redoMenuItem 	   = new JMenuItem("Redo");
		
		setColorMenuItem   = new JMenu("Set Color");
		foreGroundMenuItem = new JMenuItem("Set ForeGround");
		backGroundMenuItem = new JMenuItem("Set BackGround");
		
		undoMenuItem.addActionListener(new MenuButtonListener());
		redoMenuItem.addActionListener(new MenuButtonListener());
		foreGroundMenuItem.addActionListener(new MenuButtonListener());
		backGroundMenuItem.addActionListener(new MenuButtonListener());
		
		setColorMenuItem.add(foreGroundMenuItem);
		setColorMenuItem.add(backGroundMenuItem);
		
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.addSeparator();
		editMenu.add(setColorMenuItem);
		
		aboutMenu	= new JMenu("Help");
		aboutMenu.setMnemonic('A');
		
		helpMenuItem = new JMenuItem("Help");
		helpMenuItem.addActionListener(new MenuButtonListener());
		
		aboutMenu.add(helpMenuItem);			
		mainBar.add(fileMenu);
		mainBar.add(editMenu);
		mainBar.add(aboutMenu);

		canvasPanel 	  = new CanvasPanel();
		toolButtonPanel   = new ToolButtonPanel(canvasPanel);
		colorButtonPanel  = new ColorButtonPanel(canvasPanel);
//		statusBar       = new JLabel("Sketch your idea and send it!");
		
		mainContainer = getContentPane();
		mainContainer.add(toolButtonPanel,BorderLayout.NORTH);
		mainContainer.add(canvasPanel,BorderLayout.CENTER);
//		mainContainer.add(colorButtonPanel,BorderLayout.WEST);
//		mainContainer.add(statusBar,BorderLayout.SOUTH);
		
		setSize(700,500);
		this.setResizable(true);
		setFocusable(true);
		canvasPanel.setFocusable(true);
		setVisible(true);
		
		
/*----------------------------------------------------------------------------*/
		
 	
/*----------------------------------------------------------------------------*/
		addWindowListener (
      		new WindowAdapter () 
      		{
      			public void windowClosing (WindowEvent e) 
      			{
      				endSketchEdit();
      			}
      			public void windowDeiconified (WindowEvent e) 
      			{
      				canvasPanel.repaint();
      			}
      			public void windowActivated (WindowEvent e) 
      			{	 
      				canvasPanel.repaint();
      			}
      		}
      	);
		
	}
	
	public SketchEditor(String header, File fichero) {		
		canvasPanel.openFile(fichero);
		this.setTitle(header+": Sketch Editor");
		
	}
	
	public SketchEditor(String header,Vector content, NotifyResultSketch nrs) {
		this(header,nrs);			
		canvasPanel.initiate(content);	
		this.setTitle(header+": Sketch Editor");
		
	}
	
	public SketchEditor(String header, byte[] content, NotifyResultSketch nrs) {
		this(header,nrs);		
		Vector v;
		try {
			ObjectInputStream oos=new ObjectInputStream(new ByteArrayInputStream(content));
			v = (Vector)oos.readObject();
			canvasPanel.initiate(v);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		this.setTitle(header+": Sketch Editor");
		
	}



/*----------------------------------------------------------------------------*/


	protected void endSketchEdit() {
	
		this.notifyTo.sketchIsReady(canvasPanel.SaveCanvasToByteArray());
		this.dispose();	
	}
/*----------------------------------------------------------------------------*/
	public class MenuButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == newMenuItem || event.getSource() == closeMenuItem)
			{
				canvasPanel.clearCanvas();
				canvasPanel.setDrawMode(0);
				canvasPanel.setForeGroundColor(CanvasPanel.DEFAULT_FOREGROUND);
				canvasPanel.setBackGroundColor(CanvasPanel.DEFAULT_BACKGROUND);
				canvasPanel.repaint();
			}
			if(event.getSource() == exitMenuItem)
			{
				endSketchEdit();
			}
			if(event.getSource() == foreGroundMenuItem)
			{
				colorButtonPanel.setForeGroundColor();
				canvasPanel.repaint();
			}
			if(event.getSource() == backGroundMenuItem)
			{
				colorButtonPanel.setBackGroundColor();
				canvasPanel.repaint();
			}
			if(event.getSource() == helpMenuItem)
			{
				JOptionPane.showMessageDialog(SketchEditor.this,"HELP: Draw a sktech of your idea. You can save it. When satisfied, send it.","Sketch editor",JOptionPane.INFORMATION_MESSAGE);
				canvasPanel.repaint();
			}
			if(event.getSource() == saveMenuItem)
			{
				canvasPanel.SaveCanvasToFile();
			}
			if(event.getSource() == saveAsMenuItem)
			{
				canvasPanel.SaveAsCanvasToFile();
			}
			if(event.getSource() == openMenuItem)
			{
				canvasPanel.OpenCanvasFile();
			}
			if(event.getSource() == undoMenuItem)
			{
				canvasPanel.undo();
			}
			if(event.getSource() == redoMenuItem)
			{
				canvasPanel.redo();
			}
		}
	}
/*----------------------------------------------------------------------------*/
       
/*----------------------------------------------------------------------------*/
	public static void main(String args[])
	{
		SketchEditor editor = new SketchEditor("test",new NotifyResultSketch() {
			@Override
			public void sketchIsReady(byte[] sketch) {
				// TODO Auto-generated method stub
				
			}
		});
		editor.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		editor.setLocationRelativeTo(null);

		editor.setVisible(true);
	
	}

}

