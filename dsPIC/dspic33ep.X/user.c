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

#include <stdint.h>          /* For uint16_t definition                       */
#include <stdbool.h>         /* For true/false definition                     */
#include "user.h"            /* variables/params used by user.c               */
#include <i2c.h>
#include "mpu9150.h"
#include <math.h>
#define SYS_FREQ        140000000L
#define FCY             SYS_FREQ/2
#define I2C_BRG(freq) (FCY/freq-FCY/1111111)-1
#include <libpic30.h>
#define M_PI 3.14159265358979323846
/******************************************************************************/
/* User Functions                                                             */
/******************************************************************************/

/* <Initialize variables in user.h and insert code for user algorithms.> */

void InitApp(void) {
    InitIO();
    MapPins();
    // InitTimer();
    initUART1();
    LATBbits.LATB6 = 1;
    __delay_ms(1000);
    LATBbits.LATB6 = 0;
    intiI2C1();
    if (initMPU9150()) {
        print_line_uart1("device initalized", 17);
    } else {
        print_line_uart1("device has returned an unknown ID", 33);
    }
    test_mpu9150();
}

void InitIO(void) {
    ANSELA = 0x00; // Convert all I/O pins to digital
    ANSELB = 0x00;

    TRISBbits.TRISB6 = 0; //led
    TRISBbits.TRISB10 = 0; //U1TX
    TRISBbits.TRISB15 = 1; //U1RX
}

void MapPins(void) {

    __builtin_write_OSCCONL(OSCCON & ~(1 << 6));
    RPINR18bits.U1RXR = 47; // Assign U1Rx To Pin RPI47
    RPOR4bits.RP42R = 1; // Assign U1Tx To Pin RP42

    // Lock Registers
    //*************************************************************
    __builtin_write_OSCCONL(OSCCON | (1 << 6));
}

void InitTimer(void) {//1 ms
    //Timer1
    //Prescaler 1:8; PR1 Preload = 8750; Actual Interrupt Time = 1 ms
    T1CONbits.TON = 0; // Disable Timer

    T1CON = 0x8010;
    IPC0 = IPC0 | 0x1000;
    PR1 = 8750;

    IFS0bits.T1IF = 0; // Reset Timer1 interrupt flag
    IPC0bits.T1IP = 5; // Timer1 Interrupt priority level=4
    IEC0bits.T1IE = 1; // Enable Timer1 interrupt

    // time = 0;
    T1CONbits.TON = 1; // Enable Timer1 and start the counter
}

void initUART1(void) {
    U1MODEbits.UARTEN = 0; // Bit15 TX, RX DISABLED, ENABLE at end of func
    //U1MODEbits.notimplemented;// Bit14
    U1MODEbits.USIDL = 0; // Bit13 Continue in Idle
    U1MODEbits.IREN = 0; // Bit12 No IR translation
    U1MODEbits.RTSMD = 0; // Bit11 Simplex Mode

    //U1MODEbits.notimplemented;// Bit10
    U1MODEbits.UEN = 0; // Bits8,9 TX,RX enabled, CTS,RTS not
    U1MODEbits.WAKE = 0; // Bit7 No Wake up (since we don't sleep here)
    U1MODEbits.LPBACK = 0; // Bit6 No Loop Back
    U1MODEbits.ABAUD = 0; // Bit5 No Autobaud (would require sending '55')
    U1MODEbits.BRGH = 0; // Bit3 16 clocks per bit period
    U1MODEbits.PDSEL = 0; // Bits1,2 8bit, No Parity
    U1MODEbits.STSEL = 0; // Bit0 One Stop Bit

    // Load a value into Baud Rate Generator.  Example is for 9600.
    // See section 19.3.1 of datasheet.
    //  U1BRG = (Fcy/(16*BaudRate))-1
    //  U1BRG = (37M/(16*38400))-1
    // U1BRG = 38400;
    U1BRG = 37; // 140Mhz osc, 115200 Baud

    // Load all values in for U1STA SFR
    //U1STAbits.UTXISEL1 = 0; //Bit15 Int when Char is transferred (1/2 config!)
    U1STAbits.UTXINV = 0; //Bit14 N/A, IRDA config
    U1STAbits.UTXISEL0 = 0; //Bit13 Other half of Bit15

    //U1STAbits.notimplemented = 0;//Bit12
    U1STAbits.UTXBRK = 0; //Bit11 Disabled
    U1STAbits.UTXEN = 0; //Enable the UARTEN bit (UxMODE<15>) before enabling this bit
    // U1STAbits.UTXBF = 0; //Bit9 *Read Only Bit*
    // U1STAbits.TRMT = 0; //Bit8 *Read Only bit*
    U1STAbits.URXISEL = 0; //Bits6,7 Int. on character recieved
    U1STAbits.ADDEN = 0; //Bit5 Address Detect Disabled
    //U1STAbits.RIDLE = 0; //Bit4 *Read Only Bit*
    //U1STAbits.PERR = 0; //Bit3 *Read Only Bit*
    //U1STAbits.FERR = 0; //Bit2 *Read Only Bit*
    //U1STAbits.OERR = 0; //Bit1 *Read Only Bit*
    //U1STAbits.URXDA = 0; //Bit0 *Read Only Bit*


    IPC2bits.U1RXIP = 6;

    IFS0bits.U1RXIF = 0; // Clear the Recieve Interrupt Flag
    IEC0bits.U1RXIE = 1; // Enable Recieve Interrupts

    U1MODEbits.UARTEN = 1; // And turn the peripheral on
    U1STAbits.UTXEN = 1; //turn tx on
    __delay_ms(100);
}

