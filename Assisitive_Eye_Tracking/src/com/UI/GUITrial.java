package com.UI;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;

import java.lang.String;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.Test.dataLogging;
import com.camera.EyeEventNotifier;
import com.UI.highlightLabel;
import com.UI.highlightLabel.State;
public class GUITrial implements ActionListener, MouseMotionListener, Runnable 
{	
	// CONSTANTS
	private final int MIN_X_RESOLUTION = 640;
	private final int MIN_Y_RESOLUTION = 480;
	private final int POSFRAME_X = 200;
	private final int POSFRAME_Y = 200;
	private String letterTitle = "Eye Tracking Calibrator";
	private String posTitle = "Eye Position";
	static final Color USER_HIGHLIGHT_COLOR = Color.CYAN;
	private final Path LINUX_LOGPATH = Paths.get( "/home/kevin/dataLog.csv");
	private final Path WINDOWS_LOGPATH = Paths.get("C:\\dataLog.csv");
	private final int NUMRUNS = 5;
	private final int DEMO_UPDATE_RATE_MS = 33; // ~30Hz 
	private final String CALIBRATE_BUTTON_TEXT = "Calibrate Sensors";
	private final String START_DEMO_TEXT = "Start Demo";
	private JFrame letterFrame;
	private JFrame posFrame;
	private LayoutManager layout;
	private LayoutManager posLayout;
	private JTextArea posUpdate;
	private mouseDebugOverlay overlay;
	private dataLogging logger;
	private Random rng = new Random();
	private int runsCompleted = 0;
	private Timer demoTimer;
	private highlightLabel activeLabel;
	
