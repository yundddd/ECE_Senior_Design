package DataConversion;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.*;
import javax.swing.JFrame;

/*
import seniorDesign.ImageCapture;
import seniorDesign.calibration;
import seniorDesign.calibrationDraw;
*/

public class positionCalculator 
{
	//used for deciding method for calibration 
	int method; 
	
	//Global Variables for all methods
	calibration cal = new calibration(); 
	JFrame frame1 =  new JFrame("Replay");
	imageCapture IC = new imageCapture(); 
	calibrationDraw cDraw = new calibrationDraw(0, 0);
	double slopeX = 0; 
	double slopeY = 0; 
	double bX = 0; 
	double bY = 0; 
	int eyeX = 0; 
	int eyeY = 0; 
	int calX = 0; 
	int calY = 0; 
	int intHeight;
	int intWidth;
	double calculation;
	
	//Filtering Data
	double xlast1;
	double xlast2;
	double xlast3; 
	double xlast4; 
	double xlast5; 
	double xlast6; 
	
	//Global Variables for method 1
	double eyeXCenterAverage =0;  
	double eyeYCenterAverage =0;  
	double eyeXMaxAverage =0;  
	double eyeXMinAverage =0; 
	double eyeYMaxAverage =0;  
	double eyeYMinAverage =0; 
	int calXCenter = 0;
	int calYCenter = 0;
	int calXMax =0;
	int calXMin =0;
	int calYMax =0; 
	int calYMin =0;
	
	//Global Variables for method 2
	int calXUpperMid =0;
	int calXLowerMid =0; 
	double eyeXUpperMidAverage =0; 
	double eyeXLowerMidAverage =0; 
	double slopeXLower;
	double slopeXUpper; 

	public positionCalculator(int choseMethod) throws InterruptedException
	{
		//Chooses which algorithm will be used for the calibration 
		method = choseMethod; 
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
		System.out.print("Total width " + intWidth);
		if(method == 1)
		{
			calibrateMethodOne();
			testCalibrationOne(); 
		}
		else if(method ==2)
		{
			calibrateMethodTwo();
			testCalibrationTwo(); 
			
		}
		
	}
	
//===========================================================================================
	public void calibrateMethodOne()
	{

		
		boolean calFinished = false; 
		int count = 0; 
		
		//How long each calibration time runs
		//NOTE: Any change here must also be made in calibration class
		int intervalTime = 50; 
		

		//Used to find the average position 
		int eyeXCenterSum =0;  
		int eyeYCenterSum =0;  
		int eyeXMaxSum =0;  
		int eyeXMinSum =0; 
		int eyeYMaxSum =0;  
		int eyeYMinSum =0; 
		int[] eyeXCenter = new int[intervalTime];
		int[] eyeYCenter = new int[intervalTime];	
		int[] eyeXMax = new int[intervalTime];
		int[] eyeXMin = new int[intervalTime];
		int[] eyeYMax = new int[intervalTime];
		int[] eyeYMin = new int[intervalTime];
		
		//tracks eye position while having the calibration class move the dot
		while(calFinished == false)
		{
			 
			
			calFinished = cal.runCal();
			
			//gets the x and y position where the dot is
			calX = cal.getXPosition(); 
			calY = cal.getYPosition(); 
			
			//gets the x and y position of the ir dot
			IC.findPosition(); 
			eyeX=IC.getEyePosX(); 
			eyeY=IC.getEyePosY(); 
			
			//gets position of each dot
			if(count < intervalTime)
			{
				eyeXCenterSum = calX;
				eyeYCenterSum = calY; 
				eyeXCenter[count] = eyeX;
				eyeYCenter[count] = eyeY; 
			}
			else if(count < 2*intervalTime)
			{
				calYMax = calY; 
				eyeYMax[count-intervalTime] = eyeY;
			}
			else if(count < 3*intervalTime)
			{
				calYMin = calY; 
				eyeYMin[count-2*intervalTime] = eyeY;
			}	
			else if(count < 4*intervalTime)
			{
				calXMax = calX; 
				eyeXMax[count-3*intervalTime] = eyeX;
			}
			else if(count < 5*intervalTime)
			{
				calXMin = calX;
				eyeXMin[count-4*intervalTime] = eyeX; 
			}
			
			count++;
		}
		
		for(int i =0; i < intervalTime; i++)
		{
			eyeYCenterSum = eyeYCenterSum + eyeYCenter[i];
			eyeXCenterSum = eyeXCenterSum + eyeXCenter[i];
			eyeXMinSum = eyeXMinSum + eyeXMin[i];
			eyeXMaxSum = eyeXMaxSum + eyeXMax[i];
			eyeYMinSum = eyeYMinSum + eyeYMin[i];
			eyeYMaxSum = eyeYMaxSum + eyeYMax[i];
		}
		
		eyeXCenterAverage =eyeXCenterSum/intervalTime;  
		eyeYCenterAverage =eyeYCenterSum/intervalTime;  
		eyeXMaxAverage = eyeXMaxSum/intervalTime;  
		eyeXMinAverage = eyeXMinSum/intervalTime; 
		eyeYMaxAverage = eyeYMaxSum/intervalTime;  
		eyeYMinAverage = eyeYMinSum/intervalTime; 
		
		slopeX = (calXMin-calXMax)/(eyeXMinAverage-eyeXMaxAverage);
		slopeY = (calYMin-calYMax)/(eyeYMinAverage-eyeYMaxAverage);
		
		System.out.println("  " + calXMax + "  " + calXMin);
		System.out.println("  " + eyeXMaxAverage + "  " + eyeXMinAverage);
		System.out.println("   " + slopeX);
	}
	
