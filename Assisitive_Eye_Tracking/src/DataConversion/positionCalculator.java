package DataConversion;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.*;
import javax.swing.JFrame;
import com.imu.IMU;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter; 
import java.io.IOException;
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
	IMU portCom = new IMU();

	calibrationDraw cDraw = new calibrationDraw(0, 0);
	double slopeX = 0; 
	double slopeY = 0; 
	double slopeXHead; 
	double slopeYHead; 
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
	double ylast1;
	double ylast2;
	double ylast3; 
	double ylast4; 
	double ylast5; 
	double ylast6; 
	
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

	
	//Global Variables for head Cal
	double yawCenterAverage =0;
	double pitchCenterAverage =0; 
	double headXCenterAverage =0;  
	double headYCenterAverage =0;  
	double yawMaxAverage =0;  
	double yawMinAverage =0;
	double yawSineMax = 0;
	double yawSineMin = 0;
	double pitchMaxAverage =0;  
	double pitchMinAverage =0;
	double pitchSineMax = 0;
	double pitchSineMin = 0;
	double yawFrame=0; 
	double pitchFrame = 0; 
	double rollFrame = 0;
	double[] frameVector = new double[3];
	
	//Global Variables for head Test
	double headDx; 
	double headDy; 
	double lastHeadXPos; 
	double lastHeadYPos; 
	double lastYaw; 
	double lastPitch; 
	
	public positionCalculator(int choseMethod) throws InterruptedException, IOException
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
		
		portCom.initialize();
		
		if(method == 1)
		{
			calibrateMethodOne();
			testCalibrationOne(); 
			calibrateHeadOne();
			
		}
		else if(method ==2)
		{
			calibrateMethodTwo();
			testCalibrationTwo(); 
			
		}
		else if(method ==3)
		{
			calibrateHeadOne(); 
			//testHeadCalibrationOne();
			dataCalculation();
			
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
	
	public void calibrateHeadOne() throws InterruptedException
	{
		boolean calFinished = false; 
		int count = 0; 
		
		double pitch;
		double roll; 
		double yaw; 
		double yawFrameSum=0; 
		double pitchFrameSum=0; 
		double rollFrameSum=0; 
		
		//How long each calibration time runs
		//NOTE: Any change here must also be made in calibration class
		int intervalTime = 50; 
		

		//Used to find the average position 
		double yawCenterSum =0;  
		double pitchCenterSum =0;  
		double yawMaxSum =0;  
		double yawMinSum =0; 
		double pitchMaxSum =0;  
		double pitchMinSum =0; 	
		double[] yawCenter = new double[intervalTime];
		double[] pitchCenter = new double[intervalTime];
		double[] yawMax = new double[intervalTime];
		double[] yawMin = new double[intervalTime];
		double[] pitchMax = new double[intervalTime];
		double[] pitchMin = new double[intervalTime];
		
		
		double[] vectors = new double[3]; 
		//portCom.startCalibration();
		//portCom.calibrateGyro();
		
		//tracks eye position while having the calibration class move the dot
	while(calFinished == false)
		{
			 
			
			portCom.requestData();
			calFinished = cal.runCal();
			//System.out.println("Is it finsihed " + count);
			//gets the x and y position where the dot is
			calX = cal.getXPosition(); 
			calY = cal.getYPosition(); 
			calXCenter = calX; 
			calYCenter = calY; 
			//gets the x and y position of the ir dot
			vectors = portCom.getSerialData();
			yaw = vectors[2];
			roll = vectors[1]; 
			pitch = vectors[0];
			
			System.out.println("Yaw = " + yaw);
			System.out.println("Pitch = " + pitch);
			//gets position of each dot
			
			
			if(count < intervalTime)
			{
//				yawFrameSum = yawFrameSum + yaw; 
//				pitchFrameSum = pitchFrameSum + pitch; 
//				rollFrameSum = rollFrameSum + roll; 
//				//System.out.println(yawFrameSum + "   " + pitchFrameSum + "   "  + rollFrameSum);
				pitchCenter[count]= pitch;
				yawCenter[count] = yaw; 

			}
			else if(count < 2*intervalTime)
			{
//				yawFrame = yawFrameSum/intervalTime; 
//				pitchFrame = pitchFrameSum/intervalTime; 
//				rollFrame = rollFrameSum/intervalTime; 
//				System.out.println(yawFrame + "   " + pitchFrame + "   "  + rollFrame);
				
				calYMax = calY; 
				pitchMax[count-intervalTime] = pitch;
			}
			else if(count < 3*intervalTime)
			{
				calYMin = calY; 
				pitchMin[count-2*intervalTime] = pitch;
			}	
			else if(count < 4*intervalTime)
			{
				calXMax = calX; 
				yawMax[count-3*intervalTime] = yaw;
			}
			else if(count < 5*intervalTime)
			{
				calXMin = calX;
				yawMin[count-4*intervalTime] = yaw; 
			}
			
			Thread.sleep(100);
			count++;
		}
		
		for(int i =0; i < intervalTime; i++)
		{
			yawCenterSum = yawCenterSum + yawCenter[i];
			pitchCenterSum = pitchCenterSum + pitchCenter[i];
			yawMinSum = yawMinSum + yawMin[i];
			yawMaxSum = yawMaxSum + yawMax[i];
			pitchMinSum = pitchMinSum + pitchMin[i];
			pitchMaxSum = pitchMaxSum + pitchMax[i];
		}
		
		pitchCenterAverage = pitchCenterSum/intervalTime;  
		yawCenterAverage = yawCenterSum/intervalTime;  
		
		yawMaxAverage = yawMaxSum/intervalTime;  
		yawMinAverage = yawMinSum/intervalTime;
		// Convert to sine values for testing
		yawSineMax = Math.sin(Math.toRadians(yawMaxAverage))*100;
		yawSineMin = Math.sin(Math.toRadians(yawMinAverage))*100;
		
		pitchMaxAverage = pitchMaxSum/intervalTime;  
		pitchMinAverage = pitchMinSum/intervalTime; 
		
		pitchSineMax = Math.sin(Math.toRadians(pitchMaxAverage))*100;
		pitchSineMin = Math.sin(Math.toRadians(pitchMinAverage))*100;
		
		System.out.println(yawMaxAverage);
		System.out.println(yawMinAverage);
		System.out.println(pitchMaxAverage);
		System.out.println(pitchMinAverage);
		
		//slopeXHead = (calXMin-calXMax)/(yawMinAverage-yawMaxAverage);
		slopeXHead = (calXMin-calXMax)/(yawMinAverage-yawMaxAverage);
		//slopeYHead = (calYMin-calYMax)/(pitchMinAverage-pitchMaxAverage);
		slopeYHead = (calYMin-calYMax)/(pitchMinAverage-pitchMaxAverage);

		
		
		System.out.println("pitch  " + pitchCenterAverage+ "  " + yawCenterAverage);
		System.out.println("yaw  " + yawSineMin + "  " + yawSineMax);
		System.out.println("   " + slopeX);
	}
	
	public void testCalibrationOne()
	{
		frame1.setVisible(true);
		int posHeight = 0; 
		int posWidth = 0; 
		while(true)
		{
			posHeight = getYEyePos(); 
			posWidth = getXEyePos(1);
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
			posHeight = getYEyePos(); 
			posWidth = getXEyePos(2);
			System.out.println("x = " + posWidth);
			cDraw.setPoints(intHeight/2, posWidth);
			frame1.repaint();

		}
	}
	public void testHeadCalibrationOne() throws InterruptedException
	{
		frame1.setVisible(true);
		double posHeight = cal.getYCenter();
		double posWidth = cal.getXCenter(); 
	
		lastYaw = yawCenterAverage; 
		lastPitch = pitchCenterAverage;  
		
		double[] vectors = new double[3];
		double foundHeight = 0; 
		double foundWidth = 0; 
		 //Wait just to make sure data is working
		for(int x = 0; x < 20; x++)
		{
			portCom.requestData();
			vectors = portCom.getSerialData();
		
			foundHeight = vectors[0]; 
			foundWidth = vectors[2];
			
			headDx = lastYaw - foundWidth;   
			headDy = lastPitch - foundHeight; 
			lastHeadYPos = posHeight; 
			lastHeadXPos = posWidth;
			lastPitch = vectors[0];
			lastYaw = vectors[2];
		}
		
		//gets data 
		while(true)
		{
			 
			portCom.requestData();
	
			posWidth = getXHeadPos(1);
			posHeight = getYHeadPos(1); 

			cDraw.setPoints((int)posHeight, (int)posWidth);
			Thread.sleep(50);
			frame1.repaint();

		}
		/*
		while(true)
		{
			portCom.requestData();
			
			
			posHeight = getYHeadPos(1); 
			posWidth = getXHeadPos(1);
			
			
			
			if(posWidth > calXMax)
			{
				posWidth = calXMax; 
			}
			if(posWidth > calYMax)
			{
				posHeight = calYMax; 
			}
			if(posWidth < calXMin)
			{
				posWidth = calXMin; 
			}
			if(posWidth < calYMin)
			{
				posHeight = calYMin; 
			}
			
			
			System.out.println(posWidth + "   " + posHeight);
			cDraw.setPoints(posHeight, posWidth);
			Thread.sleep(50);
			frame1.repaint();

		}
		*/
		
	}
	
	
	public void dataCalculation() throws IOException, InterruptedException
	{
		
		cal.resetRuntimes(); 
		File outFile = new File("C:\\Users\\David\\Desktop\\out.txt");
		FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		boolean calFinished =false; 
		double[] vectors = new double[3]; 
		
		double yaw = 0; 
		double roll = 0; 
		double pitch = 0; 
		
		int dotLocationX = 0; 
		int dotLocationY = 0; 
		int gazeLocationX =0; 
		int gazeLocationY =0; 
		
		while(calFinished == false)
		{
			 
			portCom.requestData();
			calFinished = cal.runCal();

			dotLocationX = cal.getXPosition(); 
			dotLocationY = cal.getYPosition(); 
			gazeLocationX = (int)getXHeadPos(1);
			gazeLocationY = (int)getYHeadPos(1);
			
			try
			{
				bw.write("" + dotLocationX + " " + gazeLocationX + " " + dotLocationY + " " + gazeLocationY);
			}
			catch(IOException e)
			{
				
			}
			Thread.sleep(50);
		}
		
		bw.close(); 
	}
	
	public int getXEyePos(int mode)
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
				double scale = (newPos*Math.sqrt(calXMax)/eyeXMaxAverage)-Math.sqrt(calXMax);
				calculation = -(Math.pow(scale, 2)-calXLowerMid);
				calculation = calculation + calXMin;
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
	
	public int getYEyePos()
	{
		int yPosition = 0; 
		return yPosition; 
	}
	
	public double getXHeadPos(int mode)
	{
		/*
		double[] vectors = new double[3];
		vectors = portCom.getSerialData();
		double newPos = vectors[2];
		
		System.out.println("Yaw " + newPos);
		//calculation = slopeXHead*(Math.sin(Math.toRadians(newPos))*100-yawSineMin)+calXMin;
		calculation = slopeXHead*(newPos-yawSineMin)+calXMin;
		*/
		double calculation; 
		double[] vectors = new double[3];
		double foundWidth = 0; 
		
		portCom.requestData();
		vectors = portCom.getSerialData();	
		foundWidth = vectors[2]; 
		System.out.println("Yaw " + foundWidth);
		
		headDx = lastYaw - foundWidth;  	
		calculation = lastHeadXPos + headDx*23; 
		lastHeadXPos = calculation; 
		
		lastYaw= vectors[2];
		
		xlast1 = xlast2; 
		xlast2 = xlast3;
		xlast3 = xlast4; 
		xlast4 = xlast5; 
		xlast5 = xlast6; 
		xlast6 = calculation; 
		
		calculation = (-.05156*xlast1 + .28176*xlast2 + .60313*xlast3+ .28176*xlast4+-.05156*xlast5);

		return calculation; 
		
	}
	public double getYHeadPos(int mode)
	{
		/*
		double[] vectors = new double[3];
		vectors = portCom.getSerialData();
		double newPos = vectors[0];
		System.out.println("Pitch " + newPos);
		//System.out.println(vectors[0]);
		//double mycalculation = slopeYHead*(Math.sin(Math.toRadians(newPos))*100-pitchSineMin)+calYMin;
		double mycalculation = slopeYHead*(newPos-pitchSineMin)+calYMin;
		*/
		double mycalculation; 
		double[] vectors = new double[3];
		double foundHeight = 0; 
		
		portCom.requestData();
		vectors = portCom.getSerialData();	
		foundHeight = vectors[0]; 
		System.out.println("Pitch " +foundHeight);
		
		headDy = lastPitch - foundHeight;  	
		mycalculation = lastHeadYPos + headDy*23; 
		lastHeadYPos = mycalculation; 
		
		lastPitch = vectors[0];

		
		ylast1 = ylast2; 
		ylast2 = ylast3;
		ylast3 = ylast4; 
		ylast4 = ylast5; 
		ylast5 = ylast6; 
		ylast6 = mycalculation; 
		
		mycalculation = (-.05156*ylast1 + .28176*ylast2 + .60313*ylast3+ .28176*ylast4+-.05156*ylast5);
		
		return mycalculation; 
		
	}
	
	public int getXPos()
	{
		int xValue = 0;
		
		//xValue = getXHeadPos(1) + getXEyePos(1); 
		
		return xValue; 
	}
	public int getYPos()
	{
		int yValue = 0;
		
		//yValue = getYHeadPos(1) + getYEyePos(); 
		
		return yValue; 
	}
}
