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
#define writeAddr(addr) ( (addr << 1) & 0xFE)
#define readAddr(addr) ( (addr << 1) | 0x01)
#define SYS_FREQ        140000000L
#define FCY             SYS_FREQ/2
#define I2C_BRG(freq) (FCY/freq-FCY/1111111)-1
#include <libpic30.h>
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
    initMPU9150();
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

void initMPU9150() {
    unsigned char buffer[18];
    I2C1_write_byte(MPU9150_CLOCK_PLL_XGYRO & 0x07, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_PWR_MGMT_1); //set gyro as clock reference
    I2C1_write_byte(MPU9150_GYRO_FS_500 << 3, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_GYRO_CONFIG); //set gyro range 500dps
    I2C1_write_byte(MPU9150_ACCEL_FS_2 << 3, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_ACCEL_CONFIG); //set acc range 2g
    if (0x68 == I2C1_read_byte(MPU9150_DEFAULT_ADDRESS, MPU9150_RA_WHO_AM_I)) {//check ID
        print_line_uart1("device initalized", 17);
    } else {
        print_line_uart1("device has returned an unknown ID", 33);
    }


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


    print_line_uart1("Translated:", 11);


    int translated[9];
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
    print_uart1(&temp, 1);

    while (1);
}

void print_line_uart1(char* message, int len) {
    int i;
    for (i = 0; i < len; i++) {

        U1TXREG = *message;
        message++;
        __delay_ms(1);
    }
    U1TXREG = 0x0D;
    __delay_ms(1);
    U1TXREG = 0x0A;
    __delay_ms(1);
}

void print_uart1(char* message, int len) {
    int i;
    for (i = 0; i < len; i++) {

        U1TXREG = *message;
        message++;
        __delay_ms(1);
    }


}

void I2C1_write_byte(char data, int device, int reg) {
    IdleI2C1();
    StartI2C1(); //S
    /* Wait till Start sequence is completed */
    while (I2C1CONbits.SEN);
    // print_line_uart1("I2C started", 11);
    MasterWriteI2C1(writeAddr(device)); //AD + W
    while (I2C1STATbits.TBF); //wait for transmission
    //  print_line_uart1("slave address transmitted", 25);
    while (I2C1STATbits.ACKSTAT); //wait for ACK
    //  print_line_uart1("ACK received", 12);
    MasterWriteI2C1(reg); //RA
    while (I2C1STATbits.ACKSTAT); //wait for ACK
    //  print_line_uart1("ACK received", 12);
    MasterWriteI2C1(data); //DATA

    while (I2C1STATbits.ACKSTAT); //wait for ACK
    //  print_line_uart1("ACK received", 12);

    StopI2C1();
}

char I2C1_read_byte(int device, int reg) {
    char data = 0;
    IdleI2C1();
    StartI2C1(); //S
    while (I2C1CONbits.SEN);
    MasterWriteI2C1(writeAddr(device)); //AD + W
    while (I2C1STATbits.TBF); //wait for transmission
    while (I2C1STATbits.ACKSTAT); //wait for ACK
    MasterWriteI2C1(reg); //RA
    while (I2C1STATbits.ACKSTAT); //wait for ACK
    StartI2C1(); //S
    while (I2C1CONbits.SEN);
    MasterWriteI2C1(readAddr(device)); //AD + R
    while (I2C1STATbits.ACKSTAT); //wait for ACK
    data = MasterReadI2C1(); //DATA
    NotAckI2C1(); //Master NACK
    StopI2C1(); //STTOP
    while (I2C1CONbits.PEN);
    return data;
}

void I2C1_read_bytes(unsigned char* buff, int num, int device, int reg) {
    int i;
    IdleI2C1();
    StartI2C1(); //S
    while (I2C1CONbits.SEN);
    MasterWriteI2C1(writeAddr(device)); //AD + W
    while (I2C1STATbits.TBF); //wait for transmission
    while (I2C1STATbits.ACKSTAT); //wait for ACK
    MasterWriteI2C1(reg); //RA
    while (I2C1STATbits.ACKSTAT); //wait for ACK

    StartI2C1(); //S
    while (I2C1CONbits.SEN);
    MasterWriteI2C1(readAddr(device)); //AD + R
    while (I2C1STATbits.ACKSTAT); //wait for ACK
    for (i = 0; i < num - 1; i++) {
        *(buff++) = MasterReadI2C1(); //DATA
        IFS1bits.MI2C1IF = 0;
        AckI2C1();
        while (!IFS1bits.MI2C1IF); //wait for ack transmission, master event
    }
    *(buff++) = MasterReadI2C1();
    NotAckI2C1(); //Master NACK
    StopI2C1(); //STTOP
    while (I2C1CONbits.PEN);

}

void getAccGyro(unsigned char* buff) {
    I2C1_read_bytes(buff, 12, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_ACCEL_XOUT_H); //get acc and gyro first
}

void getMag(unsigned char* buff) {
    char temp;
    I2C1_write_byte(0x02, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_INT_PIN_CFG); //set bypass mode
    I2C1_write_byte(0x01, MPU9150_RA_MAG_ADDRESS, 0x0A); //set mag to single measurement mode
    __delay_ms(10); //wait for mag to finish measuring
    I2C1_read_bytes(buff, 6, MPU9150_RA_MAG_ADDRESS, MPU9150_RA_MAG_XOUT_L);
    /*    temp = *buff;   //switching H and L bytes
     *buff = *(buff + 1);
     *(buff + 1) = temp;
        temp = *(buff + 2);
     *(buff + 2) = *(buff + 3);
     *(buff + 3) = temp;
        temp = *(buff + 4);
     *(buff + 4) = *(buff + 5);
     *(buff + 5) = temp; //swap H and L*/
}

/** Translate raw sensor info into integers without scaling factor
 * @param buffer, unsigned char array that holds raw sensor measurement bytes
 * @param output 16-bit signed integer that has combined sensor measurements w/o scaling facotr
 */
void translateMeasurements(unsigned char *buffer, int *output) {//acc gryo  H-L, Mag L-H
    *(output++) = (((unsigned int) buffer[0]) << 8) | buffer[1];
    *(output++) = (((unsigned int) buffer[2]) << 8) | buffer[3];
    *(output++) = (((unsigned int) buffer[4]) << 8) | buffer[5];

    *(output++) = (((unsigned int) buffer[6]) << 8) | buffer[7];
    *(output++) = (((unsigned int) buffer[8]) << 8) | buffer[9];
    *(output++) = (((unsigned int) buffer[10]) << 8) | buffer[11];

    *(output++) = (((unsigned int) buffer[13]) << 8) | buffer[12];
    *(output++) = (((unsigned int) buffer[15]) << 8) | buffer[14];
    *(output) = (((unsigned int) buffer[17]) << 8) | buffer[16];
}

/** Scales gyro measurements with a scaling factor defined in initMPU9150().
 * @param buffer, unsigned char array that holds raw sensor measurement bytes
 * @param output, float which is in radians/sec. They will be passed to a sensor fusion filter.
 */
void scaleGyro(int *buff,float *output){

}