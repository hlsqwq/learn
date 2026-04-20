#include "run_track.h"
#include "run_mode.h"
#include "lcd.h"
#include "voice.h"
#include "tripod.h"
#include "relay.h"
#include "logic.h"
#include "diwen_lcd.h"
#include "tim.h"
#include "pid.h"
#include <math.h>

float track_speed = 10.0f;
int16_t track_x_ahead = -50;
int16_t track_y_ahead = -10;
uint16_t track_correct = 1;

Run_Track_Struct Run_Track;

/***************************************/
void Run_Mode_Track_Init(void)
{
	Run_Mode_Track.mode_enter = Run_Mode_Track_Enter;
	Run_Mode_Track.mode_exit = Run_Mode_Track_Exit;
	Run_Mode_Track.mode_run = Run_Mode_Track_Run;

	Run_Track.i = 0;
	Run_Track.step = 0;
	Run_Track.user_num1 = 0;
	Run_Track.user_num2 = 0;
}

/***************************************/
void Run_Mode_Track_Enter(void)
{
	Run_Track.i = 0;
	Run_Track.step = 0;
	Run_Track.user_num1 = 0;
	Run_Track.user_num2 = 0;

	DiWenLcd_Show_RunState(129, "跟踪模式"); // 迪文屏显示运行模式

	// Lcd_printf("enter track mode");

	Tripod_X_Coord(1365);
	Tripod_Y_Coord(384);

	while (Tripod_X.is_running)
		;
	while (Tripod_Y.is_running)
		;

	Tripod_X.encoder_is_using = true;
	Tripod_Y.encoder_is_using = true;
	Tripod_Encoder_X.coord = 1365;
	Tripod_Encoder_Y.coord = 384;
	Tripod_Encoder_X.angle = Tripod_X.angle;
	Tripod_Encoder_Y.angle = Tripod_Y.angle;

	Relay_Control(RELAY_ON);

	Pid_Init(&Track_Position_PID_X);
	Pid_Init(&Track_Position_PID_Y);

	__HAL_TIM_SET_PRESCALER(&htim2, 65535);
	__HAL_TIM_SET_PRESCALER(&htim3, 65535);
}

/*************************************/
void Run_Mode_Track_Exit(void)
{
	Relay_Control(RELAY_OFF); // 关闭激光笔
	Tripod_X.encoder_is_using = false;
	Tripod_Y.encoder_is_using = false;
	HAL_TIM_Base_Stop_IT(&htim2);
	HAL_TIM_Base_Stop_IT(&htim3);

	Tripod_X_Go_Mechzero();
	Tripod_Y_Go_Mechzero();

	Tripod_X.angle = 0.0;
    Tripod_X.coord = 684;
    Tripod_X.is_running = false;
    Tripod_X.is_limit = false;
    Tripod_X.step_num = 0;
    Tripod_Y.angle = 0.0;
    Tripod_Y.coord = Y_OFFSET;
    Tripod_Y.is_running = false;
    Tripod_Y.is_limit = false;
    Tripod_Y.step_num = 0;
}

