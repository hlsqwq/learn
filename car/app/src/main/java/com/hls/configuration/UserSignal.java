package com.hls.configuration;

import lombok.Getter;

/**
 * 小车控制信号枚举
 * 已校准：变量名后缀数字与枚举值(Value)严格对应
 */
@Getter
public enum UserSignal {
    // --- 基础系统信号 ---
    SIG_START(0),
    SIG_UART1_RX_CPLT(1),
    SIG_UART2_RX_CPLT(2),
    SIG_UART3_RX_CPLT(3),
    SIG_BT_HEAT_BEAT_5S(6),
    SIG_TRANSMIT_A_TO_B(8),

    // --- 小车简单运动 (已根据后缀校准值) ---
    SIG_CAR_USOA_MOVE_11(201),        // 左上
    SIG_CAR_USOA_MOVE_12(202),        // 正北/前
    SIG_CAR_USOA_MOVE_1(203),         // 右上
    SIG_CAR_USOA_MOVE_3(204),         // 正东/右
    SIG_CAR_USOA_MOVE_5(205),         // 右下
    SIG_CAR_USOA_MOVE_6(206),         // 正南/后
    SIG_CAR_USOA_MOVE_7(207),         // 左下
    SIG_CAR_USOA_MOVE_9(208),         // 正西/左

    // 坐标运动与停止逻辑
    SIG_CAR_USOA_MOVE_TO_XY(209),     // 格式: "sig SIG_CAR_USOA_MOVE_TO_XY f,b,l,r\n"
    SIG_CAR_USOA_STOP(210),           // 停止
    SIG_CAR_USOA_REPORT_POSITION(211),// 坐标上报
    SIG_CAR_USOA_MOVE_BASE_END(212),


    // --- 转向与旋转 ---
    SIG_CAR_MOVE_YAW(160),
    SIG_CAR_SIMPLE_MOVE_Left_DiaoTou(111),
    SIG_CAR_SIMPLE_MOVE_Right_DiaoTou(112),


    // --- 传感器相关 ---
    SIG_DHT11_GET_VALUE(901),
    SIG_MPU6050_GET_VALUE(912),

    // --- 执行器 ---
    SIG_RELAY_OPEN(2001),
    SIG_RELAY_CLOSE(2002);

    private final int value;

    UserSignal(int value) {
        this.value = value;
    }


}