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

void I2C1_write_byte(char data, int device, int reg);
char I2C1_read_byte(int device, int reg);