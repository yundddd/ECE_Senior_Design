package seniorDesign;

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

public class ImageProcessing 
{
	private static BufferedImage image; 
	private static BufferedImage testImage; 
	private static BufferedImage filteredImage; 
	private static BufferedImage newFilteredImage; 
	
	public static void main( String[] args) throws InterruptedException
	{ 
		
		JFrame frame1 =  new JFrame("WebCame Capture");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(500,500);
		frame1.setBounds(0,0,frame1.getWidth(), frame1.getHeight());
		Panel panel1 = new Panel(); 
		frame1.setContentPane(panel1);
		frame1.setVisible(true);
		JFrame frame2 = new JFrame("HSV");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setSize(640,480);
		Panel panel2 = new Panel();
		frame2.setContentPane(panel2);
		frame2.setVisible(true); 
		JFrame frame3 = new JFrame("S,V Distance");
		frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame3.setSize(640,480);
		frame3.setBounds(600,200, frame3.getWidth()+600, 200+frame3.getHeight());
		Panel panel3 = new Panel();
		frame3.setContentPane(panel3);
		frame3.setVisible(true);
		JFrame frame4 = new JFrame("Threshold");
		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame4.setSize(640,480);
		frame4.setBounds(900,300, frame3.getWidth()+900,300+frame3.getHeight());
		Panel panel4 = new Panel();
		frame4.setContentPane(panel4);
		frame4.setVisible(true);
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME);
		
		VideoCapture vc = new VideoCapture(0); 
		Mat webcamImage = new Mat();
		Mat hsv_image = new Mat();
		Mat thresholded=new Mat();
		Mat thresholded2=new Mat();
		Mat circles = new Mat();
		Mat grayImg = new Mat();
		
		Mat mat = Mat.eye(3,3, CvType.CV_8UC1);
		vc.read(webcamImage);
		
		frame1.setSize(webcamImage.width()+40,webcamImage.height()+60);
		frame2.setSize(webcamImage.width()+40,webcamImage.height()+60);
		frame3.setSize(webcamImage.width()+40,webcamImage.height()+60);
		frame4.setSize(webcamImage.width()+40,webcamImage.height()+60);
		
		Mat array255 = new Mat(webcamImage.height(),webcamImage.width(),CvType.CV_8UC1);
		array255.setTo(new Scalar(255));
		
		//List<Mat> lhsv = new ArrayList<Mat>(3);
		
		Scalar hsv_min = new Scalar(0,0, 150, 0); 
		Scalar hsv_max = new Scalar(255, 300, 360, 0);
		Scalar hsv_min2 = new Scalar(50,10,25,0);
		Scalar hsv_max2 = new Scalar(550, 255, 255,0);
		
		double [] data = new double[3]; 
		
		
		
		if(vc.isOpened())
		{
			Thread.sleep(1000);
			
			//Mat mat = new Mat();
			vc.retrieve(mat);
			//Highgui.imwrite("C:\\Users\\David\\Pictures\\test.jpg", mat);
		    
			while(true)
			{
				
				vc.read(webcamImage);
				vc.retrieve(mat);
				
				//Imgproc.cvtColor(webcamImage, hsv_image,Imgproc.COLOR_BGR2HSV);
				Imgproc.cvtColor(webcamImage, grayImg, Imgproc.COLOR_BGR2GRAY);
				//Core.inRange(webcamImage, hsv_min, hsv_max, thresholded);
				//Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);
				Imgproc.threshold(grayImg, thresholded, 150, 255, 0);
				Imgproc.GaussianBlur(thresholded, thresholded, new Size(9,9),0,0);
				Imgproc.Canny(thresholded, thresholded, 500, 150);
				//Imgproc.GaussianBlur(thresholded2, thresholded2, new Size(9,9),0,0);
				//Imgproc.Canny(thresholded2, thresholded2, 500, 150);
				Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 2, thresholded.height()/4, 250, 50, 0, 0);
				//Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 100, 300, 100, 0, 0);
				//data=webcamImage.get(210,210);
				
				int cols = circles.cols();
				int rows = circles.rows();
				int elemSize = (int)circles.elemSize();
				float[] data2 = new float[rows *elemSize/4];
				
				
		        if (data2.length>0)
		        {  
		              circles.get(0, 0, data2); // Points to the first element and reads the whole thing                 
		               for(int i=0; i<data2.length; i=i+3)
		               {  
		            	 System.out.println("x = " + data2[i]);
		            	 System.out.println("y = " + data2[i+1]);
		                 Point center = new Point(data2[i], data2[i+1]); 
		                 Core.ellipse(webcamImage, center, new Size((double)data2[i], (double)data2[i+2]), 0, 0, 360, new Scalar( 140, 140, 140 ), 4, 0, 0);
		                 //Core.ellipse(webcamImage, center, new Size((double)data2[i+2], (double)data2[i+2]), 0, 0, 360, new Scalar( 90, 0, 255 ), 4, 8, 0 );  
		        
		               }
		        }
		        else
		        {
		        	System.out.print("nothing");
		        }
				
				
				image=matToBufferedImage(mat);
				//testImage = matToBufferedImage(hsv_image);
				filteredImage = matToBufferedImage(thresholded);
				//newFilteredImage = matToBufferedImage(thresholded2);
				
				panel1.setImage(image);
				panel2.setImage(testImage); 
				panel3.setImage(filteredImage);
				//panel4.setImage(newFilteredImage);
				
	
				
				frame1.repaint();
				frame2.repaint(); 
				frame3.repaint();
				//frame4.repaint(); 
			}
			
		} 
		else 
		{
			System.out.print("Fuckin Shit");
		}	
		
		
		System.out.println("C:\\Users\\David\\Videos\\Megamind.avi");
		//System.out.print(result);	
		
		//Mat camFrame = new Mat();
		
		//vc.retrieve(camFrame);
		
		
		//Mat frameBlur = new Mat();
		
		
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
}
