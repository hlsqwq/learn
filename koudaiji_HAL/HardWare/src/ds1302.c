#include "ds1302.h"

DS1302_Time_t Real_Time;

GPIO_Struct Ds1302_CLK_Pin, Ds1302_DAT_Pin, Ds1302_RST_Pin;

static void writeSDA(void) {
	GPIO_InitTypeDef GPIO_InitStructure;

	GPIO_InitStructure.Pin = Ds1302_DAT_Pin.GPIO_Pin;
	GPIO_InitStructure.Mode =  GPIO_MODE_OUTPUT_PP;
	GPIO_InitStructure.Speed = GPIO_SPEED_FREQ_HIGH;
	HAL_GPIO_Init(Ds1302_DAT_Pin.GPIOx, &GPIO_InitStructure);
	
}

static void readSDA(void) {
	GPIO_InitTypeDef GPIO_InitStructure;

	GPIO_InitStructure.Pin = Ds1302_DAT_Pin.GPIO_Pin;
	GPIO_InitStructure.Mode =  GPIO_MODE_INPUT;
	GPIO_InitStructure.Pull = GPIO_PULLDOWN;
	GPIO_InitStructure.Speed = GPIO_SPEED_FREQ_HIGH;
	HAL_GPIO_Init(Ds1302_DAT_Pin.GPIOx, &GPIO_InitStructure);	
}

static void DS1302_SendCmd(uint8_t cmd) {
	uint8_t i;
	for (i = 0; i < 8; i ++) 
	{	
		//		DS1302_SDA = (bit)(addr & 1);
		HAL_GPIO_WritePin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin, (cmd & 1) ?  GPIO_PIN_SET :  GPIO_PIN_RESET);
		//		DS1302_SCK = 1;
		HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_SET);
		Soft_Delay_Us(1);
		//		DS1302_SCK = 0;
		HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_RESET);
		Soft_Delay_Us(1);
		cmd >>= 1;
	}
}

static void DS1302_WriteByte(uint8_t addr, uint8_t d)
{
	uint8_t i;

	//	DS1302_RST = 1;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_SET);	
	
	//addr = addr & 0xFE;
	DS1302_SendCmd(addr);	// Отправка адреса
	
	for (i = 0; i < 8; i ++) 
	{
		//		DS1302_SDA = (bit)(d & 1);
		HAL_GPIO_WritePin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin, (d & 1) ?  GPIO_PIN_SET :  GPIO_PIN_RESET);
		//		DS1302_SCK = 1;
		HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_SET);
		Soft_Delay_Us(1);
		//		DS1302_SCK = 0;
		HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_RESET);
		Soft_Delay_Us(1);
		d >>= 1;
	}
	
	//	DS1302_RST = 0;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_RESET);
	//	DS1302_SDA = 0;
	HAL_GPIO_WritePin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin,  GPIO_PIN_RESET);
}


/* Sends 'cmd' command and writes in burst mode 'len' bytes from 'temp' */
static void DS1302_WriteBurst(uint8_t cmd, uint8_t len, uint8_t * temp)
{
	uint8_t i, j;
	
	DS1302_WriteByte(DS1302_CONTROL, 0x00);			// Отключить защиту от записи

	//	DS1302_RST = 1;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_SET);	
	
	DS1302_SendCmd(cmd);	// Sends burst write command
	
	for(j = 0; j < len; j++) {
		for (i = 0; i < 8; i ++) 
		{
			//			DS1302_SDA = (bit)(d & 1);
			HAL_GPIO_WritePin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin, (temp[j] & 1) ?  GPIO_PIN_SET :  GPIO_PIN_RESET);
			//			DS1302_SCK = 1;
			HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_SET);
			Soft_Delay_Us(1);
			//			DS1302_SCK = 0;
			HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_RESET);
			Soft_Delay_Us(1);
			temp[j] >>= 1;
		}
	}
	
	//	DS1302_RST = 0;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_RESET);
	//	DS1302_SDA = 0;
	HAL_GPIO_WritePin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin,  GPIO_PIN_RESET);
	
	DS1302_WriteByte(DS1302_CONTROL, 0x80);			// Включить защиту от записи
}


