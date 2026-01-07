package com.hls;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    private double temperature;
    private double humidity;
    private double pressure;

}