void intiI2C1(void) {
    unsigned int config2, config1;

    config2 = I2C_BRG(88500); //100Khz

    config1 = (I2C1_ON & I2C1_IDLE_CON & I2C1_CLK_HLD &
            I2C1_IPMI_DIS & I2C1_7BIT_ADD &
            I2C1_SLW_DIS & I2C1_SM_DIS &
            I2C1_GCALL_DIS & I2C1_STR_DIS &
            I2C1_NACK & I2C1_ACK_DIS & I2C1_RCV_DIS &
            I2C1_STOP_DIS & I2C1_RESTART_DIS &
            I2C1_START_DIS);

    OpenI2C1(config1, config2);
    //print_line_uart1("I2C opened", 10);

}

void print_line_uart1(char* message, int len) {
    int i;
    for (i = 0; i < len; i++) {
        if (*message == 0x00) {//breaks on null
            break;
        }
        U1TXREG = *message;
        message++;
        while (U1STAbits.UTXBF); //wait until stop bit
    }
    U1TXREG = 0x0D;
    while (U1STAbits.UTXBF); //wait until stop bit
    U1TXREG = 0x0A;

}

void print_uart1(char* message, int len) {
    int i;
    for (i = 0; i < len; i++) {

        U1TXREG = *message;
        message++;
        while (U1STAbits.UTXBF); //wait until stop bit
    }


}

void print_string(char* message) {//print until null
    while (*message) {
        U1TXREG = *(message++);
        // U1TXREG=0x31;

        while (U1STAbits.UTXBF); //wait until stop bit
    }
}

void send_floats_to_PC(float *measurements, int num) {
    int i;
    unsigned char temp;
    long templong;
    for (i = 0; i < num; i++) {
        templong = floatToLong(measurements++);
        temp = (templong >> 24)&0xFF;
        print_uart1((char *) &temp, 1);
        temp = (templong >> 16)&0xFF;
        print_uart1((char *) &temp, 1);
        temp = (templong >> 8)&0xFF;
        print_uart1((char *) &temp, 1);
        temp = templong & 0xFF;
        print_uart1((char *) &temp, 1);
    }
}

long floatToLong(float *num) {

    return (long) (1000 * (*num));
}

void test_mpu9150(void) {
    unsigned char buffer[18];
    getAccGyro(buffer);
    getMag(buffer + 12);

    print_uart1((char *) &buffer[0], 1);
    print_line_uart1((char *) &buffer[1], 1);
    print_uart1((char *) &buffer[2], 1);
    print_line_uart1((char *) &buffer[3], 1);
    print_uart1((char *) &buffer[4], 1);
    print_line_uart1((char *) &buffer[5], 1);

    print_uart1((char *) &buffer[6], 1);
    print_line_uart1((char *) &buffer[7], 1);
    print_uart1((char *) &buffer[8], 1);
    print_line_uart1((char *) &buffer[9], 1);
    print_uart1((char *) &buffer[10], 1);
    print_line_uart1((char *) &buffer[11], 1);

    print_uart1((char *) &buffer[12], 1);
    print_line_uart1((char *) &buffer[13], 1);
    print_uart1((char *) &buffer[14], 1);
    print_line_uart1((char *) &buffer[15], 1);
    print_uart1((char *) &buffer[16], 1);
    print_line_uart1((char *) &buffer[17], 1);


    /* print_line_uart1("Translated:", 11);


     float translated[9];
     translateMeasurements(buffer, translated);
     int gx = *translated;
     int gy = *(translated + 1);
     int gz = *(translated + 2);
     long GZ = gx / 16.384;
     char temp = (GZ >> 24)&0xFF;
     print_uart1(&temp, 1);
     temp = (GZ >> 16)&0xFF;
     print_uart1(&temp, 1);
     temp = (GZ >> 8)&0xFF;
     print_uart1(&temp, 1);
     temp = GZ & 0xFF;
     print_uart1(&temp, 1);
     GZ = gy / 16.384;
     temp = (GZ >> 24)&0xFF;
     print_uart1(&temp, 1);
     temp = (GZ >> 16)&0xFF;
     print_uart1(&temp, 1);
     temp = (GZ >> 8)&0xFF;
     print_uart1(&temp, 1);
     temp = GZ & 0xFF;
     print_uart1(&temp, 1);
     GZ = gz / 16.384;
     temp = (GZ >> 24)&0xFF;
     print_uart1(&temp, 1);
     temp = (GZ >> 16)&0xFF;
     print_uart1(&temp, 1);
     temp = (GZ >> 8)&0xFF;
     print_uart1(&temp, 1);
     temp = GZ & 0xFF;
     print_uart1(&temp, 1);*/
}

float rad2deg(float r) {
    return r * 180.0f / M_PI;
}

void quat_2_euler(float q[4], float e[3]) {
    float sqw = q[0] * q[0];
    float sqx = q[1] * q[1];
    float sqy = q[2] * q[2];
    float sqz = q[3] * q[3];
    e[0] = atan2f(2.f * (q[1] * q[2] + q[3] * q[0]), sqx - sqy - sqz + sqw);
    e[1] = asinf(-2.f * (q[1] * q[3] - q[2] * q[0]));
    e[2] = atan2f(2.f * (q[2] * q[3] + q[1] * q[0]), -sqx - sqy + sqz + sqw);
}