package DataConversion;

import java.io.IOException;

import com.imu.IMU;

class main 
{
	public static void main(String[] args) throws InterruptedException, IOException
	{
		int a = 3; 
		IMU PortCom = new IMU(); 
		//PortCom.initialize();
		//PortCom.sendByte( (byte) 0x31);
		positionCalculator PC = new positionCalculator(a);
	}
}
