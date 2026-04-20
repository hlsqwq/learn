#ifndef __LOGIC_H__
#define __LOGIC_H__

#include <stdbool.h>
#include <stdint.h>
#include "ds1302.h"
#include "run_mode.h"

typedef struct Object_Message_Str
{
    DS1302_Time_t time;
    Mode_Struct *mode;
    uint8_t color;
    uint8_t shape;
    uint16_t x;
    uint16_t y;
}Object_Message_Struct;

typedef struct Object_Log_Str
{
    Object_Message_Struct object_message[100];
    uint8_t page_current;
    uint8_t page_code;
}Object_Log_Struct;

extern Object_Log_Struct Object_Log;

extern volatile bool manual_claib_finish;
extern uint8_t select_color;
extern uint8_t select_shape;

extern uint8_t ColorStr[][8];
extern uint8_t ShapeStr[][16];
extern uint8_t ColorStrZh[][8];
extern uint8_t ShapeStrZh[][16];

extern bool track_mode_is_enable;

extern uint8_t page_num;
extern uint8_t submenu;
extern uint8_t submenu_num;

//void Logic_Matrix_Key(void);
void Logic_Touch_Key(void);
void Logic_Tripod_Calib(void);
void Logic_System_Check(void);

void Logic_Lcd_Page(void);

void Object_Message_Store(uint16_t x, uint16_t y);

#endif



