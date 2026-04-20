#include "lcd.h"
#include "spi.h"
#include "dma.h"
#include "logic.h"
#include <string.h>
#include <stdlib.h>
#include <stdarg.h>
#include <stdio.h>

#define LCD_WRITE_CMD  HAL_GPIO_WritePin(LCD_DC_Pin.GPIOx, LCD_DC_Pin.GPIO_Pin, GPIO_PIN_RESET)
#define LCD_WRITE_DATA HAL_GPIO_WritePin(LCD_DC_Pin.GPIOx, LCD_DC_Pin.GPIO_Pin, GPIO_PIN_SET)  //LCD数据和命令切换

GPIO_Struct LCD_DC_Pin, LCD_CS_Pin, LCD_RST_Pin; //LCD控制引脚变量
uint16_t LCD_BACKGROUND = BLACK;  //背景色，默认黑色
uint16_t LCD_TYPEFACE = WHITE;  //画笔色，默认白色

LcdData mUserLcdData = {0}; //LCD屏显示结构体变量

uint8_t Page_Now = 0;

/*lcd控制引脚初始化*/
void Lcd_Pin_Init(void)
{
    GPIO_InitTypeDef GPIO_InitStruct = {0};

    LCD_DC_Pin.GPIOx = GPIOC;
    LCD_DC_Pin.GPIO_Pin = GPIO_PIN_3; //C3

    LCD_CS_Pin.GPIOx = GPIOC;
    LCD_CS_Pin.GPIO_Pin = GPIO_PIN_2;  //C2

    LCD_RST_Pin.GPIOx = GPIOD;
    LCD_RST_Pin.GPIO_Pin = GPIO_PIN_0;

    __HAL_RCC_GPIOC_CLK_ENABLE();
    __HAL_RCC_GPIOD_CLK_ENABLE();

    GPIO_InitStruct.Pin = LCD_DC_Pin.GPIO_Pin | LCD_CS_Pin.GPIO_Pin;
    GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;  //推挽输出
    GPIO_InitStruct.Pull = GPIO_NOPULL;  //没有上下拉
    GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_MEDIUM;  //翻转速度快速
    HAL_GPIO_Init(GPIOC, &GPIO_InitStruct);

    GPIO_InitStruct.Pin = LCD_RST_Pin.GPIO_Pin;
    GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;  //推挽输出
    GPIO_InitStruct.Pull = GPIO_NOPULL;  //没有上下拉
    GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_MEDIUM;  //翻转速度快速
    HAL_GPIO_Init(GPIOD, &GPIO_InitStruct);

    HAL_GPIO_WritePin(LCD_DC_Pin.GPIOx, LCD_DC_Pin.GPIO_Pin, GPIO_PIN_RESET);
    HAL_GPIO_WritePin(LCD_CS_Pin.GPIOx, LCD_CS_Pin.GPIO_Pin, GPIO_PIN_RESET);  //默认低电平
    HAL_GPIO_WritePin(LCD_RST_Pin.GPIOx, LCD_RST_Pin.GPIO_Pin, GPIO_PIN_SET);
}

/*向lcd发送命令*/
void Lcd_Write_Cmd(uint8_t cmd)
{
    uint8_t write_cmd = cmd;
    LCD_WRITE_CMD; //切换为命令模式
    HAL_SPI_Transmit_DMA(&hspi1, &write_cmd, 1); //开启spi1的DMA传输
    while(HAL_DMA_GetState(&hdma_spi1_tx) == HAL_DMA_STATE_BUSY);  //等待数据发送完成
}

/*向lcd发送数据*/
void Lcd_Write_Data8(uint8_t data)
{
    uint8_t write_data = data;
    LCD_WRITE_DATA;
    HAL_SPI_Transmit_DMA(&hspi1, &write_data, 1);
    while(HAL_DMA_GetState(&hdma_spi1_tx) == HAL_DMA_STATE_BUSY);
}

