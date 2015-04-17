package com.UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class highlightLabel extends JLabel
{
	private final Color highlightColor;
	private State state;
	private Color currentColor;
	private Color finalColor;
	private int animCounter = 0;
	private boolean highlighted = false;
	// Color animation variables
	private int deltaRed = 0;
	private int deltaGreen = 0;
	private int deltaBlue = 0;
	
	public static enum State 
	{
		MOUSE_ENTERED, NORMAL, TRANSITION, MOUSE_HIGHLIGHT
	}
	public highlightLabel(String s, Color highlightColor)
	{
		this.setText(s);
		this.highlightColor = highlightColor;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFont(this.getFont().deriveFont(64.0f));
		this.state = State.NORMAL;
	}
	
	public boolean isHighlighted()
	{
		return highlighted;
	}
	
	public void highlight(boolean b)
	{
		/** Set the label opaque and change the background color */
		highlighted = b;
		if (b & (state == State.NORMAL))
		{
			this.setOpaque(true);
			this.currentColor = highlightColor;
			this.setBackground(highlightColor);
			this.repaint();
		}
		else if( !b & state == State.NORMAL)
		{
			this.setOpaque(false);
			this.setBackground(null);
		}
	}
	
	public void setState(State s)
	{
		/** Update label state based on mouse position and timer updates. */
		state = s;
	}
	public State getState()
	{
		return state;
	}
	public void setColor(Color c)
	{
		/** Set the current color depending on current state */
		if (state == State.NORMAL)
		{
			this.setBackground(c);
			this.currentColor = c;
		}
	}
	
	public void setFinalColor(Color c)
	{
		this.finalColor = c;
	}
	
	public boolean update()
	{
		/** Called every frame tick by the main ActionEvent handler. 
		 *  Allows the label to update state if in animation, 
		 *  finish animating, and change state. 
		 */
		switch (state)
		{
			case NORMAL:
				// DO NOTHING
				break;
			case MOUSE_ENTERED:
				// Start animation for mouse highlight color
				// Generate step sizes
				this.currentColor = this.getBackground();
				deltaRed = (int) Math.floor((finalColor.getRed() - currentColor.getRed()) / 30);
				deltaGreen = (int) Math.floor((finalColor.getGreen() - currentColor.getGreen()) / 30);
				deltaBlue = (int) Math.floor((finalColor.getBlue() - currentColor.getBlue()) / 30);
				this.state = State.TRANSITION;
				break;
			case TRANSITION:
				// Update background color every frame & count how many ticks have elapsed
				Color nextColor = new Color(currentColor.getRed() + deltaRed, currentColor.getGreen() + deltaGreen,
						currentColor.getBlue() + deltaBlue);
				this.setBackground(nextColor);
				this.currentColor = nextColor;
				animCounter++;
				if (animCounter == 30)
				{
					this.state = State.MOUSE_HIGHLIGHT;
				}
				break;
			case MOUSE_HIGHLIGHT:
				animCounter = 0;
				return true;
		}
		return false;
	}

}
