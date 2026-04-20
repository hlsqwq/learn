#ifndef UART_LINK_LAYER_H
#define UART_LINK_LAYER_H

#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include <rt_main.h>

/* 协议常量 */
#define UART_FRAME_HEAD_1       0xAA
#define UART_FRAME_HEAD_2       0x55
#define UART_FRAME_TAIL         0x0D
#define UART_FRAME_MIN_LEN      7       /* 无数据时最小帧长 */
#define UART_FRAME_MAX_DATA     56      /* 最大数据长度 */
#define UART_FRAME_MAX_SIZE     64      /* 总缓冲区限制 */

/* 解析状态机 */
typedef enum {
    FRAME_STATE_HEAD1 = 0,  /* 等待帧头1 */
    FRAME_STATE_HEAD2,      /* 等待帧头2 */
    FRAME_STATE_CMD_H,      /* 等待命令高字节 */
    FRAME_STATE_CMD_L,      /* 等待命令低字节 */
    FRAME_STATE_LEN,        /* 等待长度 */
    FRAME_STATE_DATA,       /* 接收数据 */
    FRAME_STATE_SUM,        /* 等待校验 */
    FRAME_STATE_TAIL        /* 等待帧尾 */
} FrameState_t;

/* 帧结构体 */
typedef struct {
    uint16_t cmd;                           /* 2字节命令 */
    uint8_t  len;                           /* 数据长度 */
    uint8_t  data[UART_FRAME_MAX_DATA];     /* 数据缓冲区 */
} Frame_t;


/* 回调函数类型: 用户处理接收到的帧 */
typedef void (*FrameHandler_t)  (uint16_t cmd, uint8_t *data, uint8_t len);


/* 接收上下文 */
typedef struct {
    FrameState_t state;                     /* 当前状态 */
    uint8_t  rx_buf[UART_FRAME_MAX_SIZE];   /* 接收缓冲区 */
	uint8_t  tx_buf[UART_FRAME_MAX_SIZE];   /* 发送缓冲区 */
    uint16_t rx_index;                      /* 当前写入位置 */
    uint16_t data_len;                      /* 期望的数据长度 */
    uint16_t cmd_temp;                      /* 命令临时存储 */
    Frame_t  frame;                         /* 解析完成的帧 */
    bool     frame_ready;                   /* 帧就绪标志 */

	UART_HandleTypeDef *huart; //哪个串口
	FrameHandler_t     handler;
} UartRxCtx_t;




/* 对外接口 */
void uart_link_init();
void uart_rx_byte(uint8_t byte, UartRxCtx_t *uart_ctx);              /* 中断中调用，每收到一字节 */
void uart_tx_frame(UartRxCtx_t *uart_ctx, uint16_t cmd, uint8_t *data, uint8_t len); /* 发送帧 */

extern UartRxCtx_t    uart1_rx_ctx;
extern UartRxCtx_t    uart2_rx_ctx;
extern UartRxCtx_t    uart3_rx_ctx;

#endif