/*向lcd发送int16型数据*/
void Lcd_Write_Data16(uint16_t data)
{
    uint8_t write_data[2];
    LCD_WRITE_DATA;
    write_data[0] = data >> 8;
    write_data[1] = data & 0xFF;
    HAL_SPI_Transmit_DMA(&hspi1, write_data, 2);
    while(HAL_DMA_GetState(&hdma_spi1_tx) == HAL_DMA_STATE_BUSY);
}

/*向lcd发送指定长度数据*/
void Lcd_Write_Data(uint8_t* data, uint16_t len)
{
    LCD_WRITE_DATA;
    HAL_SPI_Transmit_DMA(&hspi1, data, len);
    while(HAL_DMA_GetState(&hdma_spi1_tx) == HAL_DMA_STATE_BUSY);
}

/*在lcd上开辟一个操作窗口*/
void Lcd_Window_Set(uint16_t x1, uint16_t y1, uint16_t x2, uint16_t y2)
{
    /* 指定X方向操作区域 */
    Lcd_Write_Cmd(0x2A);
    Lcd_Write_Data16(x1);
    Lcd_Write_Data16(x2);

    /* 指定Y方向操作区域 */
    Lcd_Write_Cmd(0x2B);
    Lcd_Write_Data16(y1);
    Lcd_Write_Data16(y2);

    /* 发送该命令，LCD开始等待接收显存数据 */
    Lcd_Write_Cmd(0x2C);
}

/*lcd清屏*/
void Lcd_Clear(uint16_t color)
{
    uint16_t i;

    uint8_t* write_data = malloc(sizeof(uint16_t) * LCD_W_SIZE);
    for(i=0; i<LCD_W_SIZE*sizeof(uint16_t);)
    {
        *(write_data+i) = color>>8;
        i++;
        *(write_data+i) = color & 0x00ff;
        i++;
    }
    /* 指定显存操作地址为全屏幕 */
    Lcd_Window_Set(0, 0, LCD_W_SIZE-1, LCD_H_SIZE-1);
    /* 将显存缓冲区的数据全部写入缓冲区 */
    for(i = 0; i < LCD_H_SIZE; i++)
    {
        Lcd_Write_Data((uint8_t*)write_data, LCD_W_SIZE*sizeof(uint16_t));
    }
    free(write_data);
}

/*在lcd指定位置上画点*/
void Lcd_Draw_Point(uint16_t x, uint16_t y, uint16_t color)//横屏，左上角为（0，0）
{
	Lcd_Write_Cmd(0x2A);
	Lcd_Write_Data16(x);

	Lcd_Write_Cmd(0x2B);
	Lcd_Write_Data16(y);

	Lcd_Write_Cmd(0x2C);
	Lcd_Write_Data16(color);
}

