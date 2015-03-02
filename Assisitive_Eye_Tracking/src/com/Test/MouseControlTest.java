package com.Test;

import seniorDesign.ImageCapture;

import com.UI.GUITrial;
import com.UI.EyeMouseController;
import com.camera.EyeEventNotifier;

public class MouseControlTest 
{
	private static GUITrial gui;
	private static ImageCapture cap;
	private static EyeMouseController controller;
	private static EyeEventNotifier notifier;
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		System.out.println("MouseTest Program.");
		gui = new GUITrial();
		// Allow the main GUI to update
		gui.main(args);
		controller = new EyeMouseController(gui);
		notifier = new EyeEventNotifier(controller);
		cap = new ImageCapture(notifier);
		while (true)
		{
			cap.findPosition();
		}
	}

}
