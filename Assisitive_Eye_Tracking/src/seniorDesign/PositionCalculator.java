package seniorDesign;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PositionCalculator
{
	int eyeX = 0; 
	int eyeY = 0; 
	int calX = 0; 
	int calY = 0; 
	
	double slopeX = 0; 
	double slopeY = 0; 
	double bX = 0; 
	double bY = 0; 
	JFrame frame1 =  new JFrame("Replay");
	
	ImageCapture IC; 
	
	
	public PositionCalculator() throws InterruptedException
	{
		int sumXeye = 0; 
		int sumXcal = 0;
		int sumYeye = 0; 
		int sumYcal = 0; 
		double xNumTot = 0; 
		double yNumTot = 0; 
		double xDenTot = 0; 
		double yDenTot = 0; 

		//Testing initializtation start 
		int intWidth = 0;
		int intHeight = 0; 
		calibrationDraw cDraw = new calibrationDraw(0, 0);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();
		intWidth = width.intValue();
		intHeight = height.intValue();
		frame1.add(cDraw);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(intWidth,intHeight);
		frame1.setBounds(0,0,frame1.getWidth(), frame1.getHeight());
		frame1.setVisible(false);
		double xEyeAverage = 0; 
		double yEyeAverage = 0; 
		double xCalAverage = 0; 
		double yCalAverage = 0; 
		
		boolean calFinished = false; 
		calibration cal = new calibration();
		IC = new ImageCapture(); 
		int indexNum = 0; 
	
		int[] xCalPositions = new int[1035];
		int[] xEyePositions = new int[1035];	
		int[] yCalPositions = new int[1035];
		int[] yEyePositions = new int[1035];
		
		//Gathers the calibration Data
		while(indexNum < 1034)
		{
			indexNum = indexNum+1; 
			calFinished =cal.runCal();
			
			//gets position of the calibration dot
			xCalPositions[indexNum] = cal.getXPosition(); 
			yCalPositions[indexNum] = cal.getYPosition(); 
			//System.out.println("x = " + xCalPositions[indexNum] + "   y = " + yCalPositions[indexNum]);
			
			//gets position of the eye
			IC.findPosition(); 
			xEyePositions[indexNum]=IC.getEyePosX(); 
			yEyePositions[indexNum]=IC.getEyePosY(); 
			//System.out.println("x = " + xEyePositions[indexNum] + "   y = " + yEyePositions[indexNum]);
			
			//sums for best fit line
			sumXeye = sumXeye+xEyePositions[indexNum];
			sumYeye = sumYeye+yEyePositions[indexNum];
			sumXcal = sumXcal+xCalPositions[indexNum];
			sumYcal = sumYcal+yCalPositions[indexNum];
					
			Thread.sleep(5);
		
		}	
		
		xCalAverage = sumXcal/1034;
		yCalAverage = sumYcal/1034; 
		xEyeAverage = sumXeye/1034; 
		yEyeAverage = sumYeye/1034; 
		
		for(int i = 0; i<1035; i++)
		{
			xNumTot = xNumTot +(xEyePositions[i]-xEyeAverage)*(xCalPositions[i]-xCalAverage);
			yNumTot = yNumTot +(yEyePositions[i]-yEyeAverage)*(yCalPositions[i]-yCalAverage);
			xDenTot = xNumTot +(xEyePositions[i]-xEyeAverage)*(xEyePositions[i]-xEyeAverage);
			yDenTot = yNumTot +(yEyePositions[i]-yEyeAverage)*(yEyePositions[i]-yEyeAverage);
		}
		slopeX = xNumTot/xDenTot; 
		slopeY = yNumTot/yDenTot; 
		
		bX = xCalAverage - xEyeAverage * slopeX; 
		bY = yCalAverage - yEyeAverage * slopeY; 
		System.out.println("" + bX + "  " + slopeX); 
		
		frame1.setVisible(true);
		//test loop
		int posHeight=0; 
		int posWidth=0; 
		for(int i = 0; i<1035; i++)
		{
			posWidth = (int)(slopeX * xEyePositions[i] + bX); 
			posHeight = (int)(slopeY * yEyePositions[i] + bY); 
			System.out.println("x = " + posWidth + "   " + xEyePositions[i] + "  " + xCalPositions[i]);
			System.out.println("y = " + posHeight + "   " + yEyePositions[i]+ "  " + xCalPositions[i]);
			cDraw.setPoints(posHeight, posWidth);
			frame1.repaint(); 
			Thread.sleep(5);
		}
	}
	
	public int getXPos()
	{
		IC.findPosition();  
		return(IC.getEyePosY());
	}
	
	public int getYPos()
	{
		IC.findPosition();  
		return(IC.getEyePosX());		
	}
}