	// Labels
	private highlightLabel lblA;
	private highlightLabel lblB;
	private highlightLabel lblC;
	private highlightLabel lblD;
	private highlightLabel lblE;
	private highlightLabel[] labels;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new GUITrial());
	}

	/**
	 * Create the application.
	 */
	public GUITrial() 
	{
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
		posFrame.add(posUpdate);
		
		JButton calibrateButton = new JButton(CALIBRATE_BUTTON_TEXT);
		JButton startDemoButton = new JButton(START_DEMO_TEXT);
		calibrateButton.addActionListener(this);
		startDemoButton.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(calibrateButton);
		buttonPanel.add(startDemoButton);
		posFrame.add(buttonPanel);
				
		overlay = new mouseDebugOverlay(letterFrame,posUpdate);
		
		JLabel labelNW = new JLabel("");
		letterFrame.getContentPane().add(labelNW);
		
		lblA = new highlightLabel("A", USER_HIGHLIGHT_COLOR);
		lblA.setVerticalAlignment(SwingConstants.CENTER);
		labelMouseListener aListener = new labelMouseListener(lblA, overlay);
		lblA.addMouseListener(aListener);
		lblA.addMouseMotionListener(aListener);
		letterFrame.getContentPane().add(lblA);
		
		JLabel labelNE = new JLabel("");
		letterFrame.getContentPane().add(labelNE);
		
		lblC = new highlightLabel("C", USER_HIGHLIGHT_COLOR);
		labelMouseListener cListener = new labelMouseListener(lblC, overlay);
		lblC.addMouseListener(cListener);
		lblC.addMouseMotionListener(cListener);
		letterFrame.getContentPane().add(lblC);
		
		lblE = new highlightLabel("E", USER_HIGHLIGHT_COLOR);
		labelMouseListener eListener = new labelMouseListener(lblE, overlay);
		lblE.addMouseListener(eListener);
		lblE.addMouseMotionListener(eListener);
		letterFrame.getContentPane().add(lblE);
		
		lblD = new highlightLabel("D", USER_HIGHLIGHT_COLOR);
		labelMouseListener dListener = new labelMouseListener(lblD, overlay);
		lblD.addMouseListener(dListener);
		lblD.addMouseMotionListener(dListener);
		letterFrame.getContentPane().add(lblD);
		
		JLabel labelSW = new JLabel("");
		letterFrame.getContentPane().add(labelSW);
		
		lblB = new highlightLabel("B", USER_HIGHLIGHT_COLOR);
		lblB.setVerticalAlignment(SwingConstants.CENTER);
		labelMouseListener bListener = new labelMouseListener(lblB, overlay);
		lblB.addMouseListener(bListener);
		lblB.addMouseMotionListener(bListener);
		letterFrame.getContentPane().add(lblB);
		
		JLabel labelSE = new JLabel("");
		letterFrame.getContentPane().add(labelSE);
		
		// Consolidate test labels
		labels = new highlightLabel[5];
		labels[0] = lblA;
		labels[1] = lblB;
		labels[2] = lblC;
		labels[3] = lblD;
		labels[4] = lblE;
		
		letterFrame.setGlassPane(overlay);
		letterFrame.addMouseMotionListener(this);
		
		// Configure the dataLogger for some output path
		Path sysPath = null;
		if (System.getProperty("os.name") == "Windows")
		{
			sysPath = WINDOWS_LOGPATH;
		}
		else
		{
			sysPath = LINUX_LOGPATH;
		}
		System.out.println("Datalog can be found at " + sysPath + "\n");
		logger = new dataLogging(sysPath);
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
	public void actionPerformed(ActionEvent arg0) 
	{
		// Check what type of ActionEvent we received
		//System.out.println(arg0.getID());
		String actionCommand = arg0.getActionCommand();
		int id = arg0.getID();
		//System.out.println(actionCommand);
		if (id == 1001)
		{
			switch (actionCommand)
			{
				case CALIBRATE_BUTTON_TEXT:
				{
					System.out.println("Calibrating Head Orientation Sensors...");
					// TODO Actually connect this to the IMU class
					break;
				}
				case START_DEMO_TEXT:
				{
					// Start the user demo
					System.out.println("Start the user demo...");
					demoTimer = new Timer(DEMO_UPDATE_RATE_MS, this);
					demoTimer.start();
					break;
				}
			}
		}
		else
		{
			// Timer Event
			// Check if we have an active highlighted label
			if (runsCompleted < NUMRUNS)
			{
				if (activeLabel == null)
				{
					// Select a random label to be highlighted
					int rnd = rng.nextInt(5);
					activeLabel = labels[rnd];
					activeLabel.highlight(true);
				}
				else
				{
					// Check if we should log the current mouse position
					if (activeLabel.getState() == State.TRANSITION)
					{
						// Log the current mouse position
						logger.addPoint(MouseInfo.getPointerInfo().getLocation());
					}
					// Check if we have been inside the label for the requisite # of ticks
					if (activeLabel.update())
					{
						activeLabel.highlight(false);
						if (runsCompleted != NUMRUNS)
						{
							int rnd = rng.nextInt(5);
							activeLabel = labels[rnd];
							activeLabel.highlight(true);
						}
						runsCompleted++;
						
					}
				}
			}
			else
			{
				runsCompleted = 0;
				demoTimer.stop();
				System.out.println("End of demo!");
				System.out.println("Writing the datafile...");
				logger.writeFile();
			}
		}
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
		// TODO Auto-generated method stub`
		try {
			GUITrial window = this;
			window.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class labelMouseListener implements MouseListener, MouseMotionListener
{
	private static final Color MOUSE_HIGHLIGHT_COLOR = Color.GREEN;
	
	private mouseDebugOverlay overlay;
	private highlightLabel label;
	private Color lastColorState;
	private boolean lastOpaqueState;
	public labelMouseListener(highlightLabel label, mouseDebugOverlay overlay)
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
		lastOpaqueState = label.isOpaque();
		label.setOpaque(true);
		lastColorState = label.getBackground();
		if (label.isHighlighted())
		{
			label.setFinalColor(MOUSE_HIGHLIGHT_COLOR);
		}
		else
		{
			label.setBackground(MOUSE_HIGHLIGHT_COLOR);
		}
		label.setState(highlightLabel.State.MOUSE_ENTERED);
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		label.setBackground(lastColorState);
		// TODO Auto-generated method stub
		if (label.getState() != State.MOUSE_HIGHLIGHT)
		{
			label.setOpaque(lastOpaqueState);
		}
		else
		{
			label.setOpaque(false);
			label.setState(State.NORMAL);
		}
		label.setState(highlightLabel.State.NORMAL);
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
		overlay.setAbsPos(e.getLocationOnScreen());
		overlay.setPos(SwingUtilities.convertPoint(label, e.getPoint(), overlay));
	}
	
}

//class startDemoListener implements ActionListener
//{
//	private Timer demoTimer;
//	public startDemoListener(int updateRate, ActionListener updateListener)
//	{
//		// Setup the timer to periodically update the user demo code
//		// Provide an update listener to do the checks.
//		this.demoTimer = new Timer(updateRate, updateListener);
//	}
//	@Override
//	public void actionPerformed(ActionEvent arg0) 
//	{
//		/** Handles events from the startDemo Button,
//		 * and the updateActionListener to stop the
//		 * timer when all of the demo has finished.
//		 */
//		if (arg0.getActionCommand() == "STOP")
//		{
//			// Stop the timer
//			this.demoTimer.stop();
//		}
//		else 
//		{
//			// Start the timer
//			this.demoTimer.start();
//		}
//	}
//	
//}