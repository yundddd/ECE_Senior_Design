/* 
 * File:   MadgwickAHRS.h
 * Author: Tim
 *
 * Created on March 2, 2015, 10:45 PM
 */

#ifndef MADGWICKAHRS_H
#define	MADGWICKAHRS_H

#ifdef	__cplusplus
extern "C" {
#endif
    extern volatile float beta; // algorithm gain
    extern volatile float q0, q1, q2, q3; // quaternion of sensor frame relative to auxiliary frame
    // Function declarations

    void MadgwickAHRSupdate(float gx, float gy, float gz, float ax, float ay, float az, float mx, float my, float mz);
    void MadgwickAHRSupdateIMU(float gx, float gy, float gz, float ax, float ay, float az);



#ifdef	__cplusplus
}
#endif

#endif	/* MADGWICKAHRS_H */

