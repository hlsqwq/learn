#ifndef __TRIPOD_H__
#define __TRIPOD_H__

#include <stdint.h>
#include <stdbool.h>
#include "gpio.h"

#define X_STEP_ANGLE 59.5f  //云台X轴运行一度需要的步数
#define Y_STEP_ANGLE 60.0f  //云台Y轴运行一度需要的步数
#define DIS_X   976			// 屏幕分辨率是1366  768  面积是70cm*39.2cm 假设云台到屏幕距离是50，那么此处的值是50/70*1366
#define DIS_Y 768

extern uint16_t Y_OFFSET;

typedef enum
{
    TRIPOD_LEFT = 0,
    TRIPOD_RIGHT,
    TRIPOD_UP,
    TRIPOD_DOWN,
}Tripod_Dir_Enum;

typedef struct Tripod_Str  //云台相关结构体
{
    volatile uint32_t step_num;  //需要运行的步数
    volatile uint32_t step_code;
    uint16_t coord;  //当前坐标
    float angle;  //当前角度
    volatile bool is_limit;  //是否到达限位
    volatile bool is_running;  //是否正在运行 （volatile标记这是一个不能优化的变量）
    volatile bool encoder_is_using; //编码器是否使能
    Tripod_Dir_Enum dir;
}Tripod_Struct; 

typedef struct Tripod_Encoder_Str  //云台编码器相关结构体
{
    uint16_t coord;
    float angle;
    float speed;
}Tripod_Encoder_Struct; 

extern GPIO_Struct Tripod_X_DIR, Tripod_X_EN, Tripod_X_STEP, Tripod_X_LIMIT, Tripod_Y_DIR, Tripod_Y_EN, Tripod_Y_STEP, Tripod_Y_LIMIT;
extern Tripod_Struct Tripod_X, Tripod_Y;
extern Tripod_Encoder_Struct Tripod_Encoder_X, Tripod_Encoder_Y;

void Tripod_Init(void);
void Tripod_X_Dir(Tripod_Dir_Enum dir);
void Tripod_X_Step(uint16_t num);

void Tripod_Y_Dir(Tripod_Dir_Enum dir);
void Tripod_Y_Step(uint16_t num);
void Tripod_Key_Scan(void);
void Tripod_X_Go_Mechzero(void);
void Tripod_Y_Go_Mechzero(void);
void Tripod_X_Angle(float x_dat);
void Tripod_Y_Angle(float y_dat);
void Tripod_X_Coord(int x_dat);
void Tripod_Y_Coord(int y_dat);

uint16_t Tripod_X_Speed_To_Prescaler(float speed);
uint16_t Tripod_Y_Speed_To_Prescaler(float speed);
void Tripod_X_Get_Encoder_Coord(void);
void Tripod_Y_Get_Encoder_Coord(void);

#endif



