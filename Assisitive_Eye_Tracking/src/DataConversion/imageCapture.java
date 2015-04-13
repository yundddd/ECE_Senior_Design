package DataConversion;

import java.awt.Graphics;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Point; 
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType; 
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;


import org.opencv.imgproc.Moments;
import com.camera.EyeEventNotifier;

class Panel extends JPanel
{
	private BufferedImage image;
	
	public Panel()

	{
		super();
	}
	private BufferedImage getimage()
	{
		return image;
	}
	public void setImage(BufferedImage newImage)
	{
		image = newImage; 
		return; 
	}
	public void paint(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
	
}

public class imageCapture
{
	
	private static BufferedImage image; 
	private static BufferedImage testImage; 
	private static BufferedImage filteredImage; 
	private static BufferedImage newFilteredImage;

	
	JFrame frame1 =  new JFrame("WebCame Capture");
	JFrame frame2 = new JFrame("Thresholded");
	Panel panel1 = new Panel();
	Panel panel2 = new Panel();

	private EyeEventNotifier notifier;
	
	private static final int MAXIMUM_OBJECT_MASK_SIZE = 6;
	private static final int GAUSSIAN_KERNEL_SIZE = 3;
	private static final int THRESHOLDING_VALUE = 150;
	
	VideoCapture vc; 
	
	Mat mat;
	Mat webcamImage;
	Mat hsv_image;
	Mat thresholded;
	Mat grayImg;
	Mat mask;
	Mat postMask;
	Mat circles;
	
	
	Scalar hsv_min = new Scalar(0, 0, 0, 0); 
	Scalar hsv_max = new Scalar(360, 180, 50, 0);
	
	float xPos = 0; 
	float yPos = 0;
	public imageCapture() throws InterruptedException
	{ 
		this.notifier = notifier;
		
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(500,500);
		frame1.setBounds(0,0,frame1.getWidth(), frame1.getHeight());
		frame1.setContentPane(panel1);
		frame1.setVisible(true);
		
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setSize(640,480);
		frame2.setContentPane(panel2);
		frame2.setVisible(true); 

		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		
		vc = new VideoCapture(0);
		webcamImage = new Mat();
		hsv_image = new Mat();
		thresholded=new Mat();
		grayImg = new Mat();
		mask = new Mat();
		postMask = new Mat();
		circles = new Mat();
		
		mat = Mat.eye(3,3, CvType.CV_8UC1);
		
		vc.read(webcamImage);
		
		frame1.setSize(webcamImage.width()+40,webcamImage.height()+60);
		frame2.setSize(webcamImage.width()+40,webcamImage.height()+60);

		
		Mat array255 = new Mat(webcamImage.height(),webcamImage.width(),CvType.CV_8UC1);
		array255.setTo(new Scalar(255));
		
		

		
		double [] data = new double[3]; 
		
	}
	public void findPosition()
	{

		if(vc.isOpened())
		{
			
			vc.retrieve(mat);
			vc.read(webcamImage);
			
			//Real code for real testing
			
			//Gets image from glasses
			Imgproc.cvtColor(webcamImage, grayImg,Imgproc.COLOR_BGR2GRAY);
			Imgproc.GaussianBlur(grayImg, grayImg, new Size(GAUSSIAN_KERNEL_SIZE,GAUSSIAN_KERNEL_SIZE),0,0);
			Imgproc.threshold(grayImg, thresholded, THRESHOLDING_VALUE, 255, 0);
			Moments m = Imgproc.moments(thresholded, false);
			Point centroid = new Point(m.get_m10()/m.get_m00(), m.get_m01()/m.get_m00());
			if ((!Double.isNaN(centroid.x)) && (!Double.isNaN(centroid.y)))
			{
			
				java.awt.Point cpoint = new java.awt.Point();
				cpoint.setLocation(centroid.x, centroid.y);
				xPos = (float) centroid.x;
				yPos = (float) centroid.y;
			}
			
			
			//Code for David Personal testing only
			//Do Not Touch
			//That means you STEVE!
			/*
			Imgproc.cvtColor(webcamImage, grayImg, Imgproc.COLOR_BGR2GRAY);
			Core.inRange(webcamImage, hsv_min, hsv_max, thresholded);
			Imgproc.GaussianBlur(thresholded, thresholded, new Size(9,9),0,0);
			Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 10, thresholded.height()/4, 250, 50, 0, 0);
			int cols = circles.cols();
			int rows = circles.rows();
			int elemSize = (int)circles.elemSize();
			float[] data2 = new float[rows *elemSize/4];
			if (data2.length>0)
	        {  
	              circles.get(0, 0, data2); // Points to the first element and reads the whole thing                 
	               for(int i=0; i<data2.length; i=i+3)
	               {  
	            	 //System.out.println("x = " + data2[i]);
	            	 //System.out.println("y = " + data2[i+1]);		
	            	 xPos = data2[i];
	 				 yPos = data2[i+1];
	                 Point center = new Point(data2[i], data2[i+1]); 
	                 Core.ellipse(webcamImage, center, new Size((double)data2[i], (double)data2[i+2]), 0, 0, 360, new Scalar( 140, 140, 140 ), 4, 0, 0);
	                 //Core.ellipse(webcamImage, center, new Size((double)data2[i+2], (double)data2[i+2]), 0, 0, 360, new Scalar( 90, 0, 255 ), 4, 8, 0 );  
	        
	               }
	        }
	        else
	        {
	        	System.out.print("nothing");
	        }
	        */
			//End David's personal code
			//I swear Steve if you start renaming variables as slightly different names
			//I will have you banished to the shadow realm
			
			
			
			
			
			image=matToBufferedImage(mat);
			filteredImage = matToBufferedImage(thresholded);
			
			panel1.setImage(image);
			panel2.setImage(filteredImage); 

	        frame1.repaint();
			frame2.repaint(); 
		
		
		} 
		else 
		{
			System.out.print("Something is not correct.");
		}	
		
	}
	
	public int getEyePosX()
	{
		return (int)xPos; 
	}
	public int getEyePosY()
	{
		return (int)yPos; 
	}
	
	public void paintComponent(Graphics g)
	{
		g.drawImage(image,0,0,null);
	}
	public static BufferedImage matToBufferedImage(Mat matrix)
	{
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int)matrix.elemSize();
		
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		
		matrix.get(0, 0, data);
		switch (matrix.channels()) 
		{
			case 1:
				type = BufferedImage.TYPE_BYTE_GRAY;
				break;
			case 3:
				type = BufferedImage.TYPE_3BYTE_BGR;
				byte b; 
			for(int i = 0; i<data.length; i=i+3)
			{
				b = data[i];
				data[i]=data[i+2];
				data[i+2]=b;
			}
			break;
			
			default:
				return null;
		}
 
		BufferedImage image2 = new BufferedImage(cols,rows, type);
		image2.getRaster().setDataElements(0, 0, cols,rows,data);
		return image2; 
	}
	
	public void applyOpening(Mat src, Mat dst, int radius)
	{
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2*radius + 1, 2*radius +1));
		
		Imgproc.morphologyEx(src, dst, Imgproc.MORPH_OPEN, kernel);
	}
}