/* Reads a byte from addr */
static uint8_t DS1302_ReadByte(uint8_t addr)
{
	uint8_t i;
	uint8_t temp = 0;

	//	DS1302_RST = 1;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_SET);	
	addr = addr | 0x01; 	// Generate Read Address

	DS1302_SendCmd(addr);	// Sends address
	
	readSDA();
	for (i = 0; i < 8; i ++) 
	{
		temp >>= 1;
		//		if(DS1302_SDA)
		if(HAL_GPIO_ReadPin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin))
			temp |= 0x80;
		//		DS1302_SCK = 1;
		HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_SET);
		Soft_Delay_Us(1);
		//		DS1302_SCK = 0;
		HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_RESET);
		Soft_Delay_Us(1);
	}
	writeSDA();

	//	DS1302_RST = 0;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_RESET);
	//	DS1302_SDA = 0;
	HAL_GPIO_WritePin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin,  GPIO_PIN_RESET);
	return temp;
}


/* Sends 'cmd' command and reads in burst mode 'len' bytes into 'temp' */
static void DS1302_ReadBurst(uint8_t cmd, uint8_t len, uint8_t * temp) 
{
	uint8_t i, j;

	//	DS1302_RST = 1;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_SET);	
	cmd = cmd | 0x01; 		// Generate read command

	DS1302_SendCmd(cmd);	// Sends burst read command
	
	readSDA();
	for (j = 0; j < len; j ++) {
		temp[j] = 0;
		for (i = 0; i < 8; i ++) 
		{
			temp[j] >>= 1;
			//			if(DS1302_SDA)
			if(HAL_GPIO_ReadPin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin))
				temp[j] |= 0x80;

			//			DS1302_SCK = 1;
			HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_SET);
			Soft_Delay_Us(1);
		
			//			DS1302_SCK = 0;
			HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_RESET);
			Soft_Delay_Us(1);

		}
	}
	writeSDA();

	//	DS1302_RST = 0;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_RESET);
	HAL_GPIO_WritePin(Ds1302_DAT_Pin.GPIOx, Ds1302_DAT_Pin.GPIO_Pin,  GPIO_PIN_RESET);
}


/* Writes time byte by byte from 'buf' */
void DS1302_WriteTime(DS1302_Time_t* time) 
{	
	DS1302_WriteByte(DS1302_CONTROL, 0x00);			// 解除保护
	Soft_Delay_Us(1);
	DS1302_WriteByte(DS1302_SEC, 0x80);
	DS1302_WriteByte(DS1302_YEAR,  HEX2BCD(time->year));
	DS1302_WriteByte(DS1302_MONTH, HEX2BCD(time->month));
	DS1302_WriteByte(DS1302_DATE,  HEX2BCD(time->date));
	DS1302_WriteByte(DS1302_HOUR,  HEX2BCD(time->hour));
	DS1302_WriteByte(DS1302_MIN,   HEX2BCD(time->minute));
	DS1302_WriteByte(DS1302_SEC,   HEX2BCD(time->second));
	DS1302_WriteByte(DS1302_DAY,   HEX2BCD(time->week));
	DS1302_WriteByte(DS1302_CONTROL, 0x80);			// 写保护
	Soft_Delay_Us(1);
}


/* Reads time byte by byte to 'buf' */
void DS1302_ReadTime(DS1302_Time_t* time)  
{ 
   	uint8_t tmp;
	tmp = DS1302_ReadByte(DS1302_YEAR); 	
	time->year= BCD2HEX(tmp);		 
	tmp = DS1302_ReadByte(DS1302_MONTH); 	
	time->month = BCD2HEX(tmp);	 
	tmp = DS1302_ReadByte(DS1302_DATE); 	
	time->date = BCD2HEX(tmp);
	tmp = DS1302_ReadByte(DS1302_HOUR);		
	time->hour = BCD2HEX(tmp);
	tmp = DS1302_ReadByte(DS1302_MIN);		
	time->minute = BCD2HEX(tmp); 
	tmp = DS1302_ReadByte((DS1302_SEC)) & 0x7F;
	time->second = BCD2HEX(tmp);
	tmp = DS1302_ReadByte(DS1302_DAY);		
	time->week = BCD2HEX(tmp);
}


