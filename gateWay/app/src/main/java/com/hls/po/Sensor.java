package com.hls.po;


import java.util.Calendar;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <p>
 * 
 * </p>
 *
 * @author hls
 * @since 2025-12-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    private Integer id;

    private double temperature;

    private double humidity;

    private double illumination;

    private String createTime;

}
