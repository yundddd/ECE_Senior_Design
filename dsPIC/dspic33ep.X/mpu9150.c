#include "mpu9150.h"

#define SYS_FREQ        140000000L
#define FCY             SYS_FREQ/2
#include <libpic30.h>
#include <i2c.h>
#include <math.h>

#define M11 1.023
#define M12 -0.056
#define M13 0.017
#define M21 0.092
#define M22 1.039
#define M23 0.002
#define M31 0.004
#define M32 0.011
#define M33 1.05
#define Bx 3.017
#define By 6.355
#define Bz 11.868
float calibrated_values[3];


unsigned char buff[20]; //buffer to hold raw bytes
int offset_acc[3] = {-327, 163, 0};
int offset_gyro[3] = {0x00};
int offset_mag[3] = {0, 2, 0};

/** write a byte to I2C1 bus
 * @param char data
 * @param int slave device address
 * @param int register address to be written
 **/
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

/** read a byte from I2C1 bus
 * @param char data
 * @param int slave device address
 * @param int register address to be read
 * returns a byte
 **/
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

/** read bytes from I2C1 bus
 * @param char* buffer
 * @param int num, number of bytes to be read
 * @param int device address to be read
 * @param int register address to be read
 **/
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

/** Initialize sensor
 **/
int initMPU9150() {
    I2C1_write_byte(MPU9150_CLOCK_PLL_XGYRO & 0x07, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_PWR_MGMT_1); //set gyro as clock reference
    I2C1_write_byte(MPU9150_GYRO_FS_500 << 3, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_GYRO_CONFIG); //set gyro range 500dps
    I2C1_write_byte(MPU9150_ACCEL_FS_2 << 3, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_ACCEL_CONFIG); //set acc range 2g
    if (0x68 == I2C1_read_byte(MPU9150_DEFAULT_ADDRESS, MPU9150_RA_WHO_AM_I)) {//check ID
        return 1; //success
    } else {
        return 0; //fail
    }
}

/** get calculated floats from all three sensors
 * @param output, float array that holds sensor measurements. g,degree/s,uT
 **/
void get9dof(float *output) {
    getAccGyro(buff);
    getMag(buff + 14);
    translateMeasurements(buff, output);

}

/** get raw bytes from acc and gyro
 * @param buffer, unsigned char array that holds raw sensor measurement bytes
 **/
void getAccGyro(unsigned char* buff) {
    I2C1_read_bytes(buff, 14, MPU9150_DEFAULT_ADDRESS, MPU9150_RA_ACCEL_XOUT_H); //get acc and gyro first
}

/** get raw bytes from mag
 * @param buffer, unsigned char array that holds raw sensor measurement bytes
 **/
void getMag(unsigned char* buff) {
    // char temp;
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
 * @param output float that has combined sensor measurements with scaling facotr
 */
void translateMeasurements(unsigned char *buffer, float *output) {//acc gryo  H-L, Mag L-H, buff[6][7] is temperature
    int unscaled[9];
    unscaled[0] = (((unsigned int) buffer[0]) << 8) | buffer[1];
    unscaled[1] = (((unsigned int) buffer[2]) << 8) | buffer[3];
    unscaled[2] = (((unsigned int) buffer[4]) << 8) | buffer[5];

    unscaled[3] = (((unsigned int) buffer[8]) << 8) | buffer[9];
    unscaled[4] = (((unsigned int) buffer[10]) << 8) | buffer[11];
    unscaled[5] = (((unsigned int) buffer[12]) << 8) | buffer[13];

    unscaled[6] = (((unsigned int) buffer[15]) << 8) | buffer[14];
    unscaled[7] = (((unsigned int) buffer[17]) << 8) | buffer[16];
    unscaled[8] = (((unsigned int) buffer[19]) << 8) | buffer[18];


    unscaled[0] += offset_acc[0]; //applying offset for acc
    unscaled[1] += offset_acc[1];
    unscaled[2] += offset_acc[2];

    unscaled[3] += offset_gyro[0]; //applying offset for gyro
    unscaled[4] += offset_gyro[1];
    unscaled[5] += offset_gyro[2];



    *(output++) = unscaled[0] / 16384.0; //gx
    *(output++) = unscaled[1] / 16384.0; //gy
    *(output++) = unscaled[2] / 16384.0; //gz
    *(output++) = unscaled[3] * 2.66E-4; //grx rad/s    1/scale * pi / 180
    *(output++) = unscaled[4] * 2.66E-4; //gry
    *(output++) = unscaled[5] * 2.66E-4; //grz
    *(output++) = unscaled[7] *0.3; //mx //x y are swapped and z reversed because the board layout
    *(output++) = unscaled[6] *0.3; //my
    *(output++) = -unscaled[8] *0.3; //mz
}

void calibrateGyro(void) {
    unsigned char buffer[14]; //only need gyro at buffer+8
    int i;
    int gyro[3] = {0x00};
    for (i = 0; i < 10; i++) {//possibility of overflowing is very low
        __delay_ms(100);
        getAccGyro(buffer);
        gyro[0] += (((unsigned int) buffer[8]) << 8) | buffer[9];
        gyro[1] += (((unsigned int) buffer[10]) << 8) | buffer[11];
        gyro[2] += (((unsigned int) buffer[12]) << 8) | buffer[13];
    }
    gyro[0] /= 10; //averaging
    gyro[1] /= 10;
    gyro[2] /= 10;

    offset_gyro[0] = 0 - gyro[0];
    offset_gyro[1] = 0 - gyro[1];
    offset_gyro[2] = 0 - gyro[2];
}

void transformation(float *uncalibrated_values) {
    //calibration_matrix[3][3] is the transformation matrix
    //replace M11, M12,..,M33 with your transformation matrix data
    double calibration_matrix[3][3] = {
        {M11, M12, M13},
        {M21, M22, M23},
        {M31, M32, M33}
    };
    //bias[3] is the bias
    //replace Bx, By, Bz with your bias data
    double bias[3] = {
        Bx,
        By,
        Bz
    };
    int i, j;
    float result[3] = {0, 0, 0};
    //calculation
    for (i = 0; i < 3; ++i) uncalibrated_values[i] = uncalibrated_values[i] - bias[i];

    for (i = 0; i < 3; ++i)
        for (j = 0; j < 3; ++j)
            result[i] += calibration_matrix[i][j] * uncalibrated_values[j];
    for (i = 0; i < 3; ++i) uncalibrated_values[i] = result[i];
}
