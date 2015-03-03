using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GUI_for_new_lazer_accelerameter_encoder
{
    class kalman
    {
        double Q_angle = 0.01;
        double Q_bias = 0.06;
        double R_measure = 800.0;
        public double angle = 0; // Reset the angle
        double bias = 0; // Reset bias
        double rate; // Unbiased rate calculated from the rate and the calculated bias - you have to call getAngle to update the rate
        public double[,] aP = new double[2, 2]; // Error covariance matrix - This is a 2x2 matrix
        double[] aK = new double[2]; // Kalman gain - This is a 2x1 vector
        double ay; // Angle difference
        double aS; // Estimate error
        ArrayList Rvalues;
        int Rptr = 0;
        // double oldrate = 0;

        public double arduinoKalmanUpdate(double newAngle, double newRate, double dt, double sigmaAcc)
        {

            rate = newRate - bias;
            angle += dt * rate;


            //if (newRate - oldrate >= 100) { newRate = (oldrate + newRate)/2; }          
            //      oldrate = newRate;           
            //Console.WriteLine(newRate);
            // Update estimation error covariance - Project the error covariance ahead
            /* Step 2 */
            aP[0, 0] += dt * (dt * aP[1, 1] - aP[0, 1] - aP[1, 0] + Q_angle);
            aP[0, 1] -= dt * aP[1, 1];
            aP[1, 0] -= dt * aP[1, 1];
            aP[1, 1] += Q_bias * dt;

            // Discrete Kalman filter measurement update equations - Measurement Update ("Correct")
            // Calculate Kalman gain - Compute the Kalman gain
            /* Step 4 */
            aS = aP[0, 0] + R_measure;
            /* Step 5 */
            aK[0] = aP[0, 0] / aS;
            aK[1] = aP[1, 0] / aS;

            // Calculate angle and bias - Update estimate with measurement zk (newAngle)
            /* Step 3 */
            ay = newAngle - angle;
            /* Step 6 */
            angle += aK[0] * ay;
            bias += aK[1] * ay;

            // Calculate estimation error covariance - Update the error covariance
            /* Step 7 */
            aP[0, 0] -= aK[0] * aP[0, 0];
            aP[0, 1] -= aK[0] * aP[0, 1];
            aP[1, 0] -= aK[1] * aP[0, 0];
            aP[1, 1] -= aK[1] * aP[0, 1];
            return angle;
        }
        public void setRmeasure(double newR_measure) { R_measure = newR_measure; Rptr = 0; }
        public void setQbias(double newQ_bias) { Q_bias = newQ_bias; }
        public void setAngle(double newAngle) { angle = newAngle; } // Used to set angle, this should be set as the starting angle
        public void setQangle(double newQ_angle) { Q_angle = newQ_angle; }
        public void updateRmeasure(double sigmaAcc, double newrate)
        {
            if ((Math.Abs(newrate) < 3) && sigmaAcc < 0.97&& sigmaAcc>1.03)
            {//linearly acc
                
            }
        }
        
    }
}
