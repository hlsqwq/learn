#ifndef __MAIN_H
#define __MAIN_H

#ifdef __cplusplus
extern "C" {
#endif

#include "core.h"
#include <stdbool.h>

extern volatile bool main_1ms_flag;
extern uint8_t uart_buf[20];


	/* Private defines -----------------------------------------------------------*/
#define num5_Pin GPIO_PIN_1
#define num5_GPIO_Port GPIOF
#define num2_Pin GPIO_PIN_2
#define num2_GPIO_Port GPIOF
#define num4_Pin GPIO_PIN_3
#define num4_GPIO_Port GPIOF
#define num1_Pin GPIO_PIN_4
#define num1_GPIO_Port GPIOF
#define num0_Pin GPIO_PIN_14
#define num0_GPIO_Port GPIOE
#define ch1_Pin GPIO_PIN_15
#define ch1_GPIO_Port GPIOE
#define num3_Pin GPIO_PIN_9
#define num3_GPIO_Port GPIOD
#define ch2_Pin GPIO_PIN_11
#define ch2_GPIO_Port GPIOD
#define num6_Pin GPIO_PIN_12
#define num6_GPIO_Port GPIOD
#define num9_Pin GPIO_PIN_13
#define num9_GPIO_Port GPIOD
#define num7_Pin GPIO_PIN_15
#define num7_GPIO_Port GPIOD
#define num8_Pin GPIO_PIN_5
#define num8_GPIO_Port GPIOD
	
	/* USER CODE BEGIN Private defines */


	char getInput();




#ifdef __cplusplus
}
#endif

#endif

