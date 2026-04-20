#ifndef __DS1302_H__
#define __DS1302_H__

#include "core.h"
#include "gpio.h"

#define DS1302_SEC				0x80
#define DS1302_MIN				0x82
#define DS1302_HOUR				0x84
#define DS1302_DATE				0x86
#define DS1302_MONTH				0x88
#define DS1302_DAY				0x8A
#define DS1302_YEAR				0x8C
#define DS1302_CONTROL				0x8E
#define DS1302_CHARGER				0x90
#define DS1302_CLKBURST				0xBE
#define DS1302_RAMBURST 			0xFE


#define RAMSIZE 				0x31
#define DS1302_RAMSTART				0xC0 


#define HEX2BCD(v)	((v) % 10 + (v) / 10 * 16)
#define BCD2HEX(v)	((v) % 16 + (v) / 16 * 10)

extern GPIO_Struct Ds1302_CLK_Pin, Ds1302_DAT_Pin, Ds1302_RST_Pin;

typedef struct _time
{
    uint8_t second;
    uint8_t minute;
    uint8_t hour;
    uint8_t date;
    uint8_t month;
    uint8_t week;
    uint8_t year;
} DS1302_Time_t;

extern DS1302_Time_t Real_Time;

void DS1302_Init(void);
void DS1302_ReadTime(DS1302_Time_t* time);
void DS1302_WriteTime(DS1302_Time_t* time); 
void DS1302_WriteRam(uint8_t addr, uint8_t val);
uint8_t DS1302_ReadRam(uint8_t addr);
void DS1302_ClearRam(void);
void DS1302_ReadTimeBurst(uint8_t * temp);
void DS1302_WriteTimeBurst(uint8_t * buf);
void DS1302_ReadRamBurst(uint8_t len, uint8_t * buf);
void DS1302_WriteRamBurst(uint8_t len, uint8_t * buf);
void DS1302_ClockStart(void);
void DS1302_ClockStop(void);
void DS1302_ClockClear(void);

#endif


