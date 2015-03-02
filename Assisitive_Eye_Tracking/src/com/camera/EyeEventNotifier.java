package com.camera;

import java.awt.Point;

public class EyeEventNotifier 
{
	private EyeMotionListener listener;
	private Point eyePos;
	
	public EyeEventNotifier (EyeMotionListener listener)
	{
		this.listener = listener;
	}
	
	public void mousePosUpdate(double x, double y)
	{
		Point newPos = new Point();
		newPos.x = (int) x;
		newPos.y = (int) y;
		if (!newPos.equals(eyePos))
		{
			// Eye Position has moved, inform functions
			eyePos = newPos;
			listener.eyeMoved(eyePos);
		}
	}
}
