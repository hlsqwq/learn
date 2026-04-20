#ifndef __RUN_TRACK_H__
#define __RUN_TRACK_H__

#include "vision.h"

typedef struct Run_Track_Str  //慢速模式运行相关的结构体
{
    uint8_t step;  //运行的步数标记
    Res_Coord_Struct targrt_coord;  //跟踪目标的坐标

    uint16_t user_num1;
    uint16_t user_num2;  //模式运行时的时间控制变量
    uint16_t i; //模式运行时需要循环时的次数控制变量
    int16_t bais_corrd_x;
    int16_t bais_corrd_y;

    uint32_t time_bias;
    uint32_t time_now;
} Run_Track_Struct;

extern Run_Track_Struct Run_Track;

void Run_Mode_Track_Init(void);
void Run_Mode_Track_Enter(void);
void Run_Mode_Track_Exit(void);
void Run_Mode_Track_Run(void);

#endif