/*lcd初始化*/
void Lcd_Init(void)
{
    HAL_GPIO_WritePin(LCD_RST_Pin.GPIOx, LCD_RST_Pin.GPIO_Pin, GPIO_PIN_RESET);
    HAL_Delay(500);
    HAL_GPIO_WritePin(LCD_RST_Pin.GPIOx, LCD_RST_Pin.GPIO_Pin, GPIO_PIN_SET);
    HAL_Delay(100);
    /* 关闭睡眠模式 */
    Lcd_Write_Cmd(0x11);
    HAL_Delay(120);

    /* 开始设置显存扫描模式，数据格式等 */
    Lcd_Write_Cmd(0x36);
    Lcd_Write_Data8(0xA0);  //bit7 0:上到下；bit6 0：左到右；bit5 0：不翻转
    /* RGB 5-6-5-bit格式  */
    Lcd_Write_Cmd(0x3A);
    Lcd_Write_Data8(0x65);
    /* porch 设置 */
    Lcd_Write_Cmd(0xB2);
    Lcd_Write_Data8(0x0C);
    Lcd_Write_Data8(0x0C);
    Lcd_Write_Data8(0x00);
    Lcd_Write_Data8(0x33);
    Lcd_Write_Data8(0x33);
    /* VGH设置 */
    Lcd_Write_Cmd(0xB7);
    Lcd_Write_Data8(0x72);
    /* VCOM 设置 */
    Lcd_Write_Cmd(0xBB);
    Lcd_Write_Data8(0x3D);
    /* LCM 设置 */
    Lcd_Write_Cmd(0xC0);
    Lcd_Write_Data8(0x2C);
    /* VDV and VRH 设置 */
    Lcd_Write_Cmd(0xC2);
    Lcd_Write_Data8(0x01);
    /* VRH 设置 */
    Lcd_Write_Cmd(0xC3);
    Lcd_Write_Data8(0x19);
    /* VDV 设置 */
    Lcd_Write_Cmd(0xC4);
    Lcd_Write_Data8(0x20);
    /* 普通模式下显存速率设置 60Mhz */
    Lcd_Write_Cmd(0xC6);
    Lcd_Write_Data8(0x0F);
    /* 电源控制 */
    Lcd_Write_Cmd(0xD0);
    Lcd_Write_Data8(0xA4);
    Lcd_Write_Data8(0xA1);
    /* 电压设置 */
    Lcd_Write_Cmd(0xE0);
    Lcd_Write_Data8(0xD0);
    Lcd_Write_Data8(0x04);
    Lcd_Write_Data8(0x0D);
    Lcd_Write_Data8(0x11);
    Lcd_Write_Data8(0x13);
    Lcd_Write_Data8(0x2B);
    Lcd_Write_Data8(0x3F);
    Lcd_Write_Data8(0x54);
    Lcd_Write_Data8(0x4C);
    Lcd_Write_Data8(0x18);
    Lcd_Write_Data8(0x0D);
    Lcd_Write_Data8(0x0B);
    Lcd_Write_Data8(0x1F);
    Lcd_Write_Data8(0x23);
    /* 电压设置 */
    Lcd_Write_Cmd(0xE1);
    Lcd_Write_Data8(0xD0);
    Lcd_Write_Data8(0x04);
    Lcd_Write_Data8(0x0C);
    Lcd_Write_Data8(0x11);
    Lcd_Write_Data8(0x13);
    Lcd_Write_Data8(0x2C);
    Lcd_Write_Data8(0x3F);
    Lcd_Write_Data8(0x44);
    Lcd_Write_Data8(0x51);
    Lcd_Write_Data8(0x2F);
    Lcd_Write_Data8(0x1F);
    Lcd_Write_Data8(0x1F);
    Lcd_Write_Data8(0x20);
    Lcd_Write_Data8(0x23);
    /* 显示开 */
    Lcd_Write_Cmd(0x20);
    Lcd_Write_Cmd(0x29);
}

