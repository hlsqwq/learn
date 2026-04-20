#ifndef __MY_RT_NANO_FUNC__FILE
#define  __MY_RT_NANO_FUNC__FILE

#include <stdlib.h>
#include <string.h>
#include <rtthread.h>
#include <string.h>
#include <stdbool.h>
#include <stdio.h>


#include "main.h"
#include "stm32f1xx_hal.h"
//#include "stm32f4xx_hal.h"
#include "user_signal.h"
//#include "i2c.h"
//#include "adc.h"
#include "usart.h"
#include "serial.h"

#define SERIAL_BUFFER_SIZE 64
#define rt_print_float(str,x)    rt_kprintf("%s%d.%03d ",str,(int)(x), (int)((x)*1000)%1000);





extern unsigned char rx1buf[SERIAL_BUFFER_SIZE] ;
extern unsigned char rx1pos,rx1data;

extern unsigned char rx2buf[SERIAL_BUFFER_SIZE] ;
extern unsigned char rx2pos,rx2data;

extern unsigned char rx3buf[SERIAL_BUFFER_SIZE] ;
extern unsigned char rx3pos,rx3data;


extern rt_mailbox_t mb1 ;
extern rt_mailbox_t mb2 ;


 



//±¾µØº¯Êý
void init_rt_thread();
void thread_1_entry(); 
void thread_2_entry();


void uart_1_rx_handler(uint16_t cmd, uint8_t *data, uint8_t len);
void uart_2_rx_handler(uint16_t cmd, uint8_t *data, uint8_t len);
void uart_3_rx_handler(uint16_t cmd, uint8_t *data, uint8_t len);



void timer1_callback(void *parameter);
void timer2_callback(void *parameter);



#endif
