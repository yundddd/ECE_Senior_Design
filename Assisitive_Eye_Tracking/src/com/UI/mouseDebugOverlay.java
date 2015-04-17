package com.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

class mouseDebugOverlay extends JComponent
{
	Point posMouse;
	Point absPos;
	int radius;
	JFrame parent;
	JTextArea posUpdate;
	
	public mouseDebugOverlay(JFrame parent, JTextArea posUpdate)
	{
		radius = 5;
		this.parent = parent;
		this.posUpdate = posUpdate;
		this.absPos = new Point();
		this.posMouse = new Point();
		//this.addMouseMotionListener(this);
	}
	
	protected void paintComponent(Graphics g)
	{
		if (posMouse != null)
		{
			g.setColor(Color.blue);
			g.fillOval(posMouse.x - radius, posMouse.y - radius, radius*2, radius*2);
		}
	}
	
	public void setPos(Point currentPos)
	{
		posMouse = currentPos;
		this.repaint();
		updatePosFrame();
	}
	
	public void setAbsPos(Point absPos)
	{
		this.absPos = absPos;
	}
	
	private void updatePosFrame()
	{
		posUpdate.setText("Window X: ".concat(Integer.toString(posMouse.x)).concat("\nWindow Y: ".concat(Integer.toString(posMouse.y))).concat("\n").concat(
		  "Eye X: ".concat(Integer.toString(absPos.x)).concat("\nEye Y: ".concat(Integer.toString(absPos.y))).concat("\n")));
	}
	
}