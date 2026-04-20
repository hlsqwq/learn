#include "pid.h"

PID_Struct Track_Position_PID_X, Track_Position_PID_Y;

void Pid_Init(PID_Struct* pid)
{
    if(pid == &Track_Position_PID_X)
    {
        pid->mode = POSITION_TYPE;
        pid->kp = 0.4f;
        pid->ki = 0.1f;
        pid->kd = 0.0f;
        pid->out_min = 0;
        pid->out_max = 50;
        pid->bias_sum_limit = 1000.0f;
        pid->bias_current = 0.0f;
        pid->bias_last = 0.0f;
        pid->bias_sum = 0.0f;
    }
    else if(pid == &Track_Position_PID_Y)
    {
        pid->mode = POSITION_TYPE;
        pid->kp = 0.2f;
        pid->ki = 0.02f;
        pid->kd = 0.0f;
        pid->out_min = 0;
        pid->out_max = 50;
        pid->bias_sum_limit = 1000.0f;
        pid->bias_current = 0.0f;
        pid->bias_last = 0.0f;
        pid->bias_sum = 0.0f;
    }
}

void Pid_Calculate(PID_Struct *pid)
{
    pid->bias_sum += pid->bias_current;
    pid->bias_sum = (pid->bias_sum > pid->bias_sum_limit) ? pid->bias_sum_limit : pid->bias_sum;
    pid->bias_sum = (pid->bias_sum < (-1.0f*pid->bias_sum_limit)) ? (-1.0f*pid->bias_sum_limit) : pid->bias_sum;

    if(pid->mode == POSITION_TYPE)
    {
        pid->out_p = pid->kp * pid->bias_current;
        pid->out_i = pid->ki * pid->bias_sum;
        pid->out_d = pid->kd * (pid->bias_current - pid->bias_last);
    }

    else
    {
        pid->out_p = pid->kp * (pid->bias_current - pid->bias_last);
        pid->out_i = pid->ki * pid->bias_current;
        pid->out_d = pid->kd * (pid->bias_current - 2*pid->bias_last + pid->bias_last_last);
    }

    pid->out_pid = pid->out_p + pid->out_i + pid->out_d;
    if(pid->out_pid > pid->out_max)
    {
        pid->out_pid = pid->out_max;
    }
    else if(pid->out_pid < (-1.0f*pid->out_max))
    {
        pid->out_pid = pid->out_max * -1.0f;
    }
    else if((pid->out_pid > 0) && (pid->out_pid < pid->out_min))
    {
        pid->out_pid = pid->out_min;
    }
    else if((pid->out_pid < 0) && (pid->out_pid > (-1.0f * pid->out_min)))
    {
        pid->out_pid = (-1.0f * pid->out_min);
    }
    

    pid->bias_last_last = pid->bias_last;
    pid->bias_last = pid->bias_current;
}