/*
*功能：lcd上显示一个字符
*参数：x-y：显示位置，num：显示的字符，size：字体大小，color：字体颜色
*/
void Lcd_ShowChar(uint16_t x, uint16_t y, uint8_t num, Show_Size_Enum size, uint16_t Color)
{
	uint8_t temp;
	uint8_t pos, t;
	
    if((x > LCD_W_SIZE - 7) || (y > LCD_H_SIZE - 7)) return;	//设置窗口
	
	num = num - ' ';//得到偏移后的值
	
	if(size == SHOW_SIZE_32)
	{
		Lcd_Write_Cmd(0x2A);
		Lcd_Write_Data16(x);
		Lcd_Write_Data16(x + 16 - 1);

		Lcd_Write_Cmd(0x2B);
		Lcd_Write_Data16(y);
		Lcd_Write_Data16(y + 32 - 1);

		Lcd_Write_Cmd(0x2C);
		
		for(pos=0; pos<size; pos++)
		{
			//先写入高8位
			temp = asc2_3216[num][2*pos];//调用3216字体
			for(t=0; t<8; t++)
			{
				if(temp&0x01)
				{
					Lcd_Write_Data16(Color);
					temp >>= 1; 
				}
				else 
				{
					Lcd_Write_Data16(LCD_BACKGROUND);
					temp >>= 1;
					continue;
				}
			}
			
			//后写入低8位
			temp = asc2_3216[num][2*pos + 1];//调用3216字体
			for(t=8; t<16; t++)
			{
				if(temp&0x01)
				{
					Lcd_Write_Data16(Color);
					temp >>= 1; 
				}
				else 
				{
					Lcd_Write_Data16(LCD_BACKGROUND);
					temp >>= 1;
					continue;
				}
			}
		}
	}
    else if(size == SHOW_SIZE_16)
	{
		
		Lcd_Write_Cmd(0x2A);
		Lcd_Write_Data16(x);
		Lcd_Write_Data16(x + 8 - 1);

		Lcd_Write_Cmd(0x2B);
		Lcd_Write_Data16(y);
		Lcd_Write_Data16(y + 16 - 1);

		Lcd_Write_Cmd(0x2C);
		
	
		for(pos=0; pos<size; pos++)
		{
			temp = asc2_1608[num][pos];		 //调用1608字体
			
			for(t=0; t<size/2; t++)
			{
				if(temp&0x01)
				{
					Lcd_Write_Data16(Color);
				}
				else
				{
					Lcd_Write_Data16(LCD_BACKGROUND);
				}
				temp >>= 1;
			}
		}
	}
}

/*
*功能：lcd上显示一个字符串
*参数：x-y：显示位置，p：字符串指针，size：字体大小，color：字体颜色
*/
void Lcd_ShowString(uint16_t x, uint16_t y, char *p, Show_Size_Enum size, uint16_t Color)
{
    while(*p!='\0')
    {
        if(x > LCD_W_SIZE)
        {
            break;
        }
				
        if(y > LCD_H_SIZE)
        {
            break;
        }
				
        Lcd_ShowChar(x, y, *p, size, Color);
        x+=(size/2);
        p++;
    }
}

/*在lcd上打印字符串*/
void Lcd_printf(char *fmt, ...)//打印字符串
{
    uint8_t i;

	uint8_t buffer[40];
	va_list arg;
	
	va_start(arg, fmt);
	vsnprintf((char *)buffer, sizeof(buffer),fmt,arg);
	va_end(arg);

    if(strlen((char*)buffer) > 40)
    {
        memcpy(mUserLcdData.line_str[mUserLcdData.cur_line], buffer, 40); 
    }
    else
    {
        memcpy(mUserLcdData.line_str[mUserLcdData.cur_line], buffer, strlen((char*)buffer)); 
        for(i=strlen((char*)buffer); i<40; i++)
        {
            mUserLcdData.line_str[mUserLcdData.cur_line][i] = ' ';
        }
    }

    if(mUserLcdData.cur_line < 10)
    {
        Lcd_ShowString(0, (mUserLcdData.cur_line+4)*16, mUserLcdData.line_str[mUserLcdData.cur_line], SHOW_SIZE_16, LCD_TYPEFACE);
    }
    else
    {
        for(i=0; i<9; i++)
        {
            Lcd_ShowString(0, (i+4)*16 , mUserLcdData.line_str[mUserLcdData.cur_line-9+i], SHOW_SIZE_16, LCD_TYPEFACE);
        }
        Lcd_ShowString(0, (i+4)*16 , mUserLcdData.line_str[mUserLcdData.cur_line-9+i], SHOW_SIZE_16, LCD_TYPEFACE);
    }

    mUserLcdData.cur_line++;
    mUserLcdData.mem_page = mUserLcdData.cur_line / 10;
    mUserLcdData.cur_page = mUserLcdData.cur_line / 10;
}

