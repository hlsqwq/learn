#include "run_stop.h"
#include "run_mode.h"
#include "lcd.h"
#include "voice.h"
#include "diwen_lcd.h"
#include "tripod.h"
#include "infared.h"
#include "relay.h"
#include "logic.h"

/*停止模式初始化*/
void Run_Mode_Stop_Init(void)
{
    Run_Mode_Stop.mode_enter = Run_Mode_Stop_Enter; //将函数实体注册到函数指针
    Run_Mode_Stop.mode_exit = Run_Mode_Stop_Exit;
    Run_Mode_Stop.mode_run = Run_Mode_Stop_Run;
}

/*停止模式进入函数*/
void Run_Mode_Stop_Enter(void)
{
    DiWenLcd_Show_RunState(129, "停止检测"); //迪文屏显示当前状态
    Voice_Play((uint8_t)STOP_DETECT); //语音播报
    Relay_Control(RELAY_OFF);
    Tripod_X_Coord(684);
    Tripod_Y_Coord(Y_OFFSET); //云台归位
    while(Tripod_Y.is_running);
    while(Tripod_X.is_running); //等待云台运动结束
    Infared.Infared_Is_Pause = false;
}

/*停止模式退出函数*/
void Run_Mode_Stop_Exit(void)
{
}

/*停止模式运行函数*/
void Run_Mode_Stop_Run(void)
{
}


