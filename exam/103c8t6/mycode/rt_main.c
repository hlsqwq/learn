
#include "rt_main.h"
#include "main.h"




//--------------------------------------

extern UART_HandleTypeDef huart1;
extern UART_HandleTypeDef huart2;
extern UART_HandleTypeDef huart3;


rt_mailbox_t mb1 = RT_NULL; //for thread1
rt_mailbox_t mb2 = RT_NULL; //for thread2


rt_timer_t tim1= RT_NULL;  
rt_timer_t tim2= RT_NULL;  



//串口相关
unsigned char rx1buf[SERIAL_BUFFER_SIZE] ={0};
unsigned char rx1pos=0;
unsigned char rx1data;

unsigned char rx2buf[SERIAL_BUFFER_SIZE] ={0};
unsigned char rx2pos=0;
unsigned char rx2data;

unsigned char rx3buf[SERIAL_BUFFER_SIZE] ={0};
unsigned char rx3pos=0;
unsigned char rx3data;





//-------------------------------------------
void init_rt_thread(){


  //1. mail box
  mb1     = rt_mb_create("mb1",10, RT_IPC_FLAG_FIFO);
  mb2     = rt_mb_create("mb2",10, RT_IPC_FLAG_FIFO);


  
  //2. thread
  rt_thread_t tid1 = rt_thread_create("thread-1",  thread_1_entry, RT_NULL, 1024,20, 20);
  if(tid1) rt_thread_startup(tid1);

  rt_thread_t tid2 = rt_thread_create("thread-2",  thread_2_entry, RT_NULL, 1024,15, 20);
  if(tid2) rt_thread_startup(tid2);


  //3. 启动定时器timer 
  tim1 = rt_timer_create("timer-1",timer1_callback, (void *)123, 1000,RT_TIMER_FLAG_PERIODIC);   //for led default
  //tim2 = rt_timer_create("timer-2",timer2_callback, (void *)456, 1000,RT_TIMER_FLAG_PERIODIC);   //
  rt_timer_start(tim1);
  //rt_timer_start(tim2);


  //4. 串口启动接收
	uart_link_init(); 
 

  //5. adc init
  //注意：F4系列没有校准函数
  //HAL_ADCEx_Calibration_Start(&hadc1);


  //printf("init ok-----\n");
}



///-------------  rx data from serial2 ----------
void thread_1_entry(  )
{
    USER_SIGNAL_TYPE  sig;


	//waiting for signal
    while(1)
    {
      if(rt_mb_recv(mb1,(rt_ubase_t*) &sig, RT_WAITING_NO) == RT_EOK){




      }
      sig=SIG_NULL;

      rt_thread_mdelay(10);
    }
}



void thread_2_entry(){
  USER_SIGNAL_TYPE  sig;

  while(1){
    if (rt_mb_recv(mb2, (rt_ubase_t*)&sig, RT_WAITING_NO) == RT_EOK)
    {
      printf("sig=%d\r\n",sig);


	  if(sig>=SIG_CAR_MOVE_BASE && sig <= SIG_CAR_MOVE_BASE_END)
	  {
         
	  }


      else if(sig==SIG_TRANSMIT_A_TO_B)
      {
         
      }
	  
	  
      else if(sig==SIG_TRANSMIT_B_TO_C)
      {

      }

       sig=SIG_NULL;
    } 

    rt_thread_mdelay(10);
  }
}



void HAL_UART_RxCpltCallback(UART_HandleTypeDef *huart){
  
  //huart1 maybe used by Fin-shell
  #if 0
  if(huart==&huart2)
  {
  }else
  #endif
  
  if(huart==&huart2)
  {
  	uart_rx_byte(rx2data, &uart2_rx_ctx);
	HAL_UART_Receive_IT(&huart2,&rx2data,1);
  }
  else if(huart==&huart3)
  {
  	uart_rx_byte(rx3data, &uart3_rx_ctx);
	HAL_UART_Receive_IT(&huart3,&rx3data,1);
  }

  
  return;
}




////////////---------TIMER --------------------
//timer1的作用:
void timer1_callback(void *parameter)
{
   //Example
   HAL_GPIO_TogglePin(LED_GPIO_Port,LED_Pin);
	 printf("helakdf\n");
}
//timer2的作用:
void timer2_callback(void *parameter)
{ 

}



#if 0
////////////---------SHELL CMMD --------------------
static int move(int argc, char** argv) {
	USER_SIGNAL_TYPE sig=SIG_NULL;
	
    if (argc < 2) {
        return -RT_ERROR;
    }

	do{


	}while(0);
  
    return RT_EOK;
}
MSH_CMD_EXPORT(move, "Usage: move <w|qweasdzxc|rt>\n");
#endif




/*-----------------------------
  串口1:接蓝牙/上位机
*/
void uart_1_rx_handler(uint16_t cmd, uint8_t *data, uint8_t len){
   
}

/*-----------------------------
  串口2接:蓝牙/上位机
*/
void uart_2_rx_handler(uint16_t cmd, uint8_t *data, uint8_t len){
    //中断函数内不要用printf
    
	if(cmd==SIG_CAR_MOVE_YAW)
    {
   
   
    }


	//如果是本地的sig，发送给sig handler
	if(cmd<SIG_TO_SLAVE_BOARD_BASE)
	{
       //rt_mb_send(mb2 ,cmd); 
	   //rt_mb_send(mb1 ,cmd); 
	}
	
	//如果>10000，那么透传给串口3
    else
    {
       //rt_kprintf("to slave(%d)\n",cmd);
	   //uart_tx_frame(&uart3_rx_ctx,cmd,data,len);
    }



}

/*-----------------------------
  串口3接slave板
*/
void uart_3_rx_handler(uint16_t cmd, uint8_t *data, uint8_t len){
	//如果是本地的sig，发送给sig handler
	if(cmd<SIG_TO_SLAVE_BOARD_BASE)
	{
	  printf("to local(%d)\n",cmd);
	  //rt_mb_send(mb2,cmd); 
	}
	//如果>10000，那么透传给串口2（也就是通过蓝牙转发给上位机：手机或平板）
    else
    {
       printf("to gate(%d)\n",cmd);
	   //uart_tx_frame(&uart2_rx_ctx,cmd,data,len);
    }

}


