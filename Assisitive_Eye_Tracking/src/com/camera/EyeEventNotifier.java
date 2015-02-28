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
	
	public void mousePosUpdate(Point newPos)
	{
		if (!newPos.equals(eyePos))
		{
			// Eye Position has moved, inform functions
			eyePos = newPos;
			listener.eyeMoved(eyePos);
		}
	}
}
