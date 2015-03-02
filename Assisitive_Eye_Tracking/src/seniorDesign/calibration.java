package seniorDesign;

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
		
		frame1.setVisible(true);
		
		
	}
	public boolean runCal()
	{
		runtime++; 

		if(firstChange==false)
		{
			posHeight = 4 + posHeight;	
			if(posHeight > (intHeight-40))
			{
				firstChange = true; 
			}
		}
		else if(secondChange == false)
		{
			posHeight = posHeight-4;
			if(posHeight < 0)
			{
				secondChange = true; 
			}

		}
		else if(thirdChange == false)
		{
			posHeight = posHeight +4;
			if(posHeight > intHeight/2)
			{
				thirdChange = true; 
			}
		}
		else if(fourthChange == false)
		{
			posWidth = posWidth + 4; 
			if(posWidth > (intWidth-40))
			{
				fourthChange = true; 
			}
		}
		else if(fifthChange == false)
		{
			posWidth = posWidth -4; 
			if(posWidth < 0)
			{
				fifthChange = true; 
			}
		}
		else if(calFinished == false)
		{
			posWidth = posWidth +4; 
			if(posWidth > intWidth/2)
			{
				calFinished = true; 
			}
		}
		//System.out.println(posHeight); 
		 
		if(calFinished)
		{
			frame1.setVisible(false);
		}
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

}