void Lcd_Clear_Osci(uint16_t color)
{
    uint16_t i;

    uint8_t* write_data = malloc(sizeof(uint16_t) * LCD_W_SIZE);
    for(i=0; i<LCD_W_SIZE*sizeof(uint16_t);)
    {
        *(write_data+i) = color>>8;
        i++;
        *(write_data+i) = color & 0x00ff;
        i++;
    }
    /* 指定显存操作地址为全屏幕 */
    Lcd_Window_Set(0, 18, LCD_W_SIZE, LCD_H_SIZE);
    /* 将显存缓冲区的数据全部写入缓冲区 */
    for(i = 20; i <= LCD_H_SIZE; i++)
    {
        Lcd_Write_Data((uint8_t*)write_data, LCD_W_SIZE*sizeof(uint16_t));
    }
    free(write_data);
}

void Lcd_Clear_Print(uint16_t color)
{
    uint16_t i;

    uint8_t* write_data = malloc(sizeof(uint16_t) * LCD_W_SIZE);
    for(i=0; i<LCD_W_SIZE*sizeof(uint16_t);)
    {
        *(write_data+i) = color>>8;
        i++;
        *(write_data+i) = color & 0x00ff;
        i++;
    }
    /* 指定显存操作地址为全屏幕 */
    Lcd_Window_Set(0, 65, LCD_W_SIZE, LCD_H_SIZE);
    /* 将显存缓冲区的数据全部写入缓冲区 */
    for(i = 65; i <= LCD_H_SIZE; i++)
    {
        Lcd_Write_Data((uint8_t*)write_data, LCD_W_SIZE*sizeof(uint16_t));
    }
    free(write_data);
}

void Lcd_Osci_printf(char *fmt, ...)
{
	uint8_t buffer[40];
	va_list arg;
	
	va_start(arg, fmt);
	vsnprintf((char *)buffer, sizeof(buffer),fmt,arg);
	va_end(arg);

    Lcd_ShowString(0, 0, (char*)buffer, SHOW_SIZE_16, GREEN);
}

void Lcd_ShowTime(uint16_t x, uint16_t y, uint16_t color, DS1302_Time_t *time)
{
    uint8_t time_string[20] = {0};

    time_string[0] = '2'; time_string[1] = '0'; time_string[2] = '2'; time_string[3] = '4'; time_string[4] = '/';
    time_string[5] = time->month / 10 + 0x30;
    time_string[6] = time->month % 10 + 0x30; time_string[7] = '/';
    time_string[8] = time->date / 10 + 0x30;
    time_string[9] = time->date % 10 + 0x30; time_string[10] = ' ';
    time_string[11] = time->hour / 10 + 0x30;
    time_string[12] = time->hour % 10 + 0x30; time_string[13] = ':';
    time_string[14] = time->minute / 10 + 0x30;
    time_string[15] = time->minute % 10 + 0x30; time_string[16] = ':';
    time_string[17] = time->second / 10 + 0x30;
    time_string[18] = time->second % 10 + 0x30; time_string[19] = '\0';

    Lcd_ShowString(x, y, (char*)time_string, SHOW_SIZE_16, color);
}

