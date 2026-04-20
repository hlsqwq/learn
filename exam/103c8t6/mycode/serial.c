#include "serial.h"

/* 静态实例 */
UartRxCtx_t    uart1_rx_ctx;
UartRxCtx_t    uart2_rx_ctx;
UartRxCtx_t    uart3_rx_ctx;

//static FrameHandler_t s_frame_handler = NULL;

/* 底层发送接口 (需用户实现或映射到HAL) */
extern void uart_hal_send_byte(uint8_t byte);  /* 阻塞发送一字节 */

/* 计算累加和校验 */
static uint8_t calc_checksum(uint16_t cmd, uint8_t len, uint8_t *data)
{
    uint8_t sum = 0;
    sum += (cmd >> 8) & 0xFF;   /* 命令高字节 */
    sum += cmd & 0xFF;          /* 命令低字节 */
    sum += len;                 /* 长度 */
    for (uint8_t i = 0; i < len; i++) {
        sum += data[i];         /* 数据 */
    }
    return sum;
}

/* 初始化 */
void uart_link_init()
{
	#if 0
    memset(&uart1_rx_ctx, 0, sizeof(uart1_rx_ctx));
    uart1_rx_ctx.state = FRAME_STATE_HEAD1;
	uart1_rx_ctx.huart = &huart1;
    uart1_rx_ctx.handler = uart_1_rx_handler;
    #endif
	
	memset(&uart2_rx_ctx, 0, sizeof(uart2_rx_ctx));
    uart2_rx_ctx.state = FRAME_STATE_HEAD1;
	uart2_rx_ctx.huart = &huart2;
    uart2_rx_ctx.handler = uart_2_rx_handler;

	memset(&uart3_rx_ctx, 0, sizeof(uart3_rx_ctx));
    uart3_rx_ctx.state = FRAME_STATE_HEAD1;
	uart3_rx_ctx.huart = &huart3;
    uart3_rx_ctx.handler = uart_3_rx_handler;


    //启动全部串口的收发工作
	//HAL_UART_Receive_IT(&huart1,&rx1data,1);
	HAL_UART_Receive_IT(&huart2,&rx2data,1);
	HAL_UART_Receive_IT(&huart3,&rx3data,1);
}

/* 字节接收处理 - 在中断服务程序中调用 */
void uart_rx_byte(uint8_t byte, UartRxCtx_t *uart_ctx)
{
    UartRxCtx_t *ctx = uart_ctx;
    
    switch (ctx->state) {
        case FRAME_STATE_HEAD1:
            if (byte == UART_FRAME_HEAD_1) {
                ctx->state = FRAME_STATE_HEAD2;
            }
            break;
            
        case FRAME_STATE_HEAD2:
            ctx->state = (byte == UART_FRAME_HEAD_2) ? 
                         FRAME_STATE_CMD_H : FRAME_STATE_HEAD1;
            if (byte != UART_FRAME_HEAD_2 && byte == UART_FRAME_HEAD_1) {
                ctx->state = FRAME_STATE_HEAD2; /* 连续AA AA 55处理 */
            }
            break;
            
        case FRAME_STATE_CMD_H:
            ctx->cmd_temp = (uint16_t)byte << 8;
            ctx->state = FRAME_STATE_CMD_L;
            break;
            
        case FRAME_STATE_CMD_L:
            ctx->cmd_temp |= byte;
            ctx->state = FRAME_STATE_LEN;
            break;
            
        case FRAME_STATE_LEN:
            if (byte > UART_FRAME_MAX_DATA) {
                ctx->state = FRAME_STATE_HEAD1; /* 长度非法，丢弃 */
                break;
            }
            ctx->data_len = byte;
            ctx->frame.len = byte;
            ctx->frame.cmd = ctx->cmd_temp;
            ctx->rx_index = 0;
            ctx->state = (byte == 0) ? FRAME_STATE_SUM : FRAME_STATE_DATA;
            break;
            
        case FRAME_STATE_DATA:
            if (ctx->rx_index < UART_FRAME_MAX_DATA) {
                ctx->frame.data[ctx->rx_index++] = byte;
            }
            if (ctx->rx_index >= ctx->data_len) {
                ctx->state = FRAME_STATE_SUM;
            }
            break;
            
        case FRAME_STATE_SUM:
            {
                uint8_t calc_sum = calc_checksum(ctx->frame.cmd, 
                                                  ctx->frame.len, 
                                                  ctx->frame.data);
                if (byte == calc_sum) {
                    ctx->state = FRAME_STATE_TAIL;
                } else {
                    ctx->state = FRAME_STATE_HEAD1; /* 校验失败，丢弃 */
                }
            }
            break;
            
        case FRAME_STATE_TAIL:
            if (byte == UART_FRAME_TAIL) {
                /* 帧接收完成，设置标志 */
                ctx->frame_ready = true;

				//不在中断函数中处理，发消息到线程中去处理
			    ctx->handler(ctx->frame.cmd, 
                             ctx->frame.data, 
                             ctx->frame.len);
                
				//ctx->handler =false;
				#if 0
				if(ctx->huart == &huart1)
  			      HAL_UART_Receive_IT(ctx->huart,&rx1data,1);
				else if(ctx->huart == &huart2)
  			      HAL_UART_Receive_IT(ctx->huart,&rx2data,1);
				else if(ctx->huart == &huart3)
  			      HAL_UART_Receive_IT(ctx->huart,&rx3data,1);
				#endif
            }
            /* 无论是否成功，都重置状态机 */
            ctx->state = FRAME_STATE_HEAD1;
            break;
            
        default:
            ctx->state = FRAME_STATE_HEAD1;
            break;
    }
}

/* 发送完整帧 */
static uint8_t txbuf[64];

void uart_tx_frame(UartRxCtx_t *uart_ctx, uint16_t cmd, uint8_t *data, uint8_t len)
{
    static uint8_t i;

    if (len > UART_FRAME_MAX_DATA) return;
    
    uint8_t checksum;
    
    /* 帧头 */
	uart_ctx->tx_buf[0]=UART_FRAME_HEAD_1;
	uart_ctx->tx_buf[1]=UART_FRAME_HEAD_2;

    
    /* 命令 (大端序) */
	uart_ctx->tx_buf[2]=(cmd >> 8) & 0xFF;
	uart_ctx->tx_buf[3]=cmd & 0xFF;

    
    /* 长度 */
	uart_ctx->tx_buf[4]=len;

    
    /* 数据 */
    for ( i = 0; i < len; i++) {
       uart_ctx->tx_buf[5+i]= data[i];
    }
    
    /* 校验 */
    checksum = calc_checksum(cmd, len, data);
    uart_ctx->tx_buf[5+len]=checksum;
    
    /* 帧尾 */
	uart_ctx->tx_buf[5+len+1]=UART_FRAME_TAIL;

	
    HAL_UART_Transmit(uart_ctx->huart,uart_ctx->tx_buf,len+7,0xffff);
}
