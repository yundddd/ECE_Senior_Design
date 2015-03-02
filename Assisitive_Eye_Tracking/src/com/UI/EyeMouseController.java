package com.UI;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.camera.EyeMotionListener;

public class EyeMouseController implements EyeMotionListener
{
	/* This class takes X,Y commands from the eye tracker and converts them to mouse movements. */
	private Point posEye;
	private int radius;
	private GUITrial gui;
	private Robot robot;
	
	//Constants
	private static final int USER_CLICK_DURATION_MS = 20; 
	
	public EyeMouseController(GUITrial gui)
	{
		radius = 20;
		this.gui = gui;
		newRobot();
	}
	public EyeMouseController( GUITrial gui, int radius)
	{
		this.radius = radius;
		this.gui = gui;
		newRobot();
	}
	private void newRobot()
	{
		// Make a new robot to generate mouse commands
				try {
					robot = new Robot();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Your OS does not support low-level access. Please change permissions for this program.");
				}
	}
	
	public void eyeMoved(Point p)
	{
		// Assuming point P is global (X,Y) coordinates
		//robot.mouseMove(p.x, p.y);
		System.out.println("Would have moved mouse to X,Y");
		System.out.println(p.x);
		System.out.println(p.y);
		gui.posUpdate(p);
	}
	
	public void eyeClick()
	{
		// Input a click
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(USER_CLICK_DURATION_MS);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
}