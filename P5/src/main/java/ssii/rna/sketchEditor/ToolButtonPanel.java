package es.ucm.sketchEditor;


/*=====================*\
|*   Bpaint            *|
|*---------------------*|
|* Author -            *|
|Barun Chakrabarty     *|
|NIT Calicut           *|
\*=====================*/
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ToolButtonPanel extends JPanel
{
	private JButton lineBtn, squareBtn, ovalBtn, polygonBtn, roundRectBtn, freeHandBtn, undoBtn, redoBtn, clearBtn,copyBtn,translateBtn,rotateBtn;		
	
	private JCheckBox fullChk;
	private CanvasPanel canvasPanel;
	public int buttonEnum=-1;
	
	
	public ToolButtonPanel(CanvasPanel inCanvasPanel)
	{
		canvasPanel = inCanvasPanel;
/*----------------------------------------------------------------------------*/		 
		lineBtn			= new JButton("",new ImageIcon(getClass().getResource("/lineBtn.gif")));
		squareBtn		= new JButton("",new ImageIcon(getClass().getResource("/squareBtn.gif")));
		ovalBtn	 		= new JButton("",new ImageIcon(getClass().getResource("/ovalBtn.gif")));
		polygonBtn		= new JButton("",new ImageIcon(getClass().getResource("/polyGonBtn.gif")));
		roundRectBtn	= new JButton("",new ImageIcon(getClass().getResource("/roundRectBtn.gif")));
		freeHandBtn		= new JButton("",new ImageIcon(getClass().getResource("/freeHandBtn.gif")));
		undoBtn			= new JButton("",new ImageIcon(getClass().getResource("/undoBtn.gif")));
		redoBtn			= new JButton("",new ImageIcon(getClass().getResource("/redoBtn.gif")));
		clearBtn		= new JButton("",new ImageIcon(getClass().getResource("/clearBtn.gif")));
		copyBtn		        = new JButton("",new ImageIcon(getClass().getResource("/duplicateBtn.gif")));
		translateBtn	        = new JButton("",new ImageIcon(getClass().getResource("/translateBtn.gif")));
		rotateBtn               = new JButton("",new ImageIcon(getClass().getResource("/rotateBtn.gif")));
		
		lineBtn.addActionListener(new ToolButtonListener());
		lineBtn.setToolTipText("Line");
		squareBtn.addActionListener(new ToolButtonListener());
		squareBtn.setToolTipText("Retangle");
		ovalBtn.addActionListener(new ToolButtonListener());
		ovalBtn.setToolTipText("Oval");
		polygonBtn.addActionListener(new ToolButtonListener());
		polygonBtn.setToolTipText("Polygon");
		roundRectBtn.addActionListener(new ToolButtonListener());
		roundRectBtn.setToolTipText("Rectangle");
		freeHandBtn.addActionListener(new ToolButtonListener());
		freeHandBtn.setToolTipText("3-D");
		undoBtn.addActionListener(new ToolButtonListener());
		undoBtn.setToolTipText("Undo");
		redoBtn.addActionListener(new ToolButtonListener());
		redoBtn.setToolTipText("Redo");
		clearBtn.addActionListener(new ToolButtonListener());
		clearBtn.setToolTipText("Clear Canvas");
		copyBtn.addActionListener(new ToolButtonListener());
		copyBtn.setToolTipText("Replicate");
		translateBtn.addActionListener(new ToolButtonListener());
		translateBtn.setToolTipText("Move");
		rotateBtn.addActionListener(new ToolButtonListener());
		rotateBtn.setToolTipText("Rotate (Only for 3-D mode)");

		/*----------------------------------------------------------------------------*/		
		fullChk = new JCheckBox("Fill");
		fullChk.addItemListener(
			new ItemListener()
			{
				public void itemStateChanged(ItemEvent event)
				{
					if(fullChk.isSelected())
						canvasPanel.setSolidMode(Boolean.TRUE);
					else
						canvasPanel.setSolidMode(Boolean.FALSE);
				}	
			}
		);
/*----------------------------------------------------------------------------*/		
		this.setLayout(new GridLayout(1,14)); // 11 Buttons & 1 CheckBox
		this.add(lineBtn);
		this.add(squareBtn);
		this.add(ovalBtn);
		this.add(polygonBtn);
		this.add(roundRectBtn);
		this.add(freeHandBtn);
		this.add(undoBtn);
		this.add(redoBtn);
		this.add(clearBtn);
		this.add(copyBtn);
		this.add(translateBtn);
		this.add(rotateBtn);
		this.add(fullChk);
		
	}
/*----------------------------------------------------------------------------*/
	private class ToolButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{	
			if(canvasPanel.isExistPolygonBuffer()!= false)
			{
				canvasPanel.flushPolygonBuffer();
			}
			if(event.getSource() == lineBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.setDrawMode(canvasPanel.LINE);
				buttonEnum=1;
				
			}
			if(event.getSource() == squareBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.setDrawMode(canvasPanel.SQUARE);
				buttonEnum=2;
				
			}
			if(event.getSource() == ovalBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.setDrawMode(canvasPanel.OVAL);
				buttonEnum=3;
			}
			if(event.getSource() == polygonBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.setDrawMode(canvasPanel.POLYGON);
				buttonEnum=4;
			}
			if(event.getSource() == roundRectBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.setDrawMode(canvasPanel.ROUND_RECT);
				buttonEnum=5;
			}
			if(event.getSource() == freeHandBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.setDrawMode(canvasPanel.FREE_HAND);
				buttonEnum=6;
			}
			if(event.getSource() == undoBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.undo();
				buttonEnum=7;
			}
			if(event.getSource() == redoBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.redo();
				buttonEnum=8;
			}
			if(event.getSource() == clearBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.clearCanvas();
				buttonEnum=9;
			}
			if(event.getSource() == copyBtn)
			{
				canvasPanel.translatemode=false;
				canvasPanel.rotatemode=false;
				canvasPanel.duplicate(20,20);
				buttonEnum=10;
			}
			if(event.getSource() == translateBtn)
			{
				canvasPanel.translatemode=true;
				canvasPanel.rotatemode=false;
				buttonEnum=11;
				
			}
			if(event.getSource() == rotateBtn)
			{
				
				canvasPanel.rotatemode=true;
				canvasPanel.translatemode=false;
				canvasPanel.setAxes();
				buttonEnum=12;
				
				
			}
			
		}
	}
}
