package com.UI;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;

import java.lang.String;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.camera.EyeEventNotifier;
public class GUITrial implements ActionListener, MouseMotionListener, Runnable {
	
	private JFrame letterFrame;
	private JFrame posFrame;
	private LayoutManager layout;
	private LayoutManager posLayout;
	private JTextArea posUpdate;
	// CONSTANTS
	private static final int MIN_X_RESOLUTION = 640;
	private static final int MIN_Y_RESOLUTION = 480;
	private static final int POSFRAME_X = 200;
	private static final int POSFRAME_Y = 200;
	private static String letterTitle = "Eye Tracking Calibrator";
	private static String posTitle = "Eye Position";
	
	private mouseOverlay overlay;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new GUITrial());
	}

	/**
	 * Create the application.
	 */
	public GUITrial() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		letterFrame = new JFrame();
		letterFrame.setTitle(letterTitle);
		
		posFrame = new JFrame();
		posFrame.setTitle(posTitle);
		letterFrame.setBounds(100, 100, 450, 300);
		letterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		posFrame.setMinimumSize(new Dimension(POSFRAME_X, POSFRAME_Y));
		
		layout = new GridLayout(0, 3, 0, 0);
		letterFrame.getContentPane().setLayout(layout);
		letterFrame.setMinimumSize(new Dimension(MIN_X_RESOLUTION, MIN_Y_RESOLUTION));
		
		// Position Frame Elements
		
		posLayout = new GridLayout(2, 1);
		posFrame.setLayout(posLayout);
				
		posUpdate = new JTextArea();
		System.out.println(posUpdate);
		posFrame.add(posUpdate);
				
		JButton calibrateButton = new JButton("Calibrate");
		calibrateButton.addActionListener(this);
		posFrame.add(calibrateButton);
				
		overlay = new mouseOverlay(letterFrame,posUpdate);
		
		JLabel label = new JLabel("");
		letterFrame.getContentPane().add(label);
		
		JLabel lblA = new JLabel("A");
		lblA.setVerticalAlignment(SwingConstants.TOP);
		lblA.setHorizontalAlignment(SwingConstants.CENTER);
		labelMouseListener aListener = new labelMouseListener(lblA, overlay);
		lblA.addMouseListener(aListener);
		lblA.addMouseMotionListener(aListener);
		letterFrame.getContentPane().add(lblA);
		
		JLabel label_1 = new JLabel("");
		letterFrame.getContentPane().add(label_1);
		
		JLabel lblC = new JLabel("C");
		letterFrame.getContentPane().add(lblC);
		
		JLabel lblE = new JLabel("E");
		lblE.setHorizontalAlignment(SwingConstants.CENTER);
		letterFrame.getContentPane().add(lblE);
		
		JLabel lblD = new JLabel("D");
		lblD.setHorizontalAlignment(SwingConstants.RIGHT);
		letterFrame.getContentPane().add(lblD);
		
		JLabel label_2 = new JLabel("");
		letterFrame.getContentPane().add(label_2);
		
		JLabel lblB = new JLabel("B");
		lblB.setVerticalAlignment(SwingConstants.BOTTOM);
		lblB.setHorizontalAlignment(SwingConstants.CENTER);
		letterFrame.getContentPane().add(lblB);
		
		JLabel label_3 = new JLabel("");
		letterFrame.getContentPane().add(label_3);
		
		
		letterFrame.setGlassPane(overlay);
		letterFrame.addMouseMotionListener(this);
		
	}
	
	public void setVisible(boolean state)
	{
		letterFrame.setVisible(state);
		posFrame.setVisible(state);
		overlay.setVisible(state);
	}
	
	public JFrame getPosFrame()
	{
		/* Get JFrame which contains the Calibrate button and Eye pos corrdinates. */
		return posFrame;
	}

	public JTextArea getPosUpdateArea()
	{
		/* Get the JTextArea to write Mouse Coordinates to. */
		System.out.println(posUpdate);
		return posUpdate;
	}
	
	public void posUpdate(Point posEye)
	{
		posUpdate.setText("Eye X: ".concat(Integer.toString(posEye.x).concat("\nEye Y: ").concat(Integer.toString(posEye.y))).concat("\n"));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Start Calibration...");
		// Add calibration code HERE
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		overlay.setAbsPos(e.getLocationOnScreen());
		overlay.setPos(SwingUtilities.convertPoint(letterFrame, e.getPoint(), overlay));
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			GUITrial window = new GUITrial();
			window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

class labelMouseListener implements MouseListener, MouseMotionListener
{
	private mouseOverlay overlay;
	private JLabel label;
	public labelMouseListener(JLabel label, mouseOverlay overlay)
	{
		this.overlay = overlay;
		this.label = label;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Entered label area");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		overlay.setPos(SwingUtilities.convertPoint(label, e.getPoint(), overlay));
	}
	
}