void Lcd_ShowString_HZ(uint8_t x, uint8_t y, uint8_t *s, uint16_t color)
{
    unsigned char i,j;
    unsigned short k;
    uint8_t num;
    uint8_t temp;

    while(*s) 
    {    
        if((*s) >= 128) 
        {        
            for (k=0;k<HZNUM;k++) 
            {
                if ((hz16[k].Index[0]==*(s))&&(hz16[k].Index[1]==*(s+1)))
                {     
                    Lcd_Window_Set(x,y,x+16-1,y+16-1);
                    for(i=0;i<32;i++)
                    {
                        for(j=0;j<8;j++)
                        {
                            if(hz16[k].Msk[i]&(0x01<<j))
                            {
                                Lcd_Write_Data16(color);
                            }   
                            else 
                            {
                                Lcd_Write_Data16(LCD_BACKGROUND);
                            }
                        }
                    }
                }
            }
            s+=2;x+=16;
        }
        else
        {
            Lcd_Window_Set(x,y,x+8-1,y+16-1);
            num = (*s);

            num = num - ' ';//得到偏移后的值
            
            for(i=0; i<16; i++)
            {
                temp = asc2_1608[num][i];		 //调用1608字体
                
                for(j=0; j<8; j++)
                {
                    if(temp&0x01)
                    {
                        Lcd_Write_Data16(color);
                    }
                    else
                    {
                        Lcd_Write_Data16(LCD_BACKGROUND);
                    }
                    temp >>= 1;
                }
            }
            s+=1;x+=8;
	    }
    }        
}

void Lcd_Draw_Circle(uint16_t x0, uint16_t y0, uint16_t r, uint16_t Color)
{
    int x, y;
    int deltax, deltay;
    int d;
    int xi;
    x = 0;
    y = r;
    deltax = 3;
    deltay = 2 - r - r;
    d = 1 - r;

    Lcd_Draw_Point(x + x0, y + y0, Color);
    Lcd_Draw_Point(x + x0, -y + y0, Color);
    for (xi = -r + x0; xi <= r + x0; xi++)
        Lcd_Draw_Point(xi, y0, Color); // 水平线填充
    while (x < y)
    {
        if (d < 0)
        {
            d += deltax;
            deltax += 2;
            x++;
        }
        else
        {
            d += (deltax + deltay);
            deltax += 2;
            deltay += 2;
            x++;
            y--;
        }
        for (xi = -x + x0; xi <= x + x0; xi++)
        {
            Lcd_Draw_Point(xi, -y + y0, Color);
            Lcd_Draw_Point(xi, y + y0, Color); // 扫描线填充
        }
        for (xi = -y + x0; xi <= y + x0; xi++)
        {
            Lcd_Draw_Point(xi, -x + y0, Color);
            Lcd_Draw_Point(xi, x + y0, Color); // 扫描线填充其量
        }
    }
}

void Lcd_ShowNum(uint16_t x, uint16_t y, int32_t num, Show_Size_Enum size, uint16_t color)
{
    uint8_t str[16] = {0};

    char index[]="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//索引表
	unsigned unum;//存放要转换的整数的绝对值,转换的整数可能是负数
	int i=0,j,k;//i用来指示设置字符串相应位，转换之后i其实就是字符串的长度；转换后顺序是逆序的，有正负的情况，k用来指示调整顺序的开始位置;j用来指示调整顺序时的交换。
 
	//获取要转换的整数的绝对值
	if(num<0)//要转换成十进制数并且是负数
	{
		unum=(unsigned)-num;//将num的绝对值赋给unum
		str[i++]='-';//在字符串最前面设置为'-'号，并且索引加1
	}
	else unum=(unsigned)num;//若是num为正，直接赋值给unum
 
	//转换部分，注意转换后是逆序的
	do
	{
		str[i++]=index[unum%10];//取unum的最后一位，并设置为str对应位，指示索引加1
		unum/=10;//unum去掉最后一位
 
	}while(unum);//直至unum为0退出循环
 
	str[i]='\0';//在字符串最后添加'\0'字符，c语言字符串以'\0'结束。
 
	//将顺序调整过来
	if(str[0]=='-') k=1;//如果是负数，符号不用调整，从符号后面开始调整
	else k=0;//不是负数，全部都要调整
 
	char temp;//临时变量，交换两个值时用到
	for(j=k;j<=(i-1)/2;j++)//头尾一一对称交换，i其实就是字符串的长度，索引最大值比长度少1
	{
		temp=str[j];//头部赋值给临时变量
		str[j]=str[i-1+k-j];//尾部赋值给头部
		str[i-1+k-j]=temp;//将临时变量的值(其实就是之前的头部值)赋给尾部
	}

    Lcd_ShowString(x, y, (char*)str, size, color);
}

