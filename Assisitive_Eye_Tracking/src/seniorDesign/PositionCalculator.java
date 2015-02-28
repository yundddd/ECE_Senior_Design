package seniorDesign;

public class PositionCalculator
{
	int eyeX = 0; 
	int eyeY = 0; 
	int calX = 0; 
	int calY = 0; 
	
	ImageCapture IC; 
	
	public PositionCalculator() throws InterruptedException
	{
		boolean calFinished = false; 
		calibration cal = new calibration();
		IC = new ImageCapture(); 
		int indexNum = 0; 
	
		int[] xCalPositions = new int[1034];
		int[] xEyePositions = new int[1034];	
		int[] yCalPositions = new int[1034];
		int[] yEyePositions = new int[1034];
		
		while(calFinished == false)
		{
			indexNum = indexNum+1; 
			calFinished =cal.runCal(); 
			//xCalPositions[indexNum] = cal.getXPosition(); 
			//yCalPositions = cal.getYPosition(); 
			
			IC.findPosition(); 
			//xEyePositions=IC.getEyePosX(); 
			//xEyePositionsIC.getEyePosY(); 
			
			Thread.sleep(5);
		
		}	
		
		System.out.println(indexNum); 
	}
	
	public int getXPos()
	{
		IC.findPosition();  
		return(IC.getEyePosX());
	}
	
	public int getYPos()
	{
		IC.findPosition();  
		return(IC.getEyePosY());		
	}
}
