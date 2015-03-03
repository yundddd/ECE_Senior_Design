/******************************************************************************/
/* User Level #define Macros                                                  */
/******************************************************************************/

/* TODO Application specific user parameters used in user.c may go here */

/******************************************************************************/
/* User Function Prototypes                                                   */
/******************************************************************************/

/* TODO User level functions prototypes (i.e. InitApp) go here */

void InitApp(void); /* I/O and Peripheral Initialization */
void InitIO(void);
void MapPins(void);
void InitTimer(void);
void initUART1(void);
void intiI2C1(void);
void print_line_uart1(char* message, int len);
void print_uart1(char* message, int len);
void test_mpu9150(void);
long floatToLong(float *num);
void send_floats_to_PC(float *measurements, int num);
void print_string(char* message);
void quat_2_euler(float q[4], float e[3]);
float rad2deg(float r);