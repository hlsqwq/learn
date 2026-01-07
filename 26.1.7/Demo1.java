package com.hls;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demo1 extends Thread{

    private String str;


    @SneakyThrows
    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        while (true){
            Thread.sleep(1000);
            String format = formatter.format(LocalDateTime.now());
            System.out.println(format);
        }
    }
}