/*慢速模式运行函数*/
void Run_Mode_Track_Run(void)
{
	static uint16_t num;

    if(Page_Now == 4)
    {
        num++;
        if(num >= 800)
        {
            num = 0;
            DS1302_ReadTime(&Real_Time);
            Lcd_ShowNum(96, 40, (run_time/1000), SHOW_SIZE_16, BLUE);
            DS1302_ReadTime(&Real_Time);
            Lcd_ShowTime(96, 70, BLUE, &Real_Time);
        }
    }
	
	if(Run_Track.step == 0)
	{
		if(Vision_Dat_Explan() != 0)
		{
			Run_Track.targrt_coord.x = Vision_Coord.Res_Coord[0].x;
			Run_Track.targrt_coord.y = Vision_Coord.Res_Coord[0].y;

			if(Run_Track.targrt_coord.x > 365)
			{
				Run_Track.user_num1++;
				if(Run_Track.user_num1 >= 3)
				{
					HAL_TIM_Base_Start_IT(&htim2);
					HAL_TIM_Base_Start_IT(&htim3);
					Run_Track.step = 1;
					Run_Track.user_num1 = 0;
				}
			}
		}
	}
	else if(Run_Track.step == 1)
	{
		if(Vision_Dat_Explan() != 0)
		{
			Run_Track.targrt_coord.x = Vision_Coord.Res_Coord[0].x;
			Run_Track.targrt_coord.y = Vision_Coord.Res_Coord[0].y;

			if(track_x_ahead > 0)
			{
				if(Run_Track.targrt_coord.x < (1365 - track_x_ahead))
				{
					Run_Track.targrt_coord.x = Run_Track.targrt_coord.x + track_x_ahead;
				}
				else
				{
					Run_Track.targrt_coord.x = 1365;
				}
			}
			else
			{
				if(Run_Track.targrt_coord.x > (-1 * track_x_ahead))
				{
					Run_Track.targrt_coord.x = Run_Track.targrt_coord.x + track_x_ahead;
				}
				else
				{
					Run_Track.targrt_coord.x = 1;
				}
			}

			if(track_y_ahead > 0)
			{
				if(Run_Track.targrt_coord.y < (1365 - track_y_ahead))
				{
					Run_Track.targrt_coord.y = Run_Track.targrt_coord.y + track_y_ahead;
				}
				else
				{
					Run_Track.targrt_coord.y = 1365;
				}
			}
			else
			{
				if(Run_Track.targrt_coord.y > (-1 * track_y_ahead))
				{
					Run_Track.targrt_coord.y = Run_Track.targrt_coord.y + track_y_ahead;
				}
				else
				{
					Run_Track.targrt_coord.y = 1;
				}
			}

			Tripod_X_Get_Encoder_Coord();
			Tripod_Y_Get_Encoder_Coord();

			if(Tripod_Encoder_X.coord < 684)
			{
				Run_Track.targrt_coord.x = Run_Track.targrt_coord.x + (((684 - Tripod_Encoder_X.coord)) / 30) * track_correct;
			}
			
			Track_Position_PID_X.bias_current = (float)(Tripod_Encoder_X.coord - Run_Track.targrt_coord.x);
			Track_Position_PID_Y.bias_current = (float)(Tripod_Encoder_Y.coord - Run_Track.targrt_coord.y);

			Pid_Calculate(&Track_Position_PID_X);
			Pid_Calculate(&Track_Position_PID_Y);

			if(Track_Position_PID_X.out_pid < 0)
			{
				Tripod_X_Dir(TRIPOD_RIGHT);
				__HAL_TIM_SET_PRESCALER(&htim2, Tripod_X_Speed_To_Prescaler(-1.0f * Track_Position_PID_X.out_pid));
			}
			else
			{
				Tripod_X_Dir(TRIPOD_LEFT);
				__HAL_TIM_SET_PRESCALER(&htim2, Tripod_X_Speed_To_Prescaler(Track_Position_PID_X.out_pid));
			}

			if(Track_Position_PID_Y.out_pid < 0)
			{
				Tripod_Y_Dir(TRIPOD_UP);
				__HAL_TIM_SET_PRESCALER(&htim3, Tripod_Y_Speed_To_Prescaler(-1.0f * Track_Position_PID_Y.out_pid));
			}
			else
			{
				Tripod_Y_Dir(TRIPOD_DOWN);
				__HAL_TIM_SET_PRESCALER(&htim3, Tripod_Y_Speed_To_Prescaler(Track_Position_PID_Y.out_pid));
			}

			Run_Track.user_num1 ++;
		}

		if(Run_Track.user_num1 > 10)
		{
			Tripod_X_Get_Encoder_Coord();
			Tripod_Y_Get_Encoder_Coord();

			if(((Tripod_Encoder_X.coord < 10) | (Tripod_Encoder_X.coord > 1355)) | ((Tripod_Encoder_Y.coord < 10) | (Tripod_Encoder_Y.coord > 757)))
			{
				HAL_TIM_Base_Stop_IT(&htim2);
				HAL_TIM_Base_Stop_IT(&htim3);
				Pid_Init(&Track_Position_PID_X);
				Pid_Init(&Track_Position_PID_Y);
				Run_Track.step = 0;
				Run_Track.user_num1 = 0;
			}
		}
	}
}
