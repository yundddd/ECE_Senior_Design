/******************************************************************************/
/* Files to Include                                                           */
/******************************************************************************/

/* Device header file */
#if defined(__XC16__)
#include <xc.h>
#elif defined(__C30__)
#if defined(__dsPIC33E__)
#include <p33Exxxx.h>
#elif defined(__dsPIC33F__)
#include <p33Fxxxx.h>
#endif
#endif


#include <stdint.h>        /* Includes uint16_t definition                    */
#include <stdbool.h>       /* Includes true/false definition                  */
#include <stdio.h>
#include "mpu9150.h"
#include "system.h"        /* System funct/params, like osc/peripheral config */
#include "user.h"          /* User funct/params, such as InitApp              */
#define SYS_FREQ        140000000L
#define FCY             SYS_FREQ/2
#define I2C_BRG(freq) (FCY/freq-FCY/1111111)-1
#include <libpic30.h>
#include "interrupts.h"
#include "MadgwickAHRS.h"
/******************************************************************************/
/* Global Variable Declaration                                                */
/******************************************************************************/

/* i.e. uint16_t <variable_name>; */

/******************************************************************************/
/* Main Program                                                               */

/******************************************************************************/
char magStringX[20];
char magStringY[20];
char magStringZ[20];
char LF = 0x0A;
char CR = 0x0D;
char MAG = 0xDE;
float euler[3];
int delay_less = 0;
int init_converge_counter = 0;
float yawOffSet=0;

int16_t main(void) {
    float measurements[9];
    /* Configure the oscillator for the device */
    ConfigureOscillator();

    /* Initialize IO ports and peripherals */
    InitApp();

    /* TODO <INSERT USER APPLICATION CODE HERE> */
    TRISBbits.TRISB6 = 0;
    LATBbits.LATB6 = 0;



    print_line_uart1("dsPIC is ready", 14);
    /*
        while (1) {
            get9dof(measurements);
            transformation(&measurements[6]);   //for magview
            sprintf(magStringX, "%d", (int) (measurements[6]*100));
            sprintf(magStringY, "%d", (int) (measurements[7]*100));
            sprintf(magStringZ, "%d", (int) (measurements[8]*100));

       //for mag master
            //   sprintf(magStringX, "%.3f", (measurements[6]));
            //   sprintf(magStringY, "%.3f", (measurements[7]));
            //  sprintf(magStringZ, "%.3f", (measurements[8]));
            //********************for mag viewer:
            print_uart1(&MAG, 1);
            print_string(magStringX);
            print_string(" ");
            print_string(magStringY);
            print_string(" ");
            print_string(magStringZ);
            print_string(" ");
            print_uart1(&CR, 1);
            print_uart1(&LF, 1);
            __delay_ms(100);
            //**********************************************
        }
     */

    while (1) {
        LATBbits.LATB6 = ~LATBbits.LATB6;
        if (sendMagwickFlag) {// 907us

            float quat[4] = {q0, q1, q2, q3};
            quat_2_euler(quat, euler);
            euler[0] = rad2deg(euler[0])-yawOffSet; //convert to deg and apply offset
            euler[1] = rad2deg(euler[1]);
            euler[2] = rad2deg(euler[2]);
            send_floats_to_PC(euler, 3); //send yaw pitch roll
            sendMagwickFlag = 0;
            delay_less = 1;

        } else if (calibrate_gyro_flag) {
            calibrate_gyro_flag = 0;
            LATBbits.LATB6 = 1;
            calibrateGyro();
            LATBbits.LATB6 = 0;
        } else if(calibrate_yaw_flag){
            yawOffSet=euler[0];//remembers the last yaw as offset
            calibrate_yaw_flag=0;
        }
        //11.7 ms

        if (init_converge_counter < 600) {
            init_converge_counter++;
            beta = 5;
        } else {
            beta = 0.041;
        }
        get9dof(measurements);
        transformation(&measurements[6]);
        // MadgwickAHRSupdateIMU(measurements[3], measurements[4], measurements[5], measurements[0], measurements[1], measurements[2]);
        MadgwickAHRSupdate(measurements[3], measurements[4], measurements[5], measurements[0], measurements[1], measurements[2],measurements[6], measurements[7], measurements[8]);
        if (delay_less) {
            __delay_ms(7);
            delay_less = 0;
        } else {
            __delay_ms(8);
        }



    }
    while (1);
}