/*******************************************页面代码*****************************************************************/
void Lcd_Show_Page_1(void)
{
    LCD_BACKGROUND = RED;
    Lcd_Clear(LCD_BACKGROUND);

    Lcd_ShowString_HZ(32, 16, "GZ029 智能电子产品设计与开发", YELLOW);
    Lcd_ShowString_HZ(32, 48, "工位号: A001", YELLOW);  //工位号显示
    Lcd_ShowString_HZ(32, 200, "按下E键进入主页面", YELLOW);

    Page_Now = 1;
}

void Lcd_Show_Page_2(void)
{
    LCD_BACKGROUND = RED;
    Lcd_Clear(LCD_BACKGROUND);

    Lcd_ShowString_HZ(16, 10, "背景颜色:", YELLOW);
    Lcd_ShowString_HZ(48, 30, "1.白色, 2.黑色", YELLOW);
    Lcd_ShowString_HZ(16, 60, "图形颜色:", YELLOW);
    Lcd_ShowString_HZ(48, 80, "1.红色, 2.绿色", YELLOW);
    Lcd_ShowString_HZ(48, 100, "3.蓝色, 4.黄色", YELLOW);
    Lcd_ShowString_HZ(16, 130, "图形形状:", YELLOW);
    Lcd_ShowString_HZ(48, 150, "1.正方形,2.圆形,3.三角形", YELLOW);
    Lcd_ShowString_HZ(48, 170, "4.圆环, 5.四边形", YELLOW);

    Page_Now = 2;
}

void Lcd_Show_Page_3(void)
{
    LCD_BACKGROUND = RED;
    Lcd_Clear(LCD_BACKGROUND);

    Lcd_ShowString_HZ(16, 10, "模式选择:", YELLOW);
    Lcd_ShowString_HZ(16, 40, "1:静态图片  F1", YELLOW);
    Lcd_ShowString_HZ(16, 70, "2:低速视频  F2", YELLOW);
    Lcd_ShowString_HZ(16, 100, "3:高速视频  F3", YELLOW);
    Lcd_ShowString_HZ(16, 130, "4:自定义坐标  F4", YELLOW);

    Page_Now = 3;
}

void Lcd_Show_Page_4(void)
{
    LCD_BACKGROUND = RED;
    Lcd_Clear(LCD_BACKGROUND);

    Lcd_ShowString_HZ(16, 10, "XXXX模式", YELLOW);
    Lcd_ShowString_HZ(16, 40, "识别计时:", YELLOW);
    Lcd_ShowString_HZ(16, 70, "系统时间:", YELLOW);

    Page_Now = 4;
}

void Lcd_Show_Page_5(void)
{
    LCD_BACKGROUND = RED;
    Lcd_Clear(LCD_BACKGROUND);

    Lcd_ShowString_HZ(16, 10, "自定义坐标:", YELLOW);
    Lcd_ShowString_HZ(16, 40, "X:", YELLOW);
    Lcd_ShowString_HZ(16, 70, "Y:", YELLOW);
    Lcd_ShowString_HZ(16, 100, "识别计时:", YELLOW);
    Lcd_ShowString_HZ(16, 130, "系统时间:", YELLOW);

    Page_Now = 5;
}

void Lcd_Show_Page_6(void)
{
    LCD_BACKGROUND = RED;
    Lcd_Clear(LCD_BACKGROUND);

    Page_Now = 6;
    page_num = 0;
    submenu = 0;
    submenu_num = 0;
}

void Lcd_Show_Page_7(void)
{
    LCD_BACKGROUND = RED;
    Lcd_Clear(LCD_BACKGROUND);

    Page_Now = 7;
    page_num = 0;
    submenu = 0;
    submenu_num = 0;
}