	public void calibrateMethodTwo()
	{
		boolean calFinished = false; 
		int count = 0; 
		
		//How long each calibration time runs
		//NOTE: Any change here must also be made in calibration class
		int intervalTime = 100; 
		

		//Used to find the average position 
		int eyeXCenterSum =0;  
		int eyeYCenterSum =0;  
		int eyeXMaxSum =0;  
		int eyeXMinSum =0;
		int eyeXUpperMidSum =0; 
		int eyeXLowerMidSum =0; 
		int eyeYMaxSum =0;  
		int eyeYMinSum =0; 

		int[] eyeXCenter = new int[intervalTime];
		int[] eyeYCenter = new int[intervalTime];	
		int[] eyeXMax = new int[intervalTime];
		int[] eyeXMin = new int[intervalTime];
		int[] eyeYMax = new int[intervalTime];
		int[] eyeYMin = new int[intervalTime];
		int[] eyeXUpperMid = new int[intervalTime];
		int[] eyeXLowerMid = new int[intervalTime];
		
		//tracks eye position while having the calibration class move the dot
		while(calFinished == false)
		{
			 
			
			calFinished = cal.runCalTwo();
			
			//gets the x and y position where the dot is
			calX = cal.getXPosition(); 
			calY = cal.getYPosition(); 
			
			//gets the x and y position of the ir dot
			IC.findPosition(); 
			eyeX=IC.getEyePosX(); 
			eyeY=IC.getEyePosY(); 
			
			//gets position of each dot
			if(count < intervalTime)
			{
				eyeXCenterSum = calX;
				eyeYCenterSum = calY; 
				eyeXCenter[count] = eyeX;
				eyeYCenter[count] = eyeY; 
			}
			else if(count < 2*intervalTime)
			{
				calYMax = calY; 
				eyeYMax[count-intervalTime] = eyeY;
			}
			else if(count < 3*intervalTime)
			{
				calYMin = calY; 
				eyeYMin[count-2*intervalTime] = eyeY;
			}	
			else if(count < 4* intervalTime)
			{
				calXUpperMid = calX;
				eyeXUpperMid[count-3*intervalTime] = eyeX; 
			}
			else if(count < 5*intervalTime)
			{
				calXLowerMid = calX;
				eyeXLowerMid[count-4*intervalTime] = eyeX;
			}
			else if(count < 6*intervalTime)
			{
				calXMax = calX; 
				eyeXMax[count-5*intervalTime] = eyeX;
			}
			else if(count < 7*intervalTime)
			{
				calXMin = calX;
				eyeXMin[count-6*intervalTime] = eyeX; 
			}
			
			count++;
		}
		
		for(int i =0; i < intervalTime; i++)
		{
			eyeYCenterSum = eyeYCenterSum + eyeYCenter[i];
			eyeXCenterSum = eyeXCenterSum + eyeXCenter[i];
			eyeXMinSum = eyeXMinSum + eyeXMin[i];
			eyeXMaxSum = eyeXMaxSum + eyeXMax[i];
			eyeXUpperMidSum = eyeXUpperMidSum + eyeXUpperMid[i];
			eyeXLowerMidSum = eyeXLowerMidSum + eyeXLowerMid[i];
			eyeYMinSum = eyeYMinSum + eyeYMin[i];
			eyeYMaxSum = eyeYMaxSum + eyeYMax[i];
		}
		
		eyeXCenterAverage =eyeXCenterSum/intervalTime;  
		eyeYCenterAverage =eyeYCenterSum/intervalTime;  
		eyeXMaxAverage = eyeXMaxSum/intervalTime;  
		eyeXMinAverage = eyeXMinSum/intervalTime; 
		eyeXUpperMidAverage = eyeXUpperMidSum/intervalTime; 
		eyeXLowerMidAverage = eyeXLowerMidSum/intervalTime; 
		eyeYMaxAverage = eyeYMaxSum/intervalTime;  
		eyeYMinAverage = eyeYMinSum/intervalTime; 
		
		slopeX = (calXLowerMid-calXUpperMid)/(eyeXLowerMidAverage-eyeXUpperMidAverage);
		slopeY = (calYMin-calYMax)/(eyeYMinAverage-eyeYMaxAverage);
		slopeXLower = (calXMin-calXLowerMid)/(eyeXMinAverage-eyeXLowerMidAverage);
		slopeXUpper = (calXUpperMid-calXMax)/(eyeXUpperMidAverage-eyeXMaxAverage);
		
		System.out.println("  " + calXMax + "  " + calXMin);
		System.out.println("  " + eyeXMaxAverage + "  " + eyeXMinAverage);
		System.out.println("   " + slopeX+ "   "+ slopeXLower + "   " + slopeXUpper);
	}
	
