package DataConversion;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class calibrationDraw extends JPanel
{
	int posHeight = 0; 
	int posWidth = 0; 
	
	public calibrationDraw(int newHeight, int newWidth)
	{
		posHeight = newHeight;
		posWidth = newWidth; 
	}
	public calibrationDraw()
	{
		
	}
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLUE);
		g.fillOval(posWidth, posHeight, 20, 20);
		
	}
	public void setPoints(int newHeight, int newWidth)
	{
		posHeight = newHeight; 
		posWidth = newWidth;
	}
}