/* Writes 'val' to ram address 'addr' */
/* Ram addresses range from 0 to 30 */
void DS1302_WriteRam(uint8_t addr, uint8_t val) {
	DS1302_WriteByte(DS1302_CONTROL, 0x00);			// 禁用写入保护
	Soft_Delay_Us(1);
	if (addr >= RAMSIZE) {
		return;
	}
	
	DS1302_WriteByte(DS1302_RAMSTART + (2 * addr), val);	
	
	DS1302_WriteByte(DS1302_CONTROL, 0x80);			// 启用写入保护。
	Soft_Delay_Us(1);
}


/* Reads ram address 'addr' */
uint8_t DS1302_ReadRam(uint8_t addr) {
	if (addr >= RAMSIZE) {
		return 0;
	}
	
	return DS1302_ReadByte(DS1302_RAMSTART + (2 * addr));	
}


/* Clears the entire ram writing 0 */
void DS1302_ClearRam(void) {
	uint8_t i;
	for(i=0; i< RAMSIZE; i++){
		DS1302_WriteRam(i, 0x00);
	}
}


/* Reads time in burst mode, includes control byte */
void DS1302_ReadTimeBurst(uint8_t * buf) {
	uint8_t temp[8] = {0, 0, 0, 0, 0, 0, 0, 0};
	
	DS1302_ReadBurst(DS1302_CLKBURST, 8, temp); 
	
	buf[1] = BCD2HEX(temp[6]);	// Year
	buf[2] = BCD2HEX(temp[4]);	// Month
	buf[3] = BCD2HEX(temp[3]);	// Date
	buf[4] = BCD2HEX(temp[2]);	// Hour
	buf[5] = BCD2HEX(temp[1]);	// Min
	buf[6] = BCD2HEX(temp[0]);	// Sec
	buf[7] = BCD2HEX(temp[5]);	// Day
	buf[0] = temp[7]; 			// Control
}


/* Writes time in burst mode, includes control byte */
void DS1302_WriteTimeBurst(uint8_t * buf) {
	uint8_t temp[8];
	
	temp[0]=HEX2BCD(buf[6]);	// Sec
	temp[1]=HEX2BCD(buf[5]);	// Min
	temp[2]=HEX2BCD(buf[4]);	// Hour
	temp[3]=HEX2BCD(buf[3]);	// Date
	temp[4]=HEX2BCD(buf[2]);	// Month
	temp[5]=HEX2BCD(buf[7]);	// Day
	temp[6]=HEX2BCD(buf[1]);	// Year
	temp[7]=buf[0];				// Control
	
	DS1302_WriteBurst(DS1302_CLKBURST, 8, temp); 
}


/* Reads ram in burst mode 'len' bytes into 'buf' */
void DS1302_ReadRamBurst(uint8_t len, uint8_t * buf) {
	uint8_t i;
	if(len <= 0) {
		return;
	}
	if (len > RAMSIZE) {
		len = RAMSIZE;
	}
	for(i = 0; i < len; i++) {
		buf[i] = 0;
	}
	DS1302_ReadBurst(DS1302_RAMBURST, len, buf);	
}


/* Writes ram in burst mode 'len' bytes from 'buf' */
void DS1302_WriteRamBurst(uint8_t len, uint8_t * buf) {
	if(len <= 0) {
		return;
	}
	if (len > RAMSIZE) {
		len = RAMSIZE;
	}
	DS1302_WriteBurst(DS1302_RAMBURST, len, buf);
}


//启动时钟。
//DS1302 最初处于 Halt 模式（已停止，省电模式）。
//为了开始计时，需要执行此函数一次。
void DS1302_ClockStart(void)
{
	uint8_t buf = 0x00;

	DS1302_WriteByte(DS1302_CONTROL, 0x00);			// 禁用写入保护。
	Soft_Delay_Us(1);

	buf = DS1302_ReadByte(DS1302_SEC) & 0x7F;		// 写入 8 位中的零的同时，保留当前秒数的值
	DS1302_WriteByte(DS1302_SEC, buf);

	DS1302_WriteByte(DS1302_CONTROL, 0x80);			// 启用写入保护。
	Soft_Delay_Us(1);
}


