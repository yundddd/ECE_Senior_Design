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
        //  LATBbits.LATB6 = ~LATBbits.LATB6;
        if (samplingFlag) {
            get9dof(measurements);
            send_to_PC(measurements);
            samplingFlag=0;
        }
        
    }
    while (1);
}

