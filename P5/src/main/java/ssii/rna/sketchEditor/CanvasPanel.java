package es.ucm.sketchEditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.KeyAdapter;

public class CanvasPanel extends JPanel implements 
MouseListener,MouseMotionListener,Serializable,MouseWheelListener
{
	protected final static int 
	LINE=1,SQUARE=2,OVAL=3,POLYGON=4,ROUND_RECT=5,FREE_HAND=6,
	SOLID_SQUARE=22, SOLID_OVAL=33, SOLID_POLYGON=44,
	SOLID_ROUND_RECT=55;

	protected final static double cm=33.467;

	public boolean translatemode=false,rotatemode=false;

	protected  Vector 
	vLine,vSquare,vOval,vPolygon,vRoundRect,vFreeHand,
	vSolidSquare,vSolidOval,vSolidPolygon,vSolidRoundRect,vFile,
	xPolygon, yPolygon;						 	

	private Stack undoStack, redoStack;

	private Color foreGroundColor, backGroundColor; 

	private int x1,y1,x2,y2,linex1,linex2,liney1,liney2, drawMode=0,xcrd,ycrd,wheelrot,zcrd=0,z1,z2,y3,y4=0,x4=0;

	private float xAxis,yAxis,zAxis;
	private String ipkey;
	private boolean solidMode, polygonBuffer,rtClick=false;

	private File fileName;

	final static Color DEFAULT_FOREGROUND = Color.BLACK;
	final static Color DEFAULT_BACKGROUND = Color.WHITE;


	public CanvasPanel()
	{
		vLine 			= new Vector();
		vSquare 		= new Vector();
		vOval			= new Vector();
		vPolygon		= new Vector();
		vRoundRect		= new Vector();
		vFreeHand		= new Vector();
		vSolidSquare	= new Vector();
		vSolidOval		= new Vector();
		vSolidPolygon	= new Vector();
		vSolidRoundRect	= new Vector();
		vFile			= new Vector();
		xPolygon		= new Vector();
		yPolygon		= new Vector();

		addMouseListener(this);
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar()=='a') {
					selected=0;

				} else
					if (e.getKeyChar()=='z') {
						selected=selected+1;
					}


			}

		});
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		solidMode = false;
		polygonBuffer 	= false; 

		foreGroundColor = DEFAULT_FOREGROUND;
		backGroundColor = DEFAULT_BACKGROUND;
		setBackground(backGroundColor);

		undoStack = new Stack();
		redoStack = new Stack();




		this.getInputMap().put(KeyStroke.getKeyStroke("F12"), "selectanother");
		this.getActionMap().put("selectanother", new javax.swing.AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				selected=0;
				/*	} else
					if (ipkey.equals("z")) {
						selected=selected+1;
					}
				 */
				repaint();		

			}

		});


	}


	/*----------------------------------------------------------------------------*/		
	public void mousePressed(MouseEvent event)
	{ 

		if(!event.isMetaDown())
		{
			rtClick=false;
			x1 = linex1 = linex2 = event.getX();
			y1 = liney1 = liney2 = event.getY();
			z1=zcrd;
		}

	}

	public void mouseMoved(MouseEvent event){
		xcrd=event.getX();
		ycrd=event.getY();
		//z1=zcrd;
		repaint();

	}
	/*----------------------------------------------------------------------------*/
	public void mouseReleased(MouseEvent event)
	{
		if(!rtClick){
			if(!translatemode&&!rotatemode){

				if (drawMode == LINE)
				{	
					vLine.add(new 
							Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor));
					undoStack.push(new StepInfo(LINE ,new 
							Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor)));
				}
				if (drawMode == FREE_HAND)
				{	

					vFreeHand.add(new 
							Coordinate(x1,y1,event.getX(),event.getY(),z1,zcrd,foreGroundColor));
					undoStack.push(new StepInfo(FREE_HAND ,new 
							Coordinate(x1,y1,event.getX(),event.getY(),z1,zcrd,foreGroundColor)));
				}
				if (drawMode == SQUARE) 
				{
					if(solidMode)
					{
						if(x1 > event.getX() || y1 > event.getY())
						{
							vSolidSquare.add(new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor));
							undoStack.push(new StepInfo(SOLID_SQUARE, new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor)));
						}
						else
						{
							vSolidSquare.add(new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor));
							undoStack.push(new StepInfo(SOLID_SQUARE, new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor)));
						}
					}
					else
					{
						if(x1 > event.getX() || y1 > event.getY())
						{
							vSquare.add(new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor));
							undoStack.push(new StepInfo(SQUARE, new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor)));
						}
						else
						{
							vSquare.add(new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor));
							undoStack.push(new StepInfo(SQUARE, new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor)));
						}
					}
				}
				if (drawMode == this.OVAL) 
				{
					if(solidMode)
					{
						if(x1 > event.getX() || y1 > event.getY())
						{
							vSolidOval.add(new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor));
							undoStack.push(new StepInfo(SOLID_OVAL, new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor)));
						}
						else
						{
							vSolidOval.add(new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor));
							undoStack.push(new StepInfo(SOLID_OVAL, new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor)));
						}
					}
					else
					{
						if(x1 > event.getX() || y1 > event.getY())
						{
							vOval.add(new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor));
							undoStack.push(new StepInfo(OVAL, new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor)));
						}
						else	
						{
							vOval.add(new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor));
							undoStack.push(new StepInfo(OVAL, new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor)));
						}
					}
				}
				if (drawMode == this.POLYGON || drawMode == this.SOLID_POLYGON) 
				{
					xPolygon.add(new Integer(event.getX()));
					yPolygon.add(new Integer(event.getY()));
					polygonBuffer = true;
					repaint();       	      
				}
				if (drawMode == this.ROUND_RECT) 
				{
					if(solidMode)
					{
						if(x1 > event.getX() || y1 > event.getY())
						{
							vSolidRoundRect.add(new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor));
							undoStack.push(new StepInfo(SOLID_ROUND_RECT, new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor)));
						}
						else
						{
							vSolidRoundRect.add(new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor));
							undoStack.push(new StepInfo(SOLID_ROUND_RECT, new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor)));
						}
					}
					else
					{
						if(x1 > event.getX() || y1 > event.getY())
						{
							vRoundRect.add(new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor));
							undoStack.push(new StepInfo(ROUND_RECT, new 
									Coordinate(event.getX(),event.getY(),x1,y1,foreGroundColor)));
						}
						else
						{
							vRoundRect.add(new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor));
							undoStack.push(new StepInfo(ROUND_RECT, new 
									Coordinate(x1,y1,event.getX(),event.getY(),foreGroundColor)));
						}
					}
				}
			}

			x1=linex1=x2=linex2=0;
			y1=liney1=y2=liney2=0;
			z1=z2=0;
		}
		else rtClick=false;




	}
	/*----------------------------------------------------------------------------*/
	public void mouseEntered(MouseEvent event)
	{
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

	}



	/*----------------------------------------------------------------------------*/
	public void mouseExited(MouseEvent event)
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	/*----------------------------------------------------------------------------*/
	public void mouseDragged(MouseEvent event)
	{



		int xrel,yrel,zchange=0;
		if(!event.isMetaDown()){
			xcrd=x2 = event.getX();
			ycrd=y2 = event.getY();
			y3=y2;
			z2=zcrd;

			xrel=x1-x2;
			yrel=y1-y2;
			if(translatemode)
			{

				translate(xrel,yrel);
				x1=x2;y1=y2;

			}

			if(rotatemode)
			{



				float rX=event.getY()-y4;
				float rY=event.getX()-x4;
				//System.out.print("\n getY="+String.valueOf(event.getY())+"\t y4="+ String.valueOf(y4));
				if(rX>0)
					rX=4;
				else if(rX<0)
					rX=-4;
				else rX=0;

				if(rY>0)
					rY=4;
				else if(rY<0)
					rY=-4;
				else rY=0;

				//System.out.print("\n angle="+String.valueOf(rX));
				rotate(rX,rY);
				y4=event.getY();
				x4=event.getX();





			}

		}
		else
		{
			rtClick=true;
			zchange=y3-event.getY();
			if(zchange>0)
				zchange=1;
			else if(zchange<0)
				zchange=-1;
			else zchange=0;

			zcrd+=zchange;
			z2=zcrd;
			y3=event.getY();

		}



		repaint();
	}
	/*----------------------------------------------------------------------------*/
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		double length;

		redrawVectorBuffer(g);

		g.setColor(foreGroundColor);

		g.drawString(" X="+String.valueOf((int)(xcrd/cm))+format(xcrd/cm)+"Y="+String.valueOf((int)(ycrd/cm))+format(ycrd/cm)+" Z="+String.valueOf(zcrd),10,10);

		if(!translatemode&&!rotatemode){

			if (drawMode == LINE) 
			{
				g.drawLine(x1,y1,x2,y2);
				length = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
				g.drawString(" " + 
						String.valueOf((int)(length/cm))+ format(length/cm)+ " cm",(int)((x1+x2)/2),(int)((y1+y2)/2));
			}
			if (drawMode == OVAL) 
			{   
				int maj,min;
				int xdif,ydif;
				xdif=Math.abs(x1-x2);
				ydif=Math.abs(y1-y2);
				if(xdif>=ydif)
				{maj=xdif;
				min=ydif;
				}
				else {
					min=xdif;
					maj=ydif;
				}
				if(solidMode)
				{
					if(x1 > x2 || y1 > y2)
					{g.fillOval(x2,y2,x1-x2,y1-y2);

					g.drawString(" Maj:" + String.valueOf((int)(maj/cm))+format(maj/cm)+ " Min:" +String.valueOf((int)(min/cm))+format(min/cm)+ " cm",x1,y1);}
					else	
					{g.fillOval(x1,y1,x2-x1,y2-y1);
					g.drawString(" Maj:" + String.valueOf((int)(maj/cm))+format(maj/cm)+ " Min:" +String.valueOf((int)(min/cm))+format(min/cm)+ " cm",x2,y2);} 
				}
				else
				{
					if(x1 > x2 || y1 > y2)
					{g.drawOval (x2,y2,x1-x2,y1-y2);
					g.drawString(" Maj:" + String.valueOf((int)(maj/cm))+format(maj/cm)+ " Min:" 
							+String.valueOf((int)(min/cm))+format(min/cm)+" cm",x1,y1);}
					else
					{g.drawOval (x1,y1,x2-x1,y2-y1);
					g.drawString(" Maj:" + String.valueOf((int)(maj/cm))+format(maj/cm)+ " Min:" 
							+String.valueOf((int)(min/cm))+format(min/cm)+" cm",x2,y2);}
				}
			}
			if (drawMode == ROUND_RECT) 
			{
				if(solidMode)
				{
					if(x1 > x2 || y1 > y2)
					{g.fillRoundRect(x2,y2,x1-x2,y1-y2,25,25);
					g.drawString(" " + String.valueOf((int)((x1-x2)/cm))+format((x1-x2)/cm)+ " X " 
							+String.valueOf((int)((y1-y2)/cm))+format((y1-y2)/cm)+ " cm",x1,y1);}

					else
					{g.fillRoundRect(x1,y1,x2-x1,y2-y1,25,25);
					g.drawString(" " + String.valueOf((int)((x2-x1)/cm))+format((x2-x1)/cm)+ " X " 
							+String.valueOf((int)((y2-y1)/cm))+" cm",x2,y2);}
				}
				else
				{
					if(x1 > x2 || y1 > y2)
					{g.drawRoundRect(x2,y2,x1-x2,y1-y2,25,25);
					g.drawString(" " + String.valueOf((int)((x1-x2)/cm))+format((x1-x2)/cm)+ " X " 
							+String.valueOf((int)((y1-y2)/cm))+format((y1-y2)/cm)+ " cm",x1,y1);}
					else
					{g.drawRoundRect(x1,y1,x2-x1,y2-y1,25,25);
					g.drawString(" " + String.valueOf((int)((x2-x1)/cm))+format((x2-x1)/cm)+ " X " 
							+String.valueOf((int)((y2-y1)/cm))+" cm",x2,y2);}
				}
			}
			if (drawMode == SQUARE) 
			{   
				if(solidMode)
				{
					if(x1 > x2 || y1 > y2)
					{g.fillRect (x2,y2,x1-x2,y1-y2);
					g.drawString(" " + String.valueOf((int)((x1-x2)/cm))+format((x1-x2)/cm)+ " X " 
							+String.valueOf((int)((y1-y2)/cm))+format((y1-y2)/cm)+ " cm",x1,y1);}
					else
					{g.fillRect (x1,y1,x2-x1,y2-y1);
					g.drawString(" " + String.valueOf((int)((x2-x1)/cm))+format((x2-x1)/cm)+ " X " 
							+String.valueOf((int)((y2-y1)/cm))+" cm",x2,y2);}
				}
				else
				{
					if(x1 > x2 || y1 > y2)
					{g.drawRect (x2,y2,x1-x2,y1-y2);
					g.drawString(" " + String.valueOf((int)((x1-x2)/cm))+format((x1-x2)/cm)+ " X " 
							+String.valueOf((int)((y1-y2)/cm))+format((y1-y2)/cm)+ " cm",x1,y1);}
					else
					{g.drawRect (x1,y1,x2-x1,y2-y1);
					g.drawString(" " + String.valueOf((int)((x2-x1)/cm))+format((x2-x1)/cm)+ " X " 
							+String.valueOf((int)((y2-y1)/cm))+" cm",x2,y2);
					}

				}
			}
			if (drawMode == POLYGON || drawMode == SOLID_POLYGON)
			{
				int xPos[] = new int[xPolygon.size()];
				int yPos[] = new int[yPolygon.size()];

				for(int count=0;count<xPos.length;count++)
				{
					xPos[count] = 
							((Integer)(xPolygon.elementAt(count))).intValue();
					yPos[count] = 
							((Integer)(yPolygon.elementAt(count))).intValue();
				}
				g.drawPolyline(xPos,yPos,xPos.length);
				polygonBuffer = true;
			}
			if (drawMode == FREE_HAND) 
			{
				g.drawLine(x1,y1,x2,y2);
				g.drawString(/*String.valueOf((int)(x1/cm))+format(x1/cm)+", "+String.valueOf((int)(y1/cm))+format(y1/cm)+", "+*/String.valueOf(z1),x1,y1);
				g.drawString(/*String.valueOf((int)(x2/cm))+format(x2/cm)+", "+String.valueOf((int)(y2/cm))+format(y2/cm)+", "+*/String.valueOf(z2),x2,y2);
			}
		}
	}
	/*----------------------------------------------------------------------------*/
	public void setDrawMode(int mode)
	{
		drawMode = mode;
	}
	public int getDrawMode()
	{	
		return drawMode;	
	}
	/*----------------------------------------------------------------------------*/
	public void setSolidMode(Boolean inSolidMode)
	{
		solidMode = inSolidMode.booleanValue();
	}
	public Boolean getSolidMode()
	{
		return Boolean.valueOf(solidMode);
	}
	/*----------------------------------------------------------------------------*/
	public void setForeGroundColor(Color inputColor)
	{
		foreGroundColor = inputColor;
	}
	public Color getForeGroundColor()
	{
		return foreGroundColor;
	}
	/*----------------------------------------------------------------------------*/
	public void setBackGroundColor(Color inputColor)
	{
		backGroundColor = inputColor;
		this.setBackground(backGroundColor);
	}
	public Color getBackGroundColor()
	{
		return backGroundColor;
	}



	/*----------------------------------------------------------------------------*/
	public void setAxes()
	{
		//StepInfo tempInfo[]=new StepInfo[vFreeHand.size()];
		int[] x=new int[2*vFreeHand.size()];
		int[] y=new int[2*vFreeHand.size()];
		int[] z=new int[2*vFreeHand.size()];

		for( int i=0;i<vFreeHand.size();i++)
		{
			x[i]=((Coordinate)vFreeHand.elementAt(i)).getX1();
			y[i]=((Coordinate)vFreeHand.elementAt(i)).getY1();
			z[i]=((Coordinate)vFreeHand.elementAt(i)).getZ1();

		}

		for( int j=vFreeHand.size();j<2*vFreeHand.size();j++)
		{
			x[j]=((Coordinate)vFreeHand.elementAt(j-vFreeHand.size())).getX2();
			y[j]=((Coordinate)vFreeHand.elementAt(j-vFreeHand.size())).getY2();
			z[j]=((Coordinate)vFreeHand.elementAt(j-vFreeHand.size())).getZ2();

		}


		int xmin,xmax,ymin,ymax,zmin,zmax;

		xmin=getMin(x);ymin=getMin(y);zmin=getMin(z);
		xmax=getMax(x);ymax=getMax(y);zmax=getMax(z);


		xAxis=(xmin+xmax)/2;
		yAxis=(ymin+ymax)/2;
		zAxis=(zmin+zmax)/2;
	}
	/*----------------------------------------------------------------------------*/
	private int getMin(int arr[])
	{
		int min=arr[0];
		for(int i=1;i<arr.length;i++)
			if(min>arr[i])
				min=arr[i];

		return min;
	}
	private int getMax(int arr[])
	{
		int max=arr[0];
		for(int i=1;i<arr.length;i++)
			if(max<arr[i])
				max=arr[i];

		return max;
	}
	/*----------------------------------------------------------------------------*/
	private int incr(int a,float b)
	{
		float temp=b-a;
		if(temp>=0.5)
			return 1;
		else if(temp<=-0.5)
			return -1;
		else return 0;  


	}



	/*----------------------------------------------------------------------------*/
	public void rotate(float xrot,float yrot)
	{

		Matrix3D mat=new Matrix3D();
		Matrix3D mat1=new Matrix3D();
		mat.xrot(xrot);
		mat1.yrot(yrot);
		mat.mult(mat1);




		if(drawMode==6)
		{

			StepInfo tempInfo[]= new StepInfo[vFreeHand.size()];
			StepInfo tempInfo1[]= new StepInfo[vFreeHand.size()];

			for( int i=vFreeHand.size()-1;i>=0;i--)
			{
				tempInfo[i] = (StepInfo)undoStack.pop();
				vFreeHand.remove(vFreeHand.elementAt(i));
			}
			tempInfo1=tempInfo;
			for(int j=0;j<tempInfo1.length;j++){
				Coordinate c1=tempInfo[j].getStepCoordinate();

				float x1in,y1in,z1in,x2in,y2in,z2in;

				x1in=c1.x1-xAxis;
				x2in=c1.x2-xAxis;
				y1in=c1.y1-yAxis;
				y2in=c1.y2-yAxis;
				z1in=c1.z1-zAxis;
				z2in=c1.z2-zAxis;
				//System.out.print("\ny axis ="+ String.valueOf(yAxis)+"\tz axis ="+ String.valueOf(zAxis)); 
				c1.x1=(int)(x1in*mat.xx + y1in*mat.xy + z1in*mat.xz + xAxis);
				c1.x1=c1.x1+incr(c1.x1,(x1in*mat.xx + y1in*mat.xy + z1in*mat.xz + xAxis));
				c1.y1= (int)(x1in*mat.yx + y1in*mat.yy + z1in*mat.yz + yAxis);
				c1.y1=c1.y1+incr(c1.y1,(x1in*mat.yx + y1in*mat.yy + z1in*mat.yz + yAxis));
				c1.z1= (int)(x1in*mat.zx + y1in*mat.zy + z1in*mat.zz + zAxis);
				c1.z1=c1.z1+incr(c1.z1,(x1in*mat.zx + y1in*mat.zy + z1in*mat.zz + zAxis));

				c1.x2= (int)(x2in*mat.xx + y2in*mat.xy + z2in*mat.xz + xAxis);
				c1.x2=c1.x2+incr(c1.x2,(x2in*mat.xx + y2in*mat.xy + z2in*mat.xz + xAxis));
				c1.y2= (int)(x2in*mat.yx + y2in*mat.yy + z2in*mat.yz + yAxis);
				c1.y2=c1.y2+incr(c1.y2,(x2in*mat.yx + y2in*mat.yy + z2in*mat.yz + yAxis));
				c1.z2= (int)(x2in*mat.zx + y2in*mat.zy + z2in*mat.zz + zAxis);
				c1.z2=c1.z2+incr(c1.z2,(x2in*mat.zx + y2in*mat.zy + z2in*mat.zz + zAxis));


				//System.out.print("\n Y1="+ String.valueOf(c1.y1));
				//System.out.print(" \n points="+ mat.toString());


				tempInfo1[j].stepCoordinate=c1;
				vFreeHand.add(c1);
				undoStack.push(tempInfo1[j]);
			}


			repaint();
		}

	}


	int selected=-1;

	/*----------------------------------------------------------------------------*/
	public void translate(int xrel,int yrel)
	{

		{

			if(drawMode!=0 && selected!=-1)
			{ 				
				StepInfo tempInfo,tempInfo1;
				tempInfo = (StepInfo)undoStack.pop();
				Coordinate lastCoord=null;
				switch(tempInfo.getStepType())
				{
				case 1:	if (selected<vLine.size()) lastCoord=(Coordinate)vLine.remove(selected);
				break;
				case 2:	if (selected<vSquare.size()) lastCoord=(Coordinate)vSquare.remove(selected);
				break;
				case 3:	if (selected<vOval.size())lastCoord=(Coordinate)vOval.remove(selected);
				break;
				case 4:	if (selected<vPolygon.size()) lastCoord=(Coordinate)vPolygon.remove(selected);
				break;	
				case 5:	if (selected<vRoundRect.size()) lastCoord=(Coordinate)vRoundRect.remove(selected);
				break;
				case 6: if (selected<vFreeHand.size())	lastCoord=(Coordinate)vFreeHand.remove(selected);
				break;
				case 22:if (selected<vSolidSquare.size()) lastCoord=(Coordinate)vSolidSquare.remove(selected);
				break;
				case 33:if (selected<vSolidOval.size()) lastCoord=(Coordinate)vSolidOval.remove(selected);
				break;
				case 44: if (selected<vSolidPolygon.size()) lastCoord=(Coordinate)vSolidPolygon.remove(selected);
				break;
				case 55:if (selected<vSolidRoundRect.size()) lastCoord=(Coordinate)vSolidRoundRect.remove(selected);
				break;
				}
				tempInfo1=tempInfo;
				if (lastCoord!=null) {					
					Coordinate c1=lastCoord;
					c1.x1=c1.x1-xrel;
					c1.x2=c1.x2-xrel;
					c1.y1=c1.y1-yrel;
					c1.y2=c1.y2-yrel;
					tempInfo1.stepCoordinate=c1;

					switch(tempInfo1.getStepType())
					{
					case 1:	vLine.insertElementAt(c1,selected);
					break;
					case 2:		
						vSquare.insertElementAt(c1,selected);
		
					break;
					case 3:	vOval.insertElementAt(c1,selected);
					break;
					case 4: vPolygon.insertElementAt(c1,selected);
					break;	
					case 5:	vRoundRect.insertElementAt(c1,selected);
					break;
					case 6:	vFreeHand.insertElementAt(c1,selected);
					break;
					case 22:vSolidSquare.insertElementAt(c1,selected);
					break;
					case 33: vSolidOval.insertElementAt(c1,selected);
					break;
					case 44: vSolidPolygon.insertElementAt(c1,selected);
					break;
					case 55: vSolidRoundRect.insertElementAt(c1,selected);
					break;
					}
				}
					undoStack.push(tempInfo1);
				


				repaint();
			}
		}
	}


	/*----------------------------------------------------------------------------*/
	public void duplicate(int xrel,int yrel)
	{
		if(drawMode!=0)
		{

			StepInfo tempInfo,tempInfo1;
			tempInfo = (StepInfo)undoStack.pop();
			undoStack.push(tempInfo);
			tempInfo1=tempInfo;	
			Coordinate c1=tempInfo.getStepCoordinate();
			c1.x1=tempInfo.getStepCoordinate().x1-xrel;
			c1.x2=tempInfo.getStepCoordinate().x2-xrel;
			c1.y1=tempInfo.getStepCoordinate().y1-yrel;
			c1.y2=tempInfo.getStepCoordinate().y2-yrel;
			tempInfo1.stepCoordinate=c1;

			//System.out.print("\nPop val. ="+String.valueOf(xold)+"\n push val. ="+String.valueOf(xnew)); 

			switch(tempInfo1.getStepType())
			{
			case 1:	vLine.add(c1);
			break;
			case 2:	vSquare.add(c1);
			break;
			case 3:	vOval.add(c1);
			break;
			case 4:	vPolygon.add(c1);
			break;	
			case 5:	vRoundRect.add(c1);
			break;
			case 6:	vFreeHand.add(c1);
			break;
			case 22:vSolidSquare.add(c1);
			break;
			case 33:vSolidOval.add(c1);
			break;
			case 44:vSolidPolygon.add(c1);
			break;
			case 55:vSolidRoundRect.add(c1);
			break;
			}

			undoStack.push(tempInfo1);


			repaint();
		}
	}



	/*----------------------------------------------------------------------------*/
	public void undo()
	{
		StepInfo tempInfo;
		int test;

		if(undoStack.isEmpty())
			JOptionPane.showMessageDialog(null, "Can't Undo","Bpaint", 
					JOptionPane.INFORMATION_MESSAGE);
		else
		{
			tempInfo = (StepInfo)undoStack.pop();
			test=tempInfo.getStepCoordinate().x1;
			System.out.print("\nUndone :"+String.valueOf(test));

			switch(tempInfo.getStepType())
			{
			case 1:	vLine.remove(vLine.size()-1);
			break;
			case 2:	vSquare.remove(vSquare.size()-1);
			break;
			case 3:	vOval.remove(vOval.size()-1);
			break;
			case 4:	vPolygon.remove(vPolygon.size()-1);
			break;	
			case 5:	vRoundRect.remove(vRoundRect.size()-1);
			break;
			case 6:	vFreeHand.remove(vFreeHand.size()-1);
			break;
			case 22:vSolidSquare.remove(vSolidSquare.size()-1);
			break;
			case 33:vSolidOval.remove(vSolidOval.size()-1);
			break;
			case 44:vSolidPolygon.remove(vSolidPolygon.size()-1);
			break;
			case 55:vSolidRoundRect.remove(vSolidRoundRect.size()-1);
			break;
			}
			redoStack.push(tempInfo);
		}
		repaint();
	}
	/*----------------------------------------------------------------------------*/
	public void redo()
	{
		StepInfo tempInfo;

		if(redoStack.isEmpty())
			JOptionPane.showMessageDialog(null,"Can't Redo","Bpaint",JOptionPane.INFORMATION_MESSAGE);
		else
		{
			tempInfo = (StepInfo)redoStack.pop();

			switch(tempInfo.getStepType())
			{
			case 1:	vLine.add(tempInfo.getStepCoordinate());
			break;
			case 2:	vSquare.add(tempInfo.getStepCoordinate());
			break;
			case 3:	vOval.add(tempInfo.getStepCoordinate());
			break;
			case 4:	vPolygon.add(tempInfo.getStepCoordinate());
			break;	
			case 5:	vRoundRect.add(tempInfo.getStepCoordinate());
			break;
			case 6:	vFreeHand.add(tempInfo.getStepCoordinate());
			break;
			case 22:vSolidSquare.add(tempInfo.getStepCoordinate());
			break;
			case 33:vSolidOval.add(tempInfo.getStepCoordinate());
			break;
			case 44:vSolidPolygon.add(tempInfo.getStepCoordinate());
			break;
			case 55:vSolidRoundRect.add(tempInfo.getStepCoordinate());
			break;
			}
			undoStack.push(tempInfo);
		}
		repaint();
	}
	/*----------------------------------------------------------------------------*/
	public void clearCanvas()
	{
		vFreeHand.removeAllElements();
		vLine.removeAllElements();
		vOval.removeAllElements();
		vPolygon.removeAllElements();
		vRoundRect.removeAllElements();
		vSolidOval.removeAllElements();
		vSolidPolygon.removeAllElements();
		vSolidRoundRect.removeAllElements();
		vSolidSquare.removeAllElements();
		vSquare.removeAllElements();
		undoStack.clear();
		redoStack.clear();
		repaint();
	}
	/*----------------------------------------------------------------------------*/	
	public void SaveCanvasToFile()
	{
		if(fileName != null)
		{
			vFile.removeAllElements();
			vFile.addElement(vFreeHand);
			vFile.addElement(vLine);
			vFile.addElement(vOval);
			vFile.addElement(vPolygon);
			vFile.addElement(vRoundRect);
			vFile.addElement(vSolidOval);
			vFile.addElement(vSolidPolygon);
			vFile.addElement(vSolidRoundRect);
			vFile.addElement(vSolidSquare);
			vFile.addElement(vSquare);
			vFile.addElement(new Color(backGroundColor.getRGB()));
			RenderedImage rendImage = myCreateImage();

			try
			{
				FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(vFile);
				JOptionPane.showMessageDialog(null,"File Saved","SketchEditor",JOptionPane.INFORMATION_MESSAGE);
			}catch(Exception exp){}

			try 
			{
				File file = new File(fileName.toString() + ".jpg");        		
				ImageIO.write(rendImage, "jpg", file);
			}catch (IOException e) {}
		}
		else
		{
			SaveAsCanvasToFile();
		}
		repaint();
	}

	public byte[] SaveCanvasToByteArray()
	{

		vFile.removeAllElements();
		vFile.addElement(vFreeHand);
		vFile.addElement(vLine);
		vFile.addElement(vOval);
		vFile.addElement(vPolygon);
		vFile.addElement(vRoundRect);
		vFile.addElement(vSolidOval);
		vFile.addElement(vSolidPolygon);
		vFile.addElement(vSolidRoundRect);
		vFile.addElement(vSolidSquare);
		vFile.addElement(vSquare);
		vFile.addElement(new Color(backGroundColor.getRGB()));


		try
		{
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(vFile);
			return fos.toByteArray();
		}catch(Exception exp){}
		return new byte[0];					
	}
	/*----------------------------------------------------------------------------*/
	public void SaveAsCanvasToFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);	
		int result = fileChooser.showSaveDialog(null);

		if(result == JFileChooser.CANCEL_OPTION) return;

		fileName = fileChooser.getSelectedFile();

		if(fileName == null || fileName.getName().equals(""))
			JOptionPane.showMessageDialog(null,"Invalid File Name","SketchEditor",JOptionPane.ERROR_MESSAGE);
		else
		{
			vFile.removeAllElements();
			vFile.addElement(vFreeHand);
			vFile.addElement(vLine);
			vFile.addElement(vOval);
			vFile.addElement(vPolygon);
			vFile.addElement(vRoundRect);
			vFile.addElement(vSolidOval);
			vFile.addElement(vSolidPolygon);
			vFile.addElement(vSolidRoundRect);
			vFile.addElement(vSolidSquare);
			vFile.addElement(vSquare);	
			vFile.addElement(new Color(backGroundColor.getRGB()));

			RenderedImage rendImage = myCreateImage();

			try	{
				FileOutputStream fos = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(vFile);
				JOptionPane.showMessageDialog(null,"File Saved","SketchEditor",JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception exp){
				exp.printStackTrace();
			}

			try {
				File file = new File(fileName.toString() + ".jpg");        		
				ImageIO.write(rendImage, "jpg", file);
			}catch (IOException e) {}
		}		    
		repaint();
	}
	/*----------------------------------------------------------------------------*/
	public void openFile(File f) {
		if(f != null) {
			try{
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				vFile = (Vector) ois.readObject();

				initiate(vFile);

			}
			catch(Exception exp){
				exp.printStackTrace();
				JOptionPane.showMessageDialog(null,"Cannot Open File","SketchEditor",JOptionPane.INFORMATION_MESSAGE);
			}	

			// Dibuja el sketch del fichero
			repaint();
		}
	}

	void initiate(Vector vFile2) {
		this.clearCanvas();
		vFreeHand 		= (Vector)vFile2.elementAt(0);
		vLine 			= (Vector)vFile2.elementAt(1);
		vOval			= (Vector)vFile2.elementAt(2);
		vPolygon		= (Vector)vFile2.elementAt(3);
		vRoundRect		= (Vector)vFile2.elementAt(4);
		vSolidOval		= (Vector)vFile2.elementAt(5);
		vSolidPolygon	= (Vector)vFile2.elementAt(6);
		vSolidRoundRect	= (Vector)vFile2.elementAt(7);
		vSolidSquare	= (Vector)vFile2.elementAt(8);
		vSquare			= (Vector)vFile2.elementAt(9);
		backGroundColor = (Color)vFile2.elementAt(10);

		this.setBackground(backGroundColor);

	}

	public void OpenCanvasFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.CANCEL_OPTION) return;

		fileName = fileChooser.getSelectedFile();

		openFile(fileName);
	}
	/*----------------------------------------------------------------------------*/
	public boolean isExistPolygonBuffer()
	{
		return polygonBuffer;
	} 
	/*----------------------------------------------------------------------------*/	
	public void flushPolygonBuffer()
	{
		if(!solidMode)
		{
			vPolygon.add(new Coordinate(xPolygon, yPolygon, foreGroundColor));
			undoStack.push(new StepInfo(POLYGON,new Coordinate(xPolygon, 
					yPolygon, foreGroundColor)));
		}
		else
		{
			vSolidPolygon.add(new Coordinate(xPolygon, yPolygon, 
					foreGroundColor));
			undoStack.push(new StepInfo(SOLID_POLYGON,new Coordinate(xPolygon, 
					yPolygon, foreGroundColor)));
		}

		xPolygon.removeAllElements();
		yPolygon.removeAllElements();

		polygonBuffer = false;
		repaint();
	}
	/*----------------------------------------------------------------------------*/
	private class Coordinate implements Serializable
	{
		private int x1,y1,x2,y2,z1,z2;
		private Color foreColor;
		private Vector xPoly, yPoly;

		public Coordinate (int inx1,int iny1,int inx2, int iny2, Color color) 
		{

			x1 = inx1;
			y1 = iny1;
			x2 = inx2;
			y2 = iny2;
			foreColor = color;
		}
		public Coordinate (int inx1,int iny1,int inx2, int iny2,int inz1,int inz2, Color color) 
		{

			x1 = inx1;
			y1 = iny1;
			x2 = inx2;
			y2 = iny2;
			z1 = inz1;
			z2 = inz2;
			foreColor = color;
		}
		public Coordinate(Vector inXPolygon, Vector inYPolygon, Color 
				color)
		{
			xPoly = (Vector)inXPolygon.clone();
			yPoly = (Vector)inYPolygon.clone();
			foreColor = color;
		}
		public Color colour()
		{
			return foreColor;
		}
		public int getX1 () 
		{
			return x1;
		}
		public int getX2 () 
		{
			return x2;
		}
		public int getY1 () 
		{
			return y1;
		}
		public int getY2 () 
		{
			return y2;
		}

		public int getZ1 () 
		{
			return z1;
		}

		public int getZ2 () 
		{
			return z2;
		}
		public Vector getXPolygon()
		{
			return xPoly;
		}
		public Vector getYPolygon()
		{
			return yPoly;
		}
	}		
	/*----------------------------------------------------------------------------*/	
	private class StepInfo implements Serializable
	{
		private int stepType;
		private Coordinate stepCoordinate;

		public StepInfo(int inStepType, Coordinate inStepCoordinate)
		{
			stepType = inStepType;
			stepCoordinate = inStepCoordinate;
		}
		public int getStepType()
		{
			return stepType;
		}
		public Coordinate getStepCoordinate()
		{
			return stepCoordinate;
		}
	}
	/*----------------------------------------------------------------------------*/	
	private RenderedImage myCreateImage() 
	{
		BufferedImage bufferedImage = new BufferedImage(600,390, 
				BufferedImage.TYPE_INT_RGB);

		Graphics g = bufferedImage.createGraphics();
		redrawVectorBuffer(g);

		g.dispose();
		return bufferedImage;
	}
	/*----------------------------------------------------------------------------*/	
	private void redrawVectorBuffer(Graphics g)
	{   double xdim,ydim,maj,min;
	double xcoord,ycoord;

	for (int i=0;i<vFreeHand.size();i++){
		g.setColor(((Coordinate)vFreeHand.elementAt(i)).colour());

		g.drawLine(((Coordinate)vFreeHand.elementAt(i)).getX1(),((Coordinate)vFreeHand.elementAt(i)).getY1(),((Coordinate)vFreeHand.elementAt(i)).getX2(),((Coordinate)vFreeHand.elementAt(i)).getY2());

		double x1,y1,x2,y2,z1,z2;
		x1=((Coordinate)vFreeHand.elementAt(i)).getX1()/cm;
		y1=((Coordinate)vFreeHand.elementAt(i)).getY1()/cm;
		x2=((Coordinate)vFreeHand.elementAt(i)).getX2()/cm;
		y2=((Coordinate)vFreeHand.elementAt(i)).getY2()/cm;
		z1=((Coordinate)vFreeHand.elementAt(i)).getZ1();
		z2=((Coordinate)vFreeHand.elementAt(i)).getZ2();


		g.drawString(/*String.valueOf((int)x1)+format(x1)+", "+String.valueOf((int)y1)+format(y1)+", "+*/String.valueOf((int)z1),((Coordinate)vFreeHand.elementAt(i)).getX1(),((Coordinate)vFreeHand.elementAt(i)).getY1());
		g.drawString(/*String.valueOf((int)x2)+format(x2)+", "+String.valueOf((int)y2)+format(y2)+", "+*/String.valueOf((int)z2),((Coordinate)vFreeHand.elementAt(i)).getX2(),((Coordinate)vFreeHand.elementAt(i)).getY2());
	}
	for (int i=0;i<vLine.size();i++){
		g.setColor(((Coordinate)vLine.elementAt(i)).colour());

		g.drawLine(((Coordinate)vLine.elementAt(i)).getX1(),((Coordinate)vLine.elementAt(i)).getY1(),((Coordinate)vLine.elementAt(i)).getX2(),((Coordinate)vLine.elementAt(i)).getY2());

		xdim=((Coordinate)vLine.elementAt(i)).getX2()-((Coordinate)vLine.elementAt(i)).getX1();

		ydim=((Coordinate)vLine.elementAt(i)).getY2()-((Coordinate)vLine.elementAt(i)).getY1();


		xcoord=(((Coordinate)vLine.elementAt(i)).getX2()+((Coordinate)vLine.elementAt(i)).getX1())/2;

		ycoord=(((Coordinate)vLine.elementAt(i)).getY2()+((Coordinate)vLine.elementAt(i)).getY1())/2;

		int length=(int) Math.sqrt(xdim*xdim +ydim*ydim);
		g.drawString(" "+String.valueOf((int)(length/cm))+format(length/cm)+" cm",(int)xcoord,(int)ycoord );
	}
	for (int i=0;i<vOval.size();i++){	
		g.setColor(((Coordinate)vOval.elementAt(i)).colour());

		g.drawOval(((Coordinate)vOval.elementAt(i)).getX1(),((Coordinate)vOval.elementAt(i)).getY1(),((Coordinate)vOval.elementAt(i)).getX2()-((Coordinate)vOval.elementAt(i)).getX1(),((Coordinate)vOval.elementAt(i)).getY2()-((Coordinate)vOval.elementAt(i)).getY1());

		xdim=((Coordinate)vOval.elementAt(i)).getX2()-((Coordinate)vOval.elementAt(i)).getX1();

		ydim=((Coordinate)vOval.elementAt(i)).getY2()-((Coordinate)vOval.elementAt(i)).getY1();
		if(xdim>=ydim)
		{
			maj=xdim;
			min=ydim;
		}
		else{
			maj=ydim;
			min=xdim;
		}
		g.drawString(" Maj:"+ String.valueOf((int)(maj/cm))+ format(maj/cm)+" Min:"+String.valueOf((int)(min/cm))+format(min/cm)+ " cm" 
				,((Coordinate)vOval.elementAt(i)).getX2(),((Coordinate)vOval.elementAt(i)).getY2());

	}
	for (int i=0;i<vRoundRect.size();i++){
		g.setColor(((Coordinate)vRoundRect.elementAt(i)).colour());

		g.drawRoundRect(((Coordinate)vRoundRect.elementAt(i)).getX1(),((Coordinate)vRoundRect.elementAt(i)).getY1(),((Coordinate)vRoundRect.elementAt(i)).getX2()-((Coordinate)vRoundRect.elementAt(i)).getX1(),((Coordinate)vRoundRect.elementAt(i)).getY2()-((Coordinate)vRoundRect.elementAt(i)).getY1(),25,25);

		xdim=((Coordinate)vRoundRect.elementAt(i)).getX2()-((Coordinate)vRoundRect.elementAt(i)).getX1();

		ydim=((Coordinate)vRoundRect.elementAt(i)).getY2()-((Coordinate)vRoundRect.elementAt(i)).getY1();
		g.drawString(" "+ String.valueOf((int)(xdim/cm))+format(xdim/cm)+ " X "+String.valueOf((int)(ydim/cm))+format(ydim/cm) +" cm"
				,((Coordinate)vRoundRect.elementAt(i)).getX2(),((Coordinate)vRoundRect.elementAt(i)).getY2());
	}
	for (int i=0;i<vSolidOval.size();i++){
		g.setColor(((Coordinate)vSolidOval.elementAt(i)).colour());

		g.fillOval(((Coordinate)vSolidOval.elementAt(i)).getX1(),((Coordinate)vSolidOval.elementAt(i)).getY1(),((Coordinate)vSolidOval.elementAt(i)).getX2()-((Coordinate)vSolidOval.elementAt(i)).getX1(),((Coordinate)vSolidOval.elementAt(i)).getY2()-((Coordinate)vSolidOval.elementAt(i)).getY1());

		xdim=((Coordinate)vSolidOval.elementAt(i)).getX2()-((Coordinate)vSolidOval.elementAt(i)).getX1();

		ydim=((Coordinate)vSolidOval.elementAt(i)).getY2()-((Coordinate)vSolidOval.elementAt(i)).getY1();
		if(xdim>=ydim)
		{
			maj=xdim;
			min=ydim;
		}
		else{
			maj=ydim;
			min=xdim;
		}
		g.drawString(" Maj:"+ String.valueOf((int)(maj/cm))+format(maj/cm)+ " Min:"+String.valueOf((int)(min/cm))+format(min/cm) + " cm"
				,((Coordinate)vSolidOval.elementAt(i)).getX2(),((Coordinate)vSolidOval.elementAt(i)).getY2());
	}
	for (int i=0;i<vSolidRoundRect.size();i++){

		g.setColor(((Coordinate)vSolidRoundRect.elementAt(i)).colour());

		g.fillRoundRect(((Coordinate)vSolidRoundRect.elementAt(i)).getX1(),((Coordinate)vSolidRoundRect.elementAt(i)).getY1(),((Coordinate)vSolidRoundRect.elementAt(i)).getX2()-((Coordinate)vSolidRoundRect.elementAt(i)).getX1(),((Coordinate)vSolidRoundRect.elementAt(i)).getY2()-((Coordinate)vSolidRoundRect.elementAt(i)).getY1(),25,25);

		xdim=((Coordinate)vSolidRoundRect.elementAt(i)).getX2()-((Coordinate)vSolidRoundRect.elementAt(i)).getX1();

		ydim=((Coordinate)vSolidRoundRect.elementAt(i)).getY2()-((Coordinate)vSolidRoundRect.elementAt(i)).getY1();
		g.drawString(" "+ String.valueOf((int)(xdim/cm))+format(xdim/cm)+ " X "+String.valueOf((int)(ydim/cm))+format(ydim/cm)+ " cm" 
				,((Coordinate)vSolidRoundRect.elementAt(i)).getX2(),((Coordinate)vSolidRoundRect.elementAt(i)).getY2());
	}
	for (int i=0;i<vSquare.size();i++){
		g.setColor(((Coordinate)vSquare.elementAt(i)).colour());

		g.drawRect(((Coordinate)vSquare.elementAt(i)).getX1(),((Coordinate)vSquare.elementAt(i)).getY1(),((Coordinate)vSquare.elementAt(i)).getX2()-((Coordinate)vSquare.elementAt(i)).getX1(),((Coordinate)vSquare.elementAt(i)).getY2()-((Coordinate)vSquare.elementAt(i)).getY1());


		xdim=((Coordinate)vSquare.elementAt(i)).getX2()-((Coordinate)vSquare.elementAt(i)).getX1();

		ydim=((Coordinate)vSquare.elementAt(i)).getY2()-((Coordinate)vSquare.elementAt(i)).getY1();
		g.drawString(" "+ String.valueOf((int)(xdim/cm))+format(xdim/cm)+ " X "+String.valueOf((int)(ydim/cm))+format(ydim/cm)+ " cm" 
				,((Coordinate)vSquare.elementAt(i)).getX2(),((Coordinate)vSquare.elementAt(i)).getY2());
	}
	for (int i=0;i<vSolidSquare.size();i++){
		g.setColor(((Coordinate)vSolidSquare.elementAt(i)).colour());

		g.fillRect(((Coordinate)vSolidSquare.elementAt(i)).getX1(),((Coordinate)vSolidSquare.elementAt(i)).getY1(),((Coordinate)vSolidSquare.elementAt(i)).getX2()-((Coordinate)vSolidSquare.elementAt(i)).getX1(),((Coordinate)vSolidSquare.elementAt(i)).getY2()-((Coordinate)vSolidSquare.elementAt(i)).getY1());

		xdim=((Coordinate)vSolidSquare.elementAt(i)).getX2()-((Coordinate)vSolidSquare.elementAt(i)).getX1();

		ydim=((Coordinate)vSolidSquare.elementAt(i)).getY2()-((Coordinate)vSolidSquare.elementAt(i)).getY1();
		g.drawString(" "+ String.valueOf((int)(xdim/cm))+format(xdim/cm)+ " X "+String.valueOf((int)(ydim/cm))+format(ydim/cm) +" cm"
				,((Coordinate)vSolidSquare.elementAt(i)).getX2(),((Coordinate)vSolidSquare.elementAt(i)).getY2());
	}
	for(int i=0;i<vPolygon.size();i++){
		int xPos[] = new 
				int[((Coordinate)vPolygon.elementAt(i)).getXPolygon().size()];
		int yPos[] = new 
				int[((Coordinate)vPolygon.elementAt(i)).getYPolygon().size()];

		for(int count=0;count<xPos.length;count++)
		{
			xPos[count] = 
					((Integer)((Coordinate)vPolygon.elementAt(i)).getXPolygon().elementAt(count)).intValue();
			yPos[count] = 
					((Integer)((Coordinate)vPolygon.elementAt(i)).getYPolygon().elementAt(count)).intValue();
		}     	 
		g.setColor(((Coordinate)vPolygon.elementAt(i)).colour());
		g.drawPolygon(xPos,yPos,xPos.length);
	}
	for(int i=0;i<vSolidPolygon.size();i++){
		int xPos[] = new 
				int[((Coordinate)vSolidPolygon.elementAt(i)).getXPolygon().size()];
		int yPos[] = new 
				int[((Coordinate)vSolidPolygon.elementAt(i)).getYPolygon().size()];

		for(int count=0;count<xPos.length;count++)
		{
			xPos[count] = 
					((Integer)((Coordinate)vSolidPolygon.elementAt(i)).getXPolygon().elementAt(count)).intValue();
			yPos[count] = 
					((Integer)((Coordinate)vSolidPolygon.elementAt(i)).getYPolygon().elementAt(count)).intValue();
		}
		g.setColor(((Coordinate)vSolidPolygon.elementAt(i)).colour());
		g.fillPolygon(xPos,yPos,xPos.length);
	}	
	// if not for this instruction, the key events will not be processed
	requestFocus(true);
	}

	private String format( double x)
	{
		double res=x-(int)x;

		if(res>=0.1&&res<0.2)
			return (".1");
		if(res>=0.2&&res<0.3)
			return (".2");
		if(res>=0.3&&res<0.4)
			return (".3");
		if(res>=0.4&&res<0.5)
			return (".4");
		if(res>=0.5&&res<0.6)
			return (".5");
		if(res>=0.6&&res<0.7)
			return (".6");
		if(res>=0.7&&res<0.8)
			return (".7");
		if(res>=0.8&&res<0.9)
			return (".8");
		if(res>=0.9&&res<1)
			return (".9");
		else return ".0";


	}




	public void mouseWheelMoved(MouseWheelEvent wheel) {
		wheelrot=wheel.getWheelRotation();
		zcrd-=wheelrot;
		z2=zcrd;
		repaint();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}



}