//停止时钟。
//为了进入 Halt 模式（省电模式），此功能可能并不实用 
void DS1302_ClockStop(void)
{
	uint8_t buf = 0x00;

	DS1302_WriteByte(DS1302_CONTROL, 0x00);			// 禁用写入保护
	Soft_Delay_Us(1);

	buf = DS1302_ReadByte(DS1302_SEC) | 0x80;		// 写入 8 位中的1的同时，保留当前秒数的值
	DS1302_WriteByte(DS1302_SEC, buf);

	DS1302_WriteByte(DS1302_CONTROL, 0x80);			// 启用写入保护
	Soft_Delay_Us(1);
}


//重置时钟
//将0写入所有时钟寄存器（从0x80到0x8C）并将 DS1302 传输到 Halt 模式（省电模式）。
//要启动时钟，请使用 DS1302_ClockStart() 函数。
void DS1302_ClockClear(void)
{
	DS1302_WriteByte(DS1302_CONTROL, 0x00);			// 禁用写入保护
	Soft_Delay_Us(1);

	DS1302_WriteByte(DS1302_SEC, 0x80);				//重置秒并进入 Halt 模式。
	DS1302_WriteByte(DS1302_MIN, 0x00);
	DS1302_WriteByte(DS1302_HOUR, 0x00);
	DS1302_WriteByte(DS1302_DATE, 0x00);
	DS1302_WriteByte(DS1302_MONTH, 0x00);
	DS1302_WriteByte(DS1302_DAY, 0x00);
	DS1302_WriteByte(DS1302_YEAR, 0x00);

	DS1302_WriteByte(DS1302_CONTROL, 0x80);			// 启用写入保护。
	Soft_Delay_Us(1);
}

void DS1302_Init(void)
{
    GPIO_InitTypeDef GPIO_InitStruct = {0};

    Ds1302_CLK_Pin.GPIOx = GPIOB;
    Ds1302_CLK_Pin.GPIO_Pin = GPIO_PIN_9;
    Ds1302_DAT_Pin.GPIOx = GPIOG;
    Ds1302_DAT_Pin.GPIO_Pin = GPIO_PIN_13;
    Ds1302_RST_Pin.GPIOx = GPIOB;
    Ds1302_RST_Pin.GPIO_Pin = GPIO_PIN_8;

    __HAL_RCC_GPIOG_CLK_ENABLE();
    __HAL_RCC_GPIOB_CLK_ENABLE();

    GPIO_InitStruct.Pin = Ds1302_DAT_Pin.GPIO_Pin;
    GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;  //推挽输出
    GPIO_InitStruct.Pull = GPIO_NOPULL;  
    GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_VERY_HIGH;
    HAL_GPIO_Init(GPIOG, &GPIO_InitStruct);

    GPIO_InitStruct.Pin = Ds1302_CLK_Pin.GPIO_Pin | Ds1302_RST_Pin.GPIO_Pin;
    GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;  //推挽输出
    GPIO_InitStruct.Pull = GPIO_NOPULL;  
    GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_VERY_HIGH;
    HAL_GPIO_Init(GPIOB, &GPIO_InitStruct);
	
	DS1302_WriteByte(DS1302_CHARGER, 0x00);
		
	//	DS1302_RST = 0;
	HAL_GPIO_WritePin(Ds1302_RST_Pin.GPIOx, Ds1302_RST_Pin.GPIO_Pin,  GPIO_PIN_RESET);
	//	DS1302_SCK = 0;
	HAL_GPIO_WritePin(Ds1302_CLK_Pin.GPIOx, Ds1302_CLK_Pin.GPIO_Pin,  GPIO_PIN_RESET);

	HAL_Delay(1);
	DS1302_ClockStart();
}