	public void testCalibrationOne()
	{
		frame1.setVisible(true);
		int posHeight = 0; 
		int posWidth = 0; 
		while(true)
		{
			posHeight = getYPos(); 
			posWidth = getXPos(1);
			cDraw.setPoints(intHeight/2, posWidth);
			frame1.repaint();

		}
	}
	public void testCalibrationTwo()
	{
		frame1.setVisible(true);
		int posHeight = 0; 
		int posWidth = 0; 
		while(true)
		{
			posHeight = getYPos(); 
			posWidth = getXPos(2);
			System.out.println("x = " + posWidth);
			cDraw.setPoints(intHeight/2, posWidth);
			frame1.repaint();

		}
	}
	public int getXPos(int mode)
	{
		IC.findPosition(); 
		int newPos = IC.getEyePosX(); 
		
		if(mode == 1)
		{
			calculation = slopeX*(newPos-eyeXMinAverage)+calXMin;
		}
		else if(mode ==2)
		{
			if(newPos<eyeXLowerMidAverage)
			{
				calculation = -(((newPos-eyeXLowerMidAverage)-eyeXMinAverage)*((newPos-eyeXLowerMidAverage)-eyeXMinAverage)-calXLowerMid);
				calculation = calculation +calXMin;
			}
			else if(newPos < eyeXUpperMidAverage)
			{
				calculation = slopeX*(newPos-eyeXLowerMidAverage)+calXLowerMid;
			}
			else
			{
				calculation = ((slopeXUpper*(newPos-eyeXUpperMidAverage))-calXUpperMid);
				calculation = (calculation * calculation)+calXUpperMid;
			}
		}
		
		
		xlast1 = xlast2; 
		xlast2 = xlast3;
		xlast3 = xlast4; 
		xlast4 = xlast5; 
		xlast5 = xlast6; 
		xlast6 = calculation; 
		
		calculation = (-.05156*xlast1 + .28176*xlast2 + .60313*xlast3+ .28176*xlast4+-.05156*xlast5);

		return (int)calculation; 
		
	}
	public int getYPos()
	{
		int yPosition = 0; 
		return yPosition; 
	}

}
