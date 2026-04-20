#ifndef __PID_H__
#define __PID_H__

#include "core.h"

typedef enum
{
    POSITION_TYPE = 0, //位置式PID
    INCREAS_TYPE,      //增量式PID
}PID_Mode;

typedef struct PID_Str
{
    PID_Mode mode;

    float kp;
    float ki;
    float kd;

    float bias_current;
    float bias_last;
    float bias_last_last;
    float bias_sum;

    float out_p;
    float out_i;
    float out_d;
    float out_pid;

    float out_min;
    float out_max;
    float bias_sum_limit;
}PID_Struct;

extern PID_Struct Track_Position_PID_X, Track_Position_PID_Y;

void Pid_Init(PID_Struct* pid);
void Pid_Calculate(PID_Struct *pid);

#endif

