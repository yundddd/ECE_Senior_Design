package com.imu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.ArrayList;
import java.util.Enumeration;




public class IMU implements SerialPortEventListener {
	
	char[] bufferThing = new char[12]; 
	SerialPort serialPort;
	double[] vectorArray = new double[3];
	
	// CONSTANTS
        /** The port we're normally going to use. */
	private static final String WINDOWS_PORT = "COM5";
	private static final String LINUX_PORT = "/dev/ttyUSB0";
	private static final String DEFAULT_PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			LINUX_PORT, // Linux
			WINDOWS_PORT, // Windows
	};
	
	private static final byte REQ_DATA_CMD = 0x31;
	private static final byte CALIBRATE_YAW= 0x32;
	private static final byte CALIBRATE_GYRO = 0x33; 
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Default Milliseconds to block while waiting for port open */
	private static int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static int DATA_RATE = 115200;

	public void initialize() {
		
		System.out.println("hello");
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		try {
                System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
                System.setProperty("gnu.io.rxtx.SerialPorts", WINDOWS_PORT);
        		System.setProperty("gnu.io.rxtx.SerialPorts", LINUX_PORT);
		}
		catch (Exception e)
		{
			// DO NOTHING, FAIL GRACEFULLY BUT LOG TO CONSOLE
			System.out.println("System Set Property Cmd FAILED.");
			System.out.println(e.getMessage());
		}
		
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		System.out.println(portEnum.hasMoreElements());
		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : DEFAULT_PORT_NAMES) {
				System.out.println("1: "+ currPortId.getName());
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}
		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);
			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			
			char[] buffFlush = new char[100]; 
			
			//input.read(bufferThing, 0, 100);
			
			output = serialPort.getOutputStream();
			output.flush(); 
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		
		
		if ((oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)) {			
			
			int  bytesRead; 			
			try {
				
				int sum = 0; 
				//System.out.println("Seriel Event");
				bytesRead = input.read(bufferThing, 0, 12);
	
				//System.out.println();
				sum+=bytesRead;
				while(sum!=12)
				{
					bytesRead = input.read(bufferThing, sum, 12-sum);
					sum+=bytesRead;
					System.out.println(sum);
					Thread.sleep(1);
				}
				
				
//				for(int j = 0; j < 3; j++)
//				{
//					
//					for(int i = 0; i < 4; i++)
//					{	
//						byteTemp = (int)bufferThing[i+(j*4)];
//						//int k = i+(j*4); 
//						intTemp = (int)((byteTemp)<<(8*(3-i)));
//						//System.out.println("index number " + k);
//						//System.out.println(i+(j*3));
//						//System.out.println((byte)bufferThing[i+(j*3)]);
//						//System.out.println((int)bufferThing[i+(j*3)]);
//						summationVal = summationVal | intTemp; 
//						//System.out.println(summationVal);
//					}
//					vectorArray[j] = (summationVal/1000);
//					
//				}
				int yaw = ((bufferThing[0] << 24) & 0xFF000000) |
						((bufferThing[1] << 16) & 0x00FF0000) |
						((bufferThing[2] << 8 ) & 0x0000FF00) |
						((bufferThing[3] << 0 ) & 0x000000FF);
				int pitch = ((bufferThing[4] << 24) & 0xFF000000) |
						((bufferThing[5] << 16) & 0x00FF0000) |
						((bufferThing[6] << 8 ) & 0x0000FF00) |
						((bufferThing[7] << 0 ) & 0x000000FF);
				int roll = ((bufferThing[8] << 24) & 0xFF000000) |
						((bufferThing[9] << 16) & 0x00FF0000) |
						((bufferThing[10] << 8 ) & 0x0000FF00) |
						((bufferThing[11] << 0 ) & 0x000000FF);
				vectorArray[0] = ((double) pitch) / 1000;
				vectorArray[1] = ((double) roll) / 1000;
				vectorArray[2] = ((double) yaw) / 1000;
				//System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}


	public double[] getSerialData()
	{
		return vectorArray;
	}
	
	public boolean getPortStatus()
	{
		return (serialPort != null);
	}
	
	public void requestData()
	{
		sendByte(REQ_DATA_CMD);
	}
	
	public void sendByte(byte command)
	{
		try
		{
			output.write(command);
			output.flush();
		}
		catch (Exception e)
		{
			System.out.println("Sending a byte: " + command );
			System.out.println(e.getMessage());
		}
	}
	
	public void startCalibration()
	{
		/* Calibrate the Yaw values to zero when facing the computer screen */
		sendByte(CALIBRATE_YAW);
	}
	public void calibrateGyro()
	{
		/* Calibrate the gyro biases for the madgwick filter */
		sendByte(CALIBRATE_GYRO);
	}
	

	/*public static void main(String[] args) throws Exception {
		serialPortReader main = new serialPortReader();
		main.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}*/
}
