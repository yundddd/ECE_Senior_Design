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

    while (1) {
        get9dof(measurements);
        sprintf(magStringX, "%d", (int) (measurements[6]*100));
        sprintf(magStringY, "%d", (int) (measurements[7]*100));
        sprintf(magStringZ, "%d", (int) (measurements[8]*100));

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


    while (1) {
        //  LATBbits.LATB6 = ~LATBbits.LATB6;
        if (samplingFlag) {
            get9dof(measurements);
            send_9dof_to_PC(measurements);
            samplingFlag = 0;
        } else if (calibrate_gyro_flag) {
            calibrate_gyro_flag = 0;
            LATBbits.LATB6 = 1;
            calibrateGyro();
            LATBbits.LATB6 = 0;
        }

    }
    while (1);
}

