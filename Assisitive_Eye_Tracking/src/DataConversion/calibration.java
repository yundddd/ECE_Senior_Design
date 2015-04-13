package DataConversion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class calibration 
{
	
	JFrame frame1 =  new JFrame("CAL");
	int posHeight = 0; 
	int posWidth = 0; 
	int runtime = 0;
	int runtimeTwo = 0; 
	int intWidth =0; 
	int intHeight =0; 
	boolean firstChange =false, secondChange = false, thirdChange=false;
	boolean fourthChange = false, fifthChange = false, sixthChange = false;
	boolean calFinished; 
	calibrationDraw cDraw = new calibrationDraw(posHeight, posWidth);
	Graphics g; 
	
	public calibration()
	{
		//will fail on multiscren computers
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();
		//I like backslashes
		intWidth = width.intValue();
		intHeight = height.intValue(); 
		
		posHeight = intHeight/2; 
		posWidth = intWidth/2; 
		
		frame1.add(cDraw);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(intWidth,intHeight);
		frame1.setBounds(0,0,frame1.getWidth(), frame1.getHeight());
		//Panel panel1 = new Panel();
		
		
		
		
	}
//Different Calibration Methods
//=============================================================================================
	//Linear Moving Dot
	public boolean runCal()
	{
		boolean calFinished = false; 
		 
		frame1.setVisible(true);
		int intervalTime = 50;
		
		if(runtimeTwo < intervalTime)
		{
			posHeight = intHeight/2; 
			posWidth = intWidth/2; 
		}
		else if(runtimeTwo < 2*intervalTime)
		{
			posHeight = intHeight - 80; 
			posWidth = intWidth/2; 

		}
		else if(runtimeTwo < 3*intervalTime)
		{
			posHeight = 0;
			posWidth = intWidth/2; 
		}	
		else if(runtimeTwo < 4*intervalTime)
		{
			posHeight = intHeight/2;
			posWidth = intWidth-40; 
		}
		else if(runtimeTwo < 5*intervalTime)
		{
			posHeight = intHeight/2;
			posWidth = 0; 
		}
		else
		{
			calFinished = true; 
		}
		if(calFinished)
		{
			frame1.setVisible(false);
		}
		
		runtimeTwo++;
		cDraw.setPoints(posHeight, posWidth);
		
		frame1.repaint();
		
		return calFinished;
	}
	
	//Stationary dot at x and y extremes 
	public boolean runCalTwo()
	{
		boolean calFinished = false; 
		frame1.setVisible(true);
		
		int intervalTime = 100;
		
		if(runtimeTwo < intervalTime)
		{
			posHeight = intHeight/2; 
			posWidth = intWidth/2; 
		}
		else if(runtimeTwo < 2*intervalTime)
		{
			posHeight = intHeight - 80; 
			posWidth = intWidth/2; 

		}
		else if(runtimeTwo < 3*intervalTime)
		{
			posHeight = 0;
			posWidth = intWidth/2; 
		}	
		else if(runtimeTwo < 4*intervalTime)
		{
			posHeight = intHeight/2;
			posWidth = (intWidth)/4 + intWidth/2 -40; 
		}
		else if(runtimeTwo <5*intervalTime)
		{
			posHeight = intHeight/2;
			posWidth = (intWidth)/4 -40; 
		}
		else if(runtimeTwo < 6*intervalTime)
		{
			posHeight = intHeight/2;
			posWidth = intWidth-40; 
		}
		else if(runtimeTwo < 7*intervalTime)
		{
			posHeight = intHeight/2;
			posWidth = 0; 
		}
		else
		{
			calFinished = true; 
		}
		if(calFinished)
		{
			frame1.setVisible(false);
		}
		
		runtimeTwo++;
		cDraw.setPoints(posHeight, posWidth);
		
		frame1.repaint();
		
		return calFinished;
	}
	
	public int getXPosition()
	{
		return posWidth; 
	}
	public int getYPosition()
	{
		return posHeight; 
	}
	public int getXCenter()
	{
		 
		return intWidth/2; 
	}
	public int getYCenter()
	{
		return intHeight/2; 
		 
	}			
	public void resetRuntimes()
	{
		runtime =0; 
		runtimeTwo =0; 
		
	